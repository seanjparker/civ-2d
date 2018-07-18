package civ.core.map.cities;

import static civ.core.instance.IData.*;
import static civ.core.map.cities.CityProductionOptions.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import civ.core.data.Point;
import civ.core.data.Producable;
import civ.core.data.hex.HexCoordinate;
import civ.core.data.map.HexMap;
import civ.core.data.utils.GFXUtils;
import civ.core.display.menu.button.CityProductionButton;
import civ.core.display.menu.button.CityUnitProductionButton;
import civ.core.instance.IUnit.UnitEnum;
import civ.core.map.civilization.BaseCivilization;
import civ.core.unit.Scout;
import civ.core.unit.Settler;
import civ.core.unit.Unit;
import civ.core.unit.Warrior;
import civ.core.unit.Worker;

public class City {
  private static final int FOOD_INITIAL = 1;
  private static final int GOLD_INITIAL = 3;
  private static final int PROD_INITIAL = 1;
  private static final int SCIE_INITIAL = 4;
  private static final int CULT_INITIAL = 1;
  private static final int STRG_INITIAL = 8;
  private static final int POPU_INITIAL = 1;
  
  private static final int INITIAL_CITY_AREA = 1;
  
  private static final int UI_SECTIONS = 6;
  private static final int BORDER_WIDTH = 4;
  private static final int TOTAL_BORDER = uiYOffset + BORDER_WIDTH;
  private static final int BOX_WIDTH = WINDOW_WIDTH / UI_SECTIONS;
  private static final int BOX_XPOS = WINDOW_WIDTH - BOX_WIDTH;
  
  private static final int MAIN_BUTTON_SIZE = HEX_RADIUS;
  
  private static final BufferedImage SHIELD;
  private static final BufferedImage POPULATION;

  private String cityName;
  private HexCoordinate cityPos;
  private BaseCivilization owner;
  private int cityPopulation;
  private int cityFood;
  private int cityGold;
  private int cityProduction;
  private int cityScience;
  private int cityCulture;
  private int cityStrength;
  
  private List<HexCoordinate> cityHexes;
  private List<CityProductionButton> cityProductionButtons;
  private List<CityUnitProductionButton> unitProductionButtons;
  
  private Queue<Producable> cityProductionQueue;
 
  
  static {
    SHIELD = GFXUtils.loadImage("./gfx/icons/SHIELD.png");
    POPULATION = GFXUtils.loadImage("./gfx/icons/POPULATION.png");
  }

  public City(String cityName, HexCoordinate cityPos, BaseCivilization owner) {
    this.cityName = cityName;
    this.cityPos = cityPos;
    this.owner = owner;
    
    this.cityPopulation = POPU_INITIAL;
    this.cityFood = FOOD_INITIAL;
    this.cityGold = GOLD_INITIAL;
    this.cityProduction = PROD_INITIAL;
    this.cityScience = SCIE_INITIAL;
    this.cityCulture = CULT_INITIAL;
    this.cityStrength = STRG_INITIAL;
    
    //Get all the hexes with the range of this hex
    this.cityHexes = HexMap.getAllInRange(cityPos, INITIAL_CITY_AREA);
    //Remove the city hex from the List
    this.cityHexes.remove(cityPos);
    
    cityProductionButtons = new ArrayList<>();
    cityProductionQueue = new LinkedList<>();
    
    int buttonCount = 0;
    int buttonX = WINDOW_WIDTH - BOX_WIDTH / 2 - (MAIN_BUTTON_SIZE / 2);
    cityProductionButtons.add(new CityProductionButton(UNITS, MAIN_BUTTON_SIZE, MAIN_BUTTON_SIZE,
        buttonX, TOTAL_BORDER + MAIN_BUTTON_SIZE * buttonCount++));

    cityProductionButtons.add(new CityProductionButton(BUILDINGS, MAIN_BUTTON_SIZE,
        MAIN_BUTTON_SIZE, buttonX, TOTAL_BORDER + MAIN_BUTTON_SIZE * buttonCount++));

    cityProductionButtons.add(new CityProductionButton(WONDERS, MAIN_BUTTON_SIZE, MAIN_BUTTON_SIZE,
        buttonX, TOTAL_BORDER + MAIN_BUTTON_SIZE * buttonCount));

    int buttonSize = HEX_RADIUS / 2;
    int buttonY = MAIN_BUTTON_SIZE * cityProductionButtons.size() + TOTAL_BORDER * 2;
    buttonX = WINDOW_WIDTH - (BOX_WIDTH * 3 / 4) - (buttonSize / 2);
    int buttonsInRow = 3;
    
    int count = -1;
    unitProductionButtons = new ArrayList<>();
    for (UnitEnum unit : owner.getAvailableUnits()) {
      unitProductionButtons.add(new CityUnitProductionButton(this, unit, buttonSize, buttonSize, buttonX, buttonY));
      buttonX += buttonSize + TOTAL_BORDER;
      if (++count % buttonsInRow == buttonsInRow - 1) {
        buttonY += buttonSize + TOTAL_BORDER;
        buttonX -= (buttonSize + TOTAL_BORDER) * buttonsInRow;
      }
    }
  }

