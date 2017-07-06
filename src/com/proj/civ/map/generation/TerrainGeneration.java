package com.proj.civ.map.generation;

import java.util.Random;

public class TerrainGeneration {
	private final double PERSISTANCE = 0.3;
	private final int OCTAVES = 2;
	private final double PEAK_ELEVATION = 3.35;
	private final double SEED_ELEVATION, SEED_MOISTURE;
	private final double INCREMENT = 0.003;
	
	private double xOff, yOff;
	private int xS, yS;
	private double[] value;
	private Noise n;
	public TerrainGeneration(int widthCell, int heightCell) {
		this.xS = widthCell;
		this.yS = heightCell;
		this.value = new double[xS * yS * 6];
		this.SEED_ELEVATION = generateSeed();
		this.SEED_MOISTURE = generateSeed();
		n = new Noise();
	}
	
	public double[] generateElevation() {
		xOff = 0;
		yOff = 0;
		for (int yi = 0; yi < yS; yi++) {
			xOff = 0;
			for (int xi = 0; xi < xS; xi++) {
				int i = xi + yi * yS;
				value[i] = n.elevationNoise(xOff, yOff, OCTAVES, PERSISTANCE, SEED_ELEVATION, PEAK_ELEVATION);
				xOff += INCREMENT;
			}
			yOff += INCREMENT;
		}		
		return value;
	}
	public double[] generateMoisture() {
		xOff = 0;
		yOff = 0;
		for (int yi = 0; yi < yS; yi++) {
			for (int xi = 0; xi < xS; xi++) {
				value[xi + yi * yS] = n.moistureNoise(xOff, yOff, OCTAVES, PERSISTANCE, SEED_MOISTURE);
			
				xOff += INCREMENT;
			}
			yOff += INCREMENT;
		}		
		return value;
	}
	
	private double generateSeed() {
		Random rnd = new Random();
		return rnd.nextDouble();
	}
}
