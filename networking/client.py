#!/usr/bin/python3
import socket as socket

if __name__ == "__main__":
    with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
        userString = ""
        while userString != "stop":
            print ("enter your message")
            userString = input()
            s.sendto(bytes(userString, 'utf-8'),("127.0.0.1",9999))
