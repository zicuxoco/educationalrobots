import lejos.nxt.*;

public class HTSensorMux2 extends I2CSensor {

   public static final int STATE_AUTODETECT=0;
   public static final int STATE_HALTED=1;
   public static final int STATE_RUNNING=2;
   
   protected static final int STATUS_BATTERY_BIT = 1;
   protected static final int STATUS_BUSY_BIT = 2;
   protected static final int STATUS_HALT_BIT = 4;

   protected static final byte CMD_HALT = 0;
   protected static final byte CMD_AUTODETECT = 1;
   protected static final byte CMD_RUN = 2;
   
   
   public static final byte CHMODE_DIG_SENS_BIT = 1;
   public static final byte CHMODE_9V_EN_BIT = 2;
   public static final byte CHMODE_DIG0_BIT = 4;
   public static final byte CHMODE_DIG1_BIT = 8;
   public static final byte CHMODE_SLOW_BIT = 16;
   
   public final SMuxSensorPort S1 = new SMuxSensorPort(this,1);
   public final SMuxSensorPort S2 = new SMuxSensorPort(this,2);
   public final SMuxSensorPort S3 = new SMuxSensorPort(this,3);
   public final SMuxSensorPort S4 = new SMuxSensorPort(this,4);
   
   public HTSensorMux2(I2CPort port) {
      super(port);
      port.setType(TYPE_LOWSPEED_9V);
      this.setAddress(0x10/2);
   }
   
   public String getProductID(){
      byte[] buf = new byte[8];
      int failed = this.getData(0x08, buf, 8);
      if(failed!=0) return "failed. ";
      String str="l:";
      for(int i=0; i<8; i++){
         str+=(char)buf[i];
      }
      return str;
   }
   
   public int AutodetectAndRun(){
      if(getCurrentState()!=STATE_HALTED){
         setState(STATE_HALTED);
      }
      if(getCurrentState()!=STATE_HALTED) return 1;
      setState(STATE_AUTODETECT);
      if(getCurrentState()!=STATE_HALTED) return 2;
      setState(STATE_RUNNING);
      return 0;      
   }
   
   public String getSensorType(){
      byte[] buf = new byte[8];
      int failed = this.getData(0x10, buf, 8);
      if(failed!=0) return "failed. ";
      String str="l:";
      for(int i=0; i<8; i++){
         str+=(char)buf[i];
      }
      return str;
   }
     
   public String getVersion(){
      byte[] buf = new byte[8];
      int failed = this.getData(0x00, buf, 8);
      if(failed!=0) return "failed. ";
      String str="l:";
      for(int i=0; i<8; i++){
         str+=(char)buf[i];
      }
      return str;
   }
   
   public boolean isBatteryLow(){
      byte[] buf = new byte[1];
      getData(0x21,buf,1);
      return ((buf[0]&STATUS_BATTERY_BIT)!=0);
   }
   
   public int getCurrentState(){
      byte[] stat = new byte[1];
      getData(0x21,stat,1);
      if((stat[0]&STATUS_HALT_BIT)!=0) return STATE_HALTED;
      if((stat[0]&STATUS_BUSY_BIT)!=0) return STATE_AUTODETECT;
      return STATE_RUNNING;
   }
   
   public void setState(int state){
      if(state==STATE_HALTED){
         sendData(0x20, CMD_HALT);
         try{Thread.sleep(50);}catch(Exception e){}
      }else if(state==STATE_AUTODETECT){
         sendData(0x20, CMD_AUTODETECT);
         try{Thread.sleep(500);}catch(Exception e){}
      }else{
         sendData(0x20, CMD_RUN);         
      }
   }
   //------------------------------------------------------Channel read properties-------------------------------------------------
   
   public boolean isDigitalSensor(int channel){
      return  ((getSensorMode(channel)&CHMODE_DIG_SENS_BIT)!=0);
   }
   
   public byte getSensorMode(int channel){
      byte[] buf = new byte[1];
      getData(0x22+5*(channel-1),buf,1);
      return buf[0];
   }
   
   public void setSensorMode(int channel,byte mode){
      byte[] buf = new byte[]{mode};
      sendData(0x22+5*(channel-1),buf,1);
   }
   
   public short getAnalogueValue(int channel){
      byte[] buf = new byte[2];
      getData(0x36+2*(channel-1),buf,2);
      return (short)((buf[0] & 0x00FF) * 4 + (buf[1] & 0x00FF));
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
   
   public int getDistance(){
	   //Xander, in this part, I have doubt to know how to read the distance.
	   //Read 1st byte from Channel 1 I2C buffer: 0x10 0x40
	   byte[] buf = new byte[1];
	   setAddress(0x10);
	   int ret = getData(0x40,buf,1);
	   int currentDistance = buf[0] & 0xff; //(ret == 0 ? (buf[0] & 0xff) : 255);
	   return currentDistance;
   }
} 
