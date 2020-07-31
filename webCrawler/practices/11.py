from selenium import webdriver
import time
options = webdriver.ChromeOptions()
options.add_argument('headless')
driver = webdriver.Chrome(
    executable_path='./scripts/chromedriver', chrome_options=options)

url = 'http://pythonscraping.com/pages/javascript/ajaxDemo.html'
driver.get(url)
time.sleep(3)
print(driver.find_element_by_id('content').text)
driver.close()
