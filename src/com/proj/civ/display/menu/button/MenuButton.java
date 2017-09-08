package com.proj.civ.display.menu.button;

import com.proj.civ.instance.IData;

import javafx.geometry.Rectangle2D;

public abstract class MenuButton extends IData implements Button {
	protected final double BUTTON_CLICK_BUFFER = 1.0D;
	
	protected int bufferX, bufferY, buttonSizeX, buttonSizeY, xPos, yPos, buttonIndex;
	protected boolean isClickable;
	
	protected Rectangle2D buttonBounds;
	
	public MenuButton(int menuWidth, int menuHeight, int menuButtonIndex, boolean isClickable) {
		this.buttonSizeX = menuWidth >> 1;
		this.buttonSizeY = menuWidth >> 1;
		this.bufferX = menuWidth >> 2;
		this.bufferY = bufferX;
		this.isClickable = isClickable;
		this.xPos = bufferX;
		this.yPos = (HEIGHT - menuHeight + bufferY) + (menuButtonIndex * (this.buttonSizeY + bufferY));
		this.buttonIndex = menuButtonIndex;
		
		buttonBounds = new Rectangle2D(xPos, yPos, buttonSizeX, buttonSizeY);	
	}
	public MenuButton(int buttonSizeX, int buttonSizeY, int xPos, int yPos, boolean isClickable) {
		this.buttonSizeX = buttonSizeX;
		this.buttonSizeY = buttonSizeY;
		this.isClickable = isClickable;
		this.xPos = xPos;
		this.yPos = yPos;
		
		buttonBounds = new Rectangle2D(xPos, yPos, buttonSizeX, buttonSizeY);	
	}
	
	public boolean getIsClickable() {
		return isClickable;
	}
	public void setIsClickable(boolean isClickable) {
		this.isClickable = isClickable;
	}
}
