from urllib.request import urlopen
from urllib.error import HTTPError, URLError
import requests
import datetime
import random
from bs4 import BeautifulSoup
import sys
import io
import re
from queue import Queue
sys.stdout = io.TextIOWrapper(sys.stdout.detach(), encoding='utf-8')


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


pages = set()

random.seed(datetime.datetime.now())


def scrape(url):
    try:
        try:
            req = requests.get(url)
            bs = BeautifulSoup(req.text, 'html.parser')
            # print(bs.prettify())
            title = bs.find('title').text
            body = bs.find('body')
            content = Content(url, title, body)
            if content is not None:
                print('Title: {}'.format(content.title))
                print('URL: {}\n'.format(content.url))
                # if content.body is not None:
                #     for line in content.body:
                #         print(line)
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
    except AttributeError:
        pass


urllist = []
file = open('webPages.txt', 'r', encoding='utf-8')
while True:
    line = file.readline()
    if not line:
        break
    line = line.replace('\n', '')
    if not re.match('^http', line):
        line = 'http://' + line
    urllist.append(line)
file.close()

for url in urllist:
    def getLinks(pageUrl):
        global pages
        try:
            html = urlopen(url + pageUrl)
            bs = BeautifulSoup(html, 'html.parser')
            try:
                print(bs.h1.get_text())
                print(bs.find(id='mw-content-text').findAll('p')[0])
                print(
                    bs.find(id='ca-edit').find('span').find('a').attrs['href'])
            except AttributeError:
                print('This page is missing something! No worries though!')
            for link in bs.findAll('a', href=re.compile('^(.)*')):
                if 'href' in link.attrs:
                    if link.attrs['href'] not in pages:
                        newPage = link.attrs['href']
                        print('------------\n' + newPage)
                        pages.add(newPage)
                        getLinks(newPage)
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


# scrape(url)
