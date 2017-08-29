package com.proj.civ.unit;

import com.proj.civ.datastruct.hex.HexCoordinate;
import com.proj.civ.datastruct.map.HexMap;
import com.proj.civ.map.civilization.BaseCivilization;

public class Unit {
	private final double movement;
	private double movementTemp;
	private double movementPotential;
	
	private double strength = 0;
	private int productionCost = 0;
	
	private boolean isSpawned = false;
	private boolean isMilitary = false;
	
	private String name;
	
	private HexCoordinate curPos;
	private BaseCivilization civOwner;
	
 	public Unit(String name, BaseCivilization civOwner, HexCoordinate curPos, double movementPotential, int productionCost) {
		this.name = name;
		this.civOwner = civOwner;
 		this.curPos = curPos;
 		this.movement = movementPotential;
		this.movementPotential = movementPotential;
		this.movementTemp = movementPotential;
		this.productionCost = productionCost;
	}
	public Unit(String name, BaseCivilization civOwner, HexCoordinate curPos, double movementPotential, double strength, int productionCost, boolean isMilitary, boolean isSpawned) {
		this(name, civOwner, curPos, movementPotential, productionCost);
		this.strength = strength;
		this.isMilitary = isMilitary;
		this.isSpawned = isSpawned;
	}
	
	public HexCoordinate getPosition() {
		return curPos;
	}
	public boolean getSpawned() {
		return isSpawned;
	}
	public boolean getIsMilitary() {
		return isMilitary;
	}
	public double getStrength() {
		return strength;
	}
	public String getName() {
		return name;
	}
	public double getTotalMovement() {
		return movement;
	}
	public double getMovementPotential() {
		return movementPotential;
	}
	public BaseCivilization getOwner() {
		return civOwner;
	}
	
	public int getProductionCost() {
		return productionCost;
	}
	
	public void setIsSpawned() {
		isSpawned = true;
	}
	
	public boolean isSettler() {
		return this instanceof Settler;
	}
	public boolean isWarrior() {
		return this instanceof Warrior;
	}
	
	public void setPosition(HexCoordinate h) {
		this.curPos = h;
	}
	
	public boolean ableToMove(double hexCost) {
		return ((movementTemp -= hexCost) >= 0D) && (movementPotential - hexCost >= 0D);
	}
	public void decreaseMovement(double hexCost) {
		this.movementPotential -= hexCost;
	}
	

	public void resetMovementTemp() {
		this.movementTemp = movement;
	}
	public void resetMovementPotential() {
		this.movementPotential = movement;
	}
	public void setMovementTempForMultiMove() {
		this.movementTemp = 
				this.movementPotential != this.movement
				? this.movementPotential
				: this.movement;
	}
	
	
	public boolean isInZOC(HexCoordinate h) {
		return curPos != null ? HexMap.rangesIntersect(h, curPos, 1) : false;
	}
	public void nextTurn() {
		resetMovementTemp();
		resetMovementPotential();
	}
}
