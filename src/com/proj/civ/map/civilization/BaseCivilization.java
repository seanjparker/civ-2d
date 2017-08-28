package com.proj.civ.map.civilization;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.proj.civ.unit.Unit;

public abstract class BaseCivilization {
	private final String NAME_SINGULAR;
	private final String NAME_PLURAL;
	
	private final int ID;
	private int numberOfCities = 0;
	private int happiness = 0;
	
	private List<Unit> units;
	private String[] cityNames;
	
	private Color civColour;
	
	public BaseCivilization(String nameSingular, String namePlural, Color civColour, String[] cityNames) {
		this.ID = nameSingular.hashCode() ^ namePlural.hashCode();
		this.units = new ArrayList<Unit>();
		this.civColour = civColour;
		this.cityNames = cityNames;
		this.NAME_SINGULAR = nameSingular;
		this.NAME_PLURAL = namePlural;
	}
	
	public abstract boolean sameCivilization(int id);
	
	private String getNextCityName() {
		return canCreateCity() ? cityNames[numberOfCities]: null;
	}
	public int getHappiness() {
		return happiness;
	}
	public String createCity() {
		String cityName = getNextCityName();
		numberOfCities++;	
		return cityName;
	}
	public boolean canCreateCity() {
		return numberOfCities < cityNames.length;
	}
	public List<Unit> getUnits() {
		return units;
	}
	public int getID() {
		return this.ID;
	}
	public Color getColour() {
		return this.civColour;
	}
	
	public void addUnit(Unit u) {
		units.add(u);
	}
	public void replaceUnit(Unit oldUnit, Unit newUnit) {
		units.remove(oldUnit);
		units.add(newUnit);
	}
	public String getSingularName() {
		return this.NAME_SINGULAR;
	}
	public String getPluralName() {
		return this.NAME_PLURAL;
	}
}
