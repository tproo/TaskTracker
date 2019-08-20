import json
from driver import Driver
from command import Command
from statement import Statement


class Request(object):
    """Класс для выполнения запросов к базе neo4j"""

    def __init__(self, data):
        """Инициализация класса.
        На вход принимает строку с json'ом.
        """

        self._driver = Driver()
        self._command = Command(json.loads(data))

    def send(self) -> str:
        """Метод выполнениет запроса и возвращает json с результатом."""
        return json.dumps({'objects': self._execute_query()})

    def _execute_query(self) -> list:
        result = []
        self._driver.open()
        with self._driver.session() as session:
            for query in Statement(self._command).data:
                records = session.run(query)
                for record in records:
                    result.append(dict(record[0]))
        self._driver.close()
        return result
