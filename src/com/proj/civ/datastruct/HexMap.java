package com.proj.civ.datastruct;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.proj.civ.map.Cell;
import com.proj.civ.map.generation.TerrainGeneration;
import com.proj.civ.map.terrain.Landscape;

public class HexMap {
	private TerrainGeneration tg;
	
	private final Random rnd = new Random();
	
	private final int MAP_WIDTH;
	private final int MAP_HEIGHT;
	private Map<Integer, Cell> map = new HashMap<Integer, Cell>();
	
	public HexMap(int MAP_WIDTH, int MAP_HEIGHT, int CELL_SIZE, Layout l) {
		this.MAP_WIDTH = MAP_WIDTH;
		this.MAP_HEIGHT = MAP_HEIGHT;
		
		//tg = new TerrainGeneration(MAP_WIDTH, MAP_HEIGHT);
		
		populateMap();
	}
	
	private void populateMap() {
		int xn = 0, yn = 0;
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
		
		for (int r = 0; r < this.MAP_HEIGHT; r++) {
			int rOff = (r + 1) >> 1;
			for (int q = -rOff; q < this.MAP_WIDTH - rOff; q++) {
				Hex nextHex = new Hex(q, r);
				
				switch (rnd.nextInt(7)) {
				case 0:
					map.put(hash(nextHex), new Cell(Landscape.COAST));
					break;
				case 1:
					map.put(hash(nextHex), new Cell(Landscape.DESERT));
					break;
				case 2:
					map.put(hash(nextHex), new Cell(Landscape.GRASSLAND));
					break;
				case 3:
					map.put(hash(nextHex), new Cell(Landscape.LAKE));
					break;
				case 4:
					map.put(hash(nextHex), new Cell(Landscape.OCEAN));
					break;
				case 5:
					map.put(hash(nextHex), new Cell(Landscape.PLAINS));
					break;
				case 6:
					map.put(hash(nextHex), new Cell(Landscape.TUNDRA));
					break;
				case 7:
					map.put(hash(nextHex), new Cell(Landscape.SNOW));
					break;
				}
			}
		}
	}
	
	public static int hash(Hex h) {
		int hq = h.q;
		int hr = h.r;
		return hq ^ (hr + 0x9e3779b9 + (hq << 6) + (hq >> 2));
	}
	
	public void setCell(Hex h, Cell newC) {
		map.replace(hash(h), newC);
	}
	
	public Map<Integer, Cell> getMap() {
		return map;
	}
	
	public int getMapWidth() {
		return this.MAP_WIDTH;
	}
	public int getMapHeight() {
		return this.MAP_HEIGHT;
	}
}
