import lejos.nxt.*;
import lejos.nxt.addon.*;

/**
 * BlobChaseExample.
 * 
 * Author Juan Antonio Breña Moral
 * Email: bren@juanantonio.info
 *
 */

public class BlobChaseExample{
   public static void main(String[] args) {

      //Sensor Information
      NXTCam ncs = new NXTCam(SensorPort.S1);
      ncs.sendCommand('A');
      ncs.sendCommand('E');

      int bItems;
      int x_centre;
      int y_centre;
      int x_error;
      int y_error;
      
        while (!Button.ESCAPE.isPressed())
        {
           ncs.getBlobs();
           bItems = ncs.getBlobItems();
           if(bItems >0){
              LCD.clear();
              LCD.drawString("Tracking ...",0,0);
              LCD.refresh();
              
              Blob bObj = ncs.getBlob(0);
              
             // Find the centre of the blob using double resolution of camera
             x_centre = bObj.getLeft() + bObj.getRight();
             y_centre = bObj.getTop() + bObj.getBottom();
             
             // Compute the error from the desired position of the blob (using double resolution)
             x_error = 176 - x_centre;
             y_error = 143 - y_centre;
             
             // Drive the motors proportional to the error
             Motor.A.Rotate((y_error - x_error) / 5);
             Motor.C.Rotate((y_error + x_error) / 5);
           }else{
              Motor.A.Rotate(0);
              Motor.C.Rotate(0);
              
              String message = "Found " + bItems + " blobs";
              
              LCD.clear();
              LCD.drawString(message,0,0);
              LCD.refresh();              
      
           }
           
        }
   }
} 
