package com.proj.civ.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MouseHandler implements MouseListener, MouseMotionListener, MouseWheelListener {
	public static int mX, mY, rMX, rMY, dMX, dMY, movedMX, movedMY;
	public static int button;
	public static boolean pressedMouse = false, releasedMouse = false, draggedMouse = false, lostFocus = false;
	
	public static int zoom = 0;
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
		//zoom = e.getWheelRotation();
		//1 -> towards user (Zoom out)
		//-1 -> away from user (Zoom in)
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		movedMX = e.getX();
		movedMY = e.getY();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		lostFocus = false;
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		lostFocus = true;
	}

}
