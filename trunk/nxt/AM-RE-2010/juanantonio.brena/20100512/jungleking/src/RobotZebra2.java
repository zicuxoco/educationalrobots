import java.awt.Rectangle;
 
import javax.microedition.lcdui.Graphics;
 
import lejos.nxt.*;
import lejos.nxt.addon.NXTCam;
/**
 *
 * @author Mr.Rick
 *
 */
public class RobotZebra2 {
    /**
     * Area de propiedades del robot
     */
    private Motor leftMotor;
    private Motor rightMotor;
 
    //private static NXTCam camera; 
 
    /**
     *
     */
 
    public RobotZebra2(){
        leftMotor = Motor.A;
        rightMotor= Motor.B;
 
    }
 
    public void forward(int miliseconds){
        leftMotor.forward();
        rightMotor.forward();
        try{Thread.sleep(miliseconds);}catch(Exception e) {}
    }
    public void backward(int miliseconds){
        leftMotor.backward();
        rightMotor.backward();
        try{Thread.sleep(miliseconds);}catch(Exception e) {}
    }
 
    public void stop(){
        rightMotor.stop();
        leftMotor.stop();
    }
 
    public void speed(int speed){
        rightMotor.setSpeed(speed);
        leftMotor.setSpeed(speed);
    }
 
    public void turnLeft(int angle){
        leftMotor.rotate(angle);
    }
    public void turnRight(int angle){
        rightMotor.rotate(angle);
    }
 
    /**
     * @param args
     */
    public static void main(String[] args) {
 
        NXTCam camera;
 
         //Instanciar el sensorde camara
        camera = new NXTCam(SensorPort.S1);
        //Configuramos
        camera.sendCommand('A');
        camera.sendCommand('E');
 
        RobotZebra2 RobotZebra = new RobotZebra2();
 
        Rectangle rectangleObj = new Rectangle();
 
        boolean detected = false;
 
        RobotZebra.forward(14000);
        RobotZebra.stop();
        RobotZebra.turnRight(1940);
        RobotZebra.turnRight(425);
        RobotZebra.stop();
 
        //Detectar leon
        rectangleObj = getMaximumRectangle(camera);
        drawMaximumRectangle(rectangleObj);
        detected = detectar(rectangleObj);
 
        if(detected){
            Sound.beep();
        }
 
        RobotZebra.forward(16000);
        RobotZebra.stop();
 
        //Detectar leon
        rectangleObj = getMaximumRectangle(camera);
        drawMaximumRectangle(rectangleObj);
        detected = detectar(rectangleObj);
 
        if(detected){
            Sound.beep();
        }
 
        RobotZebra.turnRight(1500);
        RobotZebra.stop();
 
        //Detectar leon
        rectangleObj = getMaximumRectangle(camera);
        drawMaximumRectangle(rectangleObj);
        detected = detectar(rectangleObj);
 
        if(detected){
            Sound.beep();
        }
 
        RobotZebra.forward(15000);
        RobotZebra.stop();
 
        //Detectar leon
        rectangleObj = getMaximumRectangle(camera);
        drawMaximumRectangle(rectangleObj);
        detected = detectar(rectangleObj);
 
        if(detected){
            Sound.beep();
        }
 
        RobotZebra.turnLeft(1930);
        RobotZebra.stop();
 
        //Detectar leon
        rectangleObj = getMaximumRectangle(camera);
        drawMaximumRectangle(rectangleObj);
        detected = detectar(rectangleObj);
 
        if(detected){
            Sound.beep();
        }
 
        RobotZebra.forward(5000);
        RobotZebra.stop();
    }
 
    private static  boolean detectar(Rectangle rectangleObj){
 
        int nx=CameraUtils.xscale(rectangleObj.x);
        int q = 0;
 
        boolean lionDetected = false;
 
        if((nx>0) && (nx <=100)){
            lionDetected = true;
        }
 
        return lionDetected;
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
 
       private static void drawMaximumRectangle(Rectangle rectangleObj){
           Graphics g = new Graphics();
           g.clear();
           g.drawRect(CameraUtils.xscale(rectangleObj.x),CameraUtils.yscale(rectangleObj.y),
                     CameraUtils.xscale(rectangleObj.width),CameraUtils.yscale(rectangleObj.height));
           g.refresh ();
       }
 
}
