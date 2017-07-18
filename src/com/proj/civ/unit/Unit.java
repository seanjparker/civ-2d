package com.proj.civ.unit;

import com.proj.civ.datastruct.HexCoordinate;
import com.proj.civ.datastruct.HexMap;
import com.proj.civ.map.civilization.Civilization;

public class Unit {
	private double movementPotential;
	
	private int strength = 0;
	private int productionCost = 0;
	
	private boolean isSpawned = false;
	private boolean isMilitary = false;
	
	private HexCoordinate curPos;
	
	private Civilization civOwner;
	
	private String name;
	
 	public Unit(String name, Civilization civOwner, HexCoordinate curPos, double movementPotential, int productionCost) {
		this.name = name;
 		this.curPos = curPos;
		this.movementPotential = movementPotential;
		this.productionCost = productionCost;
	}
	public Unit(String name, Civilization civOwner, HexCoordinate curPos, double movementPotential, boolean isSpawned, int productionCost) {
		this(name, civOwner, curPos, movementPotential, productionCost);
		this.isSpawned = isSpawned;
	}
	public Unit(String name, Civilization civOwner, HexCoordinate curPos, double movementPotential, int strength, int productionCost) {
		this(name, civOwner, curPos, movementPotential, productionCost);
		this.strength = strength;
	}
	public Unit(String name, Civilization civOwner, HexCoordinate curPos, double movementPotential, int strength, boolean isMilitary, int productionCost) {
		this(name, civOwner, curPos, movementPotential, productionCost);
		this.strength = strength;
		this.isMilitary = isMilitary;
	}
	
	public HexCoordinate getPosition() {
		return curPos;
	}
	
	public boolean getSpawned() {
		return isSpawned;
	}
	public boolean isMilitary() {
		return isMilitary;
	}
	public int getStrength() {
		return strength;
	}
	public String getName() {
		return name;
	}
	
	public boolean ableToMove(double hexCost) {
		return (movementPotential - hexCost) >= 0;
	}
	
	public void decreaseMovement(double hexCost) {
		double finalMovement = movementPotential - hexCost;
		movementPotential = ableToMove(hexCost) ? finalMovement : movementPotential;
	}
	
	public double getMovementPotential() {
		return movementPotential;
	}
	
	public boolean isInZOC(HexCoordinate h) {
		return curPos != null ? HexMap.rangesIntersect(h, curPos, 1) : false;
	}
	
	public void setIsSpawned() {
		isSpawned = true;
	}
	
	public Civilization getOwner() {
		return civOwner;
	}
	
	public int getProductionCost() {
		return productionCost;
	}
	
	public boolean isSettler() {
		return this instanceof Settler;
	}
	public boolean isWarrior() {
		return this instanceof Warrior;
	}
}
