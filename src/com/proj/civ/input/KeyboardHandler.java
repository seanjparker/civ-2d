package com.proj.civ.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

import com.proj.civ.instance.IData;

public class KeyboardHandler implements KeyListener {	
	public final Set<Integer> pressedSet = new HashSet<>();
	public static boolean ShiftPressed;
	
	@Override
	public void keyPressed(KeyEvent k) {
		pressedSet.add(k.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent k) {
		if (k.getKeyCode() == KeyEvent.VK_SHIFT) { //Temp code to stop the hex inspect box from flickering
			ShiftPressed = false;
		} else if (k.getKeyCode() == KeyEvent.VK_N) { //Temp code for next turns
			IData.nextTurnInProgress = false; 
		}
		
		pressedSet.remove(k.getKeyCode());	
	}

	@Override
	public void keyTyped(KeyEvent k) {

	}
	

}
