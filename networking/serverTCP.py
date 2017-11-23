#!/usr/bin/python3
import socket
import threading

class ThreadedServer(threading.Thread):

    def __init__(self,ip,port,socket):
        threading.Thread.__init__(self)
        self.ip = ip
        self.port = port
        self.socket = socket
        print("connection established with", ip)

    def run(self):
        self.socket.send(bytes("welcome to the server you are connected to" + str(self.port),'utf-8'))
        while True:
            data = self.socket.recv(1024)
            print(data.decode(encoding='utf-8'))
            if not data: break

if __name__ == "__main__":
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.bind(("127.0.0.1",9991))
        s.listen(1)
        thread = []
        while True:
            conn,addr = s.accept()
            t = ThreadedServer(addr[0],addr[1],conn)
            t.start()
            thread.append(t)
