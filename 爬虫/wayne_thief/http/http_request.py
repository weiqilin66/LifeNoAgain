import requests


class HttpUtil(object):
    def __init__(self, url):
        self.base_url = url

    def get(self, path):
        url = self.base_url + path
        requests.get(url=url)