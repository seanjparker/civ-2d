package civ.core.unit;

import civ.core.data.hex.HexCoordinate;
import civ.core.data.utils.GFXUtils;
import civ.core.display.menu.UnitMenu;
import civ.core.display.menu.button.UnitMenuButton;
import civ.core.event.Events;
import civ.core.map.civilization.BaseCivilization;

public class Scout extends Unit {
  public Scout() {
    super("Scout", null, null, GFXUtils.loadImage("./gfx/units/SCOUT_WHITE.png"), 2.0D, 5.0D, 25,
        false, false);
  }
  
  public Scout(BaseCivilization civOwner, HexCoordinate curPos, boolean isSpawned) {
    super("Scout", civOwner, curPos,
        GFXUtils.loadImage(
            "./gfx/units/SCOUT_" + GFXUtils.getViewableColour(civOwner.getColour()) + ".png"),
        2.0D, 5.0D, 25, false, isSpawned, false);
    
    int b = 0;
    actionMenu = new UnitMenu(false);
    actionMenu.addButton(new UnitMenuButton(Events.MOVE, b++));
    actionMenu.addButton(new UnitMenuButton(Events.DO_NOTHING, b++));
    actionMenu.addButton(new UnitMenuButton(Events.AUTO_EXPLORE, b++));
    actionMenu.addButton(new UnitMenuButton(Events.SLEEP, b));
  }
}
