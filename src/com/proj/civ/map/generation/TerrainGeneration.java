package com.proj.civ.map.generation;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import com.proj.civ.data.hex.Hex;
import com.proj.civ.data.map.HexMap;
import com.proj.civ.map.terrain.Feature;
import com.proj.civ.map.terrain.Landscape;

public class TerrainGeneration {
  private final int OCTAVES = 2;
  private final double FEATURE_SIZE = 15.0;
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

  public Map<Integer, Hex> generateMap() {
    Map<Integer, Hex> map = new HashMap<Integer, Hex>();
    double[][] eHMap = new double[hexWidth][hexHeight];
    double[][] eTMap = new double[hexWidth][hexHeight];
    double[] e = generateElevation();
    double[] t = generateTemperature();

    for (int i = 0; i < e.length; i++) { // Calculate an average noise value for a section of the
                                         // map
      int x = i % hexWidth;
      int y = i / hexWidth;
      eHMap[x][y] = e[i];
      eTMap[x][y] = t[i];

    }


    // Decide the terrain type + features for the hex
    for (int r = 0, y = 0; r < this.hexHeight; r++, y++) { // y
      int rOff = (r + 1) >> 1;
      for (int q = -rOff, x = 0; q < this.hexWidth - rOff; q++, x++) { // x
        Hex n = new Hex(q, r, -q - r);
        Hex nextHex = generateHexTerrain(eHMap[x][y], eTMap[x][y], q, r, -q - r);
        map.put(HexMap.hash(n), nextHex);
      }
    }

    return map;
  }

  private Hex generateHexTerrain(double e, double t, int q, int r, int s) {
    // System.out.println("Elevation:" + e + ", Temperature:" + t);
    Hex h = null;
    if (e < 0.0005) { // Water
      if (t < 0.05) { // Ice
        h = new Hex(Landscape.LAKE, q, r, s);
        h.addFeature(Feature.ICE);
      } else { // Lake
        h = new Hex(Landscape.LAKE, q, r, s);
      }
    } else if (e < 0.001) { // Coast
      h = new Hex(Landscape.COAST, q, r, s);
    } else { // Land
      if (e > 0.80) {
        if (t < 0.8) { // Tundra
          h = new Hex(Landscape.TUNDRA, q, r, s);
        } else if (t < 0.5) { // Snow + Mountain
          h = new Hex(Landscape.SNOW, q, r, s);
          h.addFeature(Feature.MOUNTAINS);
        } else { // Snow
          h = new Hex(Landscape.SNOW, q, r, s);
        }
      } else if (e > 0.50) {
        if (t < 0.3) { // Plains
          h = new Hex(Landscape.PLAINS, q, r, s);
        } else if (t < 0.6) { // Woods + grassland
          h = new Hex(Landscape.GRASSLAND, q, r, s);
          h.addFeature(Feature.WOODS);
        } else if (t < 0.8) { // Rainforest + grassland
          h = new Hex(Landscape.GRASSLAND, q, r, s);
          h.addFeature(Feature.RAINFOREST);
        } else {
          h = new Hex(Landscape.TUNDRA, q, r, s);
        }
      } else if (e > 0.01) { // Grassland
        h = new Hex(Landscape.GRASSLAND, q, r, s);
        if (t < 0.3) {
          h.addFeature(Feature.MARSH); // Grassland + marsh
        } else if (t < 0.5) { // Grassland + woods
          h.addFeature(Feature.WOODS);
        } else { // Grassland + rainforest
          h.addFeature(Feature.RAINFOREST);
        }
      } else { // Desert
        h = new Hex(Landscape.DESERT, q, r, s);
      }
    }
    return h;
  }

  private double[] generateElevation() {
    double[] e = new double[width * height];
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        double nx = x / FEATURE_SIZE;
        double ny = y / FEATURE_SIZE;
        double e1 =
            elevation.noise1(nx, ny, width / FEATURE_SIZE, height / FEATURE_SIZE, OCTAVES, true);
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
