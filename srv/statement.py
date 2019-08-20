def params2str(params, delimiter='=', prefix='', delimiter2=',') -> str:
    result = ''
    for key, value in params.data.items():
        result += prefix + key + delimiter
        if type(value) == str:
            result += '\"' + value + '\"'
        else:
            result += str(value)
        result += delimiter2 + ' '
    return result[:-(len(delimiter2)+1)]


class Statement(object):
    """Класс для формирование строки запроса.

    Формирует из пользовательского json'а строку запроса к базе neo4j.

    Аттрибуты:
        command: объект класса Command.
    """

    def __init__(self, command):
        self._statement = []
        self._init_statement(command)

    @property
    def data(self) -> list:
        return self._statement

    def _init_statement(self, command) -> None:
        type_ = command.type
        if type_ == 'create':
            self._init_create(command)
        elif type_ == 'get':
            self._init_match(command)
        elif type_ == 'delete':
            self._init_delete(command)
        elif type_ == 'change':
            self._init_set(command)
        # else: except

    def _init_create(self, command) -> None:
        object_ = command.object
        data_ = params2str(params=object_, delimiter=':')
        self._statement.append("""CREATE (n:%s {%s}) RETURN n; """ % (object_.type, data_))
        self._create_relations(object_)

    def _init_match(self, command) -> None:
        object_ = command.object
        data_ = params2str(params=object_, delimiter=':')
        self._statement.append("""MATCH (n:%s {%s}) RETURN n;""" % (object_.type, data_))

    def _init_delete(self, command) -> None:
        object_ = command.object
        data_ = params2str(params=object_, delimiter=':')
        self._statement.append("""MATCH (n:%s {%s}) DETACH DELETE n;""" % (object_.type, data_))

    def _init_set(self, command) -> None:
        objects_ = command.objects
        object_type = objects_['new'].type
        old_data_ = params2str(params=objects_['old'], delimiter=':')
        new_data_ = params2str(params=objects_['new'], prefix='n.')
        self._statement.append("""MATCH (n:%s {%s}) SET %s return n;""" % (object_type, old_data_, new_data_))
        self._create_relations(objects_['new'])

    def _create_relations(self, object_) -> None:
        self._statement.append("""MATCH (a), (b) WHERE a.uuid="%s" AND b.uuid="%s" CREATE UNIQUE (a)-[r:%s]->(b);""" %
                               (object_.data['uuid'], object_.data['assigned_to'], 'assigned_to'))
        self._statement.append("""MATCH (a), (b) WHERE a.uuid="%s" AND b.uuid="%s" CREATE UNIQUE (a)-[r:%s]->(b);""" %
                               (object_.data['parent'], object_.data['uuid'], 'parent'))
