import java.awt.Rectangle;

import javax.microedition.lcdui.Graphics;

import lejos.nxt.*;
import lejos.nxt.addon.NXTCam;

/**
 * Class to display the rectangles that the camera picks up on the NXT screen
 * @author Mr.Rick
 *
 */
public class Cazador2
{
   
   private static int INTERVAL=100;

   private static NXTCam camera;   
   
   public static void main(String [] args){

    	 //Instanciar el sensorde camara
         camera = new NXTCam(SensorPort.S1);
         //Configuramos
         camera.sendCommand('A');
         camera.sendCommand('E');
        
         


         Rectangle rectangleObj = new Rectangle();
         
         while(!Button.ESCAPE.isPressed()){


             	rectangleObj = getMaximumRectangle(camera);
             	drawMaximumRectangle(rectangleObj);
             	takeActions(rectangleObj);

        		try {Thread.sleep(INTERVAL);} catch (Exception e) {}                

          }

}

   private static void drawMaximumRectangle(Rectangle rectangleObj){
	   Graphics g = new Graphics();
       g.clear();
       g.drawRect(CameraUtils.xscale(rectangleObj.x),CameraUtils.yscale(rectangleObj.y),
                 CameraUtils.xscale(rectangleObj.width),CameraUtils.yscale(rectangleObj.height));  
       g.refresh ();	
   }
   
   private static void takeActions(Rectangle rectangleObj){
	   
	 Robot5 robot = new Robot5(); 
	   
  	 int nx=CameraUtils.xscale(rectangleObj.x);
     int q = 0;
     

     
     if((nx>0) && (nx <=25)){
    	 q  =1;
    	 robot.turnLeft(100);
     }else if((nx> 25) && (nx <=75)){
    	 q =2;
    	 robot.forward(500);
     }else if((nx > 75) && (nx <=100)){
    	 q =3;
    	 robot.turnRight(100);
     }else{
    	 q = 99;
    	 robot.stop();
     }
     //fin bucle calculos
     //pintar resultados
     LCD.drawString("                ", 0,0);
     LCD.drawString("q "+ q, 0, 0);
     LCD.refresh();
     

	   
   }
   
	private static Rectangle getMaximumRectangle(NXTCam camera){
		 int NXTCAM_MAXIMUN_OBJECTS =8;
	     int objectCount = 0;
		 int max_area = 0;
		 double x =0;
		 int area =0;
		 Rectangle rectangleObj = new Rectangle();
		 
	     objectCount=camera.getNumberOfObjects();
	     if(objectCount>0 && objectCount<NXTCAM_MAXIMUN_OBJECTS){
	    	 
	    	 //Calculo de objeto mas grande
	    	 for(int i =0; i<objectCount; i++){
	             Rectangle rect = camera.getRectangle(i);
	             //1. Calculo de area
	            area=rect.width*rect.height;
	
	             //2. Preguntamos si esa area es mayor que nuestro maximo
	            if (max_area <= area){                               
	          	  //2.1 si es mayor: actualizar
	          	  max_area = area;
	          	  rectangleObj = rect;
	            }
	            
	         }
	     }
		 return rectangleObj;
	}
}