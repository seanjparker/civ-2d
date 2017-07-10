package com.proj.civ.map.generation;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.proj.civ.datastruct.Hex;
import com.proj.civ.datastruct.HexMap;
import com.proj.civ.map.Cell;
import com.proj.civ.map.terrain.Feature;
import com.proj.civ.map.terrain.Landscape;

public class TerrainGeneration {
	private final int OCTAVES = 8;
	private final double FEATURE_SIZE = 100.0;
	private final Random rnd;
	
	private int width, height;
	private int hexWidth, hexHeight;
	
	private Noise elevation;
	private Noise temperature;
	
	public TerrainGeneration(int hexWidth, int hexHeight) {
		rnd = new Random();
		
		this.hexWidth = hexWidth;
		this.hexHeight = hexHeight;
		this.width = this.hexWidth;
		this.height = this.hexHeight;
		
		elevation = new Noise(rnd.nextLong());
		temperature = new Noise(rnd.nextLong());
	}
	
	public Map<Integer, Cell> generateMap() {
		Map<Integer, Cell> map = new HashMap<Integer, Cell>();
		double[][] eHMap = new double[hexWidth][hexHeight];
		double[][] eTMap = new double[hexWidth][hexHeight];
		double[] e = generateElevation();
		double[] t = generateTemperature();
		double eT = 0;
		double tT = 0;
		for (int i = 0; i < e.length; i++) { //Calculate an avergae noise value for a section of the map
			//eT = e[i];
			//tT = t[i];
			int x = i % hexWidth;
			int y = i / hexWidth;
			eHMap[x][y] = e[i];
			eTMap[x][y] = t[i];
			
			//if (i % 400 == 0) {
			//	int x = ((i / 400) % hexWidth);
			//	int y = ((i / 400) / hexWidth);
			//	eHMap[x][y] = eTotal;
			//	eTMap[x][y] = tTotal;
			//	eTotal = 0;
			//	tTotal = 0;
			//}
		}
		
		for (int r = 0, y = 0; r < this.hexHeight; r++, y++) { //y
			int rOff = (r + 1) >> 1;
			for (int q = -rOff, x = 0; q < this.hexWidth - rOff; q++, x++) { //x
				Hex nextHex = new Hex(q, r);
				Cell nextCell = generateCell(eHMap[x][y], eTMap[x][y]);
				int hexHash = HexMap.hash(nextHex);
				map.put(hexHash, nextCell);
			}
		}
		return map;
	}
	
	private Cell generateCell(double e, double t) {
		//System.out.println("Elevation:" + e + ", Temperature:" + t);
		Cell c = null;
		if (e < 0.0005) { //Water
			if (t < 0.05) { //Ice
				c = new Cell(Landscape.LAKE);
				c.addFeature(Feature.ICE);
			} else { //Lake
				c = new Cell(Landscape.LAKE);
			}
		} else if (e < 0.001) { //Coast
			c = new Cell(Landscape.COAST);
		} else { //Land
			if (e > 0.80) {
				if (t < 0.8) { //Tundra
					c = new Cell(Landscape.TUNDRA);
				} else if (t < 0.5) { //Snow + Mountain
					c = new Cell(Landscape.SNOW);
					c.addFeature(Feature.MOUNTAINS);
				} else { //Snow
					c = new Cell(Landscape.SNOW);
				}
			} else if (e > 0.50) {
				if (t < 0.3) { //Plains
					c = new Cell(Landscape.PLAINS);
				} else if (t < 0.6) { //Woods + grassland
					c = new Cell(Landscape.GRASSLAND);
					c.addFeature(Feature.WOODS);
				} else if (t < 0.8) { //Rainforest + grassland
					c = new Cell(Landscape.GRASSLAND);
					c.addFeature(Feature.RAINFOREST);
				} else {
					c = new Cell(Landscape.TUNDRA);
				}
			} else if (e > 0.01) { //Grassland
				c = new Cell(Landscape.GRASSLAND);
				if (t < 0.3) { 
					c.addFeature(Feature.MARSH); //Grassland + marsh
				} else if (t < 0.5) { //Grassland + woods
					c.addFeature(Feature.WOODS);
				} else { //Grassland + rainforest
					c.addFeature(Feature.RAINFOREST);
				}
			} else { // Desert
				c = new Cell(Landscape.DESERT);
			}
		}
		c.addFeature(Feature.CLIFFS); //Temp
		c.addFeature(Feature.WOODS); //Temp
		return c;
	}
	
	private double[] generateElevation() {
		double[] e = new double[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				double nx = x / FEATURE_SIZE;
				double ny = y / FEATURE_SIZE;
				double e1 = elevation.noise1(nx, ny, width / FEATURE_SIZE, height / FEATURE_SIZE, OCTAVES, true);
				int i = (x + y * width);
				e[i] = e1;			
			}
		}
		return e;
	}
	private double[] generateTemperature() {
		double[] t = new double[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				double nx = x / FEATURE_SIZE;
				double ny = y / FEATURE_SIZE;
				double t1 = temperature.noise2(nx, ny, OCTAVES);
				int i = (x + y * width);
				t[i] = t1;			
			}
		}
		return t;
	}
}
