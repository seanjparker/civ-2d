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
import java.util.Random;
import java.util.Set;

import com.proj.civ.ai.Pathfinding;
import com.proj.civ.datastruct.FractionalHex;
import com.proj.civ.datastruct.Hex;
import com.proj.civ.datastruct.HexCoordinate;
import com.proj.civ.datastruct.HexMap;
import com.proj.civ.datastruct.Layout;
import com.proj.civ.datastruct.Point;
import com.proj.civ.input.MouseHandler;
import com.proj.civ.map.civilization.America;
import com.proj.civ.map.civilization.Civilization;
import com.proj.civ.map.terrain.Feature;
import com.proj.civ.map.terrain.YieldType;
import com.proj.civ.unit.Settler;
import com.proj.civ.unit.Unit;
import com.proj.civ.unit.Warrior;

public class GUI {
	private Random rnd;
	
	private final int WIDTH;
	private final int HEIGHT;
	
	private int hSize;
	private int wHexes;
	private int hHexes;
	private int focusX = 0, focusY = 0;
	
	private int scrollX, scrollY, scroll;
	
	private boolean ShiftPressed;
	//private boolean farmToAdd;
	
	private Map<Integer, Hex> map;
	private List<Hex> pathToFollow;
	
	private final HexMap hexMap;
	private final Layout layout;
	private final Polygon poly;
	private final Pathfinding pf;
	
	private Hex focusHex = null;
	private Hex pathToHex = null;
	
	private Civilization c1;
	
	public GUI(int w, int h, int h_s, int o_x, int o_y) {
		rnd = new Random();
		
		this.WIDTH = w;
		this.HEIGHT = h;
		this.hSize = h_s;
		this.scroll = h_s >> 1;
		
		this.wHexes = 40;
		this.hHexes = 25;
		
		pf = new Pathfinding();
		layout = new Layout(Layout.layout, new Point(hSize, hSize), new Point(hSize, hSize));
		poly = new Polygon();
		hexMap = new HexMap(this.wHexes, this.hHexes, hSize, layout);
		pathToFollow = new ArrayList<Hex>();
		
		hexMap.populateMap();
		this.map = hexMap.getMap();
	}
	
