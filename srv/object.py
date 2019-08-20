from uuid import uuid4


class Object(object):
    """Класс для обработки объекта из пользовательского json'а.

    Аттрибуты:
        object_data: поле "object" из json'а.
        command_type: пользовательская комманда (create, get, update, delete, ...)
        object_type: тип объекта (task, epic, profile, command, ...)
    """

    def __init__(self, object_data, command_type, object_type):
        self.__dict__.update(object_data)
        self.__dict__['type'] = object_type
        self._add_uuid(command_type)

    def _add_uuid(self, command_type) -> None:
        if command_type == "create":
            self.__dict__['uuid'] = str(uuid4())

    @property
    def type(self) -> str:
        """Возвращает тип объекта."""
        return self.__dict__['type']

    @property
    def data(self) -> dict:
        """Возвращает словарь с "name": "value" полученного объекта."""
        return self.__dict__
