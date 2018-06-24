package civ.core.display.menu;

import java.awt.Color;
import java.awt.Graphics2D;
import civ.core.display.GUI;
import civ.core.unit.Unit;

public class UnitMenu extends Menu {
  private int menuWidth;
  private int menuHeight;
  private int windowHeight;
  private Unit currentUnit;

  public UnitMenu(boolean isActive, Unit currentUnit) {
    super("UnitAction", isActive);
    this.menuWidth = GUI.getHexRadius();
    this.menuHeight = GUI.getHexRadius() * 4;
    this.windowHeight = GUI.getWindowHeight();
    this.currentUnit = currentUnit;
    
  }

  @Override
  public void open() {
    setActive(true);
  }

  @Override
  public void close() {
    setActive(false);
    currentUnit.setBeingMoved(false);
    currentUnit.setBeingAttacked(false);
  }

  @Override
  public void draw(Graphics2D g) {
    g.setColor(new Color(100, 100, 100));
    g.fill3DRect(0, windowHeight - menuHeight, menuWidth, menuHeight, true);
    super.getMenuButtons().forEach(i -> i.drawButton(g));
  }

}
