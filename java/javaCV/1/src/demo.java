import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

//import Demo.RImage;

import name.audet.samuel.javacv.*;
import name.audet.samuel.javacv.jna.cxcore.IplImage;

public class demo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			// TODO Auto-generated method stub
			CanvasFrame frame = new CanvasFrame(false, "Image Frame");

			OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
			
			grabber.start();
			while (true) {
			    IplImage image = grabber.grab();
			    //name.audet.samuel.javacv.jna.highgui.cvSaveImage("/home/esmetaman/projects/phd/java/examples/javacv/1/demo.jpg",image);
			    //BufferedImage bufferedImage = null;
			    //image.copyTo(bufferedImage);
				//byte[] data = ((DataBufferByte)bufferedImage.getRaster().getDataBuffer()).getData();
				//RImage rimg = new RImage(bimg.getWidth(), bimg.getHeight(), data);			    
			    // do some processing...
	
			    frame.showImage(image);
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}
