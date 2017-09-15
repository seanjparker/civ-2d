package com.proj.civ.unit;

import com.proj.civ.data.hex.HexCoordinate;
import com.proj.civ.display.menu.UnitMenu;
import com.proj.civ.display.menu.button.UnitMenuButton;
import com.proj.civ.event.Events;
import com.proj.civ.map.civilization.BaseCivilization;

public class Warrior extends Unit {
	
	public Warrior(BaseCivilization civOwner, HexCoordinate curPos, boolean isSpawned) {
		super("Warrior", civOwner, curPos, 2.0D, 8.0D, 40, true, isSpawned);
	}

	public void init() {
		int b = 0;
		actionMenu = new UnitMenu(false);
		actionMenu.addButton(new UnitMenuButton(Events.MOVE, b++));
		actionMenu.addButton(new UnitMenuButton(Events.ATTACK, b++));
		actionMenu.addButton(new UnitMenuButton(Events.DO_NOTHING, b++));
		actionMenu.addButton(new UnitMenuButton(Events.DELETE, b++));
	}
}
