package civ.core.event;

import java.awt.Graphics2D;
import civ.core.event.callback.EventCallbackI;

public interface ButtonEventHandler {
  
  void onPress();
  void drawButton(Graphics2D g);
  
}
