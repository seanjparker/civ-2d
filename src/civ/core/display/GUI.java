package civ.core.display;

import static civ.core.instance.IData.HEX_RADIUS;
import static civ.core.instance.IData.H_HEXES;
import static civ.core.instance.IData.TEXT_SIZE;
import static civ.core.instance.IData.WINDOW_HEIGHT;
import static civ.core.instance.IData.WINDOW_WIDTH;
import static civ.core.instance.IData.W_HEXES;
import static civ.core.instance.IData.civs;
import static civ.core.instance.IData.currentUnit;
import static civ.core.instance.IData.hexMap;
import static civ.core.instance.IData.layout;
import static civ.core.instance.IData.turnCounter;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import civ.core.data.Point;
import civ.core.data.hex.Hex;
import civ.core.data.hex.HexCoordinate;
import civ.core.data.hex.PathHex;
import civ.core.data.utils.GFXUtils;
import civ.core.data.utils.Pair;
import civ.core.display.menu.button.Button;
import civ.core.display.menu.button.UIButton;
import civ.core.event.Events;
import civ.core.input.KeyboardHandler;
import civ.core.input.MouseHandler;
import civ.core.map.cities.City;
import civ.core.map.civilization.BaseCivilization;
import civ.core.map.terrain.YieldType;
import civ.core.unit.Unit;

public class GUI {
  private int scrollX;
  private int scrollY;
  private int scroll;

  private List<PathHex> pathToFollow;

  private Polygon poly;
  private List<Button> uiButtons;
  
  private Hex focusHex = null;
  
  private static final Color HEX_OUTLINE_COLOUR = new Color(80, 80, 80, 75);

  public GUI() {
    this.scroll = HEX_RADIUS >> 1;

    poly = new Polygon();
    HexCoordinate h = layout.pixelToHex(new Point(0, 0));
    Point[] pts = layout.polygonCorners(h);
    for (int k = 0; k < pts.length; k++) {
      poly.addPoint((int) pts[k].x, (int) pts[k].y);
    }

    uiButtons = new ArrayList<>();
    uiButtons.add(new UIButton(Events.NEXT_TURN, "Next Turn", HEX_RADIUS * 4, HEX_RADIUS,
        WINDOW_WIDTH - (HEX_RADIUS * 4), WINDOW_HEIGHT - HEX_RADIUS));
  }
   
