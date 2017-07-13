package com.proj.civ.map.terrain;

/**
 * Enum that contains the data on the yield modifier for the feature
 * Parameters:
 * FOOD - PRODUCTION - SCIENCE - GOLD
 * 
 * @author Sean
 */
public enum Feature {
	CLIFFS (0, 0, 0, 0, "Cliffs", 2.0, true),
	FLOODPLAINS (3, 0, 0, 0, "Floodplains", 2.0, true),
	HILLS (0, 1, 0, 0, "Hills", 2.0, true),
	ICE (0, 0, 0, 0, "Ice", 1.0, true),
	MARSH (1, 0, 0, 0, "Marsh", 1.0, true),
	MOUNTAINS (0, 0, 0, 0, "Mountain", 0.0, false),
	OASIS (3, 0, 0, 1, "Oasis", 1.0, true),
	RAINFOREST (1, 0, 0, 0, "Rainforest", 1.0, true),
	RIVER (0, 0, 0, 0, "River", 2.0, true),
	WOODS (0, 1, 0, 0, "Woods", 2.0, true);
	
	private final int food;
	private final int production;
	private final int science;
	private final int gold;
	private final String name;
	private final double movement;
	private final boolean passable;
	
	Feature(int food, int production, int science, int gold, String name, double movement, boolean passable) {
		this.food = food;
		this.production = production;
		this.science = science;
		this.gold = gold;
		this.name = name;
		this.movement = movement;
		this.passable = passable;
	}
	
	public int getFoodMod() {
		return this.food;
	}
	public int getProductionMod() {
		return this.production;		
	}
	public int getScienceMod() {
		return this.science;		
	}
	public int getGoldMod() {
		return this.gold;		
	}
	public String getName() {
		return this.name;
	}
	public double getMovement() {
		return this.movement;
	}
	public boolean getPassable() {
		return this.passable;
	}
}
