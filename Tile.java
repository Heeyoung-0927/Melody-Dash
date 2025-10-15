/*
 * Tile.java
 * Heeyoung Shin, Nathan Kim, Danyal Gu
 * June 13th 2024
 * This program contains tile class for the falling tiles in the main game of Melody Dash
 */
package graphics;

import java.awt.Color;

import hsa2.GraphicsConsole;

public class Tile {
	int x, y, width, height, speed;
	
	Tile(int x, int y, int width, int height, int speed) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.speed = speed;
	}
	
	void update() {
		y += speed;
	}
	
	void draw(GraphicsConsole gc) {
		gc.setColor(Color.BLACK);
		gc.fillRect(x, y, width, height);
	}
}