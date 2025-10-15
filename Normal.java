/*
 * Normal.java
 * Heeyoung Shin, Nathan Kim, Danyal Gu
 * June 13th 2024
 * This program displays the normal mode of Melody Dash
 */
package graphics;

//Import necessary classes
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javax.swing.Timer;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import hsa2.GraphicsConsole;

public class Normal implements ActionListener {
	final int WIDW = 1000; // Window width
	final int WIDH = 650; // Window height
	final int TILEWIDTH = WIDW / 6; // Tile width
	final int TILEHEIGHT = 100; // Tile height
	final int TILESPEED = 10; // Speed of tiles falling
	final int SPAWNINTERVAL = 35; // Period of creating one tile
	final int YLINE = WIDH - 100; // Y coordinate of the grading line
	final int TIMESPEED = 1; // Speed of the grade system (0.001 sec)
	
	// Graphics console setup
	GraphicsConsole gc = new GraphicsConsole(WIDW, WIDH, "Normal Mode");
	// Attributes
	Timer gt = new Timer(TIMESPEED, this); // grade timer
	Font gradeFont = new Font("Roboto", Font.ITALIC, 50); // Font used to indicate grade
	ArrayList<Tile> tiles; // ArrayList to add and remove tiles
	int score;
	int counter; // Counter for when to create a tile
	int timeCounter; // Counter for the timer
	Random rand;
	//Set boolean grade to notify the timing of the timer
	boolean perfect = false;
	boolean good = false;
	boolean bad = false;
	// Clip object for music
	Clip audioClip;
	
	/**
	 * Constructor 
	 */
	Normal() {
		tiles = new ArrayList<>();
		score = 0;
		counter = 0;
		rand = new Random();
	}
	
	/**
	 * Run the whole game
	 */
	void run() {
		setup();
		ready();
		String filePath = "normalMusic.wav";
		// Start music in a separate thread
		Thread musicThread = new Thread(() -> playAudio(filePath));
		musicThread.start();
		while (true) {
			update();		
			gc.setFont(gradeFont);
			draw();
			gc.sleep(20);
		}
	}
	
	/**
	 *  Setup for the game
	 */
	private void setup() {
		gc.setAntiAlias(true);
		gc.setLocationRelativeTo(null);
	}
	
	/**
	 * Display numbers from 3 to 1 for time to ready
	 */
	private void ready() {
		gc.setFont(gradeFont);
        drawSections();
        gc.sleep(1500);
        gc.setFont(gradeFont);
        gc.setColor(Color.BLACK);
        gc.drawString("3", WIDW / 2 - 40, WIDH / 2);
        gc.sleep(1500);
        gc.clear();
        drawSections();
        gc.drawString("2", WIDW / 2 - 40, WIDH / 2);
        gc.sleep(1500);
        gc.clear();
        drawSections();
        gc.drawString("1", WIDW / 2 - 40, WIDH / 2);
        gc.sleep(1500);
        gc.clear();
        drawSections();
        gc.drawString("GO!", WIDW / 2 - 40, WIDH / 2);
        gc.sleep(1500);
	}
	
	/**
	 * Check for tiles, hits, and game over and updating them
	 */
	private void update() {
		counter++;
		// Create tile every spawn interval
		if (counter % SPAWNINTERVAL == 0) {
			spawnTile();
		}
		
		for (int i = 0; i < tiles.size(); i++) {
			Tile tile = tiles.get(i);
			tile.update();
			// Remove tile and game over if it goes beyond the screen
			if (tile.y > WIDH) {
				tiles.remove(i);
				i--;
				if (gt.isRunning()) {
					gt.stop(); 
					gc.clear();
				}
				gameOver();
			}
		}
		
		// Check for all the keys pressed
		checkTileHit(KeyEvent.VK_S, 0);
		checkTileHit(KeyEvent.VK_D, 1);
		checkTileHit(KeyEvent.VK_F, 2);
		checkTileHit(KeyEvent.VK_H, 3);
		checkTileHit(KeyEvent.VK_J, 4);
		checkTileHit(KeyEvent.VK_K, 5);
	}
	
	/**
	 * Draw the whole game
	 */
	private void draw() {
		synchronized(gc) {
			gc.clear();
			drawSections();
			drawGradingLine();
			for (Tile tile : tiles) {
				tile.draw(gc);
			}
			//Set the score system
			gc.setColor(Color.GRAY);
			gc.drawString("Scores: " + score, 5, 50);
			gc.repaint();
		}
	}
	
	/**
	 * Draw sections for the tiles
	 */
	private void drawSections() {
		String[] keys = {"S", "D", "F", "H", "J", "K"};
		for (int i = 0; i < keys.length; i++) {
			int x = i * TILEWIDTH;
			gc.setColor(Color.BLACK);
			gc.drawRect(x, 0, TILEWIDTH, WIDH);
			gc.drawString(keys[i], x + (TILEWIDTH / 2) - 5, WIDH - 10);
		}
	}
	
