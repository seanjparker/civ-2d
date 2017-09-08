package com.proj.civ;

import java.awt.Graphics2D;
import java.util.List;
import java.util.Random;

import com.proj.civ.ai.Pathfinding;
import com.proj.civ.datastruct.Point;
import com.proj.civ.datastruct.hex.Hex;
import com.proj.civ.datastruct.hex.HexCoordinate;
import com.proj.civ.datastruct.hex.PathHex;
import com.proj.civ.input.KeyboardHandler;
import com.proj.civ.input.MouseHandler;
import com.proj.civ.instance.IData;
import com.proj.civ.map.civilization.America;
import com.proj.civ.unit.Settler;
import com.proj.civ.unit.Unit;
import com.proj.civ.unit.Warrior;

public class Game extends IData {
	private Random rnd;
	private Pathfinding pf;
	
	private HexCoordinate hexToPath;
	private List<HexCoordinate> pathToFollow;
	
 	public Game(int players) {
		rnd = new Random();
		pf = new Pathfinding();
		
		hexMap.populateMap(); //Generate initial map
		
		createCiv();
	}

	public void update(KeyboardHandler k) {
		if (k.pressedSet.size() > 0) {
			ui.updateKeys(k.pressedSet);
		}
		
		ui.setFocusHex();
		setCurrentUnit();
		createUnitPath();
		
		if (shouldMoveUnit()) {
			currentUnit.moveUnit(ui.getFocusHex(), ui.getScrollX(), ui.getScrollY());
			MouseHandler.pressedMouse = false;
		}
		
		if (currentUnit != null) {
			currentUnit.getMenu().getMenuButtons().stream().filter(i -> i.getIsClickable()).forEach(j -> j.onPress());
		}
		if (ui.getMenuButton().getIsClickable()) {
			ui.getMenuButton().onPress();
		}
	}
	
	public void draw(Graphics2D g) {
		ui.drawHexGrid(g);
		ui.drawUnits(g);
		ui.drawSelectedHex(g);
		ui.drawPath(g);
		ui.drawFocusHex(g);
		ui.drawHexInspect(g);
		ui.drawUI(g);
		ui.drawActionMenus(g);
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
		} while ((tempH == null));
		
		//Ensure the neighbour is in the map, the following loop will not get stuck on infinite loop
		do {
			warrior = settler.getRandomNeighbour();
		} while (hexMap.getHex(warrior) == null);
		
		//Set the units in the hexes
		Unit s = new Settler(civs.get(0), settler, true);
		Unit w = new Warrior(civs.get(0), warrior, true);
		s.addToMapAndCiv();
		w.addToMapAndCiv();
		
		ui.setInitialScroll(settler);
	}
	
	private HexCoordinate getRandomUnitCoord() {
		Hex h = null;
		do {
			double x = (double) rnd.nextInt(W_HEXES * HEX_RADIUS * 2);
			double y = (double) rnd.nextInt(H_HEXES * HEX_RADIUS * 2);
			HexCoordinate h1 = layout.pixelToHex(new Point(x, y));
			h = hexMap.getHex(h1);
		} while (h == null);
		
		return new HexCoordinate(h.q, h.r, h.s);
	}

	private void setCurrentUnit() {
		if (ui.getFocusHex() != null) {
			if (currentUnit == null) {
				List<Unit> civUnits = civs.get(0).getUnits();
				Hex currentHex = hexMap.getHex(ui.getFocusHex());
				for (Unit u : civUnits) {
					if (u.getPosition().isEqual(currentHex.getPosition())) {
						currentUnit = u;
					}
				}
			}
		}
	}
	
	public void createUnitPath() {
		Hex focusHex = ui.getFocusHex();
		if (focusHex != null) {
			if (KeyboardHandler.MoveUnitPressed) {
				int toX = MouseHandler.movedMX;
				int toY = MouseHandler.movedMY;
				int scrollX = ui.getScrollX();
				int scrollY = ui.getScrollY();
				Hex endHex;
				HexCoordinate tempTo = layout.pixelToHex(new Point(toX - scrollX, toY - scrollY));
				if (!focusHex.isEqual(tempTo)) {
					if (hexMap.getHex(tempTo) != null) {
						//System.out.println(currentUnit.getName());
						if ((hexToPath == null) || (!hexToPath.isEqual(tempTo))) {
							hexToPath = tempTo;
							endHex = hexMap.getHex(hexToPath);
							pathToFollow = pf.findPath(hexMap.getMap(), focusHex, endHex);
							List<PathHex> finalPath = currentUnit.validUnitMove(pathToFollow);
							ui.setFocusedUnitPath(finalPath);
						}
					}
				}
			}
		}
	}
	private boolean shouldMoveUnit() {
		HexCoordinate fromHex = ui.getFocusHex();
		if (fromHex != null) {
			if (MouseHandler.pressedMouse) {
				HexCoordinate toHexPlace = layout.pixelToHex(ui.getHexPosFromMouse());
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

}
