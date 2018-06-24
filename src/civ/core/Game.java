package civ.core;

import java.awt.Graphics2D;
import java.util.List;
import java.util.Random;
import civ.core.ai.Pathfinding;
import civ.core.data.Layout;
import civ.core.data.Point;
import civ.core.data.hex.Hex;
import civ.core.data.hex.HexCoordinate;
import civ.core.data.hex.PathHex;
import civ.core.data.map.HexMap;
import civ.core.display.GUI;
import civ.core.input.KeyboardHandler;
import civ.core.input.MouseHandler;
import civ.core.map.civilization.America;
import civ.core.map.civilization.BaseCivilization;
import civ.core.unit.Settler;
import civ.core.unit.Unit;
import civ.core.unit.Warrior;

public class Game {
  private Random rnd;
  private Pathfinding pf;

  private HexCoordinate hexToPath;
  private List<HexCoordinate> pathToFollow;
  private List<BaseCivilization> civs;
  private HexMap hexMap;
  private GUI ui;
  private Unit currentUnit;

  public Game(int players, GUI ui) {
    this.ui = ui;
    
    rnd = new Random();
    pf = new Pathfinding();
    hexMap = new HexMap(GUI.getMapSizeWide(), GUI.getMapSizeWide(), GUI.getHexRadius());
    
    hexMap.populateMap();

    createCiv();
  }

  public void update(KeyboardHandler k) {
    if (k.pressedSet.size() > 0) {
      ui.updateKeys(k.pressedSet);
    }

    ui.setFocusHex();
    setCurrentUnit();
    createUnitPath();

    if (currentUnit != null) {
      currentUnit.getMenu().getMenuButtons().stream().forEach(j -> j.onPress());

      if (shouldMoveUnit()) {
        currentUnit.moveUnit(ui, hexMap, civs, ui.getFocusHex(), ui.getScrollX(), ui.getScrollY());
        MouseHandler.pressedMouse = false;
      }
    }

    ui.getMenuButtons().forEach(i -> i.onPress());
  }

  public void draw(Graphics2D g) {
    ui.drawHexGrid(g);
    ui.drawCities(g);
    ui.drawUnits(g);
    ui.drawSelectedHex(g);
    ui.drawPath(g);
    ui.drawFocusHex(g);
    ui.drawHexInspect(g);
    ui.drawUI(g);
    ui.drawActionMenus(g);
  }

  private void createCiv() {
    // Initalize civ
    civs.add(new America());

    // Add units to the game map
    // Get settler and warrior co-ordinate
    HexCoordinate settler, warrior;
    Hex tempH;

    do {
      settler = getRandomUnitCoord();
      tempH = hexMap.getHex(settler);
    } while ((tempH == null));

    // Ensure the neighbour is in the map, the following loop will not get stuck on infinite loop
    do {
      warrior = settler.getRandomNeighbour();
    } while (hexMap.getHex(warrior) == null);

    // Set the units in the hexes
    Unit s = new Settler(civs.get(0), settler, true);
    Unit w = new Warrior(civs.get(0), warrior, true);
    s.addToMapAndCiv(hexMap, civs);
    w.addToMapAndCiv(hexMap, civs);

    ui.setInitialScroll(settler);
  }

  private HexCoordinate getRandomUnitCoord() {
    Hex h = null;
    do {
      double x = (double) rnd.nextInt(GUI.getMapSizeWide() * GUI.getHexRadius() * 2);
      double y = (double) rnd.nextInt(GUI.getMapSizeHeight() * GUI.getHexRadius() * 2);
      HexCoordinate h1 = Layout.pixelToHex(new Point(x, y));
      h = hexMap.getHex(h1);
    } while (h == null);

    return new HexCoordinate(h.q, h.r, h.s);
  }

  private void setCurrentUnit() {
    if (ui.getFocusHex() != null && currentUnit == null) {
      List<Unit> civUnits = civs.get(0).getUnits();
      Hex currentHex = hexMap.getHex(ui.getFocusHex());
      for (Unit u : civUnits) {
        if (u.getPosition().isEqual(currentHex.getPosition())) {
          currentUnit = u;
        }
      }
    }
  }

  public void createUnitPath() {
    Hex focusHex = ui.getFocusHex();
    if (focusHex != null && currentUnit != null && currentUnit.isBeingMoved()) {
      int toX = MouseHandler.movedMX;
      int toY = MouseHandler.movedMY;
      int scrollX = ui.getScrollX();
      int scrollY = ui.getScrollY();
      Hex endHex;
      HexCoordinate tempTo = Layout.pixelToHex(new Point(toX - scrollX, toY - scrollY));
      boolean canCreatePath =
          (!focusHex.isEqual(tempTo) && hexMap.getHex(tempTo) != null) && (hexToPath == null)
              || (!hexToPath.isEqual(tempTo));
      if (canCreatePath) {
        hexToPath = tempTo;
        endHex = hexMap.getHex(hexToPath);
        pathToFollow = pf.findPath(hexMap.getMap(), focusHex, endHex);
        List<PathHex> finalPath = currentUnit.validUnitMove(ui, hexMap, pathToFollow);
        ui.setFocusedUnitPath(finalPath);
      }
    }
  }

  private boolean shouldMoveUnit() {
    HexCoordinate fromHex = ui.getFocusHex();
    if (fromHex != null && MouseHandler.pressedMouse) {
      HexCoordinate toHexPlace = Layout.pixelToHex(ui.getHexPosFromMouse());
      if (toHexPlace != null && !fromHex.isEqual(toHexPlace)) {
        List<PathHex> path = ui.getUnitPath();
        if (path != null) {
          return (path.stream().anyMatch(i -> i.getPassable() && i.isEqual(toHexPlace)))
              || (path.stream().filter(j -> j.isEqual(toHexPlace)).anyMatch(k -> k.getCanSwitch()));
        }
      }
    }
    return false;
  }

}