  public void draw(Graphics2D g, Color cityColour, int scrollX, int scrollY) {
    Point pos = layout.hexToPixel(cityPos);

    int xCentre = (int) (pos.x + scrollX);
    int yCentre = (int) (pos.y + scrollY) - (HEX_RADIUS / 2);
    
    //Set up the font for text and calculate width of city name box
    int nameWidth = g.getFontMetrics().stringWidth(cityName);
    int nameHeight = g.getFontMetrics().getHeight();
    int cityNameCentreOffset = nameWidth >> 1;
    int textHeightOffset = nameHeight * 4 / 3;
    
    //Draw the city name box
    g.setColor(new Color(cityColour.getRed(), cityColour.getGreen(), cityColour.getBlue(), 180));
    g.fillRoundRect(xCentre - (nameWidth / 2), yCentre, nameWidth, textHeightOffset, 5, 5);
    g.setColor(cityColour);
    g.drawRoundRect(xCentre - (nameWidth / 2), yCentre, nameWidth + 1, textHeightOffset + 1, 5, 5);
    
    //Write the city name in the box
    g.setColor(GFXUtils.getColourForReadableText(cityColour));
    g.drawString(cityName, xCentre - cityNameCentreOffset, yCentre + nameHeight);
    
    //Draw the shield icon
    int width = HEX_RADIUS / 2;
    int height = (int) (width * 1.2); //Maintain the image 1:1.2 w:h ratio
    g.drawImage(SHIELD, xCentre - (3 * width / 2), yCentre + height, width, height, null);
    
    //Next draw the city strength value 
    g.setFont(new Font("SansSerif", Font.BOLD, HEX_RADIUS / 3));
    g.drawString(Integer.toString(this.cityStrength), xCentre - (HEX_RADIUS / 3) - (width / 2), yCentre + height + textHeightOffset);
    
    //Draw the city population icon
    g.drawImage(POPULATION, xCentre + (width / 2), yCentre + height, width, width, null);
    
    //Draw the city population number
    int populationWidth = g.getFontMetrics().stringWidth(Integer.toString(this.cityPopulation));
    g.setColor(Color.WHITE);
    g.drawString(Integer.toString(this.cityPopulation), xCentre + width - (populationWidth / 2), yCentre + height + textHeightOffset);
    
    
    g.setFont(new Font("SansSerif", Font.BOLD, TEXT_SIZE));
  }
  public void drawUI(Graphics2D g) {
    drawCityResourceUI(g);
    drawCityProductionUI(g);
  }
  private void drawCityProductionUI(Graphics2D g) {
    int currentHeight = MAIN_BUTTON_SIZE * cityProductionButtons.size() + TOTAL_BORDER;
    int toHeight = WINDOW_HEIGHT - HEX_RADIUS * 2;
        
    // Draw the black background for the window
    g.setColor(Color.BLACK);
    g.fillRect(BOX_XPOS, uiYOffset, BOX_WIDTH, toHeight);

    // Draw the city production options in the box
    g.setStroke(new BasicStroke(1.0f));

    // Draw production buttons line seperator
    g.setColor(new Color(255, 255, 255, 150)); // Nearly transparent white
    g.drawLine(BOX_XPOS, currentHeight, WINDOW_WIDTH, currentHeight);
    
    // Draw production buttons
    for(CityProductionButton button : cityProductionButtons)
      button.drawButton(g);
    
    //Draw either units, buildings or wonders, depending on selected option
    switch (CityProductionButton.pressed) {
      case UNITS:
        unitProductionButtons.forEach(i -> i.drawButton(g)); break;
      case BUILDINGS:
        break;
      case WONDERS:
        break;
        
    }
    if (!cityProductionQueue.isEmpty() && cityProductionQueue.peek() != null) {
      // Draw the production queue
      int productionQueueY = toHeight - HEX_RADIUS;
      g.setColor(PROD_COLOUR);
      g.fillOval(BOX_XPOS + TOTAL_BORDER, productionQueueY, HEX_RADIUS, HEX_RADIUS);
      
      g.setStroke(new BasicStroke(3.0f));
      g.setColor(PROD_COLOUR.darker());
      g.drawOval(BOX_XPOS + TOTAL_BORDER, productionQueueY, HEX_RADIUS + 1, HEX_RADIUS + 1);
      
      int turnsLeft = (cityProductionQueue.peek().getProductionCost() - cityProductionQueue.peek().getCurrentProduction()) / cityProduction;
      String turnsLeftAsString = Integer.toString(turnsLeft);
      
      g.setColor(Color.WHITE);
      ui.setTextFont(g, 2);
      g.drawString(turnsLeftAsString,
          BOX_XPOS + (g.getFontMetrics().stringWidth(turnsLeftAsString) / 2),
          productionQueueY + (g.getFontMetrics().getHeight()));
      ui.setTextFont(g, 1);
      
      int count = 0;
      for (Producable next : cityProductionQueue) {
        g.drawString(Integer.toString(count + 1) + ": " + next.getName(),
            BOX_XPOS + (TOTAL_BORDER * 2) + HEX_RADIUS,
            productionQueueY + g.getFontMetrics().getHeight() * count);
        count++;
      }
    }
    
    // Draw the box border
    g.setStroke(new BasicStroke(BORDER_WIDTH));
    g.setColor(Color.LIGHT_GRAY);
    g.drawRoundRect(BOX_XPOS, uiYOffset, BOX_WIDTH, toHeight, 10, 10);
  }
  private void drawCityResourceUI(Graphics2D g ) {
    int sepHeight = (WINDOW_HEIGHT / 2) / UI_SECTIONS;
    int currentLineHeight = sepHeight + TOTAL_BORDER;

    // Draw the black background for the window
    g.setColor(Color.BLACK);
    g.fillRect(0, uiYOffset, WINDOW_WIDTH / 4, WINDOW_HEIGHT / 2);

    // Draw the city data in the box
    g.setStroke(new BasicStroke(1.0f));

    // Draw population line seperator
    g.setColor(new Color(255, 255, 255, 150)); // Nearly transparent white
    g.drawLine(0, currentLineHeight, WINDOW_WIDTH / 4, currentLineHeight);
    currentLineHeight += sepHeight;

    // Draw the population image
    g.drawImage(POPULATION, 0, TOTAL_BORDER, HEX_RADIUS, HEX_RADIUS, null);

    // Draw the city population
    g.setColor(Color.WHITE);
    ui.setTextFont(g, 4);
    g.drawString(Integer.toString(cityPopulation), HEX_RADIUS,
        TOTAL_BORDER + g.getFontMetrics().getHeight() * 3 / 4);

    ui.setTextFont(g, 2);
    
    // Draw food
    String foodString = (cityFood >= 0 ? "+" : "") + Integer.toString(cityFood);
    drawResource(g, FOOD_COLOUR, currentLineHeight, "Food:", foodString, true);
    currentLineHeight += sepHeight;
    
    // Draw production
    String productionString = "+" + Integer.toString(cityProduction);
    drawResource(g, PROD_COLOUR, currentLineHeight, "Production:", productionString, true);
    currentLineHeight += sepHeight;
    
    // Draw gold
    String goldString = (cityGold >= 0 ? "+" : "") + Integer.toString(cityGold);
    drawResource(g, GOLD_COLOUR, currentLineHeight, "Gold:", goldString, true);
    currentLineHeight += sepHeight;
    
    // Draw science
    String scienceString = "+" + Integer.toString(cityScience);
    drawResource(g, SCIE_COLOUR, currentLineHeight, "Science:", scienceString, true);
    currentLineHeight += sepHeight;
    
    // Draw culture
    String cultureString = "+" + Integer.toString(cityCulture);
    drawResource(g, CULT_COLOUR, currentLineHeight, "Culture:", cultureString, false);
    
    
    // Draw the box border
    g.setStroke(new BasicStroke(BORDER_WIDTH));
    g.setColor(Color.LIGHT_GRAY);
    g.drawRoundRect(0, uiYOffset, WINDOW_WIDTH / 4, WINDOW_HEIGHT / 2, 10, 10);
  }
  private void drawResource(Graphics2D g, Color colour, int currentLineHeight, String quantityName, String quantity, boolean drawSeperator) {
    g.setColor(new Color(255, 255, 255, 150)); // Nearly transparent white
    
    if (drawSeperator) g.drawLine(0, currentLineHeight, WINDOW_WIDTH / 4, currentLineHeight);
    
    int textHeight = currentLineHeight - g.getFontMetrics().getHeight() * 3 / 4;
    g.setColor(colour);
    g.drawString(quantityName, 0, textHeight);
    
    g.setColor(Integer.parseInt(quantity) >= 0 ? colour : Color.RED);
    g.drawString(quantity, WINDOW_WIDTH / 4 - g.getFontMetrics().stringWidth(quantity), textHeight);
  }
  
