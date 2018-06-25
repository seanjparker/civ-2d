package civ.core.map.cities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import civ.core.data.Point;
import civ.core.data.hex.HexCoordinate;

import static civ.core.instance.IData.*;

public class City {
  private final int FOOD_INITIAL = 1;
  private final int GOLD_INITIAL = 3;
  private final int PROD_INITIAL = 1;
  private final int SCIE_INITIAL = 4;
  private final int CULT_INITIAL = 1;


  private String cityName;
  private HexCoordinate cityPos;
  private int cityPopulation;
  private int cityFood, cityGold, cityProduction, cityScience, cityCulture;

  public City(String cityName, HexCoordinate cityPos) {
    this.cityName = cityName;
    this.cityPos = cityPos;

    this.cityFood = FOOD_INITIAL;
    this.cityGold = GOLD_INITIAL;
    this.cityProduction = PROD_INITIAL;
    this.cityScience = SCIE_INITIAL;
    this.cityCulture = CULT_INITIAL;

  }

  public void draw(Graphics2D g, int scrollX, int scrollY) {
    int citySize = HEX_RADIUS;
    int citySizeOffset = citySize >> 1;

    Point pos = layout.hexToPixel(cityPos);

    int xCentre = (int) (pos.x + scrollX);
    int yCentre = (int) (pos.y + scrollY) - citySizeOffset;

    int cityNameCentreOffset = (g.getFontMetrics().stringWidth(cityName) >> 1);

    g.fillOval(xCentre - citySizeOffset, yCentre, citySize, citySize);

    g.setColor(Color.WHITE);
    g.setFont(new Font("SansSerif", Font.BOLD, TEXT_SIZE));
    g.drawString(cityName, xCentre - cityNameCentreOffset, yCentre);
  }

  public int getPopulation() {
    return cityPopulation;
  }

  public int getFood() {
    return cityFood;
  }

  public int getGold() {
    return cityGold;
  }

  public int getProduction() {
    return cityProduction;
  }

  public int getScience() {
    return cityScience;
  }

  public int getCulture() {
    return cityCulture;
  }

}
