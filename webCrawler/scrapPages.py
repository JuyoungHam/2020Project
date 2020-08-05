import requests
import datetime
from bs4 import BeautifulSoup
import sys
import io
import re
from urllib.request import urlopen
from urllib.error import HTTPError, URLError
sys.stdout = io.TextIOWrapper(sys.stdout.detach(), encoding='utf-8')


class Content:
    def __init__(self, url, title, body):
        self.url = url
        self.title = title
        self.body = body


class Website:
    def __init__(self, name, url, titleTag, bodyTag):
        self.name = name
        self.url = url
        self.titleTag = titleTag
        self.bodyTag = bodyTag

temp = []
def scrape(url):
    global temp
    try:
        try:
            req = requests.get(url)
            bs = BeautifulSoup(req.text, 'html.parser')
            # print(bs.prettify())
            items = bs.findAll(['div', 'span', 'p'],
                               string=re.compile('.전시(?!관)(?!실)|특별전|기획전|展'))
            file = open('contents1.txt', 'a+', encoding='utf-8')
            for item in items:
                if item.text not in temp:
                    temp.append(item.text)
                    if item.parent.text is not None:
                        if item.parent.text not in temp:
                            temp.append(item.parent.text)
                            print(item.parent.text)
                            file.write(item.parent.text)
            file.write("-----------------------페이지끝---------------------------\n")
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
    except AttributeError:
        pass


urllist = []
file = open('pages.txt', 'r', encoding='utf-8')
while True:
    line = file.readline()
    if not line:
        break
    line = line.replace('\n', '')
    urllist.append(line)
file.close()

for url in urllist:
    scrape(url)
