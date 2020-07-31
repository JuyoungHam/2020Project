import smtplib
from email.mime.text import MIMEText
from bs4 import BeautifulSoup
from urllib.request import urlopen
import time



def sendMail(subject, body):
    msg = MIMEText('the body of the email is here')
    msg['Subject'] = 'An Email Alert'
    msg['from'] = 'ryan@pythonscraping.com'
    msg['To'] = 

    server = smtplib.SMTP('smtp.gmail.com:587')
    server.ehlo()
    server.starttls()
    server.login()
    server.sendmail(,, msg)
    server.quit()

bs = BeautifulSoup(urlopen("http://isitchristmas.com/"), 'html.parser')
while(bs.find("a", {"id" : "answer"}).attrs['title'] == "NO"):
    print("It is not Christmas yet.")
