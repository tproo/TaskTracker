from neo4j import Node
import json


class Task(object):
    def __init__(self, obj):
        self.__name = obj['name']
        self.__description = obj['description']
        self.__parent = obj['parent']
        self.__assigned_to = obj['assigned_to']
        self.__id = 0
        if isinstance(obj, Node):
            self.__id = obj.id

    @property
    def name(self):
        return self.__name

    @property
    def description(self):
        return self.__description

    @property
    def parent(self):
        return self.__parent

    @property
    def assigned_to(self):
        return self.__assigned_to

    @property
    def id(self):
        return self.__id

    @property
    def json(self):
        data = {'id': self.id,
                'name': self.name,
                'description': self.description,
                'parent': self.parent,
                'assigned_to': self.assigned_to}
        return json.dumps(data)
