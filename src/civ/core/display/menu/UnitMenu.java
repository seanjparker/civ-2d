package civ.core.display.menu;

import java.awt.Color;
import java.awt.Graphics2D;

import static civ.core.instance.IData.*;

public class UnitMenu extends Menu {
  public final int menuWidth = HEX_RADIUS;
  public final int menuHeight = HEX_RADIUS * 4;

  public UnitMenu(boolean isActive) {
    super("UnitAction", isActive);
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
    g.fill3DRect(0, WINDOW_HEIGHT - menuHeight, menuWidth, menuHeight, true);
    super.getMenuButtons().forEach(i -> i.drawButton(g));
  }

}
