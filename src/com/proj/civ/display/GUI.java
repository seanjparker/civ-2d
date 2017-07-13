package com.proj.civ.display;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.proj.civ.ai.Pathfinding;
import com.proj.civ.datastruct.FractionalHex;
import com.proj.civ.datastruct.Hex;
import com.proj.civ.datastruct.HexMap;
import com.proj.civ.datastruct.Layout;
import com.proj.civ.datastruct.Point;
import com.proj.civ.input.MouseHandler;
import com.proj.civ.map.terrain.Feature;

public class GUI {
	private final int WIDTH;
	private final int HEIGHT;
	private int hSize;
	private int wHexes;
	private int hHexes;
	private int focusX = 0, focusY = 0;
	
	private int scrollX, scrollY;
	private boolean ShiftPressed;
	
	private Map<Integer, Hex> map;
	
	private HexMap hexMap;
	private Layout layout;
	private final Polygon poly;
	
	private Hex focusHex = null;
	
	public GUI(int w, int h, int h_s, int o_x, int o_y) {
		this.WIDTH = w;
		this.HEIGHT = h;
		this.hSize = h_s;
		
		this.wHexes = 40;
		this.hHexes = 25;
		
		layout = new Layout(Layout.layout, new Point(hSize, hSize), new Point(hSize, hSize));
		poly = new Polygon();
		hexMap = new HexMap(this.wHexes, this.hHexes, hSize, layout);
		hexMap.populateMap();
		this.map = hexMap.getMap();
	}
	
	public void drawHexGrid(Graphics2D g) {
		g.setStroke(new BasicStroke(1.0f));
		g.setFont(new Font("TimesRoman", Font.BOLD, 18));
		
		if (MouseHandler.zoom != 0) {
			hSize = (MouseHandler.zoom == 1) ? hSize >> 1 : hSize << 1;
			if (MouseHandler.zoom == 1) {
				//this.hSize = Math.max(this.hSize, MIN_HEX);	
			} else {
				//this.hSize = Math.min(this.hSize, MAX_HEX);
			}
			//reCalculateHexSize();
		}
		MouseHandler.zoom = 0;
		Hex hex;
		//int tempCounter = 1;
		for (int r = 0; r < hHexes; r++) {
			int rOff = (r + 1) >> 1;
			for (int q = -rOff; q < wHexes - rOff; q++) {
				Hex h1 = new Hex(q, r, -q - r);
				hex = map.get(HexMap.hash(h1));
				if (hex != null) {
					ArrayList<Point> p = Layout.polygonCorners(layout, hex);
					
					int drawX = (int) (p.get(0).x);
					int drawY = (int) (p.get(0).y);
					if ((drawX + scrollX < 0) || (drawX + scrollX > WIDTH + hSize * 2) || (drawY + scrollY < 0) || (drawY + scrollY > HEIGHT + hSize * 2)) {
						//tempCounter++;
						continue;
					}
					for (int k = 0; k < p.size(); k++) {
						poly.addPoint((int) (p.get(k).x) + scrollX, (int) (p.get(k).y) + scrollY);
					}			
					g.setColor(hex.getLandscape().getColour());
					g.fillPolygon(poly);
					g.setColor(Color.BLACK);
					g.drawPolygon(poly);
					poly.reset();
					
					//StringBuilder sb = new StringBuilder();
					//c.getFeatures().forEach(i -> sb.append(i.getName()));
					//g.drawString(sb.toString(), (int) Layout.hexToPixel(layout, h).x + scrollX, (int) Layout.hexToPixel(layout, h).y + scrollY);
					//g.drawString("" + tempCounter++, (int) Layout.hexToPixel(layout, h).x + scrollX, (int) Layout.hexToPixel(layout, h).y + scrollY);
				}
			}
		}
	}
	
	public void drawSelectedHex(Graphics2D g) {
		int mouseX = MouseHandler.movedMX;
		int mouseY = MouseHandler.movedMY;
		
		g.setStroke(new BasicStroke(3.5f));
			
		FractionalHex r = Layout.pixelToHex(layout, new Point(mouseX - scrollX, mouseY - scrollY));
		Hex s = FractionalHex.hexRound(r);
		if (map.get(HexMap.hash(s)) != null) {	
			ArrayList<Point> p = Layout.polygonCorners(layout, s);
			for (int k = 0; k < p.size(); k++) {
				poly.addPoint((int) (p.get(k).x) + scrollX, (int) (p.get(k).y) + scrollY);
			}
			g.setColor(Color.WHITE);
			g.drawPolygon(poly);
			poly.reset();
		}
	}
	
