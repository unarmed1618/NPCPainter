package start;
import java.io.*;
import java.util.Stack;
//TODO: Fix Undo Functionality so it isn't stupid.
import java.awt.image.*;
import java.awt.Image;
import javax.imageio.*;
import javax.swing.undo.UndoManager;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

//import java.math.*;
class PadDraw extends JComponent{
		/**
		 * Adding two words
		 */
		private static final long serialVersionUID = -7025646393833483754L;
		UndoManager undoManager = new UndoManager();
		Image image;
		Image activeImage;
		
		BufferedImage bigImage;
		BufferedImage bigImageTemplate;
		BufferedImage[] framesToHandle = new BufferedImage[7];
		Stack<BufferedImage> undoStack = new Stack<BufferedImage>();
		BufferedImage undoSwap;
		
		
		int currentImage = -1;
		Color transparency = new Color(69,98,234);
		/**
		 * Scanned Image = 7 images stitched together without borders *
		 * On bigImageLoad, buffer the 'default' png splitting up between the different working images. *
		 * Combobox for pointing at different frame-images *
		 * Changes in Images create changes in finalImage *
		 * Preview reflects full frameset *
		 * Preview of animation in preview window
		 * Pressing template reverts only the current frame
		 */
		public void shiftUp() {
			BufferedImage swapImage = deepCopy(toBufferedImage(activeImage));
			clear();
			activeImage.getGraphics().drawImage(swapImage.getSubimage(0,1,16,31),0,0,null);
		}
		public void shiftLeft() {
			BufferedImage swapImage = deepCopy(toBufferedImage(activeImage));
			clear();
			activeImage.getGraphics().drawImage(swapImage.getSubimage(0,0,15,32),1,0,null);
		}
		public void shiftRight() {
			BufferedImage swapImage = deepCopy(toBufferedImage(activeImage));
			clear();
			activeImage.getGraphics().drawImage(swapImage.getSubimage(1,0,15,32),0,0,null);
		}
		public void shiftDown() {
			BufferedImage swapImage = deepCopy(toBufferedImage(activeImage));
			clear();
			activeImage.getGraphics().drawImage(swapImage.getSubimage(0,0,16,31),0,1,null);
		}
		public Color getColor() {
			return activeColor;
		}
		public void undoThings() {
			
			if(undoStack.size() != 0 &&imgCompare(undoStack.peek(),bigImage))
				undoStack.pop();
			if(undoStack.size() == 1)
				bigImage.getGraphics().drawImage(undoStack.peek(),0,0,112,32, null);
			else if(undoStack.size() ==0)
				;
			else 
				bigImage.getGraphics().drawImage(undoStack.pop(), 0,0,112,32,null);
				
		}
		public void storeHistory() {
			undoSwap.getGraphics().drawImage(bigImage,0,0,null);
			
			if(undoStack.size()==0||!imgCompare(undoSwap, undoStack.peek()))
				undoStack.push(deepCopy(undoSwap));
		}
		public void revertToTemplate(Graphics g) {
			bigImage.getGraphics().drawImage(bigImageTemplate,0,0,112,32, null);
			
		}
		public void updateImages() {
			bigScreen.drawImage(activeImage, 16*currentImage, 0, null);
			framesToHandle[currentImage].getGraphics().drawImage(activeImage, 0, 0, null);	
		}
		public void copyActiveToFrame(int to) {
			storeHistory();

			bigScreen.drawImage(activeImage, to *16, 0, null);
			framesToHandle[to].getGraphics().drawImage(activeImage, 0, 0, null);
	
			repaint();
			
		}
		public void loadBaseImage() {
			try {
				bigImage = ImageIO.read(Paint.class.getResource("/images/multiframe.png"));
				bigImageTemplate = ImageIO.read(Paint.class.getResource("/images/multiframe.png"));		
				undoSwap = ImageIO.read(Paint.class.getResource("/images/multiframe.png"));
				for(int i = 0; i<7;i++)
					framesToHandle[i] = bigImage.getSubimage(i*16,0,16,32);
			
				activeImage = framesToHandle[0]; //lookRight;
				currentImage = 0;
				bigScreen = (Graphics2D)bigImage.getGraphics();
				
			}
			catch (Exception e)
			{
				System.out.println("default image not present");
			}
			
		}
		public void setActiveFrame(int frame) {
			//System.out.println(frame);
			currentImage = frame;
			activeImage = framesToHandle[frame];
		
			graphics2D = (Graphics2D)activeImage.getGraphics();
			graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			repaint();
		}
		
