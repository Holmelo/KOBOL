#include <Wire.h>
#include <SPI.h>
#include <Adafruit_Sensor.h>
#include <Adafruit_BME280.h>
#include <Adafruit_MMA8451.h>
#include <Adafruit_L3GD20_U.h>
#include <CurieBLE.h>
#include <CurieIMU.h>

BLEPeripheral blePeripheral;                                    // BLE Peripheral Device (the board you're programming)
BLEService ledService("19B10000-E8F2-537E-4F6C-D104768A1214");  // BLE LED Service

// BLE LED Switch Characteristic - custom 128-bit UUID, read and writable by central
BLEUnsignedCharCharacteristic switchCharacteristic("19B10001-E8F2-537E-4F6C-D104768A1214", BLERead | BLEWrite);

const int ledPin = 13; // pin to use for the LED

#define SEALEVELPRESSURE_HPA (1013.25)

unsigned long loop_delay = 500;

Adafruit_BME280 temperature;
Adafruit_MMA8451 accelerometer = Adafruit_MMA8451();
Adafruit_L3GD20_Unified gyro = Adafruit_L3GD20_Unified(20);

void setup() {
    Serial.begin(9600);

    CurieIMU.begin();
      
    setup_bluetooth();
    setup_gyro();
    setup_accelerometer();
    setup_temperature();
}

void loop() { 
  
  BLECentral central = blePeripheral.central();
  
  if (central) {
    Serial.print("Connected to central: ");
    Serial.println(central.address());
  
    while (central.connected()) {

      if (switchCharacteristic.written()) {
        if (switchCharacteristic.value()) {   // any value other than 0
          Serial.println("LED on");
          digitalWrite(ledPin, HIGH);         // will turn the LED on
        } else {                              // a 0 value
          Serial.println(F("LED off"));
          digitalWrite(ledPin, LOW);          // will turn the LED off
        }
      }
    }
  
    Serial.print(F("Disconnected from central: "));
    Serial.println(central.address());
  }
  
   print_temperature();
   delay(loop_delay);
}

void setup_bluetooth(){
  Serial.begin(9600);
  
  pinMode(ledPin, OUTPUT);
  
  blePeripheral.setLocalName("Sleep Monitor Device");
  blePeripheral.setAdvertisedServiceUuid(ledService.uuid());
  
  blePeripheral.addAttribute(ledService);
  blePeripheral.addAttribute(switchCharacteristic);
  
  switchCharacteristic.setValue(0);
  
  blePeripheral.begin();
  
  Serial.println("BLE LED Peripheral");
}

void setup_gyro(){
  CurieIMU.setGyroRange(250);
}

void setup_temperature(){
  
    bool status = temperature.begin();  
    if (!status) {
        Serial.println("Could not find a valid BME280 (temperature) sensor, check wiring!");
        while (1);
    }
    Serial.println("BME280 (temperature) sensor found!");
}

void setup_accelerometer(){
  
  CurieIMU.setAccelerometerRange(2);
  
}

void print_gyro(){
  float gx, gy, gz; //scaled Gyro values

  // read gyro measurements from device, scaled to the configured range
  CurieIMU.readGyroScaled(gx, gy, gz);

  // display tab-separated gyro x/y/z values
  Serial.print("g:\t");
  Serial.print(gx);
  Serial.print("\t");
  Serial.print(gy);
  Serial.print("\t");
  Serial.print(gz);
  Serial.println();
}

void print_accelerometer(){
  float ax, ay, az;   //scaled accelerometer values

  // read accelerometer measurements from device, scaled to the configured range
  CurieIMU.readAccelerometerScaled(ax, ay, az);

  // display tab-separated accelerometer x/y/z values
  Serial.print("a:\t");
  Serial.print(ax);
  Serial.print("\t");
  Serial.print(ay);
  Serial.print("\t");
  Serial.print(az);
  Serial.println();
}

void print_temperature() {
    Serial.print("Temperature = ");
    Serial.print(temperature.readTemperature());
    Serial.println(" *C");
    
    Serial.print("Pressure = ");
    Serial.print(temperature.readPressure() / 100.0F);
    Serial.println(" hPa");
    
    Serial.print("Approx. Altitude = ");
    Serial.print(temperature.readAltitude(SEALEVELPRESSURE_HPA));
    Serial.println(" m");
    
    Serial.print("Humidity = ");
    Serial.print(temperature.readHumidity());
    Serial.println(" %");

    Serial.println();
}
