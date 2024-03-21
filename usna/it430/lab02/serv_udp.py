from socket import *
from select import select
import sys

sock = socket(type=SOCK_DGRAM)
sock.bind(('0.0.0.0', 9000))
addr = ('127.0.0.1', 9000)

while True:
    buffers = [sock, sys.stdin]
    (r, w, e) = select(buffers, [], [])

    if sock in r:
        data = sock.recvfrom(1024)
        addr = data[1]
        print(f"From ('{data[1][0]}', {data[1][1]}) : {data[0].decode()}", end="")
    
    if sys.stdin in r:
        s = input()
        if s == "quit":
            break
        s += "\n"
        sock.sendto(s.encode(), addr)