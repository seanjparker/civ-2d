package civ.core.unit;

import civ.core.data.hex.HexCoordinate;
import civ.core.display.menu.button.UnitMenuButton;
import civ.core.event.Events;
import civ.core.map.civilization.BaseCivilization;

public class Warrior extends Unit {

  public Warrior(BaseCivilization civOwner, HexCoordinate curPos, boolean isSpawned) {
    super("Warrior", civOwner, curPos, 2.0D, 8.0D, 40, true, isSpawned);
  }

  public void init() {
    int b = 0;
    actionMenu = null;//new UnitMenu(false);
    actionMenu.addButton(new UnitMenuButton(Events.MOVE, b++));
    actionMenu.addButton(new UnitMenuButton(Events.ATTACK, b++));
    actionMenu.addButton(new UnitMenuButton(Events.DO_NOTHING, b++));
    actionMenu.addButton(new UnitMenuButton(Events.DELETE, b++));
  }
}
