package civ.core.event;

import java.awt.Graphics2D;

public interface ButtonEventHandler {
  public void onPress();
  public void drawButton(Graphics2D g);
}