  public void drawHexGrid(Graphics2D g) {
    g.setStroke(new BasicStroke(3.0f));
    final int bnd = 8;

    int centreX = (-scrollX) + WINDOW_WIDTH / 2;
    int centreY = (-scrollY) + WINDOW_HEIGHT / 2;

    HexCoordinate hexc = layout.pixelToHex(new Point(centreX, centreY));
    Point p1 = null;
    Point[] p2 = null;
    
    Hex h = null; 
    
    for (int dx = -bnd; dx <= bnd; dx++) {
      for (int dy = Math.max(-bnd, -dx - bnd); dy <= Math.min(bnd, -dx + bnd); dy++) {
        int dz = -dx - dy;

        h = hexMap.getHex(new HexCoordinate(hexc.q + dx, hexc.r + dy, hexc.s + dz));
        
        if (h != null) {
          p1 = layout.getPolygonPositionEstimate(h);
          if ((p1.x + scrollX < -HEX_RADIUS) || (p1.x + scrollX > WINDOW_WIDTH + HEX_RADIUS)
              || (p1.y + scrollY < -HEX_RADIUS) || (p1.y + scrollY > WINDOW_HEIGHT + HEX_RADIUS)) {
            continue;
          }
          
          p2 = layout.polygonCorners(h);
          for (int k = 0; k < p2.length; k++) {
            poly.addPoint((int) (p2[k].x) + scrollX, (int) (p2[k].y) + scrollY);
          }

          g.setColor(h.getLandscape().getColour());
          g.fillPolygon(poly);

          g.setColor(HEX_OUTLINE_COLOUR);
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

    HexCoordinate s = layout.pixelToHex(new Point(mouseX - scrollX, mouseY - scrollY));
    if (hexMap.getHex(s) != null) {
      Point[] p = layout.polygonCorners(s);
      for (int k = 0; k < p.length; k++) {
        poly.addPoint((int) (p[k].x) + scrollX, (int) (p[k].y) + scrollY);
      }
      g.setColor(Color.WHITE);
      g.drawPolygon(poly);
      poly.reset();
    }
  }

  public void drawHexInspect(Graphics2D g) {
    if (KeyboardHandler.isShiftPressed()) {
      int mouseX = MouseHandler.movedMX;
      int mouseY = MouseHandler.movedMY;

      HexCoordinate h = layout.pixelToHex(new Point(mouseX - scrollX, mouseY - scrollY));
      Hex currentFocusHex = hexMap.getHex(h);

      if (currentFocusHex != null) {
        
        //Set the font
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, TEXT_SIZE));
        
        //Set the position of the box
        int xOff = g.getFont().getSize();
        final int padding = 3;
        final int rectArc = 10;
        int yOff = g.getFontMetrics().getHeight();
        int rectW = 200;
        int rectH = yOff * 2 + padding;

        //Using the current hex, create string of stats
        Pair<String, Integer> hexBoxPair = currentFocusHex.createFormattedString(rectH, yOff);
        
        rectH = hexBoxPair.getSecond();
        
        //Determine the start drawing position of the inspect box
        boolean flipX = mouseX - rectW < 0;
        boolean flipY = mouseY - rectH < 0;
        int startX = flipX ? mouseX + padding : mouseX - rectW + padding;
        int startY = flipY ? mouseY : mouseY - rectH;

        // Draw rectangle at the mouse
        g.fillRoundRect(startX - padding, startY, rectW, rectH, rectArc,
            rectArc);

        // Write text in the box about hex yeild
        drawYieldAmount(g, YieldType.FOOD, 
            Color.GREEN, currentFocusHex, startX, startY, 0);
        
        drawYieldAmount(g, YieldType.PRODUCTION, 
            new Color(150, 75, 5), currentFocusHex, startX, startY, xOff);
        
        drawYieldAmount(g, YieldType.SCIENCE, 
            Color.BLUE, currentFocusHex, startX, startY, xOff * 2);
        
        drawYieldAmount(g, YieldType.GOLD, 
            new Color(204, 150, 0), currentFocusHex, startX, startY, xOff * 3);
        
        //Finally, draw the hex data
        drawStringBuilderData(g, hexBoxPair.getFirst(), startX, startY + yOff, yOff);
      }
    }
  }

  private void drawYieldAmount(Graphics2D g, YieldType yield, Color c, Hex h, int x, int y,
      int xOff) {
    String amount = Integer.toString(h.getYieldTotal(yield));
    int widthX = g.getFontMetrics().stringWidth(amount);
    g.setColor(c);
    g.drawString(amount, x + widthX + xOff, y + g.getFontMetrics().getHeight());
  }

  private void drawStringBuilderData(Graphics2D g, String s, int x, int y, int yOff) {
    g.setColor(Color.BLACK);
    for (String l : s.split("\n")) {
      y += yOff;
      g.drawString(l, x, y);
    }
  }

  public void drawPath(Graphics2D g) {
    boolean unitAndHexValid = focusHex != null 
        && currentUnit != null
        && currentUnit.isBeingMoved()
        && pathToFollow != null;
    
    if (unitAndHexValid) {
      Point hexCentre = null;
      for (PathHex h : pathToFollow) {
        if (!h.equals(focusHex)) {
          boolean unitMoveable = h.getPassable() || h.getCanSwitch();
          g.setColor(unitMoveable ? Color.WHITE : Color.RED);
          hexCentre = layout.hexToPixel(h);
          g.drawOval((int) (hexCentre.x + scrollX) - 10, 
              (int) (hexCentre.y + scrollY) - 10, 20, 20);
        }
      }
    }
  }

  public void drawFocusHex(Graphics2D g) {
    if (focusHex != null && hexMap.getHex(focusHex) != null) {
      g.setStroke(new BasicStroke(5.0f));

      Point[] p = layout.polygonCorners(focusHex);
      for (int k = 0; k < p.length; k++) {
        poly.addPoint((int) (p[k].x) + scrollX, (int) (p[k].y) + scrollY);

      }
      g.setColor(Color.WHITE);
      g.drawPolygon(poly);
      poly.reset();
    }
  }

  public void drawCities(Graphics2D g) {
    for (BaseCivilization civ : civs) {
      for (City city : civ.getCities()) {
        city.draw(g, civ.getColour(), scrollX, scrollY);
        drawCityAOO(g, city, civ);
      }
    }
  }
  
  public void drawCityAOO(Graphics2D g, City city, BaseCivilization civ) {
    Point[] p = null;
    Color civColour = civ.getColour();
    Color hexColour = new Color(civColour.getRed(), civColour.getGreen(), civColour.getBlue(), 100);
   
    for (HexCoordinate h : city.getCityHexes()) {
      if (hexMap.getHex(h) != null) {
        g.setStroke(new BasicStroke(3.5f));
        p = layout.polygonCorners(h);
        for (int k = 0; k < p.length; k++) {
          poly.addPoint((int) (p[k].x) + scrollX, (int) (p[k].y) + scrollY);
        }
        g.setColor(hexColour);
        g.fillPolygon(poly);
        poly.reset();
      }
    }
  }
  
  public void drawUnits(Graphics2D g) {
    g.setColor(Color.BLACK);
    g.setFont(new Font("SansSerif", Font.BOLD, TEXT_SIZE));
    Hex h = null;
    Point p = null;
    for (BaseCivilization c : civs) {
      for (Unit u : c.getUnits()) {
        h = hexMap.getHex(u.getPosition());
        p = layout.hexToPixel(h);
        int x = (int) (p.x + scrollX - (HEX_RADIUS >> 2));
        int y = (int) (p.y + scrollY - (HEX_RADIUS >> 2));
        drawUnit(g, u, x, y);
      }
    }
  }

  private void drawUnit(Graphics2D g, Unit u, int x, int y) {
    Color baseCol = u.getOwner().getColour();
    Color cB = baseCol.brighter();
    Color cD = baseCol.darker();
    int radius = HEX_RADIUS >> 1;
    int stroke = 2;
    
    g.setColor(baseCol);
    g.fillOval(x, y, radius, radius);

    g.setStroke(new BasicStroke(stroke));
    g.setColor(cD);
    g.drawArc(x, y, radius, radius, 50, 200);

    g.setColor(cB);
    g.drawArc(x, y, radius, radius, 50, -160);
    
    int size = HEX_RADIUS / 3;
    g.drawImage(u.getImage(), x + size / 4 , y + size / 4, size, size, null);
  }

  public void drawUI(Graphics2D g) {
    g.setColor(Color.BLACK);
    g.setFont(new Font("Lucida Sans", Font.BOLD, TEXT_SIZE));

    int fontHeight = g.getFontMetrics().getHeight();
    int fontWidth = g.getFont().getSize();
    int textX = 0;
    int offsetX = 100;
    int yieldHeight = (int) (0.75 * fontHeight);

    // Draw the top bar of the ui
    g.fillRect(0, 0, WINDOW_WIDTH, fontHeight);

    // Draw the turn counter
    g.setColor(Color.WHITE);
    g.drawString("Turn: " + turnCounter, WINDOW_WIDTH - offsetX, yieldHeight);

    if (civs.get(0).getNumberOfCities() > 0) {
      // Get the civ yield per turn
      int civSciencePT = civs.get(0).getSciencePT();
      int civGoldTotal = civs.get(0).getGoldTotal();
      int civGoldPT = civs.get(0).getGoldPT();
      int civCultureTotal = civs.get(0).getCultureTotal();
      int civCulturePT = civs.get(0).getCulturePT();
      int civCultureReq = civs.get(0).getCultureRequired();
      int civHappiness = civs.get(0).getHappiness();

      // Draw the yields
      g.setColor(new Color(91, 154, 255)); // Blue for science
      g.drawString("+" + Integer.toString(civSciencePT), textX, yieldHeight); // Science

      textX += offsetX;
      g.setColor(new Color(244, 244, 34)); // Dark Yellow for Gold
      g.drawString(Integer.toString(civGoldTotal) + "(+" + Integer.toString(civGoldPT) + ")", textX,
          yieldHeight); // Gold

      textX += offsetX;
      g.setColor(Color.YELLOW); // Brighter yellow for happiness
      g.drawString("\u263B", textX, yieldHeight);

      g.setColor(civHappiness >= 0 ? Color.GREEN : Color.RED); // Happiness colour setting depending on civ happiness
      g.drawString("" + Math.abs(civHappiness), textX + fontWidth, yieldHeight);

      textX += offsetX;
      g.setColor(new Color(186, 16, 160)); // Purple for culture
      g.drawString(Integer.toString(civCultureTotal) + "/" + Integer.toString(civCultureReq) + "(+"
          + Integer.toString(civCulturePT) + ")", textX, yieldHeight);
    }
    // Draw all buttons in all open menus
    uiButtons.stream().forEach(i -> i.drawButton(g));
  }
  
  public void setRenderHints(Graphics2D g) {
    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
  }
  
  public void drawActionMenus(Graphics2D g) {
    if (currentUnit != null)
      currentUnit.getMenu().draw(g);
  }

  public void setInitialScroll(HexCoordinate h) {
    Point p = layout.hexToPixel(new Hex(h.q, h.r, h.s));
    int sX = Math.min(((int) -p.x + (WINDOW_WIDTH >> 2)), HEX_RADIUS); // Ensure the units are shown
                                                                // on-screen
    int sY = Math.min(((int) -p.y + (WINDOW_HEIGHT >> 2)), 0);

    // Round the values to a multiple of the scroll value
    scrollX = sX + scroll / 2;
    scrollX -= scrollX % scroll;

    scrollY = sY + scroll / 2;
    scrollY -= scrollY % scroll;

    // Ensure the scroll is not outside bounds
    if (scrollX < -getAdjustedWidth()) {
      scrollX = -getAdjustedWidth();
    }
    if (scrollY < -getAdjustedHeight()) {
      scrollY = -getAdjustedHeight();
    }
  }

  public void updateKeys(Set<Integer> keys) {
    if (!keys.isEmpty()) {
      keys.stream().forEach(k -> {
        switch (k) {
          case KeyEvent.VK_UP:
            scrollY += scrollY < 0 ? scroll : 0;
            break;
          case KeyEvent.VK_DOWN:
            scrollY -= scrollY > -getAdjustedHeight() ? scroll : 0;
            break;
          case KeyEvent.VK_LEFT:
            scrollX += scrollX < HEX_RADIUS ? scroll : 0;
            break;
          case KeyEvent.VK_RIGHT:
            scrollX -= scrollX > -getAdjustedWidth() ? scroll : 0;
            break;
          case KeyEvent.VK_SHIFT:
            KeyboardHandler.setShiftPressed(true);
            break;
          case KeyEvent.VK_ESCAPE:
            KeyboardHandler.setEscPressed(true);
            setFocusedUnitPath(null);
            break;
          default:
            break;
        }
      });
    }
  }

  public static void nextTurn() { // Temp code
    for (BaseCivilization c : civs)
      for (Unit u : c.getUnits())
        u.nextTurn();
    turnCounter++;
  }

  /*
   * public void addFarm() { if (farmToAdd) { farmToAdd = false; int mX = MouseHandler.movedMX; int
   * mY = MouseHandler.movedMY; FractionalHex fh = Layout.pixelToHex(layout, new Point(mX - scrollX,
   * mY - scrollY)); Hex h = FractionalHex.hexRound(fh); int hexKey = HexMap.hash(h); Improvement i
   * = new Farm(); Hex mapHex = map.get(hexKey); mapHex.setImprovement(i); map.put(hexKey, mapHex);
   * } }
   */

  private int getAdjustedWidth() {
    return (int) ((Math.sqrt(3) * HEX_RADIUS * W_HEXES) - WINDOW_WIDTH);
  }

  private int getAdjustedHeight() {
    return (H_HEXES * HEX_RADIUS * 3 / 2) - WINDOW_HEIGHT + HEX_RADIUS;
  }

  public Point getHexPosFromMouse() {
    return new Point(MouseHandler.mX - scrollX, MouseHandler.mY - scrollY);
  }

  public int getScrollX() { 
    return scrollX;
  }

  public int getScrollY() {
    return scrollY;
  }

  public void setFocusHex() {
    if (MouseHandler.pressedMouse) {
      int focusX = MouseHandler.mX;
      int focusY = MouseHandler.mY;
      HexCoordinate tempFocusHex = layout.pixelToHex(new Point(focusX - scrollX, focusY - scrollY));
      Hex mapHex = hexMap.getHex(tempFocusHex);
      boolean shouldSetFocusHex =
          mapHex != null && (!mapHex.canSetMilitary() || !mapHex.canSetCivilian());
      if (shouldSetFocusHex)
        focusHex = new Hex(tempFocusHex.q, tempFocusHex.r, tempFocusHex.s);
    }
  }

  public void resetFocusData() {
    focusHex = null;
    if (currentUnit != null)
      currentUnit.getMenu().close();
    
    currentUnit = null;
  }

  public Hex getFocusHex() {
    return focusHex;
  }

  public void setFocusedUnitPath(List<PathHex> pathToFollow) {
    if (pathToFollow == null)
      resetFocusData();
    this.pathToFollow = pathToFollow;
  }

  public List<PathHex> getUnitPath() {
    return pathToFollow;
  }

  public List<Button> getMenuButtons() {
    return uiButtons;
  }
}
