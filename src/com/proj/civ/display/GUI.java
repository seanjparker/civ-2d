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

import com.proj.civ.datastruct.FractionalHex;
import com.proj.civ.datastruct.Hex;
import com.proj.civ.datastruct.HexMap;
import com.proj.civ.datastruct.Layout;
import com.proj.civ.datastruct.Point;
import com.proj.civ.input.MouseHandler;
import com.proj.civ.map.Cell;
import com.proj.civ.map.terrain.Feature;

public class GUI {
	private final int WIDTH;
	private final int HEIGHT;
	private int hSize;
	private int wHexes;
	private int hHexes;
	
	private int scrollX, scrollY;
	
	private Map<Integer, Cell> map;
	
	private HexMap hexMap;
	private Layout layout;
	private final Polygon poly;
	
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
		
		Hex h;
		Cell c;
		int tempCounter = 1;
		for (int r = 0; r < hHexes; r++) {
			int rOff = (r + 1) >> 1;
			for (int q = -rOff; q < wHexes - rOff; q++) {
				h = new Hex(q, r);
				c = map.get(HexMap.hash(h));
				if (c != null) {
					ArrayList<Point> p = Layout.polygonCorners(layout, h);
					
					int drawX = (int) (p.get(0).x);
					int drawY = (int) (p.get(0).y);
					if ((drawX + scrollX < 0) || (drawX + scrollX > WIDTH + hSize * 2) || (drawY + scrollY < 0) || (drawY + scrollY > HEIGHT + hSize * 2)) {
						//tempCounter++;
						continue;
					}
					for (int k = 0; k < p.size(); k++) {
						poly.addPoint((int) (p.get(k).x) + scrollX, (int) (p.get(k).y) + scrollY);
					}			
					g.setColor(c.getLandscape().getColour());
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
		if (MouseHandler.pressedMouse) {
			g.setFont(new Font("SansSerif", Font.BOLD, 16));
			int padding = 3;
			int rectW = 200;
			int rectH = 100;
			int rectArcRatio = 20;
			int mouseX = MouseHandler.mX;
			int mouseY = MouseHandler.mY;
			
			FractionalHex r = Layout.pixelToHex(layout, new Point(mouseX - scrollX, mouseY - scrollY));
			Hex s = FractionalHex.hexRound(r);
			Cell c = map.get(HexMap.hash(s));
			if (c != null) { //This hex should be inspected
				//Draw rectangle at the mouse
				g.fillRoundRect(mouseX, mouseY, rectW, rectH, rectW / rectArcRatio, rectH / rectArcRatio);
				
				//Write text in the box
				FontMetrics m = g.getFontMetrics();
				g.setColor(Color.BLACK);
				String landscape = "Landscape: " + c.getLandscape().getName();
				g.drawString(landscape, mouseX + padding, mouseY + m.getHeight());
				
				List<Feature> features = c.getFeatures();
				StringBuilder sb = new StringBuilder(100);
				sb.append("Features: \n");
				features.forEach(i -> sb.append("- " + i.getName() + "\n"));
				drawHexInspectFeatures(g, sb, mouseX + padding, mouseY + m.getHeight(), g.getFontMetrics().getHeight());
				//g.drawString(sb.toString(), mouseX + padding, mouseY + m.getHeight() * 2);
			}
		}
	}
	
	private void drawHexInspectFeatures(Graphics2D g, StringBuilder s, int x, int y, int yOff) {
		for (String l : s.toString().split("\n")) {
			g.drawString(l, x, y += yOff);
		}
	}
	
	public void updateScroll(Set<Integer> keys) {
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
