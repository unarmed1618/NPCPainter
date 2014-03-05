package start;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Timer;
import javax.swing.JComponent;

public class Animate extends JComponent{
/**

	 * 
	 */
	
	private static final long serialVersionUID = 7553197221281693965L;
	
	
	
	
BufferedImage[] frames = new BufferedImage[6];
//BufferedImage thisImage;
//Thread runner;
PadDraw pointer;
//int totalImages = 0;
int currentImage = 0;
long pause = 500;


public void setAnimationSource(PadDraw point){
	pointer = point;
}
private int _timeSlice = 50;
private Timer _timer = new  Timer (_timeSlice, new ActionListener ()                                       {
  public void actionPerformed (ActionEvent e) {
	stepForward();
    repaint();
  }
});
public void paintComponent(Graphics g) {
		updateImages();
		g.drawImage(frames[currentImage], 0, 0, 48,96,null);
	
}
public void updateImages() {
	frames[0] = pointer.framesToHandle[3];
	frames[1] = pointer.framesToHandle[4];
	frames[2] = pointer.framesToHandle[4];
	
	frames[3] = pointer.framesToHandle[5];
	frames[4] = pointer.framesToHandle[6];
	frames[5] = pointer.framesToHandle[6];
}

public void stepForward() {
	updateImages();
	if(currentImage <5)
		currentImage++;
	else
		currentImage = 0;
	
	//repaint();
}
public void stopAnimating() {
	_timer.stop();
}
public void oneCycle() {
//	stepForward();
	_timer.start();

}



}
