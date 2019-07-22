import configparser


class Config(object):
    """Класс для чтения конфигурационных файлов.

    Аттрибуты:
        configuration: название файла конфигурации (database, server, ...)
    """

    def __init__(self, configuration):
        self._config_name = 'config/%s.ini' % configuration

    def _parse_config(self) -> dict:
        _cfg_data = {}
        cfg = configparser.ConfigParser()
        cfg.read(self._config_name)
        for key in cfg['DEFAULT']:
            _cfg_data[key] = cfg['DEFAULT'][key]
        return _cfg_data

    def data(self) -> dict:
        """Получение словаря с полями и значениями из конфига."""
        return self._parse_config()
