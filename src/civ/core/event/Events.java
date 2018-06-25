package civ.core.event;

import java.awt.Image;
import javax.swing.ImageIcon;
import civ.core.event.callback.EventCallbackI;

import static civ.core.instance.IData.*;

public enum Events {

  // Unit events
  FOUND_CITY(new ImageIcon("./gfx/buttons/FOUND_CITY.png").getImage(), () -> {
    
  }),

  MOVE(new ImageIcon("./gfx/buttons/MOVE.png").getImage(), () -> {
    System.out.println("Move");
  }),

  ATTACK(new ImageIcon("./gfx/buttons/ATTACK.png").getImage(), () -> {
    System.out.println("Attack");
  }),

  AUTO_EXPLORE(new ImageIcon("./gfx/buttons/AUTO_EXPLORE.png").getImage(), () -> {
    System.out.println("Auto Explore");
  }),

  DO_NOTHING(new ImageIcon("./gfx/buttons/DO_NOTHING.png").getImage(), () -> {
    System.out.println("Do Nothing");
  }),

  SLEEP(new ImageIcon("./gfx/buttons/SLEEP.png").getImage(), () -> {
    System.out.println("Sleep");
  }),

  DELETE(new ImageIcon("./gfx/buttons/DELETE.png").getImage(), () -> {
    System.out.println("Delete");
  }),

  // Other button events
  NEXT_TURN(null, null),
  CIVILOPEDIA_OPEN(null, null),
  RESEARCH_TREE_OPEN(null,null),
  CITY_OVERVIEW_OPEN(null, null),
  CITY_PRODUCTION_OPEN(null, null),
  CULTURE_TREE_OPEN(null, null);

  private final Image img;
  private final EventCallbackI funCall;

  Events(Image img, EventCallbackI funCall) {
    this.img = img;
    this.funCall = funCall;
  }

  public Image getImage() {
    return this.img;
  }

  public EventCallbackI getFunctionCall() {
    return this.funCall;
  }
}
