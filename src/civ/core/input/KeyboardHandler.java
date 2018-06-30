package civ.core.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class KeyboardHandler implements KeyListener {
  static {
    pressedSet = new HashSet<>();
  }
  
  protected static Set<Integer> pressedSet;
  
  protected static boolean shiftPressed;
  protected static boolean escPressed;

  
  @Override
  public void keyPressed(KeyEvent k) {
    pressedSet.add(k.getKeyCode());
  }

  @Override
  public void keyReleased(KeyEvent k) {
    if (k.getKeyCode() == KeyEvent.VK_SHIFT)
      setShiftPressed(false);
    if (k.getKeyCode() == KeyEvent.VK_ESCAPE)
      setEscPressed(false);
    
    pressedSet.remove(k.getKeyCode());
  }

  @Override
  public void keyTyped(KeyEvent k) {
    //Empty: We need to know when any key is pressed, not just unicode charcater keys
  }
  
  public static boolean isShiftPressed() {
    return KeyboardHandler.shiftPressed;
  }
  public static boolean isEscPressed() {
    return KeyboardHandler.escPressed;
  }
  
  public static void setShiftPressed(boolean isPressed) {
    KeyboardHandler.shiftPressed = isPressed;
  }
  public static void setEscPressed(boolean isPressed) {
    KeyboardHandler.escPressed = isPressed;
  }

  public static Set<Integer> getPressedSet() {
    return pressedSet;
  }

}
