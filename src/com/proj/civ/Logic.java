package com.proj.civ;


import java.util.Map;

import com.proj.civ.datastruct.Hex;
import com.proj.civ.datastruct.HexCoordinate;
import com.proj.civ.datastruct.HexMap;
import com.proj.civ.datastruct.Layout;
import com.proj.civ.datastruct.Point;
import com.proj.civ.input.MouseHandler;
import com.proj.civ.map.civilization.Civilization;
import com.proj.civ.unit.Unit;

public class Logic {
	private Layout layout;
	
	public Logic(Layout layout) {
		this.layout = layout;
		
	}
	
	public void moveUnit(Map<Integer, Hex> map, Civilization c, Hex focusHex, int scrollX, int scrollY) {
		//If a friendly unit is currently selected
			//If the new location is a valid position and (empty or a piece of the same type occupies)
				//If new location is empty -- set current piece to the new location -- update map and civ unit list
				//If new location of same type occupies it -- switch the pieces positions
		if (focusHex != null) {
			System.out.println("is focused to move");
			/*
			Hex fromHex = map.get(HexMap.hash(focusHex));
			if (!fromHex.canSetCivilian() || !fromHex.canSetMilitary()) {
				Unit cu = fromHex.getCivilianUnit();
				Unit mu = fromHex.getMilitaryUnit();
				
				int mouseX = MouseHandler.dMX;
				int mouseY = MouseHandler.dMY;
				HexCoordinate h = layout.pixelToHex(layout, new Point(mouseX - scrollX, mouseY - scrollY));
				Hex toHex = map.get(HexMap.hash(h));
				
				Unit ctu = toHex.getCivilianUnit();
				Unit mtu = toHex.getMilitaryUnit();
				
				
				*/
				
				
				/*
				if (cu != null && ctu != null) {
					if (sameOwner(cu, ctu)) {
					}
					//Swap civ units
				} else if (mu != null && mtu != null) {
					//if (mu.getOwner().getName() == c1.getName()) {	
					//}
					//Swap military units
				} else if (cu != null && ctu == null) {
					//Move civ units to new hex
				} else if (mu != null && mtu == null) {
					//Move military unit ot new hex
				}
				*/
			//}
		}
	}
	
	private boolean sameOwner(Unit fromU, Unit toU) {
		return fromU.getOwner().sameCiv(toU.getOwner().getCivType());
	}
	
}
