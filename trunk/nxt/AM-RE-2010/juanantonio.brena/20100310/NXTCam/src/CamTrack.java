import java.awt.Rectangle;
import lejos.robotics.navigation.*;
import lejos.nxt.Button;
import lejos.nxt.Sound;
import lejos.nxt.addon.NXTCam;
import lejos.nxt.SensorPort;
import lejos.nxt.Motor;
import lejos.nxt.LCD;

/*
 *  This program assumes one colour is
 *  uploaded on NXTCam and keeps
 *  the object of that colour at the
 *  centre of the NXTCam.
 */

/*
  Nxtcam resolution : 176 * 144 ; original point locates in upper-left corner
  For our purposes divide up NxtCam screen into 4:
                   #2(58,0)  #3(118,0)
  #1(0,0).----------..--------..--------.                                               
         |          |         |         |
         |          |         |         |
         |          |         |         |
         |#4(58,116).---------.         |
         |          |         |         |
         .----------.---------.---------.
 * if centre point of tracked object is in #1, steer left
 * if centre point of tracked object is in #3, steer right
 * if centre point of tracked object is in #2, go forward
 * if centre point of tracked object is in #4, go forward slowly and stop when touching object

*/
public class CamTrack {
   
   //set up wheel parameters for Pilot
   static final float WHEEL_DIAM = 5.6F; //F sets number to type 'float'
   static final float TRACK_W = 13F;
     
    public static void main(String[] args) {
   
       final NXTCam tracker = new NXTCam(SensorPort.S1); //camera on port 3
      //create Pilot object to drive the robot wheels
      
       TachoPilot drive = new TachoPilot(WHEEL_DIAM, TRACK_W, Motor.C, Motor.B, true); // left, then right motor,true sets motors reversed
       drive.setSpeed(400); // Movement speed in degrees per second; Up to 900 is possible with 8 volts.
          
     //create 4 rectangles representing NextCam field of view:
     final Rectangle rect1 = new Rectangle(0, 0, 57, 144);
     final Rectangle rect2 = new Rectangle(58, 0, 59, 115);
     final Rectangle rect3 = new Rectangle(118, 0, 57, 144);
     final Rectangle rect4 = new Rectangle(58, 116, 59, 28);
    
      tracker.sendCommand('E'); //E - enable tracking
      Sound.beep();
      try {Thread.sleep(500);} catch (Exception e){} //wait a bit
     
      tracker.sendCommand('B'); //B - set object tracking mode
      Sound.beep();
      try {Thread.sleep(500);} catch (Exception e){} //wait a bit

      while (!Button.ESCAPE.isPressed()) { //loop round
         LCD.clear(); //clear screen
         
         final int objectnos = tracker.getNumberOfObjects(); //find how many objects can be seen
         //LCD.drawInt(objectnos,1,2); //write result to screen at x=1 y=2
         System.out.println("objects" + objectnos); //write no of objects seen
       
         if (objectnos > 0)   { //make sure no of objects is not zero
           
             Rectangle rect = tracker.getRectangle(0); //examine object 1
             int topleftx = (int)rect.getX(); //get dimensions of tracked rectangle - converted to int by type casting
            int toplefty = (int)rect.getY();
            System.out.println("x" + topleftx + "y" + toplefty); //write no of objects seen
            int width = (int) rect.getWidth();
            int height = (int) rect.getHeight();
            int objectxcentre = topleftx + width/2; //work out centre point of tracked retangle
            int objectycentre = toplefty + height/2;
            // if centre point of tracked object is in rectangle #1, steer left
            if (rect1.contains(objectxcentre, objectycentre)){
            	drive.steer(25);
            }
            // if centre point of tracked object is in #2, go forward
            if (rect2.contains(objectxcentre, objectycentre)){
            	drive.forward();
            }
            // if centre point of tracked object is in #2, steer right
            if (rect3.contains(objectxcentre, objectycentre)){
            	drive.steer(-25);
            }
            // if centre point of tracked object is in #4, go forward slowly and stop when touching object
            if (rect4.contains(objectxcentre, objectycentre)) {
               drive.setSpeed(100); //wheel turn degrees/sec
               drive.forward(); //drives slowly forward
               //***need to stop when correct touch switch is touched
            }
         } //end if
      } //end while
    //stop tracking
     tracker.sendCommand('D'); //D - disable tracking
      Sound.beep();
      try {Thread.sleep(500);} catch (Exception e){} //wait a bit
    } //end main
} //end class 