from selenium import webdriver

options = webdriver.ChromeOptions()
options.add_argument('headless')
driver = webdriver.Chrome(chrome_options=options)

driver.get('http://pythonscraping.com')
driver.implicitly_wait(1)
print(driver.get_cookies())
