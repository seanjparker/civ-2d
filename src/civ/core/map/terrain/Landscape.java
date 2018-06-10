package civ.core.map.terrain;

import java.awt.Color;

/**
 * Enum that contains the data on the base yields for the landscape
 * Parameters:
 * FOOD - PRODUCTION - SCIENCE - GOLD
 */
public enum Landscape {
	COAST (new Yield(1, 0, 0, 0), new Color(131, 197, 219), "Coast"), // Low elevation, avg temperature
	DESERT (new Yield(0, 0, 0, 0), new Color(242, 238, 125), "Desert"), // Low elevation, high temperature
	GRASSLAND (new Yield(2, 0, 0, 0), new Color(85, 175, 43), "Grassland"), // Avg elevation, low-avg temperature
	LAKE (new Yield(1, 0, 0, 1), new Color(90, 123, 168), "Lake"), //Low elevation
	OCEAN (new Yield(1, 0, 0, 0), new Color(36, 36, 127), "Ocean"), //V.Low elevation
	PLAINS (new Yield(1, 1, 0, 0), new Color(226, 217, 131), "Plains"), //Avg elevation, avg temperature
	TUNDRA (new Yield(1, 0, 0, 0), new Color(128, 128, 128), "Tundra"), //Avg elevation, avg-high temperature
	SNOW (new Yield(0, 0, 0, 0), new Color(242, 242, 234), "Snow"); //High elevation, low temperature
	
	private final Yield y;
	private final Color colour;
	private final String name;
	
	Landscape(Yield y, Color colour, String name) {
		this.y = y;
		this.colour = colour;
		this.name = name;
	}
	
	public int getFoodYield() {
		return this.y.getFood();
	}
	public int getProductionYield() {
		return this.y.getProduction();		
	}
	public int getScienceYield() {
		return this.y.getScience();		
	}
	public int getGoldYield() {
		return this.y.getGold();		
	}
	public Color getColour() {		
		return this.colour;
	}
	public String getName() {
		return this.name;
	}
}
