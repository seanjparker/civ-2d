package com.proj.civ.map;

import java.util.ArrayList;
import java.util.List;

import com.proj.civ.map.terrain.Landscape;
import com.proj.civ.map.terrain.Yield;
import com.proj.civ.map.terrain.Feature;

public class Cell extends Improvement {	
	private Landscape Type = null;
	private List<Feature> Features = new ArrayList<Feature>();
	
	public Cell(Landscape Type) {
		this.Type = Type;
	}
	
	public boolean validFeature(Landscape T, Feature F) {
		switch (T) {
			case COAST:
				return (F == Feature.ICE) || (F == Feature.CLIFFS);
			case DESERT:
				return (F == Feature.OASIS) || (F == Feature.FLOODPLAINS);
		}
		return false;
	}
	
	public List<Landscape> getValidFeatures() {
		return null;
	}
	
	public Landscape getLandscape() {
		return Type;
	}
	public List<Feature> getFeatures() {
		return Features;
	}
	public void setLandscape(Landscape Type) {
		this.Type = Type;
	}
	public void setAllFeatures(List<Feature> Feature) {
		this.Features.addAll(Feature);
	}
	public void addFeature(Feature Feature) {
		this.Features.add(Feature);
	}
	public void removeFeature(Feature Feature) {
		if (this.Features.contains(Feature)) {
			this.Features.remove(Feature);			
		} else {
			System.out.println("Cannot remove feature, does not exist");
		}
	}
	
	public int getYieldTotal(Yield YeildType) {
		switch (YeildType) {
			case FOOD:
				return this.Type.getFoodYield() + this.Features.stream().mapToInt(i -> i.getFoodMod()).sum();
			case PRODUCTION:
				return this.Type.getProductionYield() + this.Features.stream().mapToInt(i -> i.getProductionMod()).sum();
			case SCIENCE:
				return this.Type.getScienceYield() + this.Features.stream().mapToInt(i -> i.getScienceMod()).sum();
			case GOLD:
				return this.Type.getGoldYield() + this.Features.stream().mapToInt(i -> i.getGoldMod()).sum();
			default:
				return 0;
		}
	}
	
	public double getMovementTotal() {
		return 0D;
	}
	
}
