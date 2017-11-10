package com.proj.civ.map.civilization;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.proj.civ.data.hex.HexCoordinate;
import com.proj.civ.map.cities.City;
import com.proj.civ.unit.Unit;

public class BaseCivilization {
	private final String NAME_SINGULAR;
	private final String NAME_PLURAL;
	
	private final int ID;
	private int numberOfCities = 0;
	
	private int sciencePT = 0;
	private int scienceTotal = 0;
	private int goldTotal = 0;
	private int goldPT = 0;
	private int cultureTotal = 0;
	private int cultureRequired = 0;
	private int culturePT = 0;
	
	private int happiness = 0;
	
	private List<Unit> units;
	private String[] cityNames;
	
	private List<City> cities;
	
	private Color civColour;
	
	public BaseCivilization(String nameSingular, String namePlural, Color civColour, String[] cityNames) {
		this.ID = nameSingular.hashCode() ^ namePlural.hashCode();
		//System.out.println(nameSingular + " ID is " + this.ID);
		this.units = new ArrayList<Unit>();
		this.civColour = civColour;
		this.cityNames = cityNames;
		this.NAME_SINGULAR = nameSingular;
		this.NAME_PLURAL = namePlural;
		this.cities = new ArrayList<City>();
	}
	
	public boolean sameCivilization(int id) {
		return ID == id;
	}
	
	private String getNextCityName() {
		return canCreateCity() ? cityNames[numberOfCities]: null;
	}
	public int getHappiness() {
		return happiness;
	}
	public void decreaseHappinessByAmount(int happiness) {
		this.happiness -= happiness;
	}
	public boolean createCity(HexCoordinate hexPos) {
		if (canCreateCity()) {
			String cityName = getNextCityName();
			cities.add(new City(cityName, hexPos));
			numberOfCities++;
			return true;
		}
		return false;
	}
	private boolean canCreateCity() {
		return numberOfCities < cityNames.length;
	}
	public int getNumberOfCities() {
		return numberOfCities;
	}
	public List<Unit> getUnits() {
		return units;
	}
	public List<City> getCities() {
		return cities;
	}
	public int getID() {
		return this.ID;
	}
	public Color getColour() {
		return this.civColour;
	}
	public String getSingularName() {
		return this.NAME_SINGULAR;
	}
	public String getPluralName() {
		return this.NAME_PLURAL;
	}
	
	public int getSciencePT() {
		return sciencePT;
	}
	public void setSciencePT(int sciencePT) {
		this.sciencePT = sciencePT;
	}
	public int getGoldTotal() {
		return goldTotal;
	}
	public void setGoldTotal(int goldTotal) {
		this.goldTotal = goldTotal;
	}
	public int getGoldPT() {
		return goldPT;
	}
	public void setGoldPT(int goldPT) {
		this.goldPT = goldPT;
	}
	public int getCultureTotal() {
		return cultureTotal;
	}
	public void setCultureTotal(int cultureTotal) {
		this.cultureTotal = cultureTotal;
	}
	public int getCultureRequired() {
		return cultureRequired;
	}
	public void setCultureRequired(int cultureRequired) {
		this.cultureRequired = cultureRequired;
	}
	public int getCulturePT() {
		return culturePT;
	}
	public void setCulturePT(int culturePT) {
		this.culturePT = culturePT;
	}

	public void addUnit(Unit u) {
		units.add(u);
	}
	public void replaceUnit(Unit oldUnit, Unit newUnit) {
		units.remove(oldUnit);
		units.add(newUnit);
	}
	public void deleteUnit(Unit u) {
		units.remove(u);
	}
}
