package com.proj.civ.map.improvement;

import java.util.ArrayList;
import java.util.List;

import com.proj.civ.map.terrain.Feature;
import com.proj.civ.map.terrain.Landscape;

public class TradingPost extends Improvement {
	private List<Feature> validFeatures = new ArrayList<Feature>();
	private List<Landscape> validLandscapes = new ArrayList<Landscape>();
	
	public TradingPost() {
		super(0, 0, 0, 1, "Trading Post");
		
		validLandscapes.add(Landscape.PLAINS);
		validLandscapes.add(Landscape.GRASSLAND);
		validLandscapes.add(Landscape.DESERT);
		validLandscapes.add(Landscape.TUNDRA);
		super.addAllValidLandscapes(validLandscapes);
		
		validFeatures.add(Feature.HILLS);
		super.addAllValidFeatures(validFeatures);
	}

}
