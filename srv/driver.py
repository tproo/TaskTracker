from psycopg2 import connect
from config import Config


class Driver(object):
    """Класс для работы с базой данных."""

    def __init__(self):
        self._load_config()
        self._driver = None

    def _load_config(self) -> None:
        cfg = Config('database').data()
        self._database = cfg['database']
        self._user = cfg['name']
        self._password = cfg['password']
        self._host = cfg['host']

    def execute_statement(self, statement) -> list:
        result = []
        self._open()
        with self._cursor() as cursor:
            cursor.execute(statement.data)
            if statement.command != 'get':
                self._commit()
            fields = [desc[0] for desc in cursor.description]
            records = cursor.fetchall()
            for record in records:
                result.append(dict(zip(fields, record)))
        self._close()
        return result

    def _commit(self) -> None:
        self._connection.commit()

    def _open(self) -> None:
        """Открытие соединения с базой."""
        self._connection = connect(user=self._user,
                password=self._password,
                host=self._host,
                database=self._database)

    def _close(self) -> None:
        self._connection.close()

    def _cursor(self) -> None:
        return self._connection.cursor()
