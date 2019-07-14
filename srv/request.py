from neo4j import GraphDatabase
from task import Task


class Request(object):
    def __init__(self, task, uri='bolt://localhost:7687', user='neo4j', password='!QAZ2wsx'):
        self.__driver = GraphDatabase.driver(uri, auth=(user, password))
        self.__task = task

    def close(self):
        self.__driver.close()

    def create_task(self):
        with self.__driver.session() as session:
            result = session.run("CREATE (t:Task {"
                                 "name: $name, "
                                 "description: $description, "
                                 "parent: $parent, "
                                 "assigned_to: $assigned_to}) "
                                 "RETURN t",
                                 name=self.__task.name,
                                 description=self.__task.description,
                                 parent=self.__task.parent,
                                 assigned_to=self.__task.assigned_to
                                 )
            for record in result:
                return Task(record[0])
