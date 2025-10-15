/*
 * intro.java
 * Heeyoung Shin, Nathan Kim, Danyal Gu
 * June 13th 2024
 * This program displays the intro screen of Melody Dash
 */

package graphics;

// import necessary classes
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import hsa2.GraphicsConsole;

public class Intro {
	
	//main
	public static void main(String[] args) {
		new Intro();
	}
	
	/**
	 * Method to import image to java
	 * @param filename to be loaded
	 * @return returns the image
	 */
	BufferedImage loadImage(String fileName) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An image failed to load", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		return img;
	}
	
	//graphics console for displaying start menu
	GraphicsConsole gcIntro = new GraphicsConsole(650, 650, "Start Menu");
	//rectangles that will act as buttons
	Rectangle r1 = new Rectangle(200, 300, 250, 70);
	Rectangle r2 = new Rectangle(200, 400, 250, 70);
	Rectangle r3 = new Rectangle(200, 500, 250, 70);
	Rectangle r4 = new Rectangle(25, 590, 120, 30);
	//loading background image
	BufferedImage img = loadImage("introBackground.png");
	//mouse coordinates
	int mx, my, prevMx, prevMy;
	//variable to keep tract on currently highlighted button
	int currentRegion = -1;
	//fonts
	Font titleFont = new Font("Times New Roman", Font.BOLD, 80);
	Font difficultyFont = new Font("Times New Roman", Font.BOLD, 40);
	Font quitFont = new Font("Times New Roman", Font.BOLD, 17);
	
	/**
	 * Constructor; generates intro screen
	 */
	Intro() {
		introSetup();
		introDraw();
		introCheck();
	}
	
	/**
	 * setup for introscreen
	 */
	void introSetup() {
        gcIntro.setAntiAlias(true);
        gcIntro.setLocationRelativeTo(null);
        gcIntro.enableMouseMotion();
        gcIntro.enableMouse();
    }
	
	/**
	 *  hsa2 drawing for intro screen
	 */
	void introDraw() {
        synchronized (gcIntro) {
        	gcIntro.clear();
        	
        	//setting image as background
            gcIntro.drawImage(img, 0, 0, 650, 650);
            
            //fill buttons
            gcIntro.setColor(Color.LIGHT_GRAY);
            gcIntro.fillRoundRect(r1.x, r1.y, r1.width, r1.height, 10, 10);
            gcIntro.fillRoundRect(r2.x, r2.y, r2.width, r2.height, 10, 10);
            gcIntro.fillRoundRect(r3.x, r3.y, r3.width, r3.height, 10, 10);
            gcIntro.fillRoundRect(r4.x, r4.y, r4.width, r4.height, 5, 5);
            
            //draw button outlines
            gcIntro.setStroke(5);
            gcIntro.setColor(Color.GRAY);
            gcIntro.drawRoundRect(r1.x, r1.y, r1.width, r1.height, 10, 10);
            gcIntro.drawRoundRect(r2.x, r2.y, r2.width, r2.height, 10, 10);
            gcIntro.drawRoundRect(r3.x, r3.y, r3.width, r3.height, 10, 10);
            gcIntro.setStroke(3);
            gcIntro.drawRoundRect(r4.x, r4.y, r4.width, r4.height, 5, 5);

            //draw title
            gcIntro.setColor(Color.WHITE);
            gcIntro.setFont(titleFont);
            gcIntro.drawString("MELODY DASH", 20, 175);
            
            //draw difficulty on the buttons
            gcIntro.setColor(Color.BLACK);
            gcIntro.setFont(difficultyFont);
            gcIntro.drawString("EASY", 270, 350);
            gcIntro.drawString("NORMAL", 235, 450);
            gcIntro.drawString("HARD", 270, 550);
            
            gcIntro.setFont(quitFont);
            gcIntro.drawString("Press Q to quit", 30, 610);
        }
    }
	
	/**
	 * method to change the colour of button while button contains the mouse coordinate
	 * @param r rectangles
	 * @param text string for text in each button 
	 * @param textX the x value of each string
	 * @param textY the y value of each string
	 */
	void highlightButton(Rectangle r, String text, int textX, int textY) {
		synchronized(gcIntro) {
			gcIntro.setColor(Color.RED);
			gcIntro.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);
			gcIntro.setColor(Color.WHITE);
			gcIntro.setFont(difficultyFont);
			if (r == r4) gcIntro.setFont(quitFont);
			gcIntro.drawString(text, textX, textY);
		}
	}
	
	/**
	 *  getting user input and proceeed corresponding response 
	 */
	void introCheck() {
		while(true) {
			mx = gcIntro.getMouseX();
			my = gcIntro.getMouseY();
			boolean isRedrawNeeded = mx != prevMx || my != prevMy;
			
			//check if user presses q to quit
			if (gcIntro.getKeyChar() == 'q' || gcIntro.getKeyChar() == 'Q') {
				gcIntro.sleep(500);
				System.exit(0);
			}
			
			//checking the position of the mouse
			if (isRedrawNeeded) {
				int newRegion = -1;
				
				//checking which button the mouse is on
				if (r1.contains(mx, my)) newRegion = 1;
				else if (r2.contains(mx, my)) newRegion = 2;
				else if (r3.contains(mx, my)) newRegion = 3;
				else if (r4.contains(mx, my)) newRegion = 4;
				
				//redrawing for when the mouse moved out of the button
				if (newRegion != currentRegion) {
					introDraw();
					if (newRegion == 1) highlightButton(r1, "EASY", 270, 350);
					else if (newRegion == 2) highlightButton(r2, "NORMAL", 235, 450);
					else if (newRegion == 3) highlightButton(r3, "HARD", 270, 550);
					else if (newRegion == 4) highlightButton(r4, "Press Q to quit", 30, 610);
					currentRegion = newRegion;
				}
				prevMx = mx;
				prevMy = my;
			}
			
			//check if any button is clicked
			if (r1.contains(mx, my) && gcIntro.getMouseButton(0) && gcIntro.getMouseClick() > 0) {
                gcIntro.setVisible(false);
                gcIntro.sleep(500);
                Easy easyGame = new Easy();
                easyGame.run();
            } else if (r2.contains(mx, my) && gcIntro.getMouseButton(0) && gcIntro.getMouseClick() > 0) {
                gcIntro.setVisible(false);
                gcIntro.sleep(500);
                Normal normalGame = new Normal();
                normalGame.run();
            } else if (r3.contains(mx, my) && gcIntro.getMouseButton(0) && gcIntro.getMouseClick() > 0) {
                gcIntro.setVisible(false);
                gcIntro.sleep(500);
                Hard hardGame = new Hard();
                hardGame.run();
            } else if (r4.contains(mx, my) && gcIntro.getMouseButton(0) && gcIntro.getMouseClick() > 0) {
                gcIntro.sleep(500);
                System.exit(0);
            }
		}
	}
}