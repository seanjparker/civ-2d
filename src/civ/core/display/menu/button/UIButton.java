package civ.core.display.menu.button;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import civ.core.event.Events;

public class UIButton extends Button {

  private String TEXT;
  private int TEXT_SIZE;

  public UIButton(Events e, int[] buttonSize, int[] position) {
    super(buttonSize, position);
    this.e = e;
  }

  public UIButton(final Events e, String text, int textSize, int bWidth, int bHeight, int xPos, int yPos) {
    this(e, new int[] {bWidth, bHeight}, new int[] {xPos, yPos});
    this.TEXT = text;
    this.TEXT_SIZE = textSize;
  }

  public void onPress() {
//    if (MouseHandler.pressedMouse) {
//      if (buttonBounds.intersects(MouseHandler.mX, MouseHandler.mY, BUTTON_CLICK_BUFFER,
//          BUTTON_CLICK_BUFFER)) {
//        MouseHandler.pressedMouse = false;
//        performEvent();
//      }
//    }
  }

  public void drawButton(Graphics2D g) {
    g.setColor(new Color(48, 119, 186));
    g.fill3DRect(xPos, yPos, buttonSizeX, buttonSizeY, true);

    g.setFont(new Font("SansSerif", Font.BOLD, TEXT_SIZE * 2));
    g.setColor(Color.WHITE);
    int textWidth = g.getFontMetrics().stringWidth(TEXT);

    g.drawString(TEXT, xPos + (buttonSizeX / 2) - (textWidth / 2), yPos + (buttonSizeY / 2));
  }

}
