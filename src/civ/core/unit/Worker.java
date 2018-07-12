package civ.core.unit;

import civ.core.data.hex.HexCoordinate;
import civ.core.data.utils.GFXUtils;
import civ.core.display.menu.UnitMenu;
import civ.core.display.menu.button.UnitMenuButton;
import civ.core.event.Events;
import civ.core.map.civilization.BaseCivilization;

public class Worker extends Unit {
  
  public Worker() {
    super("Worker", null, null, GFXUtils.loadImage("./gfx/units/WORKER_WHITE.png"), 2.0D, 8.0D, 40,
        true, false);
  }
  
  public Worker(BaseCivilization civOwner, HexCoordinate curPos, boolean isSpawned) {
    super("Worker", civOwner, curPos,
        GFXUtils.loadImage(
            "./gfx/units/WORKER_" + GFXUtils.getViewableColour(civOwner.getColour()) + ".png"),
        2.0D, 8.0D, 40, true, isSpawned);
    
    int b = 0;
    actionMenu = new UnitMenu(false);
    actionMenu.addButton(new UnitMenuButton(Events.MOVE, b++));
    actionMenu.addButton(new UnitMenuButton(Events.DO_NOTHING, b++));
    actionMenu.addButton(new UnitMenuButton(Events.DELETE, b));
  }
}