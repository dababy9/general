#!/usr/bin/python3
import argparse
import hashlib
import imghdr
import os
import pickle
import posixpath
import re
import signal
import socket
import threading
import time
import urllib.parse
import urllib.request
from io import BytesIO

import ssl

ssl._create_default_https_context = ssl._create_unverified_context


# config
socket.setdefaulttimeout(2)

output_dir = './bing'  # default output dir
tried_urls = []
image_md5s = {}
in_progress = 0
urlopenheader = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/17.17134'}


# Naive URL encoding
def _encode_url(url):
    scheme, netloc, path, query, fragment = list(urllib.parse.urlsplit(url))

    path = urllib.parse.quote(path)  # path
    query = urllib.parse.quote_plus(query)  # query
    fragment = urllib.parse.quote(fragment)  # fragment

    encoded_url = urllib.parse.urlunsplit((scheme, netloc, path, query, fragment))

    return encoded_url


def download(pool_sema: threading.Semaphore, img_sema: threading.Semaphore, url: str, output_dir: str, limit: int):
    global tried_urls
    global image_md5s
    global in_progress
    global urlopenheader
    if url in tried_urls:
        print('SKIP: Already checked url, skipping')
        return
    pool_sema.acquire()
    in_progress += 1
    acquired_img_sema = False

    path = urllib.parse.urlsplit(url).path
    name, _ = os.path.splitext(posixpath.basename(path))
    if not name:
        # if path and name are empty (e.g. https://sample.domain/abcd/?query)
        name = hashlib.md5(url.encode('utf-8')).hexdigest()
    name = name.strip()[:36].strip()

    try:
        url.encode('ascii')
    except UnicodeEncodeError:  # the url contains non-ascii characters
        url = _encode_url(url)

    try:
        request = urllib.request.Request(url, None, urlopenheader)
        image = urllib.request.urlopen(request).read()
        imgtype = imghdr.what(BytesIO(image), image)
        if not imgtype:
            print('SKIP: Invalid image, not saving ' + name)
            return

        # Attach a file extension based on an image header
        ext = 'jpg' if imgtype == 'jpeg' else imgtype
        filename = name + '.' + ext

        md5_key = hashlib.md5(image).hexdigest()
        if md5_key in image_md5s:
            print('SKIP: Image is a duplicate of ' + image_md5s[md5_key] + ', not saving ' + filename)
            return

        i = 0
        while os.path.exists(os.path.join(output_dir, filename)):
            if hashlib.md5(open(os.path.join(output_dir, filename), 'rb').read()).hexdigest() == md5_key:
                print('SKIP: Already downloaded ' + filename + ', not saving')
                return
            i += 1
            filename = "%s-%d.%s" % (name, i, ext)

        image_md5s[md5_key] = filename

        img_sema.acquire()
        acquired_img_sema = True
        if limit is not None and len(tried_urls) >= limit:
            return

        imagefile = open(os.path.join(output_dir, filename), 'wb')
        imagefile.write(image)
        imagefile.close()
        print(" OK : " + filename)
        tried_urls.append(url)
    except Exception as e:
        print("FAIL: " + name, str(e))
    finally:
        pool_sema.release()
        if acquired_img_sema:
            img_sema.release()
        in_progress -= 1


def fetch_images_from_keyword(pool_sema: threading.Semaphore, img_sema: threading.Semaphore, keyword: str,
                              output_dir: str, filters: str, limit: int):
    global tried_urls
    global image_md5s
    global in_progress
    global urlopenheader
    current = 1
    last = ''
    while True:
        time.sleep(0.1)

        request_url = 'https://www.bing.com/images/async?q=' + urllib.parse.quote_plus(keyword) + '&first=' + str(
            current) + '&count=35&qft=' + ('' if filters is None else filters)
        request = urllib.request.Request(request_url, None, headers=urlopenheader)
        response = urllib.request.urlopen(request)
        html = response.read().decode('utf8')
        links = re.findall('murl&quot;:&quot;(.*?)&quot;', html)
        try:
            if links[-1] == last:
                return
            for index, link in enumerate(links):
                if limit is not None and len(tried_urls) >= limit:
                    exit(0)
                t = threading.Thread(target=download, args=(pool_sema, img_sema, link, output_dir, limit))
                t.start()
                current += 1
            last = links[-1]
        except IndexError:
            print('FAIL: No search results for "{0}"'.format(keyword))
            return


def backup_history(*args):
    global output_dir
    global tried_urls
    global image_md5s
    global in_progress
    global urlopenheader
    download_history = open(os.path.join(output_dir, 'download_history.pickle'), 'wb')
    pickle.dump(tried_urls, download_history)
    copied_image_md5s = dict(
        image_md5s)  # We are working with the copy, because length of input variable for pickle must not be changed during dumping
    pickle.dump(copied_image_md5s, download_history)
    download_history.close()
    print('history_dumped')
    if args:
        exit(0)


def main():
    global output_dir
    global tried_urls
    global image_md5s
    global in_progress
    global urlopenheader
    parser = argparse.ArgumentParser(description='Bing image bulk downloader')
    parser.add_argument('search_string', nargs="+", help='Keyword to search')
    parser.add_argument('-f', '--search-file', action='store_true', help='use search-string as a path to a file containing search strings line by line',
                        required=False)
    parser.add_argument('-o', '--output', help='Output directory', required=False)
    parser.add_argument('-a', '--adult-filter-off', help='Disable adult filter', action='store_true', required=False)
    parser.add_argument('--filters',
                        help='Any query based filters you want to append when searching for images, e.g. +filterui:license-L1', default='',
                        required=False)
    parser.add_argument('--limit', help='Make sure not to search for more than specified amount of images.',
                        required=False, type=int)
    parser.add_argument('-t', '--threads', help='Number of threads', type=int, default=20)
    args = parser.parse_args()
    print(vars(args))
    args.search_string = ' '.join(args.search_string)

    if args.output:
        output_dir = args.output
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)
    output_dir_origin = output_dir
    signal.signal(signal.SIGINT, backup_history)
    try:
        download_history = open(os.path.join(output_dir, 'download_history.pickle'), 'rb')
        tried_urls = pickle.load(download_history)
        image_md5s = pickle.load(download_history)
        download_history.close()
    except (OSError, IOError):
        tried_urls = []
    if args.adult_filter_off:
        urlopenheader['Cookie'] = 'SRCHHPGUSR=ADLT=OFF'
    pool_sema = threading.BoundedSemaphore(args.threads)
    img_sema = threading.Semaphore()

    
    if not args.search_file:
        fetch_images_from_keyword(pool_sema, img_sema, args.search_string, output_dir, args.filters, args.limit)
    else:
        try:
            inputFile = open(args.search_string)
        except (OSError, IOError):
            print("FAIL: Couldn't open file {}".format(args.search_string))
            exit(1)
        for keyword in inputFile.readlines():
            output_sub_dir = os.path.join(output_dir_origin, keyword.strip().replace(' ', '_'))
            if not os.path.exists(output_sub_dir):
                os.makedirs(output_sub_dir)
            fetch_images_from_keyword(pool_sema, img_sema, keyword, output_sub_dir, args.filters, args.limit)
            backup_history()
            time.sleep(10)
        inputFile.close()


if __name__ == "__main__":
    main()
