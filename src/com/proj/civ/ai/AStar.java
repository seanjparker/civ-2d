package com.proj.civ.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.proj.civ.datastruct.Hex;
import com.proj.civ.datastruct.HexCoordinate;

public class AStar {
	
	public List<Hex> aStar(Map<Integer, Hex> map, Hex start, Hex end, int wH, int hH) {
		int size = map.size();
		final List<Hex> mapAsHex = new ArrayList<Hex>(size);
		
		final List<Hex> openSet = new ArrayList<Hex>(size);
		final Set<Hex> closedSet = new HashSet<Hex>(size);
		final Map<Hex, Hex> cameFrom = new HashMap<Hex, Hex>(size);
		
		final Map<Hex, Integer> gScore = new HashMap<Hex, Integer>();
		final Map<Hex, Integer> fScore = new HashMap<Hex, Integer>();
		
		openSet.add(start);
		gScore.put(start, 0);
		
		for (Hex h : mapAsHex) {
			fScore.put(h, Integer.MAX_VALUE);
		}
		fScore.put(start, heuristicCost(start, end));
		
		final Comparator<Hex> comparator = new Comparator<Hex>() { //Create a new hex comparator
			public int compare(Hex v1, Hex v2) {
				return fScore.get(v1) < fScore.get(v2) ? -1 : fScore.get(v1) > fScore.get(v2) ? 1 : 0;
			}
		};
		
		while (!openSet.isEmpty()) { //Iterate through the open set of hexes
			final Hex current = openSet.get(0);
			
			if (current.equals(end)) {
				return rebuildPath(cameFrom, end); //The end of the path is reached, rebuild final path
			}

			openSet.remove(0);
			closedSet.add(current);
			for (int i = 0; i < 6; i++) { //Iterate through all the neighbours of the current hex
				final Hex neighbour = current.neighbor(i);

					if (closedSet.contains(neighbour)) continue;
					final int t_GScore = gScore.get(current) + dist(current, neighbour); //Calculate the tentitive cost to move to the next cell
					
					if (!openSet.contains(neighbour)) { //New node is found
						openSet.add(neighbour);
					} else if (t_GScore >= gScore.get(neighbour)) { //This route is worse
						continue;
					}
					//New route is better
					cameFrom.put(neighbour, current);
					gScore.put(neighbour, t_GScore);
					
					final int estimatedFScore = gScore.get(neighbour) + heuristicCost(neighbour, end);
					fScore.put(neighbour, estimatedFScore);
					
					//Sort the set, based on the defined comparator
					Collections.sort(openSet, comparator);
				}
			}
		//}
		return null;
	}
	
	private int dist(Hex start, Hex end) {
		return start.distance(end);
	}
	
	private int heuristicCost(Hex start, Hex end) {
		int n = 1;
		return start.distance(end) * n; //Where n is the movement cost per hex
	}
	
	private List<Hex> rebuildPath(Map<Hex, Hex> cameFrom, Hex current) {
		final List<Hex> totalPath = new ArrayList<Hex>();
		totalPath.add(current);
		while (current != null && cameFrom.containsKey(current)) {
			current = cameFrom.get(current);
			totalPath.add(current);
		}
		return totalPath;
	}
}
