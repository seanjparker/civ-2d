package com.proj.civ.map.civilization;

import java.util.ArrayList;
import java.util.List;

import com.proj.civ.unit.Unit;

public class Civilization {
	private String CivName;
	private List<String> cityNames = new ArrayList<String>();
	private List<Unit> units = new ArrayList<Unit>();
	private int NumberOfCities = 0;
	
	public Civilization(String name) {
		this.CivName = name;
	}
	
	public String getName() {
		return this.CivName;
	}
	
	private String getNextCityName() {
		if (canCreateCity())
			return cityNames.get(NumberOfCities);
		return null;
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
	public void addNewUnit(Unit unit) {
		units.add(unit);
	}
	public List<Unit> getUnits() {
		return units;
	} 
}
