import lejos.nxt.*;


public class HTSensorMux2Test {
	public static void main(String[] args) {
		HTSensorMux2 sm1 = new HTSensorMux2(SensorPort.S1);
	
		LCD.drawString("" + sm1.getProductID(), 0,2);
		LCD.drawString("" + sm1.getVersion(), 0,3);
		LCD.drawString("" + sm1.getSensorType(), 0,4);
		LCD.refresh();

		sm1.configurateMUX();
		
		int distance = 0;
		while(!Button.ESCAPE.isPressed()){
			distance = sm1.getDistance();
			LCD.drawString("" + distance, 0,0);
			try{Thread.sleep(100);}catch(Exception e){}
		}
	}
}
