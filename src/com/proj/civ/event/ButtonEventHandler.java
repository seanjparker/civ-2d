package com.proj.civ.event;

import java.awt.Graphics2D;

public interface ButtonEventHandler {
  public abstract void onPress();

  public abstract void drawButton(Graphics2D g);
}
