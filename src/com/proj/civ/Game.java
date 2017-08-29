package com.proj.civ;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.proj.civ.ai.Pathfinding;
import com.proj.civ.datastruct.Layout;
import com.proj.civ.datastruct.Point;
import com.proj.civ.datastruct.hex.Hex;
import com.proj.civ.datastruct.hex.HexCoordinate;
import com.proj.civ.datastruct.hex.PathHex;
import com.proj.civ.datastruct.map.HexMap;
import com.proj.civ.display.GUI;
import com.proj.civ.input.KeyboardHandler;
import com.proj.civ.input.MouseHandler;
import com.proj.civ.map.civilization.America;
import com.proj.civ.map.civilization.BaseCivilization;
import com.proj.civ.unit.Settler;
import com.proj.civ.unit.Unit;
import com.proj.civ.unit.Warrior;

public class Game {
	private final int TOTAL_PLAYERS;
	private final int HEX_RADIUS;
	
	private final HexMap hexMap;
	
	private int wHexes = 128; //40
	private int hHexes = 80; //25
	
	private Random rnd;
	private GUI ui;
	private List<BaseCivilization> civs;
	private Layout layout;
	private Pathfinding pf;
	
	private HexCoordinate hexToPath;
	private List<HexCoordinate> pathToFollow;
	
 	public Game(int players, int width, int height, int hexradius) {
		this.HEX_RADIUS = hexradius;
		this.TOTAL_PLAYERS = players;
		
		hexMap = new HexMap(this.wHexes, this.hHexes, HEX_RADIUS, layout);
		
		rnd = new Random();
		ui = new GUI(hexMap, width, height, hexradius, hexradius, hexradius, wHexes, hHexes); //Initalize GUI
		civs = new ArrayList<BaseCivilization>(this.TOTAL_PLAYERS);
		layout = new Layout(Layout.POINTY_TOP, new Point(HEX_RADIUS, HEX_RADIUS), new Point(HEX_RADIUS, HEX_RADIUS));
		pf = new Pathfinding();
		
		hexMap.populateMap(); //Generate initial map
		
		createCiv();
	}

	public void update(KeyboardHandler k) {
		if (k.pressedSet.size() > 0) {
			ui.updateKeys(k.pressedSet);
		}
		
		ui.setFocusHex();
		createUnitPath();
		
		if (shouldMoveUnit()) {
			moveUnit(hexMap.getMap(), civs.get(0), ui.getFocusHex(), ui.getScrollX(), ui.getScrollY());
			ui.resetFocusHex();
			MouseHandler.pressedMouse = false;
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
			tempH = hexMap.getHex(settler);
		} while ((tempH == null)); // && (tempH.getLandscape() != Landscape.COAST)
		
		//Ensure the neighbour is in the map, the following loop will not get stuck on infinite loop
		do {

			warrior = settler.getRandomNeighbour();
		} while (hexMap.getHex(warrior) == null);
		
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
			double x = (double) rnd.nextInt(wHexes * HEX_RADIUS * 2);
			double y = (double) rnd.nextInt(hHexes * HEX_RADIUS * 2);
			HexCoordinate h1 = layout.pixelToHex(layout, new Point(x, y));
			h = hexMap.getHex(h1);
		} while (h == null);
		
