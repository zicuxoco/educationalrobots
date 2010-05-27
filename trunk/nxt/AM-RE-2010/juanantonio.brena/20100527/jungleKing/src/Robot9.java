import lejos.nxt.*;
import lejos.nxt.addon.*;
import lejos.robotics.navigation.*;
 
/**
 *
 * @author Mr.Rick
 *
 */
public class Robot9{
 
    /**
     * Area de propiedades del robot
     */
 
    //Actuadores
    private Motor leftMotor;
    private Motor rightMotor;
    private TachoPilot pilot;
    private SimpleNavigator sn;
 
    /**
     * Constructor
     */
    public Robot9(){
        leftMotor = Motor.A;
        rightMotor = Motor.B;
        //float wheelDiameter, float trackWidth
        pilot = new TachoPilot(2.25f, 4.8f, leftMotor, rightMotor);
        sn = new SimpleNavigator(pilot);
    }
 
    /**
     *
     * @param distance
     */
    public void travel(float distance){
        pilot.travel(distance,true);
    }
 
    public void forward(){
        pilot.forward();
    }
 
    /**
     *
     */
    public void stop(){
        pilot.stop();
    }
 
    public void rotate(int angle){
        pilot.rotate(angle);
    }
 
    public SimpleNavigator getPilot(){
        return sn;
    }
 
}


