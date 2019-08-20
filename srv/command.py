import re
from object import Object


class Command(object):
    """Класс для обработки пользовательской комманды. Принимает json."""

    def __init__(self, data):
        self._type_init(re.split(' ', data['command']))
        self._object_init(data)

    def _type_init(self, list_) -> None:
        self._type = list_[0].lower()
        self._object_type = list_[1].lower()

    def _object_init(self, data):
        if 'object' in data:
            self._object = Object(object_data=data['object'], command_type=self._type, object_type=self._object_type)
        else:
            self._objects = {
                'old': data[0],
                'new': data[1]
            }

    @property
    def type(self) -> str:
        """Возвращает тип команды. (create, get, update, ...)"""
        return self._type

    @property
    def object(self) -> Object:
        """Возвращает объект из пользовательского json'а. (task, epic, profile, ...)"""
        return self._object

    @property
    def objects(self) -> dict:
        """Возвращает словарь с новым и старым объектом."""
        return self._objects
