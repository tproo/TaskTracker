import sys
import select
import socket
from command import Command

HOST = ''
SOCKET_LIST = []
RECV_BUFFER = 4096
PORT = 6789


def start_server():
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    server_socket.bind((HOST, PORT))
    server_socket.listen(10)

    SOCKET_LIST.append(server_socket)

    print('server has started')

    while 1:
        ready_to_read, ready_to_write, in_error = select.select(SOCKET_LIST, [], [], 0)

        for sock in ready_to_read:
            if sock == server_socket:
                sockfd, addr = server_socket.accept()
                SOCKET_LIST.append(sockfd)
                print("Client (%s, %s) connected" % addr)
                sockfd.send(str.encode('connected'))
            else:
                try:
                    data = sock.recv(RECV_BUFFER)
                    if data:
                        print("(%s:%s) sent:" % addr)
                        print(data)
                        com = Command(data)
                        result = com.execute()
                        sockfd.send(str.encode(result))
                    else:
                        if sock in SOCKET_LIST:
                            sock.close()
                            SOCKET_LIST.remove(sock)
                        print("Client (%s, %s) disconnected" % addr)
                except Exception as e:
                    print(e)
                    continue
    server_socket.close()


if __name__ == "__main__":
    sys.exit(start_server())