  public String getCityName() {
    return this.cityName;
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
  public int getStrength() {
    return cityStrength;
  }

  public List<HexCoordinate> getCityHexes() {
    return this.cityHexes;
  }
  public List<CityProductionButton> getCityProductionButtons() {
    return cityProductionButtons;
  }
  public List<CityUnitProductionButton> getCityUnitProductionButtons() {
    return this.unitProductionButtons;
  }
  
  public HexCoordinate getCityPosition() {
    return this.cityPos;
  }
  
  public boolean addToProductionQueue(Producable next) {
    if (this.cityProductionQueue.size() < 3) {
      this.cityProductionQueue.add(next);
      return true;
    }
    return false;
  }
  
  private void addBuildToCity(Producable build) {
    if (build instanceof Unit) {
      addUnitToCity((Unit) build);
    }
  }
  private void addUnitToCity(Unit unit) {
    Unit unitToAdd = null;
    switch (unit.getName()) {
      case "Settler":
        unitToAdd = new Settler(owner, cityPos.getValidRandomNeighbour(false), true);
        break;
      case "Worker":
        unitToAdd = new Worker(owner, cityPos.getValidRandomNeighbour(false), true);
        break;
      case "Warrior":
        unitToAdd = new Warrior(owner, cityPos.getValidRandomNeighbour(true), true);
        break;
      case "Scout":
        unitToAdd = new Scout(owner, cityPos.getValidRandomNeighbour(true), true);
        break;
      default:
        unitToAdd = new Scout(owner, cityPos.getValidRandomNeighbour(true), true);
          
    }
    owner.addUnit(unitToAdd);
    unitToAdd.addToMapAndCiv();
  }
  
  public void nextTurn() {
    if (!cityProductionQueue.isEmpty()) {
      Producable nextToBuild = cityProductionQueue.peek();
      nextToBuild.addProductionToBuild(cityProduction);

      // If we have contributed enough production to complete the build, remove from queue and add
      // build to map
      if (nextToBuild.getCurrentProduction() >= nextToBuild.getProductionCost()) {
        addBuildToCity(nextToBuild); //Add the build to the city
        cityProductionQueue.poll(); //Remove from the queue
      }
    }
  }
  
  @Override
  public String toString() {
    return "City: " + cityName + " (" + owner.getSingularName() + ")\n"
        + "Resources per turn:\n"
        + " - Food: " + cityFood + "\n"
        + " - Production: " + cityProduction + "\n"
        + " - Gold: " + cityGold + "\n"
        + " - Science: " + cityScience + "\n"
        + " - Culture: " + cityCulture + "\n";
  }
}
