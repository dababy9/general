from socket import *
import sys

sock = socket()
hostname = sys.argv[1]
sock.connect((gethostbyname(hostname), 80))

req = f"GET / HTTP/1.1\r\nHost: {hostname}\r\nAccept-Language: en-us\r\nConnection: close\r\nUser-Agent: Mozilla/4.0\r\n\r\n"
sock.send(req.encode())
msg = sock.recv(1024)
while msg:
    print(msg.decode(), end="")
    msg = sock.recv(1024)
print("")