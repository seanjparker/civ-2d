package civ.core.map.civilization;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import civ.core.data.hex.HexCoordinate;
import civ.core.instance.IUnit.UnitEnum;
import civ.core.map.cities.City;
import civ.core.unit.Unit;

public class BaseCivilization {
  private final String nameSingular;
  private final String namePlural;

  private final int id;
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

  public BaseCivilization(String nameSingular, String namePlural, Color civColour,
      String[] cityNames) {
    this.id = nameSingular.hashCode() ^ namePlural.hashCode();
    this.units = new ArrayList<>();
    this.civColour = civColour;
    this.cityNames = cityNames;
    this.nameSingular = nameSingular;
    this.namePlural = namePlural;
    this.cities = new ArrayList<>();
  }

  public boolean sameCivilization(int id) {
    return this.id == id;
  }

  private String getNextCityName() {
    return canCreateCity() ? cityNames[numberOfCities] : null;
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
      City cityToAdd = new City(cityName, hexPos, this);
      cities.add(cityToAdd);
      updateResourceYields(cityToAdd);
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

  private void updateResourceYields(City c) {
    this.sciencePT += c.getScience();
    this.goldPT += c.getGold();
    this.culturePT += c.getCulture();
  }

  public List<Unit> getUnits() {
    return units;
  }

  public List<City> getCities() {
    return cities;
  }

  public int getID() {
    return this.id;
  }

  public Color getColour() {
    return this.civColour;
  }

  public String getSingularName() {
    return this.nameSingular;
  }

  public String getPluralName() {
    return this.namePlural;
  }

  public int getSciencePT() {
    return sciencePT;
  }

  public int getGoldTotal() {
    return goldTotal;
  }

  public int getGoldPT() {
    return goldPT;
  }

  public int getCultureTotal() {
    return cultureTotal;
  }

  public int getCultureRequired() {
    return cultureRequired;
  }

  public int getCulturePT() {
    return culturePT;
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
  
  public City getCityAt(HexCoordinate hex) {
    Optional<City> currentCity = this.cities.stream().filter(c -> c.getCityPosition().equals(hex)).findFirst();
    return currentCity.isPresent() ? currentCity.get() : null;
  }

  /*
   * For now, we just return all the units we have added to the game
   * But in the future, when the research tree is added, we need a way to find, based on the civ research
   * what units the civ can produce in a city
   */
  public List<UnitEnum> getAvailableUnits() {
    return Arrays.asList(
        UnitEnum.SETTLER,
        UnitEnum.WORKER,
        UnitEnum.WARRIOR,
        UnitEnum.SCOUT
        );
  }

  public void nextTurn() {
    for (City city : cities)
      city.nextTurn();
    
    for (Unit unit : units)
      unit.nextTurn();
  }
}
