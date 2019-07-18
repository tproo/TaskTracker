import json
from request import Request


class Command(object):
    def __init__(self, data):
        self._data = data
        self._result = ''
        self._parse()

    def _parse(self):
        tmp = json.loads(self._data)
        self.command = tmp['command']
        self.object = tmp['object']

    def execute(self):
        r = Request(self.object)
        objects = r.send()
        self._result = json.dumps({'objects': objects})
        return self._result
