
import lejos.nxt.*;
import lejos.nxt.addon.*;
import lejos.robotics.navigation.*;

public class CompasPilotTest {

    public static void main(String[] args){
    	LCD.drawString("CompassPilot Test",0,0);
    	LCD.refresh();
        Button.waitForPress();
        
        CompassSensor compass = new CompassSensor(SensorPort.S1);
        Motor leftMotor = Motor.A;
        Motor rightMotor = Motor.B;
        
        int power = 100;        
        leftMotor.setPower(power);
        rightMotor.setPower(power);
        
        CompassPilot pilot = new CompassPilot(compass, 2.25f, 4.8f, leftMotor, rightMotor);
        //El robot va a girar una vuelta para calibrar la brujula
        pilot.calibrate();


        int miliseconds = 2000;
        pilot.rotateTo(90);
        LCD.drawString("AI: " + pilot.getAngle(), 0, 1);
        LCD.refresh();
        Sound.beep();
        try {Thread.sleep(miliseconds);} catch (Exception e) {}
        
        pilot.rotateTo(180);
        LCD.drawString("AI: " + pilot.getAngle(), 0, 1);
        LCD.refresh();        
        Sound.beep();
        try {Thread.sleep(miliseconds);} catch (Exception e) {}
        
        pilot.rotateTo(270);
        LCD.drawString("AI: " + pilot.getAngle(), 0, 1);
        LCD.refresh();
        Sound.beep();
        try {Thread.sleep(miliseconds);} catch (Exception e) {}
        
        pilot.rotateTo(0);
        LCD.drawString("AI: " + pilot.getAngle(), 0, 1);
        LCD.refresh();
        try {Thread.sleep(miliseconds);} catch (Exception e) {}
        
        pilot.getCompass().resetCartesianZero();

        LCD.drawString("AI: " + pilot.getAngle(), 0, 1);
        LCD.refresh();
        
        Button.waitForPress();
        
        
        pilot.travel(20);
        LCD.drawString("" + pilot.getTravelDistance(), 0, 1);
        LCD.refresh();
        LCD.drawString("" + pilot.getAngle(), 0, 2);
        LCD.refresh();
        pilot.rotateTo(180);
        pilot.travel(-20);
        LCD.drawString("" + pilot.getTravelDistance(), 0, 3);
        LCD.refresh();
        LCD.drawString("" + pilot.getAngle(), 0, 4);
        LCD.refresh();
        Button.waitForPress();
    }

	
}
