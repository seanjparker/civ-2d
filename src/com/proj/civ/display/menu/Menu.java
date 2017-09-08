package com.proj.civ.display.menu;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.proj.civ.display.menu.button.MenuButton;
import com.proj.civ.instance.IData;

public abstract class Menu extends IData {
	private String menuName;
	private boolean isActive;
	private List<MenuButton> buttons;
	
	public Menu(String menuName, boolean isActive) {
		this.menuName = menuName;
		this.isActive = isActive;
		this.buttons = new ArrayList<MenuButton>();
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
	public void addButton(MenuButton b) {
		buttons.add(b);
	}
	public List<MenuButton> getMenuButtons() {
		return buttons;
	}
}
