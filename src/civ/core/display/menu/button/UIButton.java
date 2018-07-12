package civ.core.display.menu.button;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import civ.core.event.Events;
import civ.core.input.MouseHandler;

import static civ.core.instance.IData.*;

public class UIButton extends Button {

  private String text;

  public UIButton(Events e, int bWidth, int bHeight, int xPos, int yPos) {
    super(bWidth, bHeight, xPos, yPos);
    this.e = e;
  }

  public UIButton(Events e, String text, int bWidth, int bHeight, int xPos, int yPos) {
    super(bWidth, bHeight, xPos, yPos);
    this.text = text;
    this.e = e;
  }

  public void onPress() {
    if (MouseHandler.pressedMouse && buttonBounds.intersects(MouseHandler.mX, MouseHandler.mY,
        BUTTON_CLICK_BUFFER, BUTTON_CLICK_BUFFER)) {
      MouseHandler.pressedMouse = false;
      performEvent(this.e.getFunctionCall());
    }
  }

  public void drawButton(Graphics2D g) {
    g.setColor(new Color(48, 119, 186));
    g.fill3DRect(xPos, yPos, buttonSizeX, buttonSizeY, true);
    
    //Double the font size for this button
    ui.setTextFont(g, 2);
    g.setColor(Color.WHITE);
    int textWidth = g.getFontMetrics().stringWidth(text);

    g.drawString(text, xPos + (buttonSizeX / 2) - (textWidth / 2), yPos + (buttonSizeY / 2));
    
    //Reset the font size
    ui.setTextFont(g, 1);
  }

}
