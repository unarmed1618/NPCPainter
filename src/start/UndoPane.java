package start;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JComponent;

public class UndoPane extends JComponent{

	PadDraw pointer;
	Image localImage;
	
	Graphics2D pGraphics;
	
	
	public UndoPane(PadDraw original){
		setDoubleBuffered(false);
	pointer = original;
	//pGraphics = oGraphics;
	}
	public void paintComponent(Graphics g) {
	
		//localImage = pointer.undoFrames[1];
		//g.drawImage(localImage,0,0,336,96,null);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -34921878797034591L;

}
