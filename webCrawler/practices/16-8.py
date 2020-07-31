from urllib.request import urlopen
from bs4 import BeautifulSoup
import re
import random

from multiprocessing import Process
import os
import time

visited = []


def get_links(bs):
    print('Getting links in {}'.format(os.getpid()))
    links = bs.find('div', {'id': 'bodyContent'}).find_all(
        'a', href=re.compile('^(/wiki/)((?!:).)*$'))
    return [link for link in links if link not in visited]


def scrape_article(path):
    visited.append(path)
    print("Process {} list is now: {}".format(os.getpid(), visited))
    html = urlopen('http://en.wikipedia.org{}'.format(path))
    time.sleep(3)
    bs = BeautifulSoup(html, 'html.parser')
    title = bs.find('h1').get_text()
    print('Scraping {} in process {}'.format(title, os.getpid()))
    links = get_links(bs)
    if len(links) > 0:
        newArticle = links[random.randint(0, len(links) - 1)].attrs['href']
        print(newArticle)
        scrape_article(newArticle)


processes = []
if __name__ == '__main__':
    processes.append(Process(target=scrape_article,
                             args=('/wiki/Kevin_Bacon',)))
    processes.append(Process(target=scrape_article,
                             args=('/wiki/Monty_Python',)))

for p in processes:
    p.start()
