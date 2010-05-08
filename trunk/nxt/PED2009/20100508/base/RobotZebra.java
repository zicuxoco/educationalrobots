import lejos.nxt.*;
/**
 *
 * @author User Alberto Rodriguez
 *
 */
public class RobotZebra {
    /**
     * Area de propiedades del robot
     */
    private Motor leftMotor;
    private Motor rightMotor;
 
    /**
     *
     */
 
    public RobotZebra(){
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
        // TODO Auto-generated method stub
        RobotZebra RobotZebra = new RobotZebra();
 
        while(!Button.ESCAPE.isPressed()){
            RobotZebra.forward(10000);
            //RobotZebra.stop();
            //RobotZebra.turnRight(720);
            RobotZebra.stop();
            RobotZebra.backward(10000);
        }
        //RobotZebra.turnRight(180);
    }
 
}
