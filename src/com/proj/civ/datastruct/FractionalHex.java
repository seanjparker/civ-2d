package com.proj.civ.datastruct;

public class FractionalHex {
	public final double q, r, s;
	
	public FractionalHex(double q, double r, double s) {
		this.q = q;
		this.r = r;
		this.s = s;						
	}
	
	public static Hex hexRound(FractionalHex h) {
		int q = (int) Math.round(h.q);
		int r = (int) Math.round(h.r);
		int s = (int) Math.round(h.s);
		double q_diff = Math.abs(q - h.q);
		double r_diff = Math.abs(r - h.r);
		double s_diff = Math.abs(s - h.s);
		if ((q_diff > r_diff) && (q_diff > s_diff)) {
			q = -r -s;
		} else if (r_diff > s_diff) {
			r = -q -s;
		}
		return new Hex(q, r, -q - r);
	}
	
}
