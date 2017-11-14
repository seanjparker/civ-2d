package com.proj.civ.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class KeyboardHandler implements KeyListener {
  public Set<Integer> pressedSet = new HashSet<Integer>();
  public static boolean ShiftPressed;
  public static boolean EscPressed;

  @Override
  public void keyPressed(KeyEvent k) {
    pressedSet.add(k.getKeyCode());
  }

  @Override
  public void keyReleased(KeyEvent k) {
    if (k.getKeyCode() == KeyEvent.VK_SHIFT) { // Temp code to stop the hex inspect box from
                                               // flickering
      ShiftPressed = false;
    }
    if (k.getKeyCode() == KeyEvent.VK_ESCAPE) {
      EscPressed = false;
    }
    pressedSet.remove(k.getKeyCode());
  }

  @Override
  public void keyTyped(KeyEvent k) {

  }


}