	/**
	 * Create a grading line
	 */
	private void drawGradingLine() {
		gc.setColor(Color.RED);
		gc.drawLine(0, YLINE, WIDW, YLINE);
	}
	
	/**
	 * Create a tile by adding to the tile arraylist
	 */
	private void spawnTile() {
		int lane;
		// Randomly spawning a tile
		do {
			lane = rand.nextInt(6);
		} while (!isLaneFree(lane));
		
		int x = lane * TILEWIDTH;
		tiles.add(new Tile(x, -TILEHEIGHT, TILEWIDTH, TILEHEIGHT, TILESPEED));
	}
	
	/**
	 * Check if any lane is free to randomize the tile spawn
	 * @param lane	integer number of a section
	 * @return returning if it is empty or not
	 */
	private boolean isLaneFree(int lane) {
		for (Tile tile : tiles) {
			if (tile.x == lane * TILEWIDTH && tile.y < TILEHEIGHT) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Draw grades according to the timer
	 */
	@Override
	public void actionPerformed(ActionEvent ev) {
		gc.setAntiAlias(true);
		synchronized(gc) {
			gc.repaint();
			if (perfect) {
				gc.setFont(gradeFont);
				gc.setColor(Color.BLUE);
				gc.drawString("Perfect", WIDW / 2 - 90, WIDH / 2);
			}
			if (good) {
				gc.setFont(gradeFont);
				gc.setColor(Color.GREEN);
				gc.drawString("Good", WIDW / 2 - 75, WIDH / 2);
			}
			if (bad) {
				gc.setFont(gradeFont);
				gc.setColor(Color.RED);
				gc.drawString("Bad", WIDW / 2 - 45, WIDH / 2);
			}
			timeCounter++;
			// Stop the timer after 0.1 second
			if (timeCounter == 100) {
				if (gt.isRunning()) {
					gc.clear();
					perfect = false;
					good = false;
					bad = false;
					gt.stop();
				}
			}
		}
	}
	
	/**
	 * Check tiles if they are hit or not
	 * @param key 	integer value to determine which key
	 * @param lane 	integer value to determine which lane
	 */
	private void checkTileHit(int key, int lane) {
		if (gc.isKeyDown(key)) {
			for (int i = 0; i < tiles.size(); i++) {
				Tile tile = tiles.get(i);
				if (tile.x == lane * TILEWIDTH && (tile.y + tile.height) >= YLINE - TILESPEED) {
					int tileCenter = tile.y + tile.height / 2; // Y coordinate of the center of a tile
					// Select grade depending on the difference between the y coordinates of tile center and grading line
					if (Math.abs(tileCenter - YLINE) <= 5) {
						score += 5;
						perfect = true;
						good = false;
						bad = false;
						if (!gt.isRunning()) gt.restart();
					} else if (Math.abs(tileCenter - YLINE) <= 15) {
						score += 3;
						perfect = false;
						good = true;
						bad = false;
						if (!gt.isRunning()) gt.restart();
					} else if (Math.abs(tileCenter - YLINE) <= 30) {
						score += 1;
						perfect = false;
						good = false;
						bad = true;
						if (!gt.isRunning()) gt.restart();
					} else {
						if (gt.isRunning()) {
							gt.stop(); 
							gc.clear();
						}
						gameOver();
					}
					// Remove tile when hit
					tiles.remove(i);
					break;
				}
			}
		}
	}
	
	/**
	 * Show game over screen with score
	 */
	private void gameOver() {
		// Stop the music
        if (audioClip != null && audioClip.isRunning()) {
            audioClip.stop();
        }
        gc.clear();
        gc.setColor(Color.BLACK);
        gc.fillRect(0, 0, WIDW, WIDH);
        gc.setColor(Color.WHITE);
        gc.setFont(new Font("Times New Roman",Font.BOLD,50));
        gc.drawString("Game Over! Your score: " + score, WIDW / 2 - 300, WIDH / 2-50);
        gc.setFont(new Font("Times New Roman",Font.BOLD,25));
        gc.drawString("Press M for Menu", 400, 400);
        while (true) {
            if (gc.getKeyChar() == 'm' || gc.getKeyChar() == 'M') {
                gc.setVisible(false);
                Intro introScreen = new Intro();
            }
            gc.repaint();
        }
	}
	
	/**
	 * Play music file
	 * @param location	String value for the path to the music file to be played
	 */
	private void playAudio(String location) {
        try {
            File filePath = new File(location);
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(filePath);
            audioClip = AudioSystem.getClip();
            audioClip.open(audioInput);
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);
        }catch (Exception e) {
            e.printStackTrace();
        } 
    }
}	
