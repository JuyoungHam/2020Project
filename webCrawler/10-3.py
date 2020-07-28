import requests
from requests.auth import AuthBase
from requests.auth import HTTPBasicAuth

auth = HTTPBasicAuth('ryan', 'password')
url = 'http://pythonscraping.com/pages/auth/login.php'
r = requests.post(url=url, auth=auth)
print(r.text)
