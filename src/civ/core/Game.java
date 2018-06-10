package civ.core;

import java.awt.Graphics2D;
import java.util.List;
import java.util.Random;
import civ.core.ai.Pathfinding;
import civ.core.data.Point;
import civ.core.data.hex.Hex;
import civ.core.data.hex.HexCoordinate;
import civ.core.data.hex.PathHex;
import civ.core.input.KeyboardHandler;
import civ.core.input.MouseHandler;
import civ.core.instance.IData;
import civ.core.map.civilization.America;
import civ.core.unit.Settler;
import civ.core.unit.Unit;
import civ.core.unit.Warrior;

public class Game {
  private Random rnd;
  private Pathfinding pf;

  private HexCoordinate hexToPath;
  private List<HexCoordinate> pathToFollow;

  public Game(int players) {
    rnd = new Random();
    pf = new Pathfinding();

    IData.hexMap.populateMap(); // Generate initial map

    createCiv();
  }

  public void update(KeyboardHandler k) {
    if (k.pressedSet.size() > 0) {
      IData.ui.updateKeys(k.pressedSet);
    }

    IData.ui.setFocusHex();
    setCurrentUnit();
    createUnitPath();

    if (IData.currentUnit != null) {
      IData.currentUnit.getMenu().getMenuButtons().stream().forEach(j -> j.onPress());

      if (shouldMoveUnit()) {
        IData.currentUnit.moveUnit(IData.ui.getFocusHex(), IData.ui.getScrollX(), IData.ui.getScrollY());
        MouseHandler.pressedMouse = false;
      }
    }

    IData.ui.getMenuButtons().forEach(i -> i.onPress());
  }

  public void draw(Graphics2D g) {
    // ui.drawTest(g);

    IData.ui.drawHexGrid(g);
    IData.ui.drawCities(g);
    IData.ui.drawUnits(g);
    IData.ui.drawSelectedHex(g);
    IData.ui.drawPath(g);
    IData.ui.drawFocusHex(g);
    IData.ui.drawHexInspect(g);
    IData.ui.drawUI(g);
    IData.ui.drawActionMenus(g);

  }

  private void createCiv() {
    // Initalize civ
    IData.civs.add(new America());

    // Add units to the game map
    // Get settler and warrior co-ordinate
    HexCoordinate settler, warrior;
    Hex tempH;

    do {
      settler = getRandomUnitCoord();
      tempH = IData.hexMap.getHex(settler);
    } while ((tempH == null));

    // Ensure the neighbour is in the map, the following loop will not get stuck on infinite loop
    do {
      warrior = settler.getRandomNeighbour();
    } while (IData.hexMap.getHex(warrior) == null);

    // Set the units in the hexes
    Unit s = new Settler(IData.civs.get(0), settler, true);
    Unit w = new Warrior(IData.civs.get(0), warrior, true);
    s.addToMapAndCiv();
    w.addToMapAndCiv();

    IData.ui.setInitialScroll(settler);
  }

  private HexCoordinate getRandomUnitCoord() {
    Hex h = null;
    do {
      double x = (double) rnd.nextInt(IData.W_HEXES * IData.HEX_RADIUS * 2);
      double y = (double) rnd.nextInt(IData.H_HEXES * IData.HEX_RADIUS * 2);
      HexCoordinate h1 = IData.layout.pixelToHex(new Point(x, y));
      h = IData.hexMap.getHex(h1);
    } while (h == null);

    return new HexCoordinate(h.q, h.r, h.s);
  }

  private void setCurrentUnit() {
    if (IData.ui.getFocusHex() != null && IData.currentUnit == null) {
      List<Unit> civUnits = IData.civs.get(0).getUnits();
      Hex currentHex = IData.hexMap.getHex(IData.ui.getFocusHex());
      for (Unit u : civUnits) {
        if (u.getPosition().isEqual(currentHex.getPosition())) {
          IData.currentUnit = u;
        }
      }
    }
  }

  public void createUnitPath() {
    Hex focusHex = IData.ui.getFocusHex();
    if (focusHex != null && IData.currentUnit != null && IData.currentUnit.isBeingMoved()) {
      int toX = MouseHandler.movedMX;
      int toY = MouseHandler.movedMY;
      int scrollX = IData.ui.getScrollX();
      int scrollY = IData.ui.getScrollY();
      Hex endHex;
      HexCoordinate tempTo = IData.layout.pixelToHex(new Point(toX - scrollX, toY - scrollY));
      boolean canCreatePath =
          (!focusHex.isEqual(tempTo) && IData.hexMap.getHex(tempTo) != null) && (hexToPath == null)
              || (!hexToPath.isEqual(tempTo));
      if (canCreatePath) {
        hexToPath = tempTo;
        endHex = IData.hexMap.getHex(hexToPath);
        pathToFollow = pf.findPath(IData.hexMap.getMap(), focusHex, endHex);
        List<PathHex> finalPath = IData.currentUnit.validUnitMove(pathToFollow);
        IData.ui.setFocusedUnitPath(finalPath);
      }
    }
  }

  private boolean shouldMoveUnit() {
    HexCoordinate fromHex = IData.ui.getFocusHex();
    if (fromHex != null && MouseHandler.pressedMouse) {
      HexCoordinate toHexPlace = IData.layout.pixelToHex(IData.ui.getHexPosFromMouse());
      if (toHexPlace != null && !fromHex.isEqual(toHexPlace)) {
        List<PathHex> path = IData.ui.getUnitPath();
        if (path != null) {
          return (path.stream().anyMatch(i -> i.getPassable() && i.isEqual(toHexPlace)))
              || (path.stream().filter(j -> j.isEqual(toHexPlace)).anyMatch(k -> k.getCanSwitch()));
        }
      }
    }
    return false;
  }

}
