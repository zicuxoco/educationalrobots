import lejos.nxt.*;
import lejos.nxt.addon.*;
import lejos.robotics.navigation.*;

/**
 * 
 * @author Juan Antonio Brenha Moral
 *
 */
public class Robot8{

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
	private UltrasonicSensor us2;
	
	//Propiedades de la configuracion
	private int distanceThreshold = 20;
	private static int minimumObstacleDistance = 40;
	
	/**
	 * Constructor
	 */
	public Robot8(){
		leftMotor = Motor.A;
		rightMotor = Motor.B;
		//float wheelDiameter, float trackWidth
		compass = new CompassSensor(SensorPort.S1);	
		
		pilot = new CompassPilot(compass, 2.25f, 4.8f, leftMotor, rightMotor);
		pilot.getRight().setSpeed(900);
		pilot.getLeft().setSpeed(900);
		
		us1 = new UltrasonicSensor(SensorPort.S2);
		us2 = new UltrasonicSensor(SensorPort.S3);		
	}
	
	/**
	 * 
	 * @return
	 */
	public int getLeftDistance(){
		return us1.getDistance();
	}
	
	/**
	 * 
	 * @return
	 */
	public int getRightDistance(){
		return us2.getDistance();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean hasSimilarDistances(int _leftDistance,int _rightDistance){
		
		int leftDistance = _leftDistance;
		int rightDistance = _rightDistance;
		
		boolean isSimilar = false;
		
		int absoluteDifference = Math.abs(leftDistance - rightDistance);
		
		if(absoluteDifference >= distanceThreshold){
			isSimilar = false;
		}else{
			isSimilar = true;
		}
		
		return isSimilar;
	}
	
	/**
	 * 
	 * @param leftDistance
	 * @param rightDistance
	 * @return
	 */
	public static int getMinimumDistance(int leftDistance,int rightDistance){
		int minimumDistance = 0;
		
		if(leftDistance > rightDistance){
			minimumDistance = rightDistance;
		}else{
			minimumDistance = leftDistance;
		}
		
		return minimumDistance;
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

	public void forward(){
		pilot.forward();
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
		Robot8 robot = new Robot8();
		
		//Inicio
		robot.calibrate();
		robot.getCompass().resetCartesianZero();
		robot.getPilot().setHeading(0);

		int leftDistance = 0;
		int rightDistance = 0;
			
		//Bucle de control
		while(!Button.ESCAPE.isPressed()){
			leftDistance = robot.getLeftDistance();
			rightDistance = robot.getRightDistance();
			
			LCD.clear();
			LCD.drawString("Left:" + leftDistance, 0, 0);
			LCD.drawString("Right:" + rightDistance, 0, 1);
			LCD.drawString("Similar" + robot.hasSimilarDistances(leftDistance,rightDistance), 0,2);
			
			
			//Si la distancia es igual
			boolean rule1 = robot.hasSimilarDistances(leftDistance,rightDistance);
			boolean rule2 = false;
			if(getMinimumDistance(leftDistance,rightDistance) > minimumObstacleDistance){
				rule2 = true;
			}
			
			if(rule2){
				robot.forward();
			}else{
				Sound.beepSequence();
				if(leftDistance > rightDistance){
					robot.stop();
					//robot.travel(-5);
					robot.rotateTo(45);
				}else{
					robot.stop();
					//robot.travel(-5);					
					robot.rotateTo(-90);
				}
			}
		}
		
	}

}
