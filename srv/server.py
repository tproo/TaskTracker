import select
import socket
from request import Request


class Server:
    def __init__(self):
        self._socket_list = []
        self._socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self._buffer_size = 4096

    def start(self):
        self._socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self._socket.bind(('', 6789))
        self._socket.listen(10)

        self._socket_list.append(self._socket)

        print('server has started')
        self._listen()
        return 0

    def _listen(self):
        while True:
            ready_to_read = select.select(self._socket_list, [], [], 0)[0]

            for sock in ready_to_read:
                if sock == self._socket:
                    client_socket, client_address = self._socket.accept()
                    self._socket_list.append(client_socket)
                    print("Client (%s, %s) connected" % (client_address[0], client_address[1]))
                    client_socket.send(str.encode('connected'))
                else:
                    try:
                        data = sock.recv(self._buffer_size)
                        if data:
                            print("(%s:%s) sent:" % (client_address[0], client_address[1]))
                            print(data)
                            result = Request(data).send()
                            client_socket.send(str.encode(result))
                        else:
                            if sock in self._socket_list:
                                sock.close()
                                self._socket_list.remove(sock)
                            print("Client (%s, %s) disconnected" % (client_address[0], client_address[1]))
                    except Exception as e:
                        print(e)
                        continue
        self._socket.close()
