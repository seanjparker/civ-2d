package com.proj.civ.datastruct;

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
}
