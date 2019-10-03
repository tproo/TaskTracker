import json
from driver import Driver
from statement import Statement


class Request(object):
    """Класс для выполнения запросов к базе neo4j"""

    def __init__(self, data):
        """Инициализация класса.
        На вход принимает строку с json'ом.
        """

        self._driver = Driver()
        self._json = json.loads(data)

    def execute(self) -> str:
        """Метод выполнениет запроса и возвращает json с результатом."""
        return json.dumps({'objects': self._execute_query()})

    def _execute_query(self) -> list:
        return self._driver.execute_statement(Statement(self._json))

