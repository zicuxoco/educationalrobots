import lejos.nxt.*;

public class HTSensorMux3 extends I2CSensor {
   
   public HTSensorMux3(I2CPort port) {
      super(port);
      port.setType(TYPE_LOWSPEED_9V);
      this.setAddress(0x10);
   }


   public void configurateMUX(){
	   /*
	   # Set the SMUX in halted mode: 0x10 0x20 0x00
	   # Send auto-scan command: 0x10 0x20 0x01
	   # wait 500ms
	   # Set the SMUX in normal mode: 0x10 0x20 0x02 
	   */
	   //Set the SMUX in halted mode: 0x10 0x20 0x00
	   sendData(0x20, (byte)0x10);
	   sendData(0x20, (byte)0x20);
	   sendData(0x20, (byte)0x00);
	   //# Send auto-scan command: 0x10 0x20 0x01
	   sendData(0x20, (byte)0x10);
	   sendData(0x20, (byte)0x20);
	   sendData(0x20, (byte)0x01);
	   //# wait 500ms
	   try{Thread.sleep(500);}catch(Exception e){}
	   //# Set the SMUX in normal mode: 0x10 0x20 0x02 
	   sendData(0x20, (byte)0x10);
	   sendData(0x20, (byte)0x20);
	   sendData(0x20, (byte)0x02);
   }
   
   public void configurateMUX2(){
	     /*
	     # Set the SMUX in halted mode: 0x10 0x20 0x00
	     # Send auto-scan command: 0x10 0x20 0x01

	     # wait 500ms
	     # Set the SMUX in normal mode: 0x10 0x20 0x02
	     */

	     //Set the SMUX in halted mode: 0x10 0x20 0x00
	     sendData(0x20, (byte)0x00);  // send command 0x00 to register 0x20

	     // Wait 50 ms for SMUX to clean up
	     try{Thread.sleep(50);}catch(Exception e){}

	     //# Send auto-scan command: 0x10 0x20 0x01
	     sendData(0x20, (byte)0x01);    // send command 0x01 to register 0x20

	     //# wait 500ms for auto-scan to complete
	     try{Thread.sleep(500);}catch(Exception e){}

	     //# Set the SMUX in normal mode: 0x10 0x20 0x02
	     sendData(0x20, (byte)0x02);   // send command 0x02 to register 0x20
	} 
   
   public int getDistance(){
	   //Xander, in this part, I have doubt to know how to read the distance.
	   //Read 1st byte from Channel 1 I2C buffer: 0x10 0x40
	   byte[] buf = new byte[1];
	   //this.setAddress(0x10);
	   int ret = getData(0x40,buf,1);
	   int currentDistance = buf[0] & 0xff; //(ret == 0 ? (buf[0] & 0xff) : 255);
	   return currentDistance;
   }
} 
