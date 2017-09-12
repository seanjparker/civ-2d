package com.proj.civ.unit;

import com.proj.civ.datastruct.hex.HexCoordinate;
import com.proj.civ.display.menu.UnitMenu;
import com.proj.civ.display.menu.button.UnitMenuButton;
import com.proj.civ.event.Events;
import com.proj.civ.map.civilization.BaseCivilization;

public class Settler extends Unit {

	public Settler(BaseCivilization civOwner, HexCoordinate curPos, boolean isSpawned) {
		super("Settler", civOwner, curPos, 2.0D, 0.0D, 106, false, isSpawned);
	}
	
	public void init() {
		int b = 0;
		actionMenu = new UnitMenu(false);
		actionMenu.addButton(new UnitMenuButton(Events.FOUND_CITY, b++, true));
		actionMenu.addButton(new UnitMenuButton(Events.MOVE, b++, true));
		actionMenu.addButton(new UnitMenuButton(Events.DO_NOTHING, b++, true));
		actionMenu.addButton(new UnitMenuButton(Events.AUTO_EXPLORE, b++, true));
	}
}