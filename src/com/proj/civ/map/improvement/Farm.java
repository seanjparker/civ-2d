package com.proj.civ.map.improvement;

import java.util.ArrayList;
import java.util.List;

import com.proj.civ.map.terrain.Landscape;

public class Farm extends Improvement {
	private List<Landscape> validLandscapes = new ArrayList<Landscape>();
	
	public Farm() { //Must super with yield amounts
		super(1, 0, 0, 0, "Farm"); //Default yields for a farm
		
		validLandscapes.add(Landscape.PLAINS);
		validLandscapes.add(Landscape.GRASSLAND);
		validLandscapes.add(Landscape.DESERT);
		validLandscapes.add(Landscape.TUNDRA);
		super.addAllValidLandscapes(validLandscapes);
	}
}
