import lejos.nxt.*;
/**
 * 
 * @author User Alberto Rodriguez y Talita Botana
 *
 */
public class Robot5 {
    /**
     * Area de propiedades del robot
     */
	private Motor leftMotor;
	private Motor rightMotor;

	/**
	 * 
	 */
	
	public Robot5(){
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
		Robot5 boby = new Robot5();
		boby.stop();
		boby.forward(11000);
		boby.stop();
		boby.turnLeft(360);
		boby.stop();
		boby.forward(10000);
		boby.stop();
		boby.turnRight(360);
		boby.stop();
		boby.forward(12000);
		boby.stop();
	}

}
