package com.proj.civ.unit;

import com.proj.civ.datastruct.HexCoordinate;
import com.proj.civ.map.civilization.Civilization;

public class Settler extends Unit {

	public Settler(Civilization civOwner, HexCoordinate curPos) {
		super("Settler", civOwner, curPos, 0.0D, 106);
	}
	public Settler(Civilization civOwner, HexCoordinate curPos, boolean isSpawned) {
		super("Settler", civOwner, curPos, 0.0D, isSpawned, 106);
	}
}
