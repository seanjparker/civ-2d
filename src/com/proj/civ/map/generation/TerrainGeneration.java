package com.proj.civ.map.generation;

public class TerrainGeneration {
	
	private int width, height;
	private double hexWidth, hexHeight;
	
	private double[] value;
	private Noise n;
	public TerrainGeneration(int width, int height, int hexWidth, int hexHeight) {
		this.value = new double[width * height];
		this.width = width;
		this.height = height;
		this.hexWidth = hexWidth;
		this.hexHeight = hexHeight;
		
		n = new Noise();
	}
	
	public double[] generateElevation() {
		for (int yi = 0; yi < height; yi++) {
			for (int xi = 0; xi < width; xi++) {
				int i = xi + yi * height;
				value[i] = n.eval(xi / hexWidth, yi / hexHeight);
			}
		}		
		return value;
	}
	public double[] generateMoisture() {
		//for (int yi = 0; yi < yS; yi++) {
		//	for (int xi = 0; xi < xS; xi++) {
				//value[xi + yi * yS] = n.moistureNoise(xOff, yOff, OCTAVES, PERSISTANCE, SEED_MOISTURE);
			
		//	}
		//}		
		return value;
	}
}
