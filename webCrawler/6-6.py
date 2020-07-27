from random import shuffle
import re
import pymysql
import datetime
from urllib.request import urlopen
from bs4 import BeautifulSoup

conn = pymysql.connect(host='127.0.0.1', user='root',
                       passwd='1234', db='mysql', charset='utf8')

cur = conn.cursor()
cur.execute('use wikipedia')


def insertPageIfNotExists(url):
    cur.execute('select * from pages where url= %s', (url))
    if cur.rowcount == 0:
        cur.execute('insert into pages (url) values (%s)', (url))
        conn.commit()
        return cur.lastrowid
    else:
        return cur.fetchone()[0]


def loadPages():
    cur.execute('select * from pages')
    pages = [row[1] for row in cur.fetchall()]
    return pages


def insertLink(fromPageId, toPageId):
    cur.execute('select * from links where fromPageId = %s and toPageId = %s',
                (int(fromPageId), int(toPageId)))
    if cur.rowcount == 0:
        cur.execute('insert into links (fromPageId, toPageId) values (%s, %s)', (int(
            fromPageId), int(toPageId)))
        conn.commit()


def pageHasLinks(pageId):
    cur.execute('select * from links where fromPageId = %s', (int(pageId)))
    rowcount = cur.rowcount
    if rowcount == 0:
        return False
    return True


def getLinks(pageUrl, recursionLevel, pages):
    if recursionLevel > 4:
        return

    pageId = insertPageIfNotExists(pageUrl)
    html = urlopen('http://en.wikipedia.org{}'.format(pageUrl))
    bs = BeautifulSoup(html, 'html.parser')
    links = bs.findAll('a', href=re.compile('^(/wiki/)((?!:).)*$'))
    links = [link.attrs['href'] for link in links]

    for link in links:
        linkId = insertPageIfNotExists(link)
        insertLink(pageId, linkId)
        if not pageHasLinks(linkId):
            print("PAGE HAS NO LINKS: {}".format(link))
            pages.append(link)
            getLinks(link, recursionLevel + 1, pages)


getLinks('/wiki/Kevin_Bacon', 0, loadPages())
cur.close()
conn.close()
