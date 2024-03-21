from socket import *

sock = socket()
sock.bind(('0.0.0.0', 8001))
sock.listen()

while True:
    (cSock, cAddr) = sock.accept()
    data = cSock.recv(1024).decode()
    page = data.split()[1][1:]
    header = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\nContent-Length: "
    try:
        b = open(page, "rb").read()
        header += str(len(b)) + "\r\n\r\n"
        cSock.send(header.encode())
        cSock.send(b)
    except OSError:
        header = "HTTP/1.1 404 Not Found\r\n\r\n"
        cSock.send(header.encode())
    cSock.close()