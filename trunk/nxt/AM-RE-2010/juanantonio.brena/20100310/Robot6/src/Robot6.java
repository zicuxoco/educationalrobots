import lejos.nxt.*;

/**
 * 
 * @author Juan Antonio Brenha Moral
 *
 */
public class Robot6{

	/**
	 * Area de propiedades del robot
	 */
	private Motor leftMotor;
	private Motor rightMotor;
	
	/**
	 * 
	 */
	public Robot6(){
		leftMotor = Motor.A;
		rightMotor = Motor.B;
	}
	
	/**
	 * 
	 * @param seconds
	 */
	public void forward(int miliseconds){
		leftMotor.forward();
		rightMotor.forward();
		try {Thread.sleep(miliseconds);} catch (Exception e) {}
	}

	/**
	 * 
	 * @param seconds
	 */
	public void backward(int miliseconds){
		
	}

	/**
	 * 
	 * @param seconds
	 */
	public void stop(){
		
	}

	/**
	 * 
	 * @param angle
	 */
	public void turnLeft(int angle){
		
	}

	/**
	 * 
	 * @param angle
	 */
	public void turnRight(int angle){
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Instancia de la clase Robot5
		Robot6 robot = new Robot6();
		robot.forward(5000);
	}

}
