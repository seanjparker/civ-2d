package com.proj.civ.map.improvemnt;

import java.util.ArrayList;
import java.util.List;

import com.proj.civ.map.terrain.Landscape;

public class Improvement {
	private List<Landscape> validLandscapes = new ArrayList<Landscape>();
	
	public Improvement() {
	}
	
	public Improvement(List<Landscape> vl) {
		validLandscapes.addAll(vl);
	}
	
	public boolean validLandscape(Landscape c) {
		return validLandscapes.contains(c);
	}
	
	
	
}
