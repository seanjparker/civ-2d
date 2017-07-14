package com.proj.civ.map.improvement;

import java.util.ArrayList;
import java.util.List;

import com.proj.civ.map.terrain.Feature;

public class LumberMill extends Improvement {
	private List<Feature> validFeatures = new ArrayList<Feature>();
	
	public LumberMill() {
		super(0, 1, 0, 0, "Lumber Mill");
		
		validFeatures.add(Feature.WOODS);
		super.addAllValidFeatures(validFeatures);
	}
}
