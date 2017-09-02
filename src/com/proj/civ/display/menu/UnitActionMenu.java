package com.proj.civ.display.menu;

import java.awt.Color;
import java.awt.Graphics2D;

public class UnitActionMenu extends ActionMenu {

	public UnitActionMenu(boolean isActive) {
		super("UnitAction", isActive);
	}
	@Override
	public void opened() {
	}
	@Override
	public void closed() {
	}
	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.CYAN);
		g.fillRect(WIDTH - 200, HEIGHT - 200, 100, 100);
	}

}
