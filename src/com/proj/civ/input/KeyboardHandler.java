package com.proj.civ.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class KeyboardHandler implements KeyListener {	
	public final Set<Integer> pressedSet = new HashSet<>();
	
	@Override
	public void keyPressed(KeyEvent k) {
		pressedSet.add(k.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent k) {
		pressedSet.remove(k.getKeyCode());	
	}

	@Override
	public void keyTyped(KeyEvent k) {

	}
	

}
