package civ.core.display.menu.button;

import static civ.core.instance.IData.WINDOW_HEIGHT;
import java.awt.Rectangle;
import civ.core.event.ButtonEventHandler;
import civ.core.event.Events;
import civ.core.event.callback.EventCallbackI;

public abstract class Button implements ButtonEventHandler {
  protected final double BUTTON_CLICK_BUFFER = 1.0D;

  protected int bufferX, bufferY, buttonSizeX, buttonSizeY, xPos, yPos, buttonIndex;

  protected Rectangle buttonBounds;
  protected Events e = null;

  public Button(int menuWidth, int menuHeight, int menuButtonIndex) {
    this.buttonSizeX = (menuWidth / 4) * 3;
    this.buttonSizeY = buttonSizeX;
    this.bufferX = menuWidth / 6;
    this.bufferY = bufferX;
    this.xPos = bufferX;
    this.yPos = (WINDOW_HEIGHT - menuHeight + bufferY) + (menuButtonIndex * (this.buttonSizeY + bufferY));
    this.buttonIndex = menuButtonIndex;

    buttonBounds = new Rectangle(xPos, yPos, buttonSizeX, buttonSizeY);
  }

  public Button(int buttonSizeX, int buttonSizeY, int xPos, int yPos) {
    this.buttonSizeX = buttonSizeX;
    this.buttonSizeY = buttonSizeY;
    this.xPos = xPos;
    this.yPos = yPos;

    buttonBounds = new Rectangle(xPos, yPos, buttonSizeX, buttonSizeY);
  }

  protected void performEvent(EventCallbackI funCall) {
    if (funCall != null)
      funCall.invoke();
  }
}
