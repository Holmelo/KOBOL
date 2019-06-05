import csv
import sys
import time
import schedule 

def createCSV():
    with open('mytest.csv', 'w', newline='') as f:
        thewriter = csv.writer(f)
        thewriter.writerow(['Temperature', 'Humidity', 'Pressure', 'Altitude', 'Gyro'])
    
def importCSV():
    with open('mytest.csv', 'a') as f:
        thewriter = csv.writer(f, sys.stdout, lineterminator='\n')
        thewriter.writerow(['10', '9', '8', '7', '6'])
    f.close()
         
createCSV()

schedule.every(5).seconds.do(importCSV)

while True:
    schedule.run_pending()
    time.sleep(1)
