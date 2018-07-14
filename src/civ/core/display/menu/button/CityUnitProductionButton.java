package civ.core.display.menu.button;

import java.awt.Graphics2D;
import civ.core.input.MouseHandler;
import civ.core.instance.IUnit.UnitEnum;
import civ.core.map.cities.City;
import civ.core.unit.Unit;

public class CityUnitProductionButton extends Button {
  
  private City currentCity;
  private UnitEnum unit;
  public static Unit pressed;
  
  public CityUnitProductionButton(City currentCity, UnitEnum unit, int buttonSizeX, int buttonSizeY, int xPos, int yPos) {
    super(buttonSizeX, buttonSizeY, xPos, yPos);
    this.currentCity = currentCity;
    this.unit = unit;
  }

  @Override
  public void onPress() {
    if (MouseHandler.pressedMouse && buttonBounds.intersects(MouseHandler.mX, MouseHandler.mY,
        BUTTON_CLICK_BUFFER, BUTTON_CLICK_BUFFER)) {
      MouseHandler.pressedMouse = false;
      pressed = unit.get();
      pressed.addToProductionQueue(currentCity);
      pressed = null;
    }
  }

  @Override
  public void drawButton(Graphics2D g) {
    g.drawImage(unit.get().getImage(), xPos, yPos, buttonSizeX, buttonSizeY, null);
  }
}
