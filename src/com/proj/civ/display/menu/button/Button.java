package com.proj.civ.display.menu.button;

import java.awt.Image;
import java.awt.Rectangle;

import com.proj.civ.event.ButtonEventHandler;
import com.proj.civ.event.Events;
import com.proj.civ.instance.IData;

public abstract class Button extends IData implements ButtonEventHandler {
	protected final double BUTTON_CLICK_BUFFER = 1.0D;
	
	protected int bufferX, bufferY, buttonSizeX, buttonSizeY, xPos, yPos, buttonIndex;
	protected boolean isClickable;
	
	protected Rectangle buttonBounds;
	protected Events e = null;
	
	public Button(int menuWidth, int menuHeight, int menuButtonIndex, boolean isClickable) {
		this.buttonSizeX = (menuWidth / 4) * 3;
		this.buttonSizeY = buttonSizeX;
		this.bufferX = menuWidth / 2;
		this.bufferY = bufferX / 2;
		this.isClickable = isClickable;
		this.xPos = bufferX - (buttonSizeX / 2);
		this.yPos = (HEIGHT - menuHeight + bufferY) + (menuButtonIndex * (this.buttonSizeY + bufferY));
		this.buttonIndex = menuButtonIndex;
		
		buttonBounds = new Rectangle(xPos, yPos, buttonSizeX, buttonSizeY);	
	}
	public Button(int buttonSizeX, int buttonSizeY, int xPos, int yPos, boolean isClickable) {
		this.buttonSizeX = buttonSizeX;
		this.buttonSizeY = buttonSizeY;
		this.isClickable = isClickable;
		this.xPos = xPos;
		this.yPos = yPos;
		
		buttonBounds = new Rectangle(xPos, yPos, buttonSizeX, buttonSizeY);	
	}
	
	public boolean getIsClickable() {
		return isClickable;
	}
	public void setIsClickable(boolean isClickable) {
		this.isClickable = isClickable;
	}
	
	public void performEvent() {
		if (e != null) {
			switch (e) {
			case FOUND_CITY:
				System.out.println("Found City");
				break;
			case MOVE:
				System.out.println("Move");
				break;
			case ATTACK:
				System.out.println("Attack");
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
				System.out.println("Delete");
				break;
			case NEXT_TURN:
				System.out.println("Next Turn");
				ui.nextTurn();
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
}
