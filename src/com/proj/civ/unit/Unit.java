package com.proj.civ.unit;

import com.proj.civ.datastruct.HexCoordinate;
import com.proj.civ.datastruct.HexMap;
import com.proj.civ.map.civilization.Civilization;

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
	private Civilization civOwner;
	
 	public Unit(String name, Civilization civOwner, HexCoordinate curPos, double movementPotential, int productionCost) {
		this.name = name;
		this.civOwner = civOwner;
 		this.curPos = curPos;
 		this.movement = movementPotential;
		this.movementPotential = movementPotential;
		this.movementTemp = movementPotential;
		this.productionCost = productionCost;
	}
	public Unit(String name, Civilization civOwner, HexCoordinate curPos, double movementPotential, boolean isSpawned, int productionCost) {
		this(name, civOwner, curPos, movementPotential, productionCost);
		this.isSpawned = isSpawned;
	}
	public Unit(String name, Civilization civOwner, HexCoordinate curPos, double movementPotential, double strength, int productionCost) {
		this(name, civOwner, curPos, movementPotential, productionCost);
		this.strength = strength;
	}
	public Unit(String name, Civilization civOwner, HexCoordinate curPos, double movementPotential, double strength, int productionCost, boolean isSpawned) {
		this(name, civOwner, curPos, movementPotential, strength, productionCost);
		this.isSpawned = isSpawned;
	}
	public Unit(String name, Civilization civOwner, HexCoordinate curPos, double movementPotential, double strength, int productionCost, boolean isMilitary, boolean isSpawned) {
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
	public boolean isMilitary() {
		return isMilitary;
	}
	public double getStrength() {
		return strength;
	}
	public String getName() {
		return name;
	}
	
	public boolean ableToMove(double hexCost) {
		return (movementTemp -= hexCost) >= 0;
	}
	
	public boolean decreaseMovement(double hexCost) {
		boolean canMove = ableToMove(hexCost);
		double finalMovement = movementPotential - hexCost;
		movementPotential = canMove ? finalMovement : movementPotential;
		return canMove;
	}
	
	public double getMovementPotential() {
		return movementTemp;
	}
	public double getTotalMovement() {
		return movement;
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
	
	public void setPosition(HexCoordinate h) {
		this.curPos = h;
	}
	public void resetMovementTemp() {
		this.movementTemp = movement;
	}
	public void resetMovementPotential() {
		this.movementPotential = movement;
	}
}