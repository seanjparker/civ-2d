package civ.core.display.menu;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import civ.core.display.menu.button.Button;

public abstract class Menu {
  private String menuName;
  private boolean isActive;
  private List<Button> buttons;

  public Menu(String menuName, boolean isActive) {
    this.menuName = menuName;
    this.isActive = isActive;
    this.buttons = new ArrayList<>();
  }

  public abstract void open();

  public abstract void close();

  public abstract void draw(Graphics2D g);

  public String getMenuName() {
    return this.menuName;
  }

  public boolean getIsActive() {
    return this.isActive;
  }

  public void setActive(boolean active) {
    this.isActive = active;
  }

  public void addButton(Button b) {
    buttons.add(b);
  }

  public List<Button> getMenuButtons() {
    return buttons;
  }
}
