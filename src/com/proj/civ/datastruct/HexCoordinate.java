package com.proj.civ.datastruct;

import java.util.ArrayList;
import java.util.List;

public class HexCoordinate {
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
