package civ.core.map.cities;

import java.awt.image.BufferedImage;
import civ.core.data.utils.GFXUtils;

public enum CityProductionOptions {
  UNITS(GFXUtils.loadImage("./gfx/buttons/CITY_PROD_UNITS.png"), "Units"),
  BUILDINGS(GFXUtils.loadImage("./gfx/buttons/CITY_PROD_BUILDINGS.png"), "Buildings"),
  WONDERS(GFXUtils.loadImage("./gfx/buttons/CITY_PROD_WONDERS.png"), "Wonders");
  
  private String text;
  private BufferedImage image;
  
  CityProductionOptions(BufferedImage image, String text) {
    this.image = image;
    this.text = text;
  }
  
  public BufferedImage getImage() {
    return this.image;
  }
  
  public String getText() {
    return this.text;
  }
}
