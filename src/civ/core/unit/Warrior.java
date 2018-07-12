package civ.core.unit;

import civ.core.data.hex.HexCoordinate;
import civ.core.data.utils.GFXUtils;
import civ.core.display.menu.UnitMenu;
import civ.core.display.menu.button.UnitMenuButton;
import civ.core.event.Events;
import civ.core.map.civilization.BaseCivilization;

public class Warrior extends Unit {
  
  public Warrior() {
    super("Warrior", null, null, GFXUtils.loadImage("./gfx/units/WARRIOR_WHITE.png"), 2.0D, 8.0D,
        40, true, false);
  }
  
  public Warrior(BaseCivilization civOwner, HexCoordinate curPos, boolean isSpawned) {
    super("Warrior", civOwner, curPos,
        GFXUtils.loadImage(
            "./gfx/units/WARRIOR_" + GFXUtils.getViewableColour(civOwner.getColour()) + ".png"),
        2.0D, 8.0D, 40, true, isSpawned);
    
    int b = 0;
    actionMenu = new UnitMenu(false);
    actionMenu.addButton(new UnitMenuButton(Events.MOVE, b++));
    actionMenu.addButton(new UnitMenuButton(Events.ATTACK, b++));
    actionMenu.addButton(new UnitMenuButton(Events.DO_NOTHING, b++));
    actionMenu.addButton(new UnitMenuButton(Events.DELETE, b++));
  }
}
