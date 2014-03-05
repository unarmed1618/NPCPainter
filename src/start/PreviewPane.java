package start;

import java.awt.*;

import javax.swing.*;
class PreviewPane extends JComponent {
	PadDraw pointer;
	Image localImage;
	
	Graphics2D pGraphics;
	
	
	public PreviewPane(PadDraw original){
		setDoubleBuffered(false);
	pointer = original;
	//pGraphics = oGraphics;
	}
	public void paintComponent(Graphics g) {
	
		localImage = pointer.bigImage;
		g.drawImage(localImage,0,0,336,96,null);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -34921878797034591L;

}
