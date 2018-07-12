package civ.core.display.menu.button;

import java.awt.Graphics2D;
import civ.core.input.MouseHandler;
import civ.core.map.cities.CityProductionOptions;

public class CityProductionButton extends Button {

  private CityProductionOptions cpo;
  public static CityProductionOptions pressed;
  
  static {
    CityProductionButton.pressed = CityProductionOptions.UNITS;
  }
  
  public CityProductionButton(CityProductionOptions cpo, int buttonSizeX, int buttonSizeY, int xPos, int yPos) {
    super(buttonSizeX, buttonSizeY, xPos, yPos);
    this.cpo = cpo;
  }

  @Override
  public void onPress() {
    if (MouseHandler.pressedMouse && buttonBounds.intersects(MouseHandler.mX, MouseHandler.mY,
        BUTTON_CLICK_BUFFER, BUTTON_CLICK_BUFFER)) {
      MouseHandler.pressedMouse = false;
      pressed = cpo;
    }
  }

  @Override
  public void drawButton(Graphics2D g) {
    g.drawImage(cpo.getImage(), xPos, yPos, buttonSizeX, buttonSizeY, null);
  }

}
