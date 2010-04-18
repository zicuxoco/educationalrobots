import java.awt.*;
import javax.microedition.lcdui.*;
import lejos.nxt.*;
import lejos.nxt.addon.*;

/**
* Demonstration of leJOS nxtcam
*
* draws the rectangle of a recognized color on the LCD screen
* gives different beeps for the two sides of the screen (left right)
* can be used to steer motors to make a following NXT
*
* uses one very good recognizable color entered with NXTCAMVIEW
*
* @author Five March
*
*/

public class Follow {

	static NXTCam cam = new NXTCam(SensorPort.S1);
	static Rectangle myRect = new Rectangle();
	static Sound beep;

	public static void main(String[] args ){
		Graphics g = new Graphics();//to draw a rectangle on the screen
		
		//--------------------------------------------------------
		Sound.beep();//we are starting
		cam.sendCommand('A');//preparation of the camera: sort objects
		cam.sendCommand('E');//start camera tracking
		//--------------------------------------------------------
		
		while(!Button.ESCAPE.isPressed() ){
			int nb = cam.getNumberOfObjects() ;
			if (nb ==1 ){
				LCD.clear();
	
				g.drawRect(0,0,98,63);//max width on screen
			
				myRect = cam.getRectangle(0);//the biggest rectangle seen by the camera
				double scale = .6;//the camera width is different from the LCD with
			
				int rectWidth = (int)(scale*myRect.width);
				int middle = (int)(scale*myRect.x) + (int)(.5*rectWidth);
				g.drawRect((int)(scale*myRect.x), (int)(scale*myRect.y), (int)(scale*myRect.width), (int)(scale*myRect.height));
				Integer midInt = new Integer(middle);//JAVA way of doing difficult converting an int to a String
				g.drawString( midInt.toString(),1,1);
				g.refresh();
	
				if(middle > 50 ){
					beep.playTone(2000, 100);//shape to the right
				}else{
					beep.playTone(4000, 100);//shape to the left
					pause(100);//small pause otherwise the rectangles are only partially drawn
				}
			}
		}
	}

	public static void pause(int time){
		try {Thread.sleep(time);} catch (Exception e) {}
	}

}