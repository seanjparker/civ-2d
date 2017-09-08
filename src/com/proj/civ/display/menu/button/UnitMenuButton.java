package com.proj.civ.display.menu.button;

import java.awt.Color;
import java.awt.Graphics2D;

import com.proj.civ.input.MouseHandler;

public class UnitMenuButton extends MenuButton{

	public UnitMenuButton(int bIndex, boolean isClickable) {
		super(HEX_RADIUS, HEX_RADIUS * 4, bIndex, isClickable);
	}

	public void drawButton(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fill3DRect(xPos, yPos, buttonSizeX, buttonSizeY, isClickable);
	}

	public void onPress() {
		if (MouseHandler.pressedMouse) {
			if (buttonBounds.intersects(MouseHandler.mX, MouseHandler.mY, BUTTON_CLICK_BUFFER, BUTTON_CLICK_BUFFER)) {
				MouseHandler.pressedMouse = false;
				
				//Code for the button click action
			}
		}
	}

}
