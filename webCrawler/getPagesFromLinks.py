from urllib.request import urlopen
from urllib.error import HTTPError, URLError
import requests
import datetime
import random
from bs4 import BeautifulSoup
import sys
import io
import re
from urllib.parse import parse_qsl, urljoin, urlparse
sys.stdout = io.TextIOWrapper(sys.stdout.detach(), encoding='utf-8')

session = requests.Session()
headers = {
    'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5)'
    'AppleWebKit 537.36 (KHTML, like Gecko) Chrome',
    'Accept': 'text/html, application/xhtml+xml,application/xml;'
    'q=0.9,image/webp,*/*;q=0.8'
}


class Content:
    def __init__(self, url, title, body):
        self.url = url
        self.title = title
        self.body = body

    def print(self):
        print("URL: {}".format(self.url))
        print("TITLE: {}".format(self.title))
        print("BODY:\n{}".format(self.body))


class Website:
    def __init__(self, name, url, titleTag, bodyTag):
        self.name = name
        self.url = url
        self.titleTag = titleTag
        self.bodyTag = bodyTag


urllist = []
file = open('urllist.txt', 'r', encoding='utf-8')
while True:
    line = file.readline()
    if not line:
        break
    line = line.replace('\n', '')
    if not re.match('^http', line):
        line = 'http://' + line
    urllist.append(line)
file.close()


def getInternalLinks(bs, includeUrl):
    includeUrl = '{}://{}'.format(urlparse(includeUrl).scheme,
                                  urlparse(includeUrl).netloc)
    internalLinks = []
    for link in bs.findAll('a', href=re.compile('^(/|.*' + includeUrl + ')')):
        if link.attrs['href'] is not None:
            if link.attrs['href'] not in internalLinks:
                if(link.attrs['href'].startswith('/')):
                    internalLinks.append(includeUrl + link.attrs['href'])
                else:
                    internalLinks.append(link.attrs['href'])
    return internalLinks


pages = []


def getLinks(url):
    try:
        req = requests.get(url)
        bs = BeautifulSoup(req.text, 'html.parser')
        internalLinks = getInternalLinks(bs, url)
        file = open('pages.txt', 'a+', encoding='utf-8')
        for link in internalLinks:
            if not re.match('^http', link):
                link = 'http://' + link
            if link not in pages:
                pages.append(link)
                file.write(link + "\n")
        file.close()
    except HTTPError as errh:
        print("Http Error:", errh)
    except URLError as e:
        print('URLError:', e)
    except requests.exceptions.ConnectionError as errc:
        print("Error Connecting:", errc)
    except requests.exceptions.Timeout as errt:
        print("Timeout Error:", errt)
    except requests.exceptions.RequestException as err:
        print("OOps: Something Else", err)


for url in urllist:
    getLinks(url)
