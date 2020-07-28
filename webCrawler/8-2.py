from collections import Counter
from urllib.request import urlopen
from bs4 import BeautifulSoup
import re
import string


def cleanSentence(sentence):
    sentence = sentence.split(' ')
    sentence = [word.strip(string.punctuation + string.whitespace)
                for word in sentence]
    sentece = [word for word in sentence if len(word) > 1 or (
        word.lower() == 'a' or word.lower() == 'i')]
    return sentence


def cleanInput(content):
    content = content.upper()
    content = re.sub('\n|[[\d+\]]', ' ', content)
    content = bytes(content, "UTF-8")
    content = content.decode('ascii', 'ignore')
    sentences = content.split('. ')
    return [cleanSentence(sentece) for sentece in sentences]


def getNgramsFromSentence(content, n):
    output = []
    for i in range(len(content) - n + 1):
        output.append(content[i:i + n])
    return output


def getNgrams(content, n):
    content = cleanInput(content)
    ngrams = Counter()
    ngrams_list = []
    for sentence in content:
        newNgrams = [' '.join(ngram) for ngram in getNgramsFromSentence(sentence, n)]
        ngrams_list.extend(newNgrams)
        ngrams.update(newNgrams)
    return(ngrams)


html = urlopen('http://en.wikipedia.org/wiki/Python_(programming_language)')
bs = BeautifulSoup(html, 'html.parser')
content = bs.find('div', {'id': 'mw-content-text'}).get_text()
ngrams = getNgrams(content, 2)
print(ngrams)
print('2-ngrams count is: ' + str(len(ngrams)))
