package com.proj.civ.map.terrain;

/**
 * Enum that contains the data on the yield modifier for the feature
 * Parameters:
 * FOOD - PRODUCTION - SCIENCE - GOLD
 * 
 * @author Sean
 */
public enum Feature {
	CLIFFS (0, 0, 0, 0, "Cliffs"),
	FLOODPLAINS (3, 0, 0, 0, "Floodplains"),
	HILLS (0, 1, 0, 0, "Hills"),
	ICE (0, 0, 0, 0, "Ice"),
	MARSH (1, 0, 0, 0, "Marsh"),
	MOUNTAINS (0, 0, 0, 0, "Mountain"),
	OASIS (3, 0, 0, 1, "Oasis"),
	RAINFOREST (1, 0, 0, 0, "Rainforest"),
	RIVER (0, 0, 0, 0, "River"),
	WOODS (0, 1, 0, 0, "Woods");
	
	private final int food;
	private final int production;
	private final int science;
	private final int gold;
	private final String name;
	
	Feature(int food, int production, int science, int gold, String name) {
		this.food = food;
		this.production = production;
		this.science = science;
		this.gold = gold;
		this.name = name;
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
}
