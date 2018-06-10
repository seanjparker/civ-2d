package civ.core.ai;

import java.util.List;
import java.util.Map;
import civ.core.data.hex.Hex;
import civ.core.data.hex.HexCoordinate;

public class Pathfinding {
  AStar ap;

  public Pathfinding() {
    ap = new AStar();
  }

  public List<HexCoordinate> findPath(Map<Integer, Hex> map, Hex from, Hex to) {
    return ap.aStar(map, from, to);
  }
}
