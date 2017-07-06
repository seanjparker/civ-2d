package com.proj.civ.datastruct;

import java.util.ArrayList;

public class Hex {
	private final static ArrayList<Hex> directions = new ArrayList<Hex>(){{add(new Hex(1, 0, -1)); add(new Hex(1, -1, 0)); add(new Hex(0, -1, 1)); add(new Hex(-1, 0, 1)); add(new Hex(-1, 1, 0)); add(new Hex(0, 1, -1));}};
	private final static ArrayList<Hex> diagonals = new ArrayList<Hex>(){{add(new Hex(2, -1, -1)); add(new Hex(1, -2, 1)); add(new Hex(-1, -1, 2)); add(new Hex(-2, 1, 1)); add(new Hex(-1, 2, -1)); add(new Hex(1, 1, -2));}};
	public final int q, r, s;
	
	public Hex(int q, int r, int s) {
		this.q = q;
		this.r = r;
		this.s = s;
	}
	
	public Hex(int q, int r) {
		this.q = q;
		this.r = r;
		this.s = -q - r;
	}
	
    public static Hex add(Hex a, Hex b) {
        return new Hex(a.q + b.q, a.r + b.r, a.s + b.s);
    }
    public static Hex subtract(Hex a, Hex b) {
        return new Hex(a.q - b.q, a.r - b.r, a.s - b.s);
    }
    public static Hex scale(Hex a, int k) {
        return new Hex(a.q * k, a.r * k, a.s * k);
    }
    public static int length(Hex hex) {
        return (int)((Math.abs(hex.q) + Math.abs(hex.r) + Math.abs(hex.s)) / 2);
    }
    public static int distance(Hex a, Hex b){
        return Hex.length(Hex.subtract(a, b));
    }

    public static Hex direction(int direction) {
        return Hex.directions.get(direction);
    }

    public static Hex neighbor(Hex hex, int direction) {
        return Hex.add(hex, Hex.direction(direction));
    }

    public static Hex diagonalNeighbor(Hex hex, int direction) {
        return Hex.add(hex, Hex.diagonals.get(direction));
    }
}
