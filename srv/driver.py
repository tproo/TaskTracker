from neo4j import GraphDatabase
from config import Config


class Driver(object):
    """Класс для работы с базой neo4j."""

    def __init__(self):
        self._load_config()
        self._driver = None

    def _load_config(self) -> None:
        cfg = Config('database').data()
        self._uri = cfg['uri']
        self._user = cfg['user']
        self._password = cfg['password']

    def open(self) -> None:
        """Открытие соединения с базой."""
        self._driver = GraphDatabase.driver(uri=self._uri, auth=(self._user, self._password))

    def close(self) -> None:
        """Закрытие соединения."""
        self._driver.close()

    def session(self) -> None:
        """Получение сессии для отправки запроса в базу."""
        return self._driver.session()
