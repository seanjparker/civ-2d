package civ.core.instance;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import civ.core.data.Layout;
import civ.core.data.Point;
import civ.core.data.map.HexMap;
import civ.core.display.GUI;
import civ.core.map.civilization.BaseCivilization;
import civ.core.unit.Unit;

public class IData {
  public static final int WINDOW_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width * 3 / 4;
  public static final int WINDOW_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height * 3 / 4;
  public static final int HEX_RADIUS = ((WINDOW_WIDTH >> 4) + (WINDOW_HEIGHT >> 4)) >> 1;
  public static final int W_HEXES = 40;
  public static final int H_HEXES = 25;
  public static final int TEXT_SIZE = HEX_RADIUS >> 2; // Should be 16

  public static final HexMap hexMap = new HexMap(W_HEXES, H_HEXES, HEX_RADIUS);
  public static final Layout layout = new Layout(Layout.POINTY_TOP,
      new Point(HEX_RADIUS, HEX_RADIUS), new Point(HEX_RADIUS, HEX_RADIUS));
  public static final GUI ui = new GUI();

  public static List<BaseCivilization> civs = new ArrayList<BaseCivilization>();

  public static Unit currentUnit = null;

  public static int turnCounter = 0;
  public static boolean nextTurnInProgress = false;

}
