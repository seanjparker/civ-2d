package com.proj.civ.datastruct;

public class PathHex extends HexCoordinate {
	private final boolean canMoveOver;
	
	public PathHex(int q, int r, boolean passable) {
		super(q, r);
		canMoveOver = passable;
	}
	public PathHex(HexCoordinate h, boolean passable) {
		super(h.q, h.r, h.s);
		canMoveOver = passable;
	}
	
	public boolean getPassable() {
		return canMoveOver;
	}
	public boolean equals(Hex h) {
		return (super.q == h.q && super.r == h.r);
	}
}
