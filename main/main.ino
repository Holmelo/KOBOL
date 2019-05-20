#include <Wire.h>
#include <SPI.h>
#include <Adafruit_Sensor.h>
#include <Adafruit_BME280.h>
#include <Adafruit_MMA8451.h>
#include <Adafruit_L3GD20_U.h>

#define SEALEVELPRESSURE_HPA (1013.25)

unsigned long loop_delay = 500;

/* Assign a unique ID to this sensor at the same time */
Adafruit_BME280 temperature;
Adafruit_MMA8451 accelerometer = Adafruit_MMA8451();
Adafruit_L3GD20_Unified gyro = Adafruit_L3GD20_Unified(20);

void setup() {
    Serial.begin(9600);

    setup_temperature();
    setup_accelerometer();
    setup_gyro();
    
}

void loop() { 
  
    print_temperature_values();
    print_accelerometer_values();
    print_gyro_values();
    
    delay(loop_delay);
}

void setup_gyro(){
  
  /* Enable auto-ranging */
  gyro.enableAutoRange(true);
  
  /* Initialise the sensor */
  bool status = gyro.begin();  
  if(!status)
  {
    Serial.println("Could not find a valid L3GD20 (gyro) sensor, check wiring!");
    while(1);
  }
  Serial.println("L3GD20 (gyro) sensor found!");
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
  
  bool status = accelerometer.begin(); 
  if (!accelerometer.begin()) {
    Serial.println("Could not find a valid MMA8451 (accelerometer) sensor, check wiring!");
    while (1);
  }
  Serial.println("MMA8451 (accelerometer) sensor found!");
  
  accelerometer.setRange(MMA8451_RANGE_2_G);
  
}

void print_gyro_values(){
    /* Get a new sensor event */ 
  sensors_event_t event; 
  gyro.getEvent(&event);
 
  /* Display the results (speed is measured in rad/s) */
  Serial.print("X: "); Serial.print(event.gyro.x); Serial.print("  ");
  Serial.print("Y: "); Serial.print(event.gyro.y); Serial.print("  ");
  Serial.print("Z: "); Serial.print(event.gyro.z); Serial.print("  ");
  Serial.println("rad/s ");
}

void print_accelerometer_values(){
  accelerometer.read();
  sensors_event_t event; 
  accelerometer.getEvent(&event);

  Serial.println("Accelerometer");
  Serial.println(event.acceleration.x);
  Serial.println(event.acceleration.y);
  Serial.println(event.acceleration.z);
}

void print_temperature_values() {
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
