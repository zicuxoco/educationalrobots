import lejos.nxt.*;
import lejos.nxt.addon.*;
import lejos.robotics.navigation.*;

/**
 * 
 * @author Juan Antonio Brenha Moral
 *
 */
public class Robot7{

	/**
	 * Area de propiedades del robot
	 */
	
	//Actuadores
	private Motor leftMotor;
	private Motor rightMotor;
	private CompassPilot pilot;
	
	//Sensores
	private CompassSensor compass;
	private UltrasonicSensor us1;
	
	/**
	 * Constructor
	 */
	public Robot7(){
		leftMotor = Motor.A;
		rightMotor = Motor.B;
		//float wheelDiameter, float trackWidth
		pilot = new CompassPilot(compass, 2.25f, 4.8f, leftMotor, rightMotor);
		pilot.getRight().setSpeed(900);
		pilot.getLeft().setSpeed(900);
		
		compass = new CompassSensor(SensorPort.S1);
		us1 = new UltrasonicSensor(SensorPort.S2);
	}
	
	/**
	 * 
	 */
	public void calibrate(){
		pilot.calibrate();
	}
	
	/**
	 * 
	 * @param distance
	 */
	public void travel(float distance){
		pilot.travel(distance,true);
	}

	/**
	 * 
	 */
	public void stop(){
		pilot.stop();
	}

	/**
	 * 
	 * @param angle
	 */
	public void rotateTo(int angle){
		pilot.rotateTo(angle);
	}

	public CompassPilot getPilot(){
		return pilot;
	}
	
	public CompassSensor getCompass(){
		return compass;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Instancia de la clase Robot7
		Robot7 robot = new Robot7();
		
		//Ejemplo de uso
		robot.calibrate();
		robot.getCompass().resetCartesianZero();
		robot.getPilot().setHeading(0);
		robot.travel(10);
		robot.stop();
		robot.rotateTo(45);
		robot.travel(10);
	}

}
