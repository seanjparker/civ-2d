package com.proj.civ.map.civilization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.proj.civ.datastruct.Hex;
import com.proj.civ.unit.Unit;

public class Civilization {
	private String CivName;
	private List<String> cityNames = new ArrayList<String>();
	private int NumberOfCities = 0;
	private int happiness = 0;
	private List<Unit> units = new ArrayList<Unit>();
	
	public Civilization(String name) {
		this.CivName = name;
	}
	
	public String getName() {
		return this.CivName;
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
}
