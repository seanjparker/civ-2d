package com.proj.civ.data.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.proj.civ.data.hex.Hex;
import com.proj.civ.data.hex.HexCoordinate;
import com.proj.civ.map.generation.TerrainGeneration;

public class HexMap {
	private final static short HASH_CONSTANT_Q = 0x32B;
	private final static short HASH_CONSTANT_R = 0x21D;
	
	private TerrainGeneration tg;

	private final int MAP_WIDTH;
	private final int MAP_HEIGHT;
	private Map<Integer, Hex> map;
	
	public HexMap(int MAP_WIDTH, int MAP_HEIGHT, int CELL_SIZE) {
		this.MAP_WIDTH = MAP_WIDTH;
		this.MAP_HEIGHT = MAP_HEIGHT;
		
		map = new HashMap<Integer, Hex>(MAP_WIDTH * MAP_HEIGHT);
		
		tg = new TerrainGeneration(MAP_WIDTH, MAP_HEIGHT);
	}
	
	public void populateMap() {
		map = tg.generateMap();
	}
	
	public static <T extends HexCoordinate> int hash(T t) {
		return ((t.q * HASH_CONSTANT_Q) + t.r) * HASH_CONSTANT_R + t.s;
	}
	
	public void setHex(Hex h) {
		map.replace(hash(h), h);
	}
	public void setHex(HexCoordinate hPos, Hex h) {
		map.replace(hash(hPos), h);
	}
	
	public Map<Integer, Hex> getMap() {
		return map;
	}
	
	public int getMapWidth() {
		return this.MAP_WIDTH;
	}
	public int getMapHeight() {
		return this.MAP_HEIGHT;
	}
	
	public <T extends HexCoordinate> Hex getHex(T t) {
		return map.get(hash(t));
	}
	
	public static List<HexCoordinate> getAllInRange(HexCoordinate centre, int range) {
		List<HexCoordinate> results = new ArrayList<HexCoordinate>();
		for (int dx = -range; dx <= range; dx++) {
			for (int dy = Math.max(-range, -dx - range); dy <= Math.min(range, -dx + range); dy++) {
				int dz = -dx - dy;
				results.add(centre.add(new HexCoordinate(dx, dy, dz)));
			}
		}
		return results;
	}	
	public static boolean rangesIntersect(HexCoordinate h1, HexCoordinate h2, int range) {
		List<HexCoordinate> results = new ArrayList<HexCoordinate>();
		int xMin = Math.max(h1.q - range, h2.q - range);
		int xMax = Math.min(h1.q + range, h2.q + range);
		int yMin = Math.max(h1.r - range, h2.r - range);
		int yMax = Math.min(h1.r + range, h2.r + range);
		int zMin = Math.max(h1.s - range, h2.s - range);
		int zMax = Math.min(h1.s + range, h2.s + range);
		
		for (int x = xMin; x <= xMax; x++) {
			for (int y = Math.max(yMin, -x - zMax); y <= Math.min(yMax, -x -zMin); y++) {
				int z = -x - y;
				results.add(new HexCoordinate(x, y, z));
			}
		}
		
		return results.size() > 0;
	}
}
