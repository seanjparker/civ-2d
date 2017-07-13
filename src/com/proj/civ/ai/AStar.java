package com.proj.civ.ai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.proj.civ.datastruct.Hex;
import com.proj.civ.datastruct.HexMap;
import com.proj.civ.map.terrain.Feature;

public class AStar {
	
	public List<Hex> aStar(Map<Integer, Hex> map, Hex start, Hex end) {
		int size = map.size();
		final List<Hex> openSet = new ArrayList<Hex>(size);
		final Set<Hex> closedSet = new HashSet<Hex>(size);
		final Map<Hex, Hex> cameFrom = new HashMap<Hex, Hex>(size);
		
		final Map<Hex, Integer> gScore = new HashMap<Hex, Integer>();
		final Map<Hex, Integer> fScore = new HashMap<Hex, Integer>();
		
		start = map.get(HexMap.hash(start));
		end = map.get(HexMap.hash(end));
		
		openSet.add(start);
		gScore.put(start, 0);
		
		for (Hex h : map.values()) {
			fScore.put(h, Integer.MAX_VALUE);
		}
		fScore.put(start, heuristicCost(start, end));
		
		final Comparator<Hex> comparator = new Comparator<Hex>() { //Create a new hex comparator
			public int compare(Hex v1, Hex v2) {
				return fScore.get(v1) < fScore.get(v2) ? -1 : fScore.get(v1) > fScore.get(v2) ? 1 : 0;
			}
		};
		
		while (!openSet.isEmpty()) { //Iterate through the open set of hexes
			final Hex current = map.get(HexMap.hash(openSet.get(0)));
			
			if (current.equals(end)) {
				return rebuildPath(cameFrom, end); //The end of the path is reached, rebuild final path
			}

			openSet.remove(0);
			closedSet.add(current);
			for (int i = 0; i < 6; i++) { //Iterate through all the neighbours of the current hex
				final Hex neighbour = map.get(HexMap.hash(current.neighbor(i)));
				if (map.containsValue(neighbour)) { //Does the list contain the neighbour
					
					if (closedSet.contains(neighbour)) {
						continue;
					}
					final int t_GScore = gScore.get(current) + dist(current, neighbour); //Calculate the tentitive cost to move to the next cell
					
					if (!openSet.contains(neighbour)) { //New node is found
						openSet.add(neighbour);
					} else if (t_GScore >= gScore.get(neighbour)) { //This route is worse + not working -- null exception
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
		}
		return null;
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
