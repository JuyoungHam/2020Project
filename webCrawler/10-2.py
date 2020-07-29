import requests
session = requests.Session()

params = {'username': 'Ryan', 'password': 'password'}
welcome_page = 'http://pythonscraping.com/pages/cookies/welcome.php'
s = session.post(welcome_page, params)

print(s.cookies.get_dict())
print('Going to profile page...')

profile_page = 'http://pythonscraping.com/pages/cookies/profile.php'
s = session.get(profile_page)
print(s.text)
