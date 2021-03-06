package civ.core.data.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import civ.core.data.hex.Hex;
import civ.core.data.hex.HexCoordinate;
import civ.core.map.generation.TerrainGeneration;

public class HexMap {
  private static final short HASH_CONSTANT_Q = 0x32B;
  private static final short HASH_CONSTANT_R = 0x21D;

  private TerrainGeneration tg;

  private final int mapWidth;
  private final int mapHeight;
  private Map<Integer, Hex> map;

  public HexMap(final int mapWidth, final int mapHeight) {
    this.mapWidth = mapWidth;
    this.mapHeight = mapHeight;

    map = new HashMap<>(mapWidth * mapHeight);

    tg = new TerrainGeneration(mapWidth, mapHeight);
  }

  public void populateMap() {
    map = tg.generateMap();
  }

  public static <T extends HexCoordinate> int hash(T t) {
    return ((t.q * HASH_CONSTANT_Q) + t.r) * HASH_CONSTANT_R;
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
    return this.mapWidth;
  }

  public int getMapHeight() {
    return this.mapHeight;
  }

  public <T extends HexCoordinate> Hex getHex(T t) {
    return map.get(hash(t));
  }

  public static List<HexCoordinate> getAllInRange(HexCoordinate centre, int range) {
    List<HexCoordinate> results = new ArrayList<>();
    for (int dx = -range; dx <= range; dx++) {
      for (int dy = Math.max(-range, -dx - range); dy <= Math.min(range, -dx + range); dy++) {
        int dz = -dx - dy;
        results.add(centre.add(new HexCoordinate(dx, dy, dz)));
      }
    }
    return results;
  }

  public static boolean rangesIntersect(HexCoordinate h1, HexCoordinate h2, int range) {
    List<HexCoordinate> results = new ArrayList<>();
    int xMin = Math.max(h1.q - range, h2.q - range);
    int xMax = Math.min(h1.q + range, h2.q + range);
    int yMin = Math.max(h1.r - range, h2.r - range);
    int yMax = Math.min(h1.r + range, h2.r + range);
    int zMin = Math.max(h1.s - range, h2.s - range);
    int zMax = Math.min(h1.s + range, h2.s + range);

    for (int x = xMin; x <= xMax; x++) {
      for (int y = Math.max(yMin, -x - zMax); y <= Math.min(yMax, -x - zMin); y++) {
        int z = -x - y;
        results.add(new HexCoordinate(x, y, z));
      }
    }

    return !results.isEmpty();
  }
}
