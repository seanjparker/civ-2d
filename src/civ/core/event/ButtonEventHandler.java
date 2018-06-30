package civ.core.event;

import java.awt.Graphics2D;

public interface ButtonEventHandler {
  void onPress();
  void drawButton(Graphics2D g);
}
