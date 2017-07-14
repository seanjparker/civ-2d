package com.proj.civ.unit;

import com.proj.civ.datastruct.HexCoordinate;

public class Unit {
	private double movementPotential;
	
	private int strength;
	private int defence;
	
	private boolean isSpawned = false;
	private boolean isMilitary = false;
	
	private HexCoordinate curPos;
	
 	public Unit(HexCoordinate curPos, double movementPotential) {
		this.curPos = curPos;
		this.movementPotential = movementPotential;
	}
	public Unit(HexCoordinate curPos, double movementPotential, boolean isSpawned) {
		this(curPos, movementPotential);
		this.isSpawned = isSpawned;
	}
	public Unit(HexCoordinate curPos, double movementPotential, int strength, int defence) {
		this(curPos, movementPotential);
		this.strength = strength;
		this.defence = defence;
	}
	public Unit(HexCoordinate curPos, double movementPotential, int strength, int defence, boolean isMilitary) {
		this(curPos, movementPotential);
		this.strength = strength;
		this.defence = defence;
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
		if (curPos != null) {
		}
		return false;
	}
	
	public void setIsSpawned() {
		isSpawned = true;
	}
}