		Graphics2D bigScreen;
		public Color activeColor;
		public PreviewPane previewCallback;
		//this is gonna be your image that you draw on
		Graphics2D graphics2D;
		//this is what we'll be using to draw on
		int currentX, currentY, oldX, oldY;
		//these are gonna hold our mouse coordinates
		Dimension initSize, curSize;
		//Dimensions for scale adjustment
		public int xScale(double x) {
		//int outx = (int)Math.floor(x/20);
			int outx= (int)Math.floor(x * (16.0/aspectRatioFix((int)curSize.getWidth(),(int)curSize.getHeight()).width));
		//System.out.println("Inx = " + x + ". Outx = " + outx);
		return outx;
		}
		public int yScale(double y) {
//		int outy = (int)Math.round(y/20);
			int outy= (int)Math.floor(y* (32.0/aspectRatioFix((int)curSize.getWidth(),(int)curSize.getHeight()).height));
		//System.out.println("Iny = " + y + ". Outy = " + outy);
		return outy;
		}
		//Now for the constructors
		public Dimension aspectRatioFix(int width, int height) {
			float ratio = (float)height/(float)width;
			float oHeight,oWidth;
			if(ratio<2.0)
			{
				oHeight = height;
				oWidth = oHeight/2;
			}
			else if(ratio >2.0) 
			{
				oWidth = width;
				oHeight = oWidth *2;
				
				
			}
			else
			{
				oWidth = width;
				oHeight = height;
			}
			return new Dimension(Math.round(oWidth), Math.round(oHeight));
		}
		public PadDraw(){
			setDoubleBuffered(false);
			
			// The actual paint methods
			addMouseListener(new MouseAdapter(){
				public void mousePressed(MouseEvent e){
					//System.out.println(e.getPoint().toString());
					oldX = xScale(e.getX());
					oldY = yScale(e.getY());
					//oldX = e.getX();
					//oldY = e.getY();
					storeHistory();
					if(graphics2D != null)
						graphics2D.drawLine(oldX, oldY, oldX, oldY);
					
						
					repaint();
					//updateUndo();
				}
				public void mouseReleased(MouseEvent e) {
					storeHistory();
				}
			});
			//if the mouse is pressed it sets the oldX & oldY
			//coordinates as the mouses x & y coordinates
			
			
			
			
			addMouseMotionListener(new MouseMotionAdapter(){
				public void mouseDragged(MouseEvent e){
					currentX = xScale(e.getX());
					currentY = yScale(e.getY());
					//storeHistory();
					if(graphics2D != null)
					graphics2D.drawLine(currentX, currentY, currentX, currentY);
					repaint();
					//updateUndo();
					oldX = currentX;
					oldY = currentY;
				}
				

			});
			//while the mouse is dragged it sets currentX & currentY as the mouses x and y
			//then it draws a line at the coordinates
			//it repaints it and sets oldX and oldY as currentX and currentY
		}
		
		public void paintComponent(Graphics g){
			if(activeImage == null){
				//if(currentImage == -1)
					loadBaseImage();	
				//else 
					
						
					//				image = getSource(Paint.class.getResource("/images/default.png"));
				//image = createImage(16, 32);
				
				graphics2D = (Graphics2D)activeImage.getGraphics();
				graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				//clear();

			}
			curSize = getSize();
			Dimension aspect = aspectRatioFix(this.getWidth(),this.getHeight());
			
			
			g.drawImage(activeImage, 0, 0, aspect.width,aspect.height, null);
			updateImages();
			previewCallback.repaint();
		}
		

		
		/** Special Thanks to Sri Harsha Chilakapati of Stack Overflow for this method
		 * Converts a given Image into a BufferedImage
		 *
		 * @param img The Image to be converted
		 * @return The converted BufferedImage
		 */
		public static BufferedImage toBufferedImage(Image img)
		{
		    if (img instanceof BufferedImage)
		    {
		        return (BufferedImage) img;
		    }

		    // Create a buffered image with transparency
		    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		    // Draw the image on to the buffered image
		    Graphics2D bGr = bimage.createGraphics();
		    bGr.drawImage(img, 0, 0, null);
		    bGr.dispose();

		    // Return the buffered image
		    return bimage;
		}

		/** Special Thanks to Klark of Stack Overflow for this method.
		 * 
		 * @param bi
		 * @return
		 */
		public static BufferedImage deepCopy(BufferedImage bi) {
			 ColorModel cm = bi.getColorModel();
			 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
			 WritableRaster raster = bi.copyData(null);
			 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
			}
		 
		public static boolean imgCompare(BufferedImage img1, BufferedImage img2) {
			
			
			int width;
			int height;
			boolean imagesEqual = true;

			if( img1.getWidth()  == ( width  = img2.getWidth() ) && 
			    img1.getHeight() == ( height = img2.getHeight() ) ){

			    for(int x = 0;imagesEqual == true && x < width; x++){
			        for(int y = 0;imagesEqual == true && y < height; y++){
			            if( img1.getRGB(x, y) != img2.getRGB(x, y) ){
			                imagesEqual = false;
			            }
			        }
			    }
			}else{
			    imagesEqual = false;
			}
			return imagesEqual;
		}
		
		
		public void save(){
		
			
			File saveFile = new File("savedimage");
		    JFileChooser chooser = new JFileChooser();
		    chooser.setSelectedFile(saveFile);
		    int rval = chooser.showSaveDialog(null);
		    if (rval == JFileChooser.APPROVE_OPTION) {
			saveFile = chooser.getSelectedFile();
			
			/* Write the filtered image in the selected format,
			 * to the file chosen by the user.
			 */
			
			try {
			    ImageIO.write(bigImage, "png", saveFile);
			} catch (IOException ex) {
			}
			
			
		}
		}
		public void clear(){
			storeHistory();
			graphics2D.setPaint(transparency);
			graphics2D.fillRect(0, 0, getSize().width, getSize().height);
			graphics2D.setPaint(Color.black);
			
			repaint();
		}
		
		public void useColor(Color thisColor){
			activeColor = thisColor;
			graphics2D.setPaint(thisColor);
			repaint();
		}
		
}
