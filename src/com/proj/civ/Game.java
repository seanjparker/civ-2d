package com.proj.civ;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.proj.civ.datastruct.Hex;
import com.proj.civ.datastruct.HexCoordinate;
import com.proj.civ.datastruct.HexMap;
import com.proj.civ.datastruct.Layout;
import com.proj.civ.datastruct.Point;
import com.proj.civ.display.GUI;
import com.proj.civ.input.KeyboardHandler;
import com.proj.civ.input.MouseHandler;
import com.proj.civ.map.civilization.America;
import com.proj.civ.map.civilization.Civilization;
import com.proj.civ.unit.Settler;
import com.proj.civ.unit.Unit;
import com.proj.civ.unit.Warrior;

public class Game {
	private final int TOTAL_PLAYERS;
	private final int WIDTH;
	private final int HEIGHT;
	private final int HEX_RADIUS;
	
	private final HexMap hexMap;
	
	private int wHexes = 40; //40
	private int hHexes = 25; //25
	
	private boolean shouldUpdate = false;
	
	private Random rnd;
	private GUI ui;
	private Logic gl;
	private List<Civilization> civs;
	private Map<Integer, Hex> map;
	private Layout layout;
	
	public Game(int players, int width, int height, int hexradius) {
		this.WIDTH = width;
		this.HEIGHT = height;
		this.HEX_RADIUS = hexradius;
		this.TOTAL_PLAYERS = players;
		
		rnd = new Random();
		ui = new GUI(width, height, hexradius, hexradius, hexradius, wHexes, hHexes); //Initalize GUI
		civs = new ArrayList<Civilization>(this.TOTAL_PLAYERS);
		layout = new Layout(Layout.POINTY_TOP, new Point(HEX_RADIUS, HEX_RADIUS), new Point(HEX_RADIUS, HEX_RADIUS));
		hexMap = new HexMap(this.wHexes, this.hHexes, HEX_RADIUS, layout);
		gl = new Logic(layout); //Create the game logic class, contains the main game civ logic
		
		hexMap.populateMap(); //Generate initial map
		this.map = hexMap.getMap(); //Get the map from the map generation
		ui.setMap(this.map); //Set the map in the ui
		
		createCiv();
	}

	public void update(KeyboardHandler k) {
		if (k.pressedSet.size() > 0) {
			ui.updateKeys(k.pressedSet);
		}
		ui.setFocusHex();
		
		if (shouldMoveUnit()) {
			gl.moveUnit(map, civs.get(0), ui.getFocusHex(), ui.getScrollX(), ui.getScrollY());
			shouldUpdate = true;
			MouseHandler.pressedMouse = false;
		}
		if (shouldUpdate) { //Last thing, if the map has been changed, update the map
			ui.setMap(map);
			shouldUpdate = false;
		}
	}
	
	public void draw(Graphics2D g) {
		ui.drawHexGrid(g);
		ui.drawUI(g);
		ui.drawUnits(g, civs);
		ui.drawSelectedHex(g);
		ui.drawHexInspect(g);
		ui.drawPath(g);
		ui.drawFocusHex(g);
	}
	
	private void createCiv() {
		//Initalize civ
		civs.add(new America());
		
		//Add units to the game map
		//Get settler and warrior co-ordinate
		HexCoordinate settler, warrior;
		Hex tempH;
		
		do {
			settler = getRandomUnitCoord();
			tempH = map.get(HexMap.hash(settler));
		} while ((tempH == null)); // && (tempH.getLandscape() != Landscape.COAST)
		
		//Ensure the neighbour is in the map, the following loop will not get stuck on infinite loop
		do {
			warrior = settler.getRandomNeighbour();
		} while (map.get(HexMap.hash(warrior)) == null);
		
		//Set the units in the hexes
		Unit s = new Settler(civs.get(0), settler, true);
		Unit w = new Warrior(civs.get(0), warrior, true);

		addUnit(settler, s, false);
		addUnit(warrior, w, true);
		
		ui.setInitialScroll(settler);
	}
	
	private HexCoordinate getRandomUnitCoord() {
		Hex h = null;
		do {
			double x = (double) rnd.nextInt(wHexes * HEX_RADIUS);
			double y = (double) rnd.nextInt(hHexes * HEX_RADIUS);
			HexCoordinate h1 = layout.pixelToHex(layout, new Point(x, y));
			h = map.get(HexMap.hash(h1));
		} while (h == null);
		
		return new HexCoordinate(h.q, h.r, h.s);
	}
	
	private void addUnit(HexCoordinate h, Unit u, boolean isMilitary) {
		//Get the map hex for units
		int hexHash = HexMap.hash(h);
		Hex hex = map.get(hexHash);

		//Set the units in the hexes
		hex.addNewUnit(u, isMilitary);
		
		//Update the hexes in the map
		map.replace(hexHash, hex);
		
		//Add units to the civ
		Civilization c1 = civs.get(0);
		c1.addUnit(u);
		civs.set(0, c1);
	}

	public Map<Integer, Hex> getMap() {
		return map;
	}

	private boolean shouldMoveUnit() {
		HexCoordinate fromHex = ui.getFocusHex();
		if (fromHex != null) { //If a unit is currently selected
			if (MouseHandler.pressedMouse) {
				HexCoordinate toHexPlace = layout.pixelToHex(layout, ui.getHexPosFromMouse());
				if (fromHex != null) {
					if (!fromHex.isEqual(toHexPlace)) {
						return true;	
					}
				}	
			}
		}
		return false;
	}
}
