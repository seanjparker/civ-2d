package com.proj.civ.display.menu;

import java.awt.Graphics2D;

import com.proj.civ.instance.IData;

public abstract class ActionMenu extends IData {
	private String menuName;
	private boolean isActive;
	
	public ActionMenu(String menuName, boolean isActive) {
		this.menuName = menuName;
		this.isActive = isActive;
	}
	
	public abstract void opened();
	public abstract void closed();
	public abstract void draw(Graphics2D g);
	
	public String getMenuName() {
		return this.menuName;
	}
	public boolean getIsActive() {
		return this.isActive;
	}
}
