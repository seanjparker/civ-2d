package com.proj.civ.datastruct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.proj.civ.map.generation.TerrainGeneration;

public class HexMap {
	
	private TerrainGeneration tg;
	
	private final Random rnd = new Random();
	private final int MAP_WIDTH;
	private final int MAP_HEIGHT;
	private Map<Integer, Hex> map;
	
	public HexMap(int MAP_WIDTH, int MAP_HEIGHT, int CELL_SIZE, Layout l) {
		this.MAP_WIDTH = MAP_WIDTH;
		this.MAP_HEIGHT = MAP_HEIGHT;
		
		map = new HashMap<Integer, Hex>(MAP_WIDTH * MAP_HEIGHT);
		
		tg = new TerrainGeneration(MAP_WIDTH, MAP_HEIGHT);
	}
	
	public void populateMap() {
		map = tg.generateMap();
		
		
		//int xn = 0, yn = 0;
		/*
		for (int r = 0; r < MAP_HEIGHT; r++) {
			int rOff = (r + 1) >> 1;
			for (int q = -rOff; q < MAP_WIDTH - rOff; q++) {
				double e = eNoise[xn + yn * MAP_HEIGHT];
				double m = mNoise[xn + yn * MAP_HEIGHT];
				Hex nextHex = new Hex(q, r);
				if (m <= 0.4) {
					map.put(hash(nextHex), new Cell(Landscape.DESERT));
				} else if (m >= 0.7) {
					map.put(hash(nextHex), new Cell(Landscape.GRASSLAND));
				} else {
					map.put(hash(nextHex), new Cell(Landscape.GRASSLAND));
				}
				xn++;
			}
			yn++;
			xn = 0;
		}
		*/
		//for (int i = 0; i < eNoise.length; i++) {
		//	System.out.print("" + eNoise[i] + ", ");
		//}
		//System.out.println();
		//for (int i = 0; i < mNoise.length; i++) {
		//	System.out.print("" + mNoise[i] + ", ");
		//}
		/*
		for (int r = 0; r < this.MAP_HEIGHT; r++) {
			int rOff = (r + 1) >> 1;
			for (int q = -rOff; q < this.MAP_WIDTH - rOff; q++) {
				Hex nextHex = new Hex(q, r, -q - r);

				switch (rnd.nextInt(7)) {
				case 0:
					map.put(hash(nextHex), new Hex(Landscape.COAST, nextHex.q, nextHex.r, nextHex.s));
					break;
				case 1:
					map.put(hash(nextHex), new Hex(Landscape.DESERT, nextHex.q, nextHex.r, nextHex.s));
					break;
				case 2:
					map.put(hash(nextHex), new Hex(Landscape.GRASSLAND, nextHex.q, nextHex.r, nextHex.s));
					break;
				case 3:
					map.put(hash(nextHex), new Hex(Landscape.LAKE, nextHex.q, nextHex.r, nextHex.s));
					break;
				case 4:
					map.put(hash(nextHex), new Hex(Landscape.OCEAN, nextHex.q, nextHex.r, nextHex.s));
					break;
				case 5:
					map.put(hash(nextHex), new Hex(Landscape.PLAINS, nextHex.q, nextHex.r, nextHex.s));
					break;
				case 6:
					map.put(hash(nextHex), new Hex(Landscape.TUNDRA, nextHex.q, nextHex.r, nextHex.s));
					break;
				case 7:
					map.put(hash(nextHex), new Hex(Landscape.SNOW, nextHex.q, nextHex.r, nextHex.s));
					break;
				}
			}
		}
		*/
	}
	
	public static int hash(Hex h) {
		int hq = h.q;
		int hr = h.r;
		return hq ^ (hr + 0x9e3779b9 + (hq << 6) + (hq >> 2));
	}
	public static int hash(HexCoordinate h) {
		int hq = h.q;
		int hr = h.r;
		return hq ^ (hr + 0x9e3779b9 + (hq << 6) + (hq >> 2));
	}
	public static int hash(PathHex h) {
		int hq = h.q;
		int hr = h.r;
		return hq ^ (hr + 0x9e3779b9 + (hq << 6) + (hq >> 2));
	}
	
	public void setCell(Hex h, Hex newH) {
		map.replace(hash(h), newH);
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
