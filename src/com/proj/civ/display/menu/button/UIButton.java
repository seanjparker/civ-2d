package com.proj.civ.display.menu.button;

import java.awt.Color;
import java.awt.Graphics2D;

import com.proj.civ.event.Events;
import com.proj.civ.input.MouseHandler;

public class UIButton extends Button {
	
	private String text;
	
	public UIButton(Events e, int bWidth, int bHeight, int xPos, int yPos, boolean isClickable) {
		super(bWidth, bHeight, xPos, yPos, isClickable);
		this.e = e;
	}
	public UIButton(Events e, String text, int bWidth, int bHeight, int xPos, int yPos, boolean isClickable) {
		super(bWidth, bHeight, xPos, yPos, isClickable);
		this.text = text;
		this.e = e;
	}

	public void onPress() {
		if (MouseHandler.pressedMouse) {
			if (buttonBounds.intersects(MouseHandler.mX, MouseHandler.mY, BUTTON_CLICK_BUFFER, BUTTON_CLICK_BUFFER)) {
				MouseHandler.pressedMouse = false;
				performEvent();
			}
		}
	}

	public void drawButton(Graphics2D g) {
		g.setColor(new Color(48, 119, 186));
		g.fill3DRect(xPos, yPos, buttonSizeX, buttonSizeY, isClickable);
		
		g.setColor(Color.WHITE);
		int textWidth = g.getFontMetrics().stringWidth(text);
		int textHeight = g.getFontMetrics().getHeight();
		
		g.drawString(text, xPos + (buttonSizeX / 2) - (textWidth / 2), yPos + (buttonSizeY / 2) - (textHeight / 4));
	}

}
