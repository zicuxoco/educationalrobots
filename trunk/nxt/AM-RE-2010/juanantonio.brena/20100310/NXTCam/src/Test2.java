import lejos.nxt.*;
import lejos.nxt.addon.NXTCam;

public class Test2 {

   private static NXTCam camera;

   /**
    * @param args
    */
   public static void main(String[] args) {

      camera = new NXTCam(SensorPort.S1);
      //camera.setTrackingMode(NXTCam.OBJECT_TRACKING);
      //camera.enableTracking(true);
      
      //camera.sortBy(NXTCam.SIZE);
      camera.sendCommand('A');//preparation of the camera: sort objects
      //camera.enableTracking(true);
      camera.sendCommand('E');//start camera tracking

      while (!Button.ESCAPE.isPressed()) {

         if (camera.getNumberOfObjects() > 0) {
            LCD.drawString("Objects: " +  camera.getNumberOfObjects(), 1, 1);
            LCD.refresh();
         } else {
            LCD.drawString("NOTHING", 1, 1);
         }

      }

   }
}