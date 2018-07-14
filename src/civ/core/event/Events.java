package civ.core.event;

import static civ.core.instance.IData.civs;
import static civ.core.instance.IData.currentUnit;
import static civ.core.instance.IData.turnCounter;
import java.awt.image.BufferedImage;
import civ.core.display.GUI;
import civ.core.event.callback.EventCallbackI;
import civ.core.map.civilization.BaseCivilization;
import civ.core.unit.Settler;
import civ.core.unit.Unit;
import civ.core.data.utils.GFXUtils;

public enum Events {

  // Unit events
  FOUND_CITY(GFXUtils.loadImage("./gfx/buttons/FOUND_CITY.png"), () -> {
    if (currentUnit instanceof Settler)
      ((Settler) currentUnit).foundCity();
  }),

  MOVE(GFXUtils.loadImage("./gfx/buttons/MOVE.png"), () -> {
    currentUnit.toggleBeingMoved();
  }),

  ATTACK(GFXUtils.loadImage("./gfx/buttons/ATTACK.png"), () -> {
    currentUnit.toggleBeingAttacked();
  }),

  AUTO_EXPLORE(GFXUtils.loadImage("./gfx/buttons/AUTO_EXPLORE.png"), () -> {
    System.out.println("Auto Explore");
  }),

  DO_NOTHING(GFXUtils.loadImage("./gfx/buttons/DO_NOTHING.png"), () -> {
    System.out.println("Do Nothing");
  }),

  SLEEP(GFXUtils.loadImage("./gfx/buttons/SLEEP.png"), () -> {
    System.out.println("Sleep");
  }),

  DELETE(GFXUtils.loadImage("./gfx/buttons/DELETE.png"), () -> {
    currentUnit.deleteBySelling();
  }),

  // Other button events
  NEXT_TURN(null, () -> {
    for (BaseCivilization c : civs)
      c.nextTurn();
    turnCounter++;
  }),
  CIVILOPEDIA_OPEN(null, null),
  RESEARCH_TREE_OPEN(null,null),
  CITY_OVERVIEW_OPEN(null, null),
  CITY_PRODUCTION_OPEN(null, null),
  CULTURE_TREE_OPEN(null, null);

  private final BufferedImage img;
  private final EventCallbackI funCall;

  Events(BufferedImage img, EventCallbackI funCall) {
    this.img = img;
    this.funCall = funCall;
  }

  public BufferedImage getImage() {
    return this.img;
  }

  public EventCallbackI getFunctionCall() {
    return this.funCall;
  }
}
