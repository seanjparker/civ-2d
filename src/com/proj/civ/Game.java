package com.proj.civ;

import java.awt.Graphics2D;

import com.proj.civ.display.GUI;
import com.proj.civ.input.KeyboardHandler;

public class Game {
	private final int TOTAL_PLAYERS;
	private final int WIDTH;
	private final int HEIGHT;
	private final int HEX_RADIUS;
	
	private GUI ui;
	
	public Game(int players, int width, int height, int hexradius) {
		this.WIDTH = width;
		this.HEIGHT = height;
		this.HEX_RADIUS = hexradius;
		this.TOTAL_PLAYERS = players;
		
		ui = new GUI(width, height, hexradius, hexradius, hexradius);
		
		ui.createCiv();
	}
	
	public void createCiv() {
		ui.createCiv();
	}
	public void update(KeyboardHandler k) {
		if (k.pressedSet.size() > 0) {
			ui.updateKeys(k.pressedSet);
		}
	}
	
	public void draw(Graphics2D g) {
		ui.drawHexGrid(g);
		ui.drawUI(g);
		ui.drawUnits(g);
		ui.drawSelectedHex(g);
		ui.drawHexInspect(g);
		ui.drawPath(g);
		ui.drawFocusHex(g);
	}
}
