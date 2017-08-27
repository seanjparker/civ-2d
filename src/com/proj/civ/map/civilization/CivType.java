package com.proj.civ.map.civilization;

import java.awt.Color;

public enum CivType {
	AMERICA("America", new Color(34, 103, 214)),
	ENGLAND("England", new Color(237, 59, 59));
	
	private final String name;
	private final Color colour;
	
	CivType(String name, Color colour) {
		this.name = name;
		this.colour = colour;
	}
	
	public Color getColour() {
		return this.colour;
	}
	public String getName() {
		return this.name;
	}
}
