import json
from request import Request
from task import Task


class Command(object):
    def __init__(self, data):
        self.data = data
        self.command = ''
        self.object = {}
        self.__result = ''
        self.__parse()

    def __parse(self):
        tmp = json.loads(self.data)
        self.command = tmp['command']
        self.object = tmp['object']

    def execute(self):
        r = Request(Task(self.object))
        task = r.create_task()
        result = [task.json]
        self.__result = json.dumps({'objects': result})
        r.close()
        return self.__result
