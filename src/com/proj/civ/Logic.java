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
		Hex fromHex = map.get(HexMap.hash(focusHex));
		if (!fromHex.canSetCivilian() || !fromHex.canSetMilitary()) {			
			int mouseX = MouseHandler.dMX;
			int mouseY = MouseHandler.dMY;
			
			HexCoordinate h = layout.pixelToHex(layout, new Point(mouseX - scrollX, mouseY - scrollY));
			Hex toHex = map.get(HexMap.hash(h));
			
			Unit cu = fromHex.getCivilianUnit();
			Unit mu = fromHex.getMilitaryUnit();
			
			Unit ctu = toHex.getCivilianUnit();
			Unit mtu = toHex.getMilitaryUnit();
			
			for (int i = 0; i < fromHex.getUnits().length; i++) {
				for (int j = 0; j < toHex.getUnits().length; j++) {
					
				}
			}
			
			/*
			//Civ units and military units cannot occupy the same hex
			if (cu != null && ctu != null) {
				if (sameOwner(cu, ctu)) {
					//Swap the civ units
				}
			} else if (mu != null && mtu != null) {
				//if (mu.getOwner().getName() == c1.getName()) {	
				//}
				//Swap military units
			} else if (cu != null && ctu == null) {
				//Move civ units to new hex
			} else if (mu != null && mtu == null) {
				//Move military unit to new hex
			}
			*/
		}
	}
	
	private boolean sameOwner(Unit fromU, Unit toU) {
		return fromU.getOwner().sameCiv(toU.getOwner().getCivType());
	}
	
}
