import re
import importlib


def parse_arguments(object_, delimiter = 'and') -> str:
    statement = ''
    for key, value in object_.items():
        statement += key + " = '" + value + "' {} ".format(delimiter)
    return statement[:-(len(delimiter) + 2)]


class Statement(object):
    """Класс для формирование строки запроса.

    Формирует из пользовательского json'а строку запроса к базе.
    """

    def __init__(self, json_):
        self._statement = ''
        self._json = json_
        self._command = [x.lower() for x in re.split(' ', json_['command'])]
        self._init_statement()

    @property
    def data(self) -> str:
        return self._statement

    @property
    def command(self) -> str:
        return self._command[0]

    def _init_statement(self) -> None:
        type_ = self._command[0]
        if type_ == 'create':
            self._init_insert()
        elif type_ == 'get':
            self._init_select()
        elif type_ == 'delete':
            self._init_delete()
        elif type_ == 'change':
            self._init_update()
        # else: except

    def _init_insert(self) -> None:
        fields = ', '.join(list(self._json['object'].keys()))
        values = ''
        for val in list(self._json['object'].values()):
            values += "'{}', ".format(val)
        values = values[:-2]
        self._statement = """insert into {} ({}) values ({}) returning *""".format(
            'tasks',
            fields,
            values)

    def _init_select(self) -> None:
        if len(self._json['object']) > 0:
            statement = parse_arguments(self._json['object'])
            self._statement = """select * from {} where {}""".format(
                'tasks',
                statement)
        else:
            self._statement = """select * from {}""".format(
                'tasks')

    def _init_delete(self) -> None:
        statement = parse_arguments(self._json['object'])
        self._statement = """delete from {} where {}""".format(
            'tasks',
            statement)

    def _init_update(self) -> None:
        old_object = parse_arguments(self._json[0], ',')
        new_object = parse_arguments(self._json[1])
        self._statement = """update {} set {} where {}""".format(
            'tasks',
            new_object,
            old_object)