	public void drawHexGrid(Graphics2D g) {
		g.setStroke(new BasicStroke(1.0f));
		
		//if (MouseHandler.zoom != 0) {
		//	hSize = (MouseHandler.zoom == 1) ? hSize >> 1 : hSize << 1;
		//	if (MouseHandler.zoom == 1) {
				//this.hSize = Math.max(this.hSize, MIN_HEX);	
		//	} else {
				//this.hSize = Math.min(this.hSize, MAX_HEX);
		//	}
			//reCalculateHexSize();
		//}
		//MouseHandler.zoom = 0;
		Hex hex;
		for (int r = 0; r < hHexes; r++) {
			int rOff = (r + 1) >> 1;
			for (int q = -rOff; q < wHexes - rOff; q++) {
				Hex h1 = new Hex(q, r, -q - r);
				hex = map.get(HexMap.hash(h1));
				if (hex != null) {
					ArrayList<Point> p = Layout.polygonCorners(layout, hex);
					
					int drawX = (int) (p.get(0).x);
					int drawY = (int) (p.get(0).y);
					if ((drawX + scrollX < 0) || (drawX + scrollX > WIDTH + hSize * 2) || (drawY + scrollY < -hSize) || (drawY + scrollY > HEIGHT + hSize * 2)) {
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
			
			int mouseX = MouseHandler.movedMX;
			int mouseY = MouseHandler.movedMY;
			
			FractionalHex r = Layout.pixelToHex(layout, new Point(mouseX - scrollX, mouseY - scrollY));
			Hex h = FractionalHex.hexRound(r);
			Hex h1 = map.get(HexMap.hash(h));
			
			g.setFont(new Font("SansSerif", Font.BOLD, 16));
			
			if (h1 != null) {
				FontMetrics m = g.getFontMetrics();
				List<Feature> features = h1.getFeatures();
				
				int xOff = g.getFont().getSize();
				int padding = 3;
				int rectW = 200;
				int rectH = 100;
				int rectArcRatio = 20;
				int yOff = 0;
				int yOff1 = g.getFontMetrics().getHeight();
				
				//Draw rectangle at the mouse
				g.fillRoundRect(mouseX, mouseY, rectW, rectH + (features.size() * yOff1), rectW / rectArcRatio, rectH / rectArcRatio);
				
				//Write text in the box about hex yeild
				drawYieldAmount(g, YieldType.FOOD, Color.GREEN, h1, m, mouseX + padding, mouseY, 0);
				drawYieldAmount(g, YieldType.PRODUCTION, new Color(150, 75, 5), h1, m, mouseX + padding, mouseY, xOff);
				drawYieldAmount(g, YieldType.SCIENCE, Color.BLUE, h1, m, mouseX + padding, mouseY, xOff * 2);
				drawYieldAmount(g, YieldType.GOLD, new Color(244, 244, 34), h1, m, mouseX + padding, mouseY, xOff * 3);
			
				//Write text in the box (about landscape type)
				g.setColor(Color.BLACK);
				String landscape = "Landscape: " + h1.getLandscape().getName();
				g.drawString(landscape, mouseX + padding, mouseY + m.getHeight() + (yOff += yOff1));
				
				//Write text in the box (about landscape features)
				if (features.size() > 0) {
					StringBuilder sb = new StringBuilder(100);
					sb.append("Features: \n");
					features.forEach(i -> sb.append("- " + i.getName() + "\n"));
					drawHexInspectFeatures(g, sb, mouseX + padding, mouseY + m.getHeight() + yOff1, yOff1);	
				}
				
				yOff += (features.size() * yOff1) + (yOff1 * (features.size() > 0 ? 2 : 1)); //Determine text y-offset
				//Write text in the box (about improvements)
				if (h1.getImprovement() != null) {
					String improvement = "Improvement: " + h1.getImprovement().getName();
					g.drawString(improvement, mouseX + padding, mouseY + m.getHeight() + (yOff += yOff1));
				}
				
				//Write text in the box if a unit occupies it
				//if (c1.getUnits().stream().filter(i -> i.getPosition().isEqual(h1)) != null) {
					
				//}
				
			}
		}
	}
	
	private void drawYieldAmount(Graphics2D g, YieldType yield, Color c, Hex h, FontMetrics m, int x, int y, int xOff) {
		String amount = Integer.toString(h.getYieldTotal(yield));
		int widthX = m.stringWidth(amount);
		g.setColor(c);
		g.drawString(amount, x + widthX + xOff, y + m.getHeight());
	}
	
	private void drawHexInspectFeatures(Graphics2D g, StringBuilder s, int x, int y, int yOff) {
		for (String l : s.toString().split("\n")) {
			g.drawString(l, x, y += yOff);
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
			g.setColor(Color.WHITE);
			
			int toX = MouseHandler.movedMX;
			int toY = MouseHandler.movedMY;
			FractionalHex toFH = Layout.pixelToHex(layout, new Point(toX - scrollX, toY - scrollY));
			Hex endHex = FractionalHex.hexRound(toFH);
			if (!endHex.equals(pathToHex)) {
				pathToHex = endHex;
				pathToFollow = pf.findPath(map, focusHex, pathToHex);
				drawPathOnGrid(g);
			} else if (!focusHex.equals(endHex)){
				drawPathOnGrid(g);
			}
		}
	}
	
	private void drawPathOnGrid(Graphics2D g) {
		if (pathToFollow != null) {
			for (Hex h : pathToFollow) {
				if (!h.equals(focusHex)) {
					Point hexCentre = Layout.hexToPixel(layout, h);
					g.drawOval((int) (hexCentre.x + scrollX) - 10, (int) (hexCentre.y + scrollY) - 10, 20, 20);
				}
			}	
		}
	}
	
	public void drawFocusHex(Graphics2D g) {
		if (focusHex != null) {
			if (map.get(HexMap.hash(focusHex)) != null) {	
				g.setStroke(new BasicStroke(5.0f));
				
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
	
	public void createCiv() {
		c1 = new America();
		HexCoordinate settler = getRandomUnitCoord();
		HexCoordinate warrior = settler.getRandomNeighbour();
		c1.addNewUnit(new Settler(c1, settler));
		c1.addNewUnit(new Warrior(c1, warrior));
		
		c1.getUnits().forEach(x -> x.setIsSpawned());
		
		setInitialScroll(settler);
	}
	
	private HexCoordinate getRandomUnitCoord() {
		Hex h = null;
		do {
			double x = (double) rnd.nextInt(wHexes * hSize);
			double y = (double) rnd.nextInt(hHexes * hSize);
			FractionalHex fh = Layout.pixelToHex(layout, new Point(x, y));
			Hex h1 = FractionalHex.hexRound(fh);
			h = map.get(HexMap.hash(h1));
		} while (h == null);
		
		return new HexCoordinate(h.q, h.r, h.s);
	}
	
	public void drawUnits(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("SansSerif", Font.BOLD, 16));
		
		List<Unit> units = c1.getUnits();
		for (Unit u : units) {
			HexCoordinate hc = u.getPosition();
			Hex h = map.get(HexMap.hash(new Hex(hc.q, hc.r, hc.s)));
			Point p = Layout.hexToPixel(layout, h);
			String name = u.getName();
			g.drawString(name, (int) (p.x + scrollX - (name.length() * g.getFont().getSize())), (int) (p.y) + scrollY);	
		}
		//Hex h1 = map.get(HexMap.hash(new Hex(h.q, h.r, h.s)));
		//Point p = Layout.hexToPixel(layout, h1);
		
		
		//c1.getUnits().stream().filter(i -> i.isWarrior()).forEach(j -> g.drawString("Warrior", (int) (p.x) + scrollX, (int) (p.y) + scrollY));
		
		//g.drawString("Settler", (int) (p.x) + scrollX, (int) (p.y) + scrollY);
	}
	
	public void setInitialScroll(HexCoordinate h) {
		Point p = Layout.hexToPixel(layout, new Hex(h.q, h.r, h.s));
		int sX = Math.min(((int) -p.x + (WIDTH >> 2)), hSize); //Ensure the units are shown on-screen
		int sY = Math.min(((int) -p.y + (HEIGHT >> 2)), 0);
		
		
		//Round the values to a multiple of the scroll value
		scrollX = sX + scroll / 2;
		scrollX -= scrollX % scroll;
		
		scrollY = sY + scroll / 2;
		scrollY -= scrollY % scroll;
	}
	
	public void updateKeys(Set<Integer> keys) {
		if (keys.size() > 0) {
			for (Integer k : keys) {
				switch (k) {
				case KeyEvent.VK_UP:
					scrollY += scrollY < 0 ? scroll : 0;						
					break;
				case KeyEvent.VK_DOWN:
					scrollY -= scrollY > -(getAdjustedHeight()) ? scroll : 0;
					break;
				case KeyEvent.VK_LEFT:
					scrollX += scrollX < hSize ? scroll : 0;
					break;
				case KeyEvent.VK_RIGHT:
					scrollX -= scrollX > -(getAdjustedWidth()) ? scroll : 0;
					break;
				case KeyEvent.VK_SHIFT:
					ShiftPressed = true;
					break;
				//case KeyEvent.VK_F:
				//	farmToAdd = true;
				//	break;
				}
			}
			//System.out.println("ScrollX:" + scrollX + ", ScrollY:" + scrollY);
		}
	}
	
	/*
	public void addFarm() {
		if (farmToAdd) {
			farmToAdd = false;
			int mX = MouseHandler.movedMX;
			int mY = MouseHandler.movedMY;
			FractionalHex fh = Layout.pixelToHex(layout, new Point(mX - scrollX, mY - scrollY));
			Hex h = FractionalHex.hexRound(fh);
			int hexKey = HexMap.hash(h);
			Improvement i = new Farm();
			Hex mapHex = map.get(hexKey);
			mapHex.setImprovement(i);
			map.put(hexKey, mapHex);
		}
	}
	*/
	
	private int getAdjustedWidth() {
		return (int) ((Math.sqrt(3) * hSize * wHexes) - WIDTH);
	}
	private int getAdjustedHeight() {
		return (int) ((hHexes * hSize * 3 / 2) - HEIGHT + hSize);
	}
}
