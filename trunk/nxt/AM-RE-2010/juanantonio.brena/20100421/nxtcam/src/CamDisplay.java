import java.awt.Rectangle;

import javax.microedition.lcdui.Graphics;

import lejos.nxt.*;
import lejos.nxt.addon.NXTCam;

/**
 * Class to display the rectangles that the camera picks up on the NXT screen
 * @author Wayne Mac Adams
 *
 */
public class CamDisplay
{
   
   // NXT Display is 100 x 64
   // Camera view is 176 x 144
   
   private static int INTERVAL=100;
   private static int CAM_WIDTH=176;
   private static int CAM_HEIGHT=144;
   
   private static int xscale(int x)
   {
      // multiply by 1000,add 500 and divide by 1000 again
      // so that integer division will round off
      // to the closer whole number
      // I know there are better ways but its only a test class :)
      return ((((x*1000)/CAM_WIDTH)*LCD.SCREEN_WIDTH)+500)/1000;
   }


   private static int yscale(int y)
   {
      return ((((y*1000)/CAM_HEIGHT)*LCD.SCREEN_HEIGHT)+500)/1000;
   }
   
   public static void main(String [] args)
   {
      try{
         NXTCam camera = new NXTCam(SensorPort.S1);
         
         camera.sendCommand('A');
         camera.sendCommand('E');
         int objectCount;
         
         Graphics g = new Graphics();
         
         while(!Button.ESCAPE.isPressed()){
            g.clear();
            
            objectCount=camera.getNumberOfObjects();
             if(objectCount>0 && objectCount<8){
                for(int i =0; i<objectCount; i++){
                   Rectangle rect = camera.getRectangle(i);

                   g.drawRect(xscale(rect.x),yscale(rect.y),
                            xscale(rect.width),yscale(rect.height));                  
                  
                }
             }
             g.refresh();
             Thread.sleep(INTERVAL);
         }
      }catch(Exception ex){
         System.exit(0);
      }
   }
} 