package com.proj.civ.unit;

import com.proj.civ.datastruct.HexCoordinate;
import com.proj.civ.map.civilization.BaseCivilization;

public class Settler extends Unit {

	public Settler(BaseCivilization civOwner, HexCoordinate curPos, boolean isSpawned) {
		super("Settler", civOwner, curPos, 2.0D, 0.0D, 106, false, isSpawned);
	}
}
