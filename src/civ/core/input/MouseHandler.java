package civ.core.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MouseHandler implements MouseListener, MouseMotionListener, MouseWheelListener {
  public static int mX;
  public static int mY;
  public static int rMX;
  public static int rMY;
  public static int dMX;
  public static int dMY;
  public static int movedMX;
  public static int movedMY;
  public static int button;
  
  public static int zoom = 0;
  
  public static boolean pressedMouse = false;
  public static boolean releasedMouse = false;
  public static boolean draggedMouse = false;
  public static boolean lostFocus = false;


  public void mousePressed(MouseEvent e) {
    mX = e.getX();
    mY = e.getY();
    pressedMouse = true;
  }

  public void mouseReleased(MouseEvent e) {
    pressedMouse = false;
  }

  public void mouseDragged(MouseEvent e) {

  }

  public void mouseWheelMoved(MouseWheelEvent e) {
  
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    movedMX = e.getX();
    movedMY = e.getY();
  }

  @Override
  public void mouseClicked(MouseEvent e) {}

  @Override
  public void mouseEntered(MouseEvent arg0) {
    lostFocus = false;
  }

  @Override
  public void mouseExited(MouseEvent arg0) {
    lostFocus = true;
  }

}
