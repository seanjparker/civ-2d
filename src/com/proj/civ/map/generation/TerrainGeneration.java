package com.proj.civ.map.generation;

import java.util.Random;

public class TerrainGeneration {
	private final double FEATURE_SIZE = 200.0;
	private final Random rnd;
	
	private double[] e, t;
	
	private int width, height;
	private int hexWidth, hexHeight;
	
	private Noise elevation;
	private Noise humidity;
	
	public TerrainGeneration(int hexWidth, int hexHeight) {
		rnd = new Random();
		this.hexWidth = hexWidth;
		this.hexHeight = hexHeight;
		this.width = this.hexWidth * 20;
		this.height = this.hexHeight * 20;
		
		e = new double[width * height];
		t = new double[width * height];
		
		
		elevation = new Noise(rnd.nextLong());
		humidity = new Noise(rnd.nextLong());
	}
	
	private double[] generateElevation() {		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				double nx = x / FEATURE_SIZE;
				double ny = y / FEATURE_SIZE;
				double e1 = elevation.noise1(nx, ny, 8);
				int i = (x + y * width);
				e[i] = e1;			
			}
		}
		return e;
	}
	private double[] generateHumidity() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				double nx = x / FEATURE_SIZE;
				double ny = y / FEATURE_SIZE;
				double t1 = humidity.noise2(nx, ny, 8);
				int i = (x + y * width);
				t[i] = t1;			
			}
		}
		return t;
	}
}
