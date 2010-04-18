import lejos.robotics.subsumption.*;
import lejos.nxt.*;

public class HitWall2 implements Behavior {
   public UltrasonicSensor touch = new UltrasonicSensor(SensorPort.S1);
   
   public boolean takeControl() {
	  boolean result = false;
	  
	  int distance = touch.getDistance();
	  
	  if(distance <= 40){
		  result = true;
	  }else{
		  result = false;
	  }
	   
      return result;
   }

   public void suppress() {
      Motor.A.stop();
      Motor.C.stop();
   }

   public void action() {
      // Back up:
      Motor.A.backward();
      Motor.C.backward();
      try{Thread.sleep(1000);}catch(Exception e) {}
      
      // Rotate by causing only one wheel to stop:
      Motor.A.stop();
      try{Thread.sleep(300);}catch(Exception e) {}
      Motor.C.stop();
   }
}
