import select
import socket
from config import Config
from request import Request
from log import Log, LogType


class Server(object):
    """Класс - TCP сервер.

    Сервер принимает сообщения пользователя, передает запрос в базу и возвращает результат запроса.

    Синтаксис пользовательского запроса:
    {
        "command": "command_type object_type",
        "object":
        [
            {
                "name1": "value1",
                "name2": "value2",
                ...
                "nameN": "valueN"
            }
        ]
    }

    Синтаксис ответа от сервера:
    {
        "objects":
        [
            {
                "name1": "value1",
                "name2": "value2",
                ...
                "nameN": "valueN"
            },
            ...,
            {
                "name1": "value1",
                "name2": "value2",
                ...
                "nameN": "valueN"
            }
        ]
    }
    """

    def __init__(self):
        self._load_config()
        self._sockets = {}
        self._socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self._log = Log(LogType.CONSOLE_AND_FILE)

    def _load_config(self) -> None:
        cfg_ = Config('server').data()
        self._address = ('', int(cfg_['port']))
        self._lister_count = int(cfg_['listen'])
        self._buffer_size = int(cfg_['buffer_size'])

    def start(self) -> int:
        self._socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self._socket.bind(self._address)
        self._socket.listen(self._lister_count)
        self._sockets[self._socket] = self._address
        self._log.write('server has started')
        return self._listen()

    def _listen(self) -> int:
        do_listen = True
        while do_listen:
            ready_to_read = select.select(list(self._sockets.keys()), [], [], 0)[0]

            for sock in ready_to_read:
                if sock == self._socket:
                    self._client_connected(self._socket.accept())
                else:
                    try:
                        self._client_read(sock)
                    except KeyboardInterrupt:
                        do_listen = False
                    except Exception as e:
                        self._log.write(e)
                        sock.send(str(e))
                        continue
        self._socket.close()
        return 0

    def _client_connected(self, client_data) -> None:
        socket_, address_ = client_data
        self._sockets[socket_] = address_
        self._log.write("Client (%s, %s) connected" % (address_[0], address_[1]))
        socket_.send(str.encode('connected'))

    def _client_read(self, socket_) -> None:
        data_ = socket_.recv(self._buffer_size)
        address_ = self._sockets[socket_]
        if data_:
            self._log.write("(%s:%s) sent:\n%s" % (address_[0], address_[1], data_))
            socket_.send(str.encode(Request(data_).send()))
        else:
            self._client_disconnected(socket_)

    def _client_disconnected(self, socket_) -> None:
        address_ = self._sockets[socket_]
        socket_.close()
        del self._sockets[socket_]
        self._log.write("Client (%s, %s) disconnected" % (address_[0], address_[1]))
