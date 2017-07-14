package com.proj.civ.map.terrain;

/**
 * Enum that contains the data on the yield modifier for the feature
 * Parameters:
 * FOOD - PRODUCTION - SCIENCE - GOLD
 * 
 * @author Sean
 */
public enum Feature{
	CLIFFS (new Yield(0, 0, 0, 0), "Cliffs", 2.0, true),
	FLOODPLAINS (new Yield(3, 0, 0, 0), "Floodplains", 2.0, true),
	HILLS (new Yield(0, 1, 0, 0), "Hills", 2.0, true),
	ICE (new Yield(0, 0, 0, 0), "Ice", 1.0, true),
	MARSH (new Yield(1, 0, 0, 0), "Marsh", 1.0, true),
	MOUNTAINS (new Yield(0, 0, 0, 0), "Mountain", 0.0, false),
	OASIS (new Yield(3, 0, 0, 1), "Oasis", 1.0, true),
	RAINFOREST (new Yield(1, 0, 0, 0), "Rainforest", 2.0, true),
	RIVER (new Yield(0, 0, 0, 0), "River", 2.0, true),
	WOODS (new Yield(0, 1, 0, 0), "Woods", 2.0, true);
	
	private final Yield y;
	private final String name;
	private final double movement;
	private final boolean passable;
	
	Feature(Yield y, String name, double movement, boolean passable) {
		this.y = y;
		this.name = name;
		this.movement = movement;
		this.passable = passable;
	}
	
	public int getFoodMod() {
		return this.y.getFood();
	}
	public int getProductionMod() {
		return this.y.getProduction();		
	}
	public int getScienceMod() {
		return this.y.getScience();		
	}
	public int getGoldMod() {
		return this.y.getGold();		
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
