/* serial_test
   ===========
   
   Author: Ricardo Mendonça Ferreira
   Date..: 2009-12-29
   
   This Arduino sketch was written to test the serial communication
   between an Arduino with a BlueSMiRF Bluetooth modem and a
   Linux machine (PC or the Nokia N900). It will increment and send
   a number every second, and reply any message received. For more
   info, see: http://www.flickr.com/photos/ricardo_ferreira /4225911933/
*/

#define BUFFERSIZE 127
uint8_t inBuffer[BUFFERSIZE];
int inLength; // length of data in the buffer
int numLoop = 0; // number of times we looped
int ledPin = 13;

void setup() {
  Serial.begin(115200);
  pinMode(ledPin, OUTPUT);
} 

void loop() {
  // read string if available
  if (Serial.available()) {
    inLength =  0;
    while (Serial.available()) {
      inBuffer [inLength] = Serial.read();
      inLength ++;
      if (inLength >= BUFFERSIZE)
         break;
    }
  
    Serial.print("I received: ");
    Serial.write(inBuffer,inLength);
    Serial.println();
  }

  // blink the led and send a number
  digitalWrite(ledPin, HIGH); // set the LED on
  delay(10); // wait a bit

  Serial.println(numLoop);

  digitalWrite(ledPin, LOW); // set the LED off
  delay(1000);
  numLoop++;
} 
