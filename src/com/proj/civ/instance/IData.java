package com.proj.civ.instance;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import com.proj.civ.data.Layout;
import com.proj.civ.data.Point;
import com.proj.civ.data.map.HexMap;
import com.proj.civ.display.GUI;
import com.proj.civ.map.civilization.BaseCivilization;
import com.proj.civ.unit.Unit;

public class IData {
	public static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width * 3 / 4;
	public static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height * 3 / 4;
	protected static final int HEX_RADIUS = ((WIDTH >> 4) + (HEIGHT >> 4)) >> 1;
	protected static final int W_HEXES = 40;
	protected static final int H_HEXES = 25;
	protected static final int TEXT_SIZE = HEX_RADIUS >> 2; //Should be 16
	
	protected static final HexMap hexMap = new HexMap(W_HEXES, H_HEXES, HEX_RADIUS);
	protected static final Layout layout = new Layout(Layout.POINTY_TOP, new Point(HEX_RADIUS, HEX_RADIUS), new Point(HEX_RADIUS, HEX_RADIUS));
	protected static final GUI ui = new GUI();
	
	protected static List<BaseCivilization> civs = new ArrayList<BaseCivilization>();

	protected static Unit currentUnit = null;
	
	protected static int turnCounter = 0;
	public static boolean nextTurnInProgress = false;
	
}