	public void drawHexInspect(Graphics2D g) {
		if (ShiftPressed) {
			ShiftPressed = false;
			
			g.setFont(new Font("SansSerif", Font.BOLD, 16));
			int padding = 3;
			int rectW = 200;
			int rectH = 100;
			int rectArcRatio = 20;
			int mouseX = MouseHandler.movedMX;
			int mouseY = MouseHandler.movedMY;
			
			FractionalHex r = Layout.pixelToHex(layout, new Point(mouseX - scrollX, mouseY - scrollY));
			Hex h = FractionalHex.hexRound(r);
			Hex h1 = map.get(HexMap.hash(h));
			if (h1 != null) {
				//Draw rectangle at the mouse
				g.fillRoundRect(mouseX, mouseY, rectW, rectH, rectW / rectArcRatio, rectH / rectArcRatio);
				
				//Write text in the box
				FontMetrics m = g.getFontMetrics();
				g.setColor(Color.BLACK);
				String landscape = "Landscape: " + h1.getLandscape().getName();
				g.drawString(landscape, mouseX + padding, mouseY + m.getHeight());
				
				List<Feature> features = h1.getFeatures();
				if (features.size() > 0) {
					StringBuilder sb = new StringBuilder(100);
					sb.append("Features: \n");
					features.forEach(i -> sb.append("- " + i.getName() + "\n"));
					drawHexInspectFeatures(g, sb, mouseX + padding, mouseY + m.getHeight(), g.getFontMetrics().getHeight());	
				}
			}
		}
	}
	
	public void drawPath(Graphics2D g) {
		if (MouseHandler.pressedMouse) {
			focusX = MouseHandler.mX;
			focusY = MouseHandler.mY;
			FractionalHex fromFH = Layout.pixelToHex(layout, new Point(focusX - scrollX, focusY - scrollY));
			Hex tempFocusHex = FractionalHex.hexRound(fromFH);
			focusHex = tempFocusHex.equals(focusHex) ? null : tempFocusHex;		
		}
		if (focusHex != null) {
			int toX = MouseHandler.movedMX;
			int toY = MouseHandler.movedMY;
			FractionalHex toFH = Layout.pixelToHex(layout, new Point(toX - scrollX, toY - scrollY));
			Hex toH = FractionalHex.hexRound(toFH);
			if (!focusHex.equals(toH)) {
				Pathfinding pf = new Pathfinding();
				List<Hex> pathToFollow = pf.findPath(map, focusHex, toH);
				for (Hex h : pathToFollow) {
					if (!h.equals(focusHex)) {
						Point hexCentre = Layout.hexToPixel(layout, h);
						g.drawOval((int) (hexCentre.x + scrollX) - 10, (int) (hexCentre.y + scrollY) - 10, 20, 20);
					}
				}	
			}
		}
	}
	
	public void drawFocusHex(Graphics2D g) {
		if (focusHex != null) {
			g.setStroke(new BasicStroke(5.0f));
			
			if (map.get(HexMap.hash(focusHex)) != null) {	
				ArrayList<Point> p = Layout.polygonCorners(layout, focusHex);
				for (int k = 0; k < p.size(); k++) {
					poly.addPoint((int) (p.get(k).x) + scrollX, (int) (p.get(k).y) + scrollY);
				}
				g.setColor(Color.WHITE);
				g.drawPolygon(poly);
				poly.reset();
			}
		}
	}
	
	private void drawHexInspectFeatures(Graphics2D g, StringBuilder s, int x, int y, int yOff) {
		for (String l : s.toString().split("\n")) {
			g.drawString(l, x, y += yOff);
		}
	}
	
	public void updateKeys(Set<Integer> keys) {
		if (keys.size() > 0) {
			for (Integer k : keys) {
				switch (k) {
				case KeyEvent.VK_UP:
					scrollY += scrollY < 0 ? hSize >> 1 : 0;						
					break;
				case KeyEvent.VK_DOWN:
					scrollY -= scrollY > -(getAdjustedHexHeight()) ? hSize >> 1 : 0;
					break;
				case KeyEvent.VK_LEFT:
					scrollX += scrollX < hSize ? hSize >> 1 : 0;
					break;
				case KeyEvent.VK_RIGHT:
					scrollX -= scrollX > -(getAdjustedHexWidth()) ? hSize >> 1 : 0;
					break;
				case KeyEvent.VK_SHIFT:
					ShiftPressed = true;
					break;
				}
			}
		}
		//System.out.println("Scroll X: " + scrollX + ", Y: " + scrollY);
		/*
		if (MouseHandler.releasedMouse) {
			int deltaX = -(MouseHandler.rMX - MouseHandler.dMX);
			int deltaY = -(MouseHandler.rMY - MouseHandler.dMY);
			this.scrollX = deltaX;
			this.scrollY = deltaY;
		} else {			
			this.scrollX = MouseHandler.dMX;
			this.scrollY = MouseHandler.dMY;
		}
		*/
	}
	private int getAdjustedHexWidth() {
		return (int) ((Math.sqrt(3) * hSize * wHexes) - WIDTH);
	}
	private int getAdjustedHexHeight() {
		return (int) ((hHexes * hSize * 3 / 2) - HEIGHT + hSize);
	}
	//private void reCalculateHexSize() {
	//	wHexes = WIDTH / (hSize * 2);
	//	hHexes = HEIGHT / (hSize * 2);
	//	layout = new Layout(Layout.layout, new Point(hSize, hSize), new Point(hSize, hSize));
	//}
}
