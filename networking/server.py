#!/usr/bin/python3
import socket

if __name__ == "__main__":
    with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
        s.bind(("0.0.0.0",9999))
        while True:
            data, conn = s.recvfrom(1024)
            print(data.decode(encoding='utf-8'))
            if not data: break
