from urllib.request import urlopen
from bs4 import BeautifulSoup
import re
import random
from urllib.parse import parse_qsl, urljoin, urlparse
import requests
import datetime
import sys
import io
import threading
from urllib.error import HTTPError, URLError
sys.stdout = io.TextIOWrapper(sys.stdout.detach(), encoding='utf-8')

pages = set()
random.seed(datetime.datetime.now())

def getInternalLinks(bs, includeUrl):
    includeUrl = '{}://{}'.format(urlparse(includeUrl).scheme, urlparse(includeUrl).netloc)
    internalLinks = []
    for link in bs.findAll('a', href = re.compile('^(/|.*' + includeUrl + ')')):
        if link.attrs['href'] is not None:
            if link.attrs['href'] not in internalLinks:
                if(link.attrs['href'].startswith('/')):
                    internalLinks.append(includeUrl+link.attrs['href'])
                else:
                    internalLinks.append(link.attrs['href'])
    return internalLinks

def getExternalLinks(bs, excludeUrl):
    externalLinks = []
    for link in bs.findAll('a', href = re.compile('^(http|www)((?!' + excludeUrl + ').)*$')):
        if link.attrs['href'] is not None:
            if link.attrs['href'] not in externalLinks:
                externalLinks.append(link.attrs['href'])
    return externalLinks

def getRandomExternalLink(startingPage):
    html = urlopen(startingPage)
    bs = BeautifulSoup(html, 'html.parser')
    externalLinks = getExternalLinks(bs, urlparse(startingPage).netloc)
    if len(externalLinks) == 0:
        print('No external links, looking around site for one')
        domain = '{}://{}'.format(urlparse(startingPage).scheme, urlparse(startingPage).netloc)
        internalLinks = getInternalLinks(bs, domain)
        return getRandomExternalLink(internalLinks[random.randint(0, len(internalLinks) - 1)])
    else:
        return externalLinks[random.randint(0, len(externalLinks) - 1)]

def followExternalOnly(startingSite):
    externalLink = getRandomExternalLink(startingSite)
    print('Random external link is : {}'.format(externalLink))
    followExternalOnly(externalLink)

# followExternalOnly('http://oreilly.com')

allExtLinks = set()
allIntLinks = set()

def getAllExternalLinks(siteUrl):
    html = urlopen(siteUrl)
    domain = '{}://{}'.format(urlparse(siteUrl).scheme, urlparse(siteUrl).netloc)
    bs = BeautifulSoup(html, 'html.parser')
    internalLinks = getInternalLinks(bs, domain)
    externalLinks = getExternalLinks(bs, domain)

    for link in externalLinks:
        if link not in allExtLinks:
            allExtLinks.add(link)
            print(link)
    for link in internalLinks:
        if link not in allIntLinks:
            allIntLinks.add(link)
            getAllExternalLinks(link)


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


for url in urllist:
    allIntLinks.add(url)
    getAllExternalLinks(url)
