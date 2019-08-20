from enum import Enum
import datetime


class LogType(Enum):
    DISABLE = 0,
    CONSOLE = 1,
    FILE = 2,
    CONSOLE_AND_FILE = 3


def current_time() -> str:
    return datetime.datetime.now().strftime('%d-%m-%y_%H-%M-%S')


def format_message(message) -> str:
    return ('[%s] ' + str(message)) % current_time()


class Log(object):
    """Класс для логирования событий в работе сервера.

    Аттрибуты:
        type_: тип логгирования (DISABLE, CONSOLE, ...)
    """

    def __init__(self, type_):
        self._init_path()
        self._type = type_

    def _init_path(self) -> None:
        filename_ = current_time()
        self._path = "./log/%s.ini" % filename_

    def change_type(self, type_) -> None:
        self._type = type_

    def write(self, message) -> None:
        file_ = open(self._path, 'a')
        message = format_message(message)
        if self._type == LogType.CONSOLE_AND_FILE:
            print(message)
            file_.write(message + '\n')
        elif self._type == LogType.CONSOLE:
            print(message)
        elif self._type == LogType.FILE:
            file_.write(message + '\n')
        file_.close()
