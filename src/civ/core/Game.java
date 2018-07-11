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
import civ.core.map.civilization.America;
import civ.core.map.civilization.England;
import civ.core.unit.Settler;
import civ.core.unit.Unit;
import civ.core.unit.Warrior;

import static civ.core.instance.IData.*;

public class Game {
  private Random rnd;
  private Pathfinding pf;

  private HexCoordinate hexToPath;

  public Game(int players) {
    rnd = new Random();
    pf = new Pathfinding();

    hexMap.populateMap(); // Generate initial map

    createCiv(players);
  }

  public void update() {
    if (!KeyboardHandler.getPressedSet().isEmpty()) {
      ui.updateKeys(KeyboardHandler.getPressedSet());
    }

    ui.setFocusHex();
    setCurrentUnit();
    createUnitPath();

    if (currentUnit != null) {
      currentUnit.getMenu().getMenuButtons().stream().forEach(j -> j.onPress());

      if (shouldMoveUnit()) {
        currentUnit.moveUnit(ui.getFocusHex(), ui.getScrollX(), ui.getScrollY());
        MouseHandler.pressedMouse = false;
      }
    }

    ui.getMenuButtons().forEach(i -> i.onPress());
  }

  public void draw(Graphics2D g) {
    ui.setRenderOptions(g);

    ui.drawHexGrid(g);
    ui.drawCities(g);
    ui.drawUnits(g);
    ui.drawSelectedHex(g);
    ui.drawPath(g);
    ui.drawFocusHex(g);
    ui.drawHexInspect(g);
    ui.drawUI(g);
    ui.drawCityManagementUI(g);
    ui.drawActionMenus(g);
  }

  private void createCiv(int players) {
    // Initalize civ
    civs.add(new America());

    // Add units to the game map
    // Get settler and warrior co-ordinate
    HexCoordinate settler;
    HexCoordinate warrior;
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
    s.addToMapAndCiv();
    w.addToMapAndCiv();

    ui.setInitialScroll(settler);
  }

  private HexCoordinate getRandomUnitCoord() {
    Hex h = null;
    do {
      double x = (double) rnd.nextInt(W_HEXES * HEX_RADIUS * 2);
      double y = (double) rnd.nextInt(H_HEXES * HEX_RADIUS * 2);
      HexCoordinate h1 = layout.pixelToHex(new Point(x, y));
      h = hexMap.getHex(h1);
    } while (h == null);

    return new HexCoordinate(h.q, h.r, h.s);
  }

  private void setCurrentUnit() {
    if (ui.getFocusHex() != null) {
      List<Unit> civUnits = civs.get(0).getUnits();
      Hex currentHex = hexMap.getHex(ui.getFocusHex());
      for (Unit u : civUnits) {
        if (u.getPosition().equals(currentHex.getPosition()))
          currentUnit = u;
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
      HexCoordinate tempTo = layout.pixelToHex(new Point(toX - scrollX, toY - scrollY));
      boolean canCreatePath =
          (!focusHex.equals(tempTo) && hexMap.getHex(tempTo) != null) && (hexToPath == null)
              || (!hexToPath.equals(tempTo));
      if (canCreatePath) {
        hexToPath = tempTo;
        endHex = hexMap.getHex(hexToPath);
        List<HexCoordinate> pathToFollow = pf.findPath(hexMap.getMap(), focusHex, endHex);
        List<PathHex> finalPath = currentUnit.validUnitMove(pathToFollow);
        ui.setFocusedUnitPath(finalPath);
      }
    }
  }

  private boolean shouldMoveUnit() {
    HexCoordinate fromHex = ui.getFocusHex();
    if (fromHex != null && MouseHandler.pressedMouse) {
      HexCoordinate toHexPlace = layout.pixelToHex(ui.getHexPosFromMouse());
      if (toHexPlace != null && !fromHex.equals(toHexPlace)) {
        List<PathHex> path = ui.getUnitPath();
        if (path != null) {
          return path.stream().anyMatch(i -> i.getPassable() && i.equals(toHexPlace))
              || path.stream().filter(j -> j.equals(toHexPlace)).anyMatch(k -> k.getCanSwitch());
        }
      }
    }
    return false;
  }

}
