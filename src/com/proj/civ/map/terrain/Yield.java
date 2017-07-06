package com.proj.civ.map.terrain;

public enum Yield {	
	FOOD (0),
	PRODUCTION (1),
	SCIENCE (2),
	GOLD (3);
	
	private final int index;
	
	Yield(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return this.index;
	}
}
