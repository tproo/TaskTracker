from neo4j import GraphDatabase
import json
import re
import uuid


class Request(object):
    def __init__(self, data, uri='bolt://localhost:7687', user='neo4j', password='!QAZ2wsx'):
        self._driver = GraphDatabase.driver(uri, auth=(user, password))
        data = json.loads(data)
        self._command_list = re.split(' ', data['command'])
        self._object = data['object']

    def _close(self):
        self._driver.close()

    def send(self):
        result = []
        if self._command_list[0].lower() == "create":
            result = self._create(self._command_list[1].title())
        elif self._command_list[0].lower() == "get":
            result = self._get(self._command_list[1].title())

        self._close()
        return json.dumps({'objects': result})

    def _create(self, type_):
        self._object['uuid'] = str(uuid.uuid4())
        statement = """CREATE (t:%s {dict_params}) RETURN t""" % type_
        return self._execute_query(statement)

    def _get(self, type_):
        statement = """MATCH (t:%s) WHERE {dict_params} RETURN t""" % type_
        return self._execute_query(statement)

    def _execute_query(self, statement):
        result = []
        with self._driver.session() as session:
            query = session.run(statement, dict_params=self._object)
            for record in query:
                result.append(dict(record[0]))
        return result
