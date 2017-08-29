package com.proj.civ.unit;

import com.proj.civ.datastruct.hex.HexCoordinate;
import com.proj.civ.map.civilization.BaseCivilization;

public class Warrior extends Unit {
	
	public Warrior(BaseCivilization civOwner, HexCoordinate curPos, boolean isSpawned) {
		super("Warrior", civOwner, curPos, 2.0D, 8.0D, 40, true, isSpawned);
	}
}