		return new HexCoordinate(h.q, h.r, h.s);
	}
	
	private void addUnit(HexCoordinate h, Unit u, boolean isMilitary) {
		//Get the map hex for units
		Hex hex = hexMap.getHex(h);

		//Set the units in the hexes
		hex.addNewUnit(u, isMilitary);
		
		//Update the hexes in the map
		hexMap.setHex(h, hex);
		
		//Add units to the civ
		BaseCivilization c1 = civs.get(0);
		c1.addUnit(u);
		civs.set(0, c1);
	}
	private void moveUnit(Hex fromHex, Hex toHex, Unit u, double totalHexCost) {
		HexCoordinate newLocation = toHex.getPosition();
		u.decreaseMovement(totalHexCost);

		Unit tempUnit = u;
		tempUnit.setPosition(newLocation);

		fromHex.resetUnits();
		toHex.replaceUnit(u, tempUnit.getIsMilitary());
		
		//Move the unit on the map
		hexMap.setHex(toHex, toHex);
		hexMap.setHex(fromHex, fromHex);
		
		//Add units to the civ
		BaseCivilization c1 = civs.get(0);
		c1.replaceUnit(u, tempUnit);
		civs.set(0, c1);
	}
	private void swapUnit(Hex fromHex, Hex toHex, Unit currentFromUnit, Unit currentToUnit, double totalHexCost) {		
		currentToUnit.decreaseMovement(totalHexCost);
		currentFromUnit.decreaseMovement(totalHexCost);
		
		Unit tempToUnit = currentToUnit;
		Unit tempFromUnit = currentFromUnit;
		HexCoordinate unitFrom = currentFromUnit.getPosition();
		HexCoordinate unitTo = currentToUnit.getPosition();
		
		tempToUnit.setPosition(unitFrom);
		tempFromUnit.setPosition(unitTo);
		
		fromHex.replaceUnit(tempToUnit, currentToUnit.getIsMilitary());
		toHex.replaceUnit(tempFromUnit, currentFromUnit.getIsMilitary());
		
		//Move the units on the map
		hexMap.setHex(toHex, toHex);
		hexMap.setHex(fromHex, fromHex);
		
		//Add units to the civ
		BaseCivilization c1 = civs.get(0);
		c1.replaceUnit(currentFromUnit, tempFromUnit);
		c1.replaceUnit(currentToUnit, tempToUnit);
		civs.set(0, c1);
	}

	private boolean shouldMoveUnit() {
		HexCoordinate fromHex = ui.getFocusHex();
		if (fromHex != null) { //If a unit is currently selected
			if (MouseHandler.pressedMouse) {
				HexCoordinate toHexPlace = layout.pixelToHex(layout, ui.getHexPosFromMouse());
				if (fromHex != null) {
					if (!fromHex.isEqual(toHexPlace)) {
						List<PathHex> path = ui.getUnitPath();
						if (path != null) {
							return (path.stream().anyMatch(i -> i.getPassable() && i.isEqual(toHexPlace)))
									|| (path.stream().filter(j -> j.isEqual(toHexPlace)).anyMatch(k -> k.getCanSwitch()));
						}
					}
				}	
			} 
		}
		return false;
	}
	public void moveUnit(Map<Integer, Hex> map, BaseCivilization c, Hex focusHex, int scrollX, int scrollY) {
		Hex fromHex = hexMap.getHex(focusHex);
		if (!fromHex.canSetCivilian() || !fromHex.canSetMilitary()) {			
			int mouseX = MouseHandler.movedMX;
			int mouseY = MouseHandler.movedMY;
			
			HexCoordinate h = layout.pixelToHex(layout, new Point(mouseX - scrollX, mouseY - scrollY));
			Hex toHex = hexMap.getHex(h);
			
			Unit cu = fromHex.getCivilianUnit();
			Unit mu = fromHex.getMilitaryUnit();
			
			Unit ctu = toHex.getCivilianUnit();
			Unit mtu = toHex.getMilitaryUnit();
			
			double pathTotal = 0.0D;
			for (PathHex ph : ui.getUnitPath()) {
				if (ph.getPassable() || ph.getCanSwitch()) {
					Hex mapHex = hexMap.getHex(ph);
					pathTotal += mapHex.getMovementTotal();	
				}
			}
			
			//Civ units and military units cannot occupy the same hex (for now)
			if (cu != null && ctu == null && mu == null && mtu != null) { //swapping units -- civ to mil
				if (sameOwner(cu, mtu)) {
					swapUnit(fromHex, toHex, cu, mtu, pathTotal);
				}
			} else if (cu == null && ctu != null && mu != null && mtu == null) { //swapping units -- mil to civ
				if (sameOwner(mu, ctu)) {
					swapUnit(fromHex, toHex, mu, ctu, pathTotal);
				}
			} else if ((cu != null || mu != null) && ctu == null && mtu == null) { //Move civ or mil units to empty hex
				moveUnit(fromHex, toHex, cu != null ? cu : mu, pathTotal);
			}
			ui.setFocusedUnitPath(null);
		}
	}
	private boolean sameOwner(Unit fromU, Unit toU) {
		return fromU.getOwner().sameCivilization(toU.getOwner().getID());
	}
	
	public void createUnitPath() {
		Hex focusHex = ui.getFocusHex();
		Hex endHex;
		pathToFollow = new ArrayList<HexCoordinate>();
		int scrollX = ui.getScrollX();
		int scrollY= ui.getScrollY();
		
		if (focusHex != null) {
			int toX = MouseHandler.movedMX;
			int toY = MouseHandler.movedMY;
			HexCoordinate tempTo = layout.pixelToHex(layout, new Point(toX - scrollX, toY - scrollY));
			if (hexMap.getHex(tempTo) != null && !focusHex.isEqual(tempTo)) {
				if ((hexToPath == null) || (!hexToPath.isEqual(tempTo))) {
					hexToPath = tempTo;
					endHex = hexMap.getHex(hexToPath);
					pathToFollow = pf.findPath(hexMap.getMap(), focusHex, endHex);
					List<PathHex> finalPath = validUnitMove(pathToFollow, focusHex);
					ui.setFocusedUnitPath(finalPath);
				}
			}
		}
	}
	private List<PathHex> validUnitMove(List<HexCoordinate> path, Hex focusHex) {
		List<PathHex> finalPath = new ArrayList<PathHex>();
		List<Unit> civUnits = civs.get(0).getUnits();
		Hex current = hexMap.getHex(focusHex);
		Unit currentUnit = null;
		
		for (Unit u : civUnits) {
			if (u.getPosition().isEqual(current.getPosition())) {
				currentUnit = u;
			}
		}
		
		boolean unitBlocking = false;
		double currentPathCost = 0D;
		for (int i = path.size(); --i >= 0;) {
			HexCoordinate h = path.get(i);
			Hex mapHex = hexMap.getHex(h);
			
			double hexCost = current.getMovementTotal();
			boolean unitMovementRemaining = currentUnit.ableToMove(hexCost);
			boolean done = false;
			
			currentPathCost += hexCost;
			
			if (unitMovementRemaining) {
				if (!unitBlocking) {
					for (Unit u : mapHex.getUnits()) { //Check for a unit blocking the path
						if (u != null) {
							boolean canSwitch = false; 
							if (u.getOwner() == currentUnit.getOwner() && u.ableToMove(currentPathCost)) {
								canSwitch = true;
							}
							unitBlocking = true;
							finalPath.add(new PathHex(h, !unitBlocking, canSwitch));
							done = true;
						}
					}
					if (!done) {
						finalPath.add(new PathHex(h, true));
					}
				} else {
					finalPath.add(new PathHex(h, false));
				}
			} else {
				finalPath.add(new PathHex(h, false));
			}
		}
		currentUnit.setMovementTempForMultiMove();
		return finalPath;
	}
}
