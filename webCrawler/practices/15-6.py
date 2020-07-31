from selenium import webdriver

options = webdriver.ChromeOptions()
options.add_argument('headless')
driver = webdriver.Chrome(options=options)

driver.get('http://www.pythonscraping.com/')
driver.get_screenshot_as_file('./pythonscraping.png')
