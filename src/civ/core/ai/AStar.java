package civ.core.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import civ.core.data.hex.Hex;
import civ.core.data.hex.HexCoordinate;
import civ.core.data.map.HexMap;

public class AStar {

  public List<HexCoordinate> aStar(Map<Integer, Hex> map, HexCoordinate start, HexCoordinate end) {
    if (start == null || end == null)
      return Collections.emptyList();
    
    int size = map.size();
    final List<Hex> openSet = new ArrayList<>(size);
    final Set<Hex> closedSet = new HashSet<>(size);
    final Map<Hex, Hex> cameFrom = new HashMap<>(size);

    final Map<Hex, Integer> gScore = new HashMap<>();
    final Map<Hex, Integer> fScore = new HashMap<>();

    Hex startHex = map.get(HexMap.hash(start));
    Hex endHex = map.get(HexMap.hash(end));

    openSet.add(startHex);
    gScore.put(startHex, 0);

    //For each nodes in the map, we set the current score to infinite
    for (Hex h : map.values())
      fScore.put(h, Integer.MAX_VALUE);
    
    fScore.put(startHex, heuristicCost(startHex, endHex));

    final Comparator<Hex> comparator = (v1, v2) -> {
      if (fScore.get(v1) < fScore.get(v2))
        return -1;
      else if (fScore.get(v1) > fScore.get(v2))
        return 1;
      else
        return 0;
    };

    while (!openSet.isEmpty()) { // Iterate through the open set of hexes
      final Hex current = map.get(HexMap.hash(openSet.get(0)));

      if (current.equals(endHex))
        return rebuildPath(cameFrom, startHex, endHex); // The end of the path is reached, rebuild final path

      openSet.remove(0);
      closedSet.add(current);
      for (int i = 0; i < HexCoordinate.NEIGHBOURS; i++) { // Iterate through all the neighbours of the current hex
        final Hex neighbour = map.get(HexMap.hash(current.neighbor(i)));
        
        if (map.containsValue(neighbour)
            && neighbour.getFeatures().stream().allMatch(x -> x.getPassable())) { // Does the list contain the neighbour

          if (closedSet.contains(neighbour))
            continue;
            
          // Calculate the tentitive cost to move to the next cell
          final int t_GScore = gScore.get(current) + dist(current, neighbour);
          
          // New node is found
          if (!openSet.contains(neighbour))
            openSet.add(neighbour);
          else if (t_GScore >= gScore.get(neighbour)) // This route is worse
            continue;
         
          // New node is found
          if (!openSet.contains(neighbour))
            openSet.add(neighbour);

          // New route is better
          cameFrom.put(neighbour, current);
          gScore.put(neighbour, t_GScore);

          final int estimatedFScore = gScore.get(neighbour) + heuristicCost(neighbour, endHex);
          fScore.put(neighbour, estimatedFScore);

          // Sort the set, based on the defined hex comparator
          Collections.sort(openSet, comparator);
        }
      }
    }
    return Collections.emptyList();
  }

  private int dist(Hex start, Hex end) {
    return start.distance(end);
  }

  private int heuristicCost(Hex start, Hex end) {
    return (int) (start.distance(end) * getMovementCostForHex(start));
  }

  private double getMovementCostForHex(Hex h) {
    return h.getMovementTotal();
  }

  private List<HexCoordinate> rebuildPath(Map<Hex, Hex> cameFrom, Hex start, Hex current) {
    final List<HexCoordinate> totalPath = new ArrayList<>();
    totalPath.add(current.getPosition());
    while (cameFrom.containsKey(current)) {
      current = cameFrom.get(current);
      if (!start.equals(current))
        totalPath.add(current.getPosition());
    }
    return totalPath;
  }
}
