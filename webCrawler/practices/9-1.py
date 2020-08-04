from urllib.request import urlopen
from bs4 import BeautifulSoup
import re
import string
from collections import Counter


def cleanSentence(sentence):
    sentence = sentence.split(' ')
    sentence = [word.strip(string.punctuation + string.whitespace)
                for word in sentence]
    sentence = [word for word in sentence
                if len(word) > 1
                or (word.lower() == 'a' or word.lower() == 'i')]
    return sentence


def cleanInput(content):
    content = content.upper()
    content = re.sub('\n', ' ', content)
    content = bytes(content, 'UTF-8')
    content = content.decode('ascii', 'ignore')
    senteces = content.split('. ')
    return [cleanSentence(sentence) for sentence in senteces]


def getNgramsFromSentence(content, n):
    output = []
    for i in range(len(content) - n + 1):
        if not isCommon(content[i : i + n]):
            output.append(content[i : i + n])
    return output


def isCommon(ngram):
    commonWords = ['THE', 'BE', 'AND', 'OF', 'A', 'IN', 'TO', 'HAVE',
                   'IT', 'I', 'THAT', 'FOR', 'YOU', 'HE', 'WITH', 'ON',
                   'DO', 'SAY', 'THIS', 'THEY', 'IS', 'AN', 'AT', 'BUT', 'WE', 'HIS','FROM','THAT','NOT','BY','SHE','OR',
                   'AS','WHAT','GO','THEIR','CAN','WHO','GET','IF',
                   'WOULD','HER','ALL','MY','MAKE','ABOUT','KNOW','WILL',
                   'AS','UP','ONE','TIME','HAS','BEEN','THERE','YEAR','SO',
                   'THINK']
    for word in ngram:
        if word in commonWords:
            return True
    return False


def getNgrams(content, n):
    content = cleanInput(content)
    ngrams = Counter()
    ngrams_list = []
    for sentence in content:
        newNgrams = [' '.join(ngram) for ngram
                     in getNgramsFromSentence(sentence, n)]
        ngrams_list.extend(newNgrams)
        ngrams.update(newNgrams)
    return(ngrams)


speech = 'http://pythonscraping.com/files/inaugurationSpeech.txt'
content = str(urlopen(speech).read(), 'utf-8')
ngrams = getNgrams(content, 2)
print(ngrams)