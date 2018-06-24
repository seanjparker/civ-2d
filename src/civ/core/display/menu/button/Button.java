package civ.core.display.menu.button;

import java.awt.Rectangle;
import civ.core.display.GUI;
import civ.core.event.Events;
import civ.core.unit.Settler;
import civ.core.unit.Unit;

public abstract class Button {
  protected final double BUTTON_CLICK_BUFFER = 1.0D;

  protected int bufferX, bufferY, buttonSizeX, buttonSizeY, xPos, yPos, buttonIndex;

  protected Rectangle buttonBounds;
  protected Events e = null;

  public Button(final int windowHeight, int menuWidth, int menuHeight, int menuButtonIndex) {
    this.buttonSizeX = (menuWidth / 4) * 3;
    this.buttonSizeY = buttonSizeX;
    this.bufferX = menuWidth / 6;
    this.bufferY = bufferX;
    this.xPos = bufferX;
    this.yPos = (windowHeight - menuHeight + bufferY) + (menuButtonIndex * (this.buttonSizeY + bufferY));
    this.buttonIndex = menuButtonIndex;

    buttonBounds = new Rectangle(xPos, yPos, buttonSizeX, buttonSizeY);
  }

  public Button(int[] buttonSize, int[] position) {
    if (buttonSize.length != 2 || position.length != 2)
      throw new ArrayIndexOutOfBoundsException("Supply two arguments");
    
    this.buttonSizeX = buttonSize[0];
    this.buttonSizeY = buttonSize[1];
    this.xPos = position[0];
    this.yPos = position[1];

    buttonBounds = new Rectangle(xPos, yPos, buttonSizeX, buttonSizeY);
  }

  public void performEvent(Object... obj) {
    switch (e) {
      case FOUND_CITY:
        if (obj[0] instanceof Settler)
          ((Settler) obj[0]).foundCity();
        break;
      case MOVE:
        if (obj[0] instanceof Unit)
          ((Unit) obj[0]).toggleBeingMoved();
        break;
      case ATTACK:
        if (obj[0] instanceof Unit)
          ((Unit) obj[0]).toggleBeingAttacked();
        break;
      case AUTO_EXPLORE:
        System.out.println("Auto Explore");
        break;
      case DO_NOTHING:
        System.out.println("Do Nothing");
        break;
      case SLEEP:
        System.out.println("Sleep");
        break;
      case DELETE:
        if (obj[0] instanceof Unit)
          ((Unit) obj[0]).deleteBySelling();
        break;
      case NEXT_TURN:
        if (obj[0] instanceof GUI)
          ((GUI) obj[0]).nextTurn();
        break;
      case CIVILOPEDIA_OPEN:
        System.out.println("Open Civilopedia");
        break;
      case RESEARCH_TREE_OPEN:
        System.out.println("Open Research Tree");
        break;
      case CITY_OVERVIEW_OPEN:
        System.out.println("Open City Overview");
        break;
      case CITY_PRODUCTION_OPEN:
        System.out.println("Open City Production");
        break;
      case CULTURE_TREE_OPEN:
        System.out.println("Open Culture Tree");
        break;
      default:
        break;
    }
  }
}
