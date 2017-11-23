#!/usr/bin/python3
import socket as socket

if __name__ == "__main__":
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect(("127.0.0.1",9991))
        data = s.recv(1024)
        print(data.decode(encoding='utf-8'))
        userString = ""
        while userString != "stop":
            print ("enter your message")
            userString = input()
            s.sendall(bytes(userString, 'utf-8'))
