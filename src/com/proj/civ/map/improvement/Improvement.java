package com.proj.civ.map.improvement;

import java.util.ArrayList;
import java.util.List;

import com.proj.civ.map.terrain.Feature;
import com.proj.civ.map.terrain.Landscape;
import com.proj.civ.map.terrain.Yield;

public class Improvement extends Yield {
	private List<Landscape> validLandscapes = new ArrayList<Landscape>();
	private List<Feature> validFeatures = new ArrayList<Feature>();
	private String name;
	
	public Improvement(int food, int production, int science, int gold, String name) {
		super(food, production, science, gold);
		this.name = name;
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
	
	public String toString() {
		return "Food:" + getFood() + ", Production:" + getProduction() + ", Science:" + getScience() + ", Gold:" + getGold();
	}
	public String getName() {
		return name;
	}
}
