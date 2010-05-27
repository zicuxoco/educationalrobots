import lejos.nxt.*;

public class SMuxSensorPort implements ADSensorPort{

   protected int channel;
   protected HTSensorMux2 sMux;
   
   public SMuxSensorPort(HTSensorMux2 multiplexer, int channel){
      this.channel=channel;
      sMux=multiplexer;
   }
   
   
   public boolean readBooleanValue() {
      return readRawValue()>512;
   }

   
   public int readRawValue() {
      return sMux.getAnalogueValue(channel);
   }

   public int readValue() {
      return 1-readRawValue()/512;
   }

   public int getMode() {
      // TODO Auto-generated method stub
      return 0;
   }

   public int getType() {
      // TODO Auto-generated method stub
      return 0;
   }

   public void setMode(int mode) {
      // TODO Auto-generated method stub
      
   }

   public void setType(int type) {
      if(type==TYPE_LIGHT_ACTIVE){
         sMux.setState(HTSensorMux.STATE_HALTED);
         byte mode = sMux.getSensorMode(channel);
         mode=(byte)(mode|HTSensorMux.CHMODE_DIG0_BIT);
         sMux.setSensorMode(channel,mode);
         sMux.setState(HTSensorMux.STATE_RUNNING);
      }else if(type==TYPE_LIGHT_INACTIVE){
         sMux.setState(HTSensorMux.STATE_HALTED);
         byte mode = sMux.getSensorMode(channel);
         mode=(byte)(mode&(~HTSensorMux.CHMODE_DIG0_BIT));
         sMux.setSensorMode(channel,mode);
         sMux.setState(HTSensorMux.STATE_RUNNING);         
      }
   }

   public void setTypeAndMode(int type, int mode) {
      setType(type);
      setMode(mode);
   }

} 
