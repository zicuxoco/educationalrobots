import java.awt.Rectangle;

import javax.microedition.lcdui.Graphics;

import lejos.nxt.*;
import lejos.nxt.addon.NXTCam;


public class Cazador{
   
   // NXT Display is 100 x 64
   // Camera view is 176 x 144
   private static int INTERVAL=100;
   private static int CAM_WIDTH=176;
   private static int CAM_HEIGHT=144;
   
   private final static int NXTCAM_MAXIMUM_OBJECTS = 8;
   
   //Motores
   private static Motor leftMotor;
   private static Motor rightMotor;
   
   private static int xscale(int x){
      // multiply by 1000,add 500 and divide by 1000 again
      // so that integer division will round off
      // to the closer whole number
      // I know there are better ways but its only a test class :)
      return ((((x*1000)/CAM_WIDTH)*LCD.SCREEN_WIDTH)+500)/1000;
   }


   private static int yscale(int y){
      return ((((y*1000)/CAM_HEIGHT)*LCD.SCREEN_HEIGHT)+500)/1000;
   }

   
	public static void forward(int miliseconds){
		leftMotor.forward();
		rightMotor.forward();
		try {Thread.sleep(miliseconds);} catch (Exception e) {}
	}
	
	public static void stop(){
		leftMotor.stop();
		rightMotor.stop();
	}
   
   public static void main(String [] args){
	   leftMotor = Motor.A;
	   rightMotor = Motor.B;
	   leftMotor.setPower(100);
	   rightMotor.setPower(100);
		
      try{

    	  //Instanciar el sensor de camara
    	  NXTCam camera = new NXTCam(SensorPort.S1);
    	  //Configuramos
    	  camera.sendCommand('A');
    	  camera.sendCommand('E');
    	  
    	  int objectCount = 0;
         
    	  Graphics g = new Graphics();
		
    	  int max_area =0;
    	  double x;
    	  int area =0;
    	  Rectangle rectangleObj = new Rectangle();
         
    	  while(!Button.ESCAPE.isPressed()){
    		  g.clear();
            
    		  objectCount=camera.getNumberOfObjects();
    		  LCD.drawString("" + objectCount, 0,3);
    		  if(objectCount>0 && objectCount<NXTCAM_MAXIMUM_OBJECTS){

    			  //Calculo del objeto mas grande
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

                  
                  g.drawRect(xscale(rectangleObj.x),yscale(rectangleObj.y),
                          xscale(rectangleObj.width),yscale(rectangleObj.height));  
                    
                  g.refresh ();

                  int nx=xscale(rectangleObj.x);
                  int q = 0;
                  
                  //fin bucle calculos
                  //pintar resultados
                  LCD.drawString("                ", 0,0);
                  LCD.drawString("                ", 0,1);
                  LCD.drawString("                ", 0,2);
                  LCD.drawString("area "+max_area, 0, 0);
                  LCD.drawString("x "+nx, 0, 1);
                  //LCD.refresh();
                  
                  if((nx>0) && (nx <=25)){
                 	 q  =1;
                 	 stop();
                  }else if((nx> 25) && (nx <=75)){
                 	 q =2;
                 	 forward(100);
                  }else if((nx > 75) && (nx <=100)){
                 	 q =3;
                 	 stop();
                  }else{
                 	 stop();
                 	 q = 99;
                  }
                  
                  LCD.drawString("q "+ q, 0, 2);
                  LCD.refresh();
                  
                  Thread.sleep(INTERVAL);

                  //Reseteamos valores
                  max_area = 0;
                  area = 0;
    		  }else{

    		  }

          }
         
      }catch(Exception ex){
         System.exit(0);
      }
   }
} 