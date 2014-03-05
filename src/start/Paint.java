/**
 * Modifications by John Darrow to create the BlackWhite NPC Designer
 * TODO: 
 * Implement specific colors -- Done 
 * Implement specific canvas size -- Done
 * Add Click to Dot function -- Done
 * Implement Aspect Ratio Preservation -- Done
 * Implement stickers 
 * Implement Preview  -- Done
 * Implement Open/Save/SaveAs/Etc  -- Partial (Save/SaveAs)
 * Change to PNG  -- Done
 * Implement 3D views -- Sort of Done
 * Implement frame-to-frame copying
 * Implement Color Cursor - 
 * Implement animation- 
 * - Implement multi-frame painting -- Done
 * - Implement animation preview
 * - Implement functions to save to stencil animation format -- Done
 * Improve GUI
 * Implement Bucket tool
 * Test and Go
 * Implement executability
 * 
 * Would like" features ---- 
 * Implement regional copying
 * Undo button
 * 
 * 
 *  END TODO
 *  
 * Current Task - Multi-Frame  -- Animation
 * Command for jar building -- jar -cvmf manifest.mf NPCPainter.jar start images
 * 
 *  Transparency = 69, 98, 234
 * Frames to implement - Look Right (Default), Look left, back turned, 4 Frame walk cycle 
 * 
 **/

package start;
//import java.io.*;
//import java.util.TreeSet;
//import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.imageio.ImageIO;
//import java.lang.*;
import javax.swing.*;

