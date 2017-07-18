package com.proj.civ.datastruct;

import java.util.ArrayList;
import java.util.List;

import com.proj.civ.map.improvement.Farm;
import com.proj.civ.map.improvement.Improvement;
import com.proj.civ.map.terrain.Feature;
import com.proj.civ.map.terrain.Landscape;
import com.proj.civ.map.terrain.YieldType;

public class Hex extends HexCoordinate {
	
	public static final List<HexCoordinate> directions = new ArrayList<HexCoordinate>() {{
	add(new HexCoordinate(1, 0, -1));
	add(new HexCoordinate(1, -1, 0));
	add(new HexCoordinate(0, -1, 1));
	add(new HexCoordinate(-1, 0, 1));
	add(new HexCoordinate(-1, 1, 0));
	add(new HexCoordinate(0, 1, -1));
	}};
	
	private final List<HexCoordinate> diagonals = new ArrayList<HexCoordinate>() {{
	add(new HexCoordinate(2, -1, -1));
	add(new HexCoordinate(1, -2, 1));
	add(new HexCoordinate(-1, -1, 2));
	add(new HexCoordinate(-2, 1, 1));
	add(new HexCoordinate(-1, 2, -1));
	add(new HexCoordinate(1, 1, -2));
	}};
	
    private Improvement TileImprovement = null;
	private Landscape Type = null;
	private List<Feature> Features = new ArrayList<Feature>();
	
	public Hex(int q, int r, int s) {
		super(q, r, s);
	}
	
	public Hex(Landscape Type, int q, int r, int s) {
		super(q, r, s);
		this.Type = Type;
	}
	
	public boolean equals(Hex b) {
		return (b != null) && (this.q == b.q) && (this.r == b.r) && (this.s == b.s);
	}
	
    public Hex add(HexCoordinate b) {
        return new Hex(q + b.q, r + b.r, s + b.s);
    }
    private Hex subtract(HexCoordinate b) {
        return new Hex(q - b.q, r - b.r, s - b.s);
    }
    public Hex scale(int k) {
        return new Hex(q * k, r * k, s * k);
    }
    public int length(Hex hex) {
        return (int)((Math.abs(hex.q) + Math.abs(hex.r) + Math.abs(hex.s)) / 2);
    }
    public int distance(Hex b){
        return length(subtract(b));
    }

    public HexCoordinate direction(int direction) {
      return directions.get(direction);
    }

    public Hex neighbor(int direction) {
        return add(direction(direction));
    }

    public Hex diagonalNeighbor(int direction) {
        return add(diagonals.get(direction));
    }
	
	public boolean validFeature(Landscape l, Feature f) {
		switch (l) {
			case COAST:
				return (f == Feature.ICE) || (f == Feature.CLIFFS);
			case DESERT:
				return (f == Feature.OASIS) || (f == Feature.FLOODPLAINS);
		}
		return false;
	}
	
	public List<Landscape> getValidFeatures() {
		return null;
	}
	
	public Landscape getLandscape() {
		return Type;
	}
	public List<Feature> getFeatures() {
		return Features;
	}
	public void setLandscape(Landscape Type) {
		this.Type = Type;
	}
	public Improvement getImprovement() {
		return this.TileImprovement;
	}
	//public Hex setAndGetLandscape(Landscape Type) {
	//	this.Type = Type;
	//	return this;
	//}
	public void setAllFeatures(List<Feature> Feature) {
		this.Features.addAll(Feature);
	}
	public void addFeature(Feature Feature) {
		this.Features.add(Feature);
	}
	public void removeFeature(Feature Feature) {
		if (this.Features.contains(Feature)) {
			this.Features.remove(Feature);			
		} else {
			System.out.println("Cannot remove feature, does not exist");
		}
	}
	
	public Hex setImprovement(Improvement i) {
		TileImprovement = i;
		//System.out.println(TileImprovement.toString());
		return this;
	}
	
	public int getYieldTotal(YieldType yt) {
		switch (yt) {
			case FOOD:
				return this.Type.getFoodYield() + this.Features.stream().mapToInt(x -> x.getFoodMod()).sum();
			case PRODUCTION:
				return this.Type.getProductionYield() + this.Features.stream().mapToInt(x -> x.getProductionMod()).sum();
			case SCIENCE:
				return this.Type.getScienceYield() + this.Features.stream().mapToInt(x -> x.getScienceMod()).sum();
			case GOLD:
				return this.Type.getGoldYield() + this.Features.stream().mapToInt(x -> x.getGoldMod()).sum();
			default:
				return 0;
		}
	}
	
	public double getMovementTotal() {
		return this.getFeatures().stream().mapToDouble(x -> x.getMovement()).sum();
	}
	
}
