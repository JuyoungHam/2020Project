from urllib.request import urlopen
from io import StringIO
import csv

url = 'http://pythonscraping.com/files/MontyPythonAlbums.csv'
data = urlopen(url).read().decode('ascii', 'ignore')
dataFile = StringIO(data)
csvReader = csv.reader(dataFile)

for row in csvReader:
    print('The album "{}" was released in {}'.format(row[0], str(row[1])))