//import java.math.*;
public class Paint extends Object{
	public static BufferedImage cursorGen(BufferedImage mask, BufferedImage color) {
		
		BufferedImage combined = new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB);
		combined.getGraphics().drawImage(mask,0,0,null);
		combined.getGraphics().drawImage(color,24,22,null);
		
		
		return combined;
	}
	
	// The palette builders 
    public static Color[] greyscale = {new Color(255,255,255),  new Color(0,0,0), new Color(69,98,234)};
     
    public static Color[][] palette =	{{ /*Greys */	  new Color(230, 230, 230),  new Color(191, 191, 191),   new Color(128, 128, 128), new Color(64, 64, 64), new Color(25,25,25)   },	{ /* Reds */	    new Color(252, 13, 27),	    new Color(189,7,17), new Color(125, 3, 8),	 new Color(62,1,2),   new Color(37, 0, 1)	},  { /*Oranges */	    new Color(252, 107, 33),	    new Color(189, 76, 22), new Color(126,51,11),	new Color(62,26,3),    new Color(38,15,1)	},	 { /* Yellows */	    new Color(255, 253, 56),    new Color(254, 224, 50), new Color(253,187,44),new Color(190,136,30),	new Color(126,90,16)  },	{ /*Light Green */	    new Color(140, 253, 49), new Color(105,189,34), new Color(70,126,19),	    new Color(35,62,5),	    new Color(21,38,2)	}, 	  { /*Green */	    new Color(41, 253, 47),    new Color(28, 189, 32), new Color(15,126,18), new Color(4,62,5),	    new Color(1,38,2)	}, { /*Teal */	    new Color(45, 255, 254),	    new Color(31, 191, 190), new Color(17,127,126), new Color(4,63,63),	    new Color(2, 38, 38)	},	  { /* Light Blue */  new Color(25, 150, 252), new Color(16,113,188), new Color(7,75,125),	    new Color(2,38,62),	    new Color(1,22,37)	}, 	 { /* Blue */	    new Color(73, 87, 251),	    new Color(54, 65, 188), new Color(	36,43,125),    new Color(17,21,62), new Color(10,13,37)	}, 	 { /* Purple */	    new Color(159, 80, 251),   new Color(119, 60, 188), new Color(80,39,125), new Color(40,19,62),    new Color(24,11,37)	},	 { /* Pink */	    new Color(253, 81, 252), new Color(189,61,189), new Color(126,40,125), new Color(62,19,62),	    new Color(38,11,37)},  { /* Salmon */    new Color(253, 104, 136),   new Color(189,77,102), new Color(126,52,68), new Color(62,25,33), new Color(38,15,20)	}    };
    
    public static void main(String[] args) throws IOException{
    	Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Cursor[][] colorCursors = new Cursor[12][5];
		final Cursor[] gsCursors = new Cursor[2];
		
		BufferedImage mask = ImageIO.read(Paint.class.getResource("/images/CursorMask.png"));
		final Cursor eraserCursor = toolkit.createCustomCursor(ImageIO.read(Paint.class.getResource("/images/EraserCursor.png")), new Point(16,16),"Eraser" );
		String[] names = {"Grey", "Red", "Orange", "Yellow", "LightGreen", "Green", "Teal", "LightBlue", "Blue", "Purple", "Pink", "Salmon"};
		JButton[][] buttons = new JButton[12][5];
		JButton[] gsButtons = new JButton[2];

		final JFrame frame = new JFrame("x");
		final JFrame previewer = new JFrame("P");
		
		//Creates a frame with a title of "Paint it"
		
	    Container pcontent = previewer.getContentPane();   
		final Container content = frame.getContentPane();
		//Creates a new container
		
		pcontent.setLayout(new BorderLayout());
		content.setLayout(new BorderLayout());
		//sets the layout
		
		final PadDraw drawPad = new PadDraw();
		//creates a new padDraw, which is pretty much the paint program
		final PreviewPane panePreview = new PreviewPane(drawPad);
		drawPad.previewCallback = panePreview;
		content.add(drawPad, BorderLayout.CENTER);
		//sets the padDraw in the center
		
		final JPanel panel = new JPanel();
		
		final JPanel nonColorPanel = new JPanel();
		Runnable AnimationPane = new Runnable(){
			public void run(){
				
				//final JFrame animationPreview = new JFrame("A");
				//Container acontent = content;//animationPreview.getContentPane();
				//acontent.setLayout(new BorderLayout());
				final Animate animator = new Animate();
				//JPanel animationStep = new JPanel();
				//animationStep.setPreferredSize(new Dimension(64, 64));
				//animationStep.setMinimumSize(new Dimension(128,68));
				//animationStep.setMaximumSize(new Dimension(64,64));
				animator.setPreferredSize(new Dimension(50,100));
				JButton animationPlayPauseButton = new JButton( new ImageIcon(Paint.class.getResource("/images/playpause.png")));
				//JButton animationStepButton = new JButton("Go");
				//JButton animationStopButton = new JButton("Stop");
				animationPlayPauseButton.addActionListener(new ActionListener() {
					boolean isAnimating;
					public void actionPerformed(ActionEvent e){
						if(isAnimating)
						{
							try {
						
							animator.stopAnimating();
							isAnimating = false;
						}
						catch (Exception e1) {
							e1.printStackTrace();
						}
						}
							else
							{	
													
							try {
								animator.oneCycle();
								isAnimating = true;
								
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
						
							}
					}
				});
				nonColorPanel.add(animator);
				nonColorPanel.add(animationPlayPauseButton);
			
				animator.setAnimationSource(drawPad);
				
				animator.oneCycle();
				animator.stopAnimating();
				
				
			}
		};
		final Thread animationThread = new Thread(AnimationPane, "AnimationPane");
		
		
		
		//JPanel previewer = new JPanel();
		//previewer.setPreferredSize(new Dimension(64, 128));
		//previewer.setMinimumSize(new Dimension(64, 128));
		//previewer.setMaximumSize(new Dimension(64, 128));
		
		//creates a JPanel
		panel.setPreferredSize(new Dimension(120, 68));
		panel.setMinimumSize(new Dimension(120, 68));
		panel.setMaximumSize(new Dimension(120, 68));
		nonColorPanel.setPreferredSize(new Dimension(140,68));
		nonColorPanel.setMinimumSize(new Dimension(140,68));
		nonColorPanel.setMaximumSize(new Dimension(140,68));
		
		
		for( int i= 0; i<12;i++) { //Make Buttons
		    for( int j= 0; j<5;j++){
		  
		    buttons[i][j] = new JButton( new ImageIcon(Paint.class.getResource("/images/" + names[i] + "" + j + ".png")));
		    try {
		    	colorCursors[i][j] = toolkit.createCustomCursor(cursorGen(mask, ImageIO.read(Paint.class.getResource("/images/" + names[i] + "" + j + ".png"))), new Point(0,0), names[i] + "" + j + "Cursor");
		    }
		    catch(Exception e)
		    {
		    	System.out.println("CursorLoadingFailure");
		    }
		    final int iter = i;
			final int jter = j;
			buttons[i][j].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					frame.setCursor(colorCursors[iter][jter]);
				    drawPad.useColor(palette[iter][jter]);
				}
			    });
			buttons[i][j].setPreferredSize(new Dimension(16,16));
			panel.add(buttons[i][j]);
		    }
                }
		for( int k = 0; k<2; k++) { //Make Greyscale Buttons
		try { gsButtons[k] = new JButton( new ImageIcon(Paint.class.getResource("/images/gs"+k+".png")));   
			gsCursors[k] = toolkit.createCustomCursor(cursorGen(mask, ImageIO.read(Paint.class.getResource("/images/gs" +k + ".png"))), new Point(0,0), "Greyscale "+k);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
			final int kter = k;
		    	gsButtons[k].addActionListener(new ActionListener() {
		    		public void actionPerformed(ActionEvent e) {
		    			frame.setCursor(gsCursors[kter]);
		    			drawPad.useColor(greyscale[kter]);		    			
		    											}
		    	}
		    	);
		    gsButtons[k].setPreferredSize(new Dimension(16,16));
		    panel.add(gsButtons[k]);
		}
		JButton eraserButton = new JButton( new ImageIcon(Paint.class.getResource("/images/eraser-icon.png")));
		eraserButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				frame.setCursor(eraserCursor);
				drawPad.useColor(greyscale[2]);
			}
		});
		nonColorPanel.add(new JLabel("Walk Preview"));
		animationThread.start();
		//This sets the size of the panel
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e){
				drawPad.save();
			}
		});
		saveButton.setPreferredSize(new Dimension(96,24));
		JButton undoButton = new JButton("Undo");
		undoButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				drawPad.undoThings();
				drawPad.repaint();
			}
		});
		undoButton.setPreferredSize(new Dimension(96,24));
		JButton clearButton = new JButton("Clear");
		//creates the clear button and sets the text as "Clear"
		clearButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				drawPad.clear();
			}
		});
		clearButton.setPreferredSize(new Dimension(96,24));
		JButton previewButton = new JButton("Open Preview");
		previewButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				previewer.setVisible(true);
			}
		});
	
		JButton templateButton = new JButton("Revert");
		templateButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				drawPad.revertToTemplate(drawPad.activeImage.getGraphics());
				drawPad.repaint();
			}
		});
		templateButton.setPreferredSize(new Dimension(96,24));
		
		String[] frames = { "Looking Right", "Looking Left", "Looking Back", "Walking 1", "Walking 2", "Walking 3", "Walking 4" };
		final JComboBox frameSelector = new JComboBox(frames);
		frameSelector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				Color swapColor = drawPad.getColor();
				drawPad.setActiveFrame(frameSelector.getSelectedIndex());
				drawPad.useColor(swapColor);
				//System.out.println(frameSelector.getSelectedIndex());
			}
		});
		
		// It works. TODO: Make it easier to understand.
		
		final JComboBox frameCopy = new JComboBox(frames);
		frameCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				drawPad.copyActiveToFrame(frameCopy.getSelectedIndex());
			}
		});
		
		String[] directions = {"Shift Left","Shift Right","Shift Up","Shift Down"};
		final JComboBox directionShift = new JComboBox(directions);
		directionShift.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch(directionShift.getSelectedIndex()) {
				case 0 :
				{
					drawPad.shiftRight();
					break;
				}
				case 1:
				{
					drawPad.shiftLeft();
					break;
				}
				case 2:
				{
					drawPad.shiftUp();
					break;
				}
				case 3:
				{
					drawPad.shiftDown();
					break;
					
				}
				}
			}
		});
		//nonColorPanel.add(undoButton);
		
		nonColorPanel.add(new JLabel("Switch to frame:"));
		nonColorPanel.add(frameSelector);
		//nonColorPanel.add(new JLabel("    --    "));
		nonColorPanel.add(new JLabel("Copy this frame to:"));
		nonColorPanel.add(frameCopy);
		panel.add(eraserButton);
		panel.add(clearButton);
		panel.add(undoButton);
		
		panel.add(templateButton);
		panel.add(saveButton);
		
		
	
		//nonColorPanel.add(templateButton);
		//previewer.add(panePreview);
		//nonColorPanel.add(saveButton);
		//nonColorPanel.add(new JLabel("    --    "));
		nonColorPanel.add(new JLabel("Shift image by"));
		nonColorPanel.add(new JLabel("1 pixel:"));
		nonColorPanel.add(directionShift);
		//nonColorPanel.add(new JLabel("    --    "));
		nonColorPanel.add(previewButton);
		
		
		
		content.add(panel, BorderLayout.WEST);
		content.add(nonColorPanel, BorderLayout.EAST);
		pcontent.add(panePreview, BorderLayout.CENTER);
		//sets the panel to the left
		
		//Aim for 16X32 pad
		previewer.setSize(336,118);
	
		//previewer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(550,600);
		drawPad.initSize = new Dimension(frame.getSize());
		
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//makes it so you can close
		//frame.pack();
		
		frame.setVisible(true);
		previewer.setLocation(555, 0);
	    previewer.setVisible(true);
	  //  animator.
		//makes it so you can see it
	}
}

