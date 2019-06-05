'''
https://learn.adafruit.com/adafruit-mma8451-accelerometer-breakout/python-circuitpython
https://github.com/adafruit/Adafruit_Python_BME280
https://learn.adafruit.com/adafruit-triple-axis-gyro-breakout/python-circuitpython
https://raspberrypi.stackexchange.com/questions/14153/adafruit-i2c-library-problem
'''

import time
import board
import busio
import adafruit_l3gd20
import adafruit_mma8451
import adafruit_bme280
import csv
import sys
import schedule

 
# Hardware I2C setup:
I2C = busio.I2C(board.SCL, board.SDA)
SENSOR_GYRO = adafruit_l3gd20.L3GD20_I2C(I2C)
SENSOR_ACCELEROMETER = adafruit_mma8451.MMA8451(I2C) 
SENSOR_TEMPERATURE = adafruit_bme280.Adafruit_BME280_I2C(I2C)
SENSOR_TEMPERATURE.sea_level_pressure = 1013.25

def createCSV():
    with open('mytest.csv', 'w', newline='') as f:
        thewriter = csv.writer(f)
        thewriter.writerow(['Temperature', 'Humidity', 'Pressure', 'Altitude', 'Gyro'])

def importCSV():
    with open('mytest.csv', 'a') as f:
        thewriter = csv.writer(f, sys.stdout, lineterminator='\n')
        thewriter.writerow([SENSOR_TEMPERATURE.temperature,
                            SENSOR_TEMPERATURE.humidity,
                            SENSOR_TEMPERATURE.pressure,
                            SENSOR_TEMPERATURE.altitude,
                            SENSOR_GYRO.gyro])
    f.close()

createCSV()
schedule.every(5).seconds.do(importCSV)


while True:
	print("\nTemperature: %0.1f C" % SENSOR_TEMPERATURE.temperature)
	print("Humidity: %0.1f %%" % SENSOR_TEMPERATURE.humidity)
	print("Pressure: %0.1f hPa" % SENSOR_TEMPERATURE.pressure)
	print("Altitude = %0.2f meters" % SENSOR_TEMPERATURE.altitude)	
	print('Angular Momentum (rad/s): {}'.format(SENSOR_GYRO.gyro))
	x, y, z = SENSOR_ACCELEROMETER.acceleration
	print('Acceleration: x={0:0.3f}m/s^2 y={1:0.3f}m/s^2 z={2:0.3f}m/s^2'.format(x, y, z))
	schedule.run_pending()
	time.sleep(1)


