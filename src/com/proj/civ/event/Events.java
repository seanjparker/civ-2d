package com.proj.civ.event;

import java.awt.Image;

import javax.swing.ImageIcon;

public enum Events {
	
	//Unit events
	FOUND_CITY(new ImageIcon("./gfx/buttons/FOUND_CITY.png").getImage()),
	MOVE(new ImageIcon("./gfx/buttons/MOVE.png").getImage()),
	ATTACK(new ImageIcon("").getImage()),
	AUTO_EXPLORE(new ImageIcon("").getImage()),
	DO_NOTHING(new ImageIcon("").getImage()),
	SLEEP(new ImageIcon("").getImage()),
	DELETE(new ImageIcon("").getImage()),
	
	//Other button events
	NEXT_TURN(null),
	CIVILOPEDIA_OPEN(null),
	RESEARCH_TREE_OPEN(null),
	CITY_OVERVIEW_OPEN(null),
	CITY_PRODUCTION_OPEN(null),
	CULTURE_TREE_OPEN(null);
	
	private final Image img;
	
	Events(Image img) {
		this.img = img;
	}
	public Image getImage() { return this.img; }
}
