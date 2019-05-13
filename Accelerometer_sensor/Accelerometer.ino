#include <Adafruit_MMA8451.h>
#include <Adafruit_Sensor.h>

Adafruit_MMA8451 sensor_1 = Adafruit_MMA8451();
int x, y, z;
int xavg, yavg, zavg;
int xttl, yttl, zttl;
int count = 0;

void setup(void) {
  Serial.begin(9600);
  Serial.println("Adafruit MMA8451 test!");
  sensor_1.begin();
  sensor_1.setRange(MMA8451_RANGE_2_G);
}

void loop() {
  count += 1;
  sensor_1.read();
  sensors_event_t event; 
  sensor_1.getEvent(&event);
  x = event.acceleration.x;
  y = event.acceleration.y;
  z = event.acceleration.z;
  xttl += x;
  yttl += y;
  zttl += z;
  xavg = xttl/count;
  yavg = yttl/count;
  zavg = zttl/count;
  Serial.println("current");
  Serial.println(x);
  Serial.println(y);
  Serial.println(z);
  Serial.println();
  Serial.println("average");
  Serial.println(xavg);
  Serial.println(yavg);
  Serial.println(zavg);
  Serial.println();
  delay(500);

}
