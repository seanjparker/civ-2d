package com.proj.civ.datastruct.hex;

import java.util.Random;

public class HexCoordinate {
	public static final int NEIGHBOURS = 6;
	
	public final int q, r, s;
	
	public HexCoordinate(int q, int r, int s) {
		this.q = q;
		this.r = r;
		this.s = s;
	}
	public HexCoordinate(int q, int r) {
		this.q = q;
		this.r = r;
		this.s = -q - r;
	}
	
	public boolean isEqual(HexCoordinate b) {
		return (b != null) && (this.q == b.q) && (this.r == b.r) && (this.s == b.s);
	}
	
	public HexCoordinate add(HexCoordinate b) {
		return new HexCoordinate(q + b.q, r + b.r, s + b.s);
	}
	
	public HexCoordinate getRandomNeighbour() {
		Random r = new Random();
		return add(Hex.DIRECTIONS[r.nextInt(NEIGHBOURS - 1)]);
	}
	
	public HexCoordinate getPosition() {
		return this;
	}
	
	public String toString() {
		return "q:" + q + ", r:" + r + ", s:" + s;
	}
}
