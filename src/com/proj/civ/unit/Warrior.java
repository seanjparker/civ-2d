package com.proj.civ.unit;

import com.proj.civ.datastruct.HexCoordinate;
import com.proj.civ.map.civilization.Civilization;

public class Warrior extends Unit {
	
	public Warrior(Civilization civOwner, HexCoordinate curPos) {
		super("Warrior", civOwner, curPos, 8.0D, 40);
	}
	public Warrior(Civilization civOwner, HexCoordinate curPos, boolean isSpawned) {
		super("Warrior", civOwner, curPos, 8.0D, isSpawned, 40);
	}
}
