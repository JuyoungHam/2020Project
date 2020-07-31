import smtplib
from email.mime.text import MIMEText

msg = MIMEText('the body of the email is here')

msg['Subject'] = 'An Email Alert'
msg['from'] = 'ryan@pythonscraping.com'
msg['To'] = 'hamjuyoung11@gmail.com'

s = smtplib.SMTP('localhost')
s.send_message(msg)
s.quit()
