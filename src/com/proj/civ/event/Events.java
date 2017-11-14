package com.proj.civ.event;

import java.awt.Image;
import javax.swing.ImageIcon;

public enum Events {

  // Unit events
  FOUND_CITY (new ImageIcon("./gfx/buttons/FOUND_CITY.png").getImage()),
  MOVE (new ImageIcon("./gfx/buttons/MOVE.png").getImage()), 
  ATTACK (new ImageIcon("./gfx/buttons/ATTACK.png").getImage()), 
  AUTO_EXPLORE (new ImageIcon("./gfx/buttons/AUTO_EXPLORE.png").getImage()), 
  DO_NOTHING (new ImageIcon("./gfx/buttons/DO_NOTHING.png").getImage()), 
  SLEEP (new ImageIcon("./gfx/buttons/SLEEP.png").getImage()), 
  DELETE (new ImageIcon("./gfx/buttons/DELETE.png").getImage()),

  // Other button events
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

  public Image getImage() {
    return this.img;
  }
}
