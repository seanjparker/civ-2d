package com.proj.civ.ai;

import java.util.List;
import java.util.Map;

import com.proj.civ.datastruct.Hex;

/**
 * Neighbors. The sample code I provide in the pathfinding tutorial calls graph.neighbors to get the neighbors of a location. 
 * Use the function in the neighbors section for this. 
 * Filter out the neighbors that are impassable.
 * 
 * Heuristic. The sample code for A* uses a heuristic function that gives a distance between two locations.
 * Use the distance formula, scaled to match the movement costs. 
 * For example if your movement cost is 5 per hex, then multiply the distance by 5.
 */
public class Pathfinding {
	AStar ap;
	
	public Pathfinding() {
		ap = new AStar();
	}
	
	public List<Hex> findPath(Map<Integer, Hex> map, Hex from, Hex to, int widthHex, int heightHex) {
		return ap.aStar(map, from, to, widthHex, heightHex);	
	}
}
