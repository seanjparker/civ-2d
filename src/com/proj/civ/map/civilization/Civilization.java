package com.proj.civ.map.civilization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.proj.civ.datastruct.Hex;
import com.proj.civ.unit.Unit;

public class Civilization {
	private String CivName;
	private int NumberOfCities = 0;
	private int happiness = 0;
	
	private List<Unit> units = new ArrayList<Unit>();
	private List<String> cityNames = new ArrayList<String>();
	
	private CivType ct;
	
	public Civilization(CivType ct) {
		this.ct = ct;
		this.CivName = ct.name();
	}
	
	public boolean sameCiv(CivType c) {
		return this.ct == c;
	}
	
	private String getNextCityName() {
		return canCreateCity() ? cityNames.get(NumberOfCities) : null;
	}
	public int getHappiness() {
		return happiness;
	}
	
	public void addCityName(String name) {
		this.cityNames.add(name);
	}
	public String createCity() {
		String city = getNextCityName();
		NumberOfCities++;	
		return city;
	}
	public boolean canCreateCity() {
		return NumberOfCities < cityNames.size();
	}
	
	public List<Unit> getUnits() {
		return units;
	}
	
	public void addUnit(Unit u) {
		units.add(u);
	}
	
	public void replaceUnit(Unit oldUnit, Unit newUnit) {
		units.remove(oldUnit);
		units.add(newUnit);
	}
	
	public CivType getCivType() {
		return ct;
	}
}
