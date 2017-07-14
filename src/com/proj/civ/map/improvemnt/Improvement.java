package com.proj.civ.map.improvemnt;

import java.util.ArrayList;
import java.util.List;

import com.proj.civ.map.terrain.Feature;
import com.proj.civ.map.terrain.Landscape;
import com.proj.civ.map.terrain.Yield;

public class Improvement extends Yield {
	private List<Landscape> validLandscapes = new ArrayList<Landscape>();
	private List<Feature> validFeatures = new ArrayList<Feature>();

	public Improvement(int food, int production, int science, int gold) {
		super(gold, production, science, gold);
	}
	
	public void addAllValidLandscapes(List<Landscape> vl) {
		validLandscapes.addAll(vl);
	}
	public boolean validLandscape(Landscape c) {
		return validLandscapes.contains(c);
	}
	public void addAllValidFeatures(List<Feature> vf) {
		validFeatures.addAll(vf);
	}
	public boolean validFeature(Feature c) {
		return validFeatures.contains(c);
	}
}
