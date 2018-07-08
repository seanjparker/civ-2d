package civ.core.data.hex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import civ.core.data.utils.Pair;
import civ.core.map.improvement.Improvement;
import civ.core.map.terrain.Feature;
import civ.core.map.terrain.Landscape;
import civ.core.map.terrain.YieldType;
import civ.core.unit.Unit;

public class Hex extends HexCoordinate {
  public static final int CIV_UNIT = 0;
  public static final int MIL_UNIT = 1;

  protected static final HexCoordinate[] DIRECTIONS = new HexCoordinate[] {new HexCoordinate(1, 0, -1),
      new HexCoordinate(1, -1, 0), new HexCoordinate(0, -1, 1), new HexCoordinate(-1, 0, 1),
      new HexCoordinate(-1, 1, 0), new HexCoordinate(0, 1, -1),};

  protected static final HexCoordinate[] DIAGONALS = new HexCoordinate[] {new HexCoordinate(2, -1, -1),
      new HexCoordinate(1, -2, 1), new HexCoordinate(-1, -1, 2), new HexCoordinate(-2, 1, 1),
      new HexCoordinate(-1, 2, -1), new HexCoordinate(1, 1, -2),};

  private Improvement tileImprovement = null;
  private Landscape type = null;
  private List<Feature> features = new ArrayList<>();
  private Unit[] hexUnits = new Unit[1 + CIV_UNIT + MIL_UNIT];

  public Hex(int q, int r, int s) {
    super(q, r, s);
  }

  public Hex(Landscape type, int q, int r, int s) {
    super(q, r, s);
    this.type = type;
  }
  
  @Override
  public HexCoordinate getPosition() {
    return new HexCoordinate(q, r, s);
  }
  
  @Override
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
    return (Math.abs(hex.q) + Math.abs(hex.r) + Math.abs(hex.s)) / 2;
  }

  public int distance(Hex b) {
    return length(subtract(b));
  }

  public HexCoordinate direction(int direction) {
    return DIRECTIONS[(6 + (direction % 6)) % 6];
  }

  public Hex neighbor(int direction) {
    return add(direction(direction));
  }

  public Hex diagonalNeighbor(int direction) {
    return add(DIAGONALS[direction]);
  }

  public boolean validFeature(Landscape l, Feature f) {
    // switch (l) {
    // case COAST:
    // return (f == Feature.ICE) || (f == Feature.CLIFFS);
    // case DESERT:
    // return (f == Feature.OASIS) || (f == Feature.FLOODPLAINS);
    // }
    return false;
  }

  public List<Landscape> getValidFeatures() {
    return Collections.emptyList();
  }

  public Landscape getLandscape() {
    return type;
  }
  
  public List<Feature> getFeatures() {
    return features;
  }

  public void setLandscape(Landscape type) {
    this.type = type;
  }

  public Improvement getImprovement() {
    return this.tileImprovement;
  }

  // public Hex setAndGetLandscape(Landscape Type) {
  // this.Type = Type;
  // return this;
  // }
  public void setAllFeatures(List<Feature> feature) {
    this.features.addAll(feature);
  }

  public void addFeature(Feature feature) {
    this.features.add(feature);
  }

  public void removeFeature(Feature feature) {
    if (this.features.contains(feature))
      this.features.remove(feature);
    else
      System.out.println("Cannot remove feature, does not exist");
  }

  public Hex setImprovement(Improvement i) {
    tileImprovement = i;
    return this;
  }

  public int getYieldTotal(YieldType yt) {
    switch (yt) {
      case FOOD:
        return this.type.getFoodYield()
            + this.features.stream().mapToInt(x -> x.getFoodMod()).sum();
      case PRODUCTION:
        return this.type.getProductionYield()
            + this.features.stream().mapToInt(x -> x.getProductionMod()).sum();
      case SCIENCE:
        return this.type.getScienceYield()
            + this.features.stream().mapToInt(x -> x.getScienceMod()).sum();
      case GOLD:
        return this.type.getGoldYield()
            + this.features.stream().mapToInt(x -> x.getGoldMod()).sum();
      default:
        return 0;
    }
  }
  
  <T> T getObject() {
    return null;
  }

  public double getMovementTotal() {
    if (this.getFeatures().isEmpty())
      return this.getFeatures().stream().mapToDouble(x -> x.getMovement()).sum();
    else
      return 1D;
  }

  public boolean canSetMilitary() {
    return hexUnits[MIL_UNIT] == null;
  }

  public boolean canSetCivilian() {
    return hexUnits[CIV_UNIT] == null;
  }

  public void addNewUnit(Unit unit, boolean isMilitary) {
    if (isMilitary && canSetMilitary()) {
      hexUnits[MIL_UNIT] = unit;
    } else if (!isMilitary && canSetCivilian()) {
      hexUnits[CIV_UNIT] = unit;
    }
  }

  public Unit getCivilianUnit() {
    return hexUnits[CIV_UNIT];
  }

  public Unit getMilitaryUnit() {
    return hexUnits[MIL_UNIT];
  }

  public Unit[] getUnits() {
    return hexUnits;
  }

  public void setCivilianUnit(Unit u) {
    if (canSetCivilian())
      this.hexUnits[CIV_UNIT] = u;
  }

  public void setMilitaryUnit(Unit u) {
    if (canSetMilitary())
      this.hexUnits[MIL_UNIT] = u;
  }

  public void resetUnits() {
    this.hexUnits[MIL_UNIT] = null;
    this.hexUnits[CIV_UNIT] = null;
  }

  public void replaceUnit(Unit u, boolean isMilitary) {
    if (isMilitary)
      this.hexUnits[MIL_UNIT] = u;
    else
      this.hexUnits[CIV_UNIT] = u;
  }
  
  public Pair<String, Integer> createFormattedString(int rectH, int yOff) {
    //Create a string builder
    StringBuilder sb = new StringBuilder();
    StringBuilder sbUnits = new StringBuilder();
    StringBuilder sbFeatures = new StringBuilder();
    String landscape = null;
    String improvement = null;
    
    landscape = "Landscape: " + getLandscape().getName() + "\n";

    if (getImprovement() != null) {
      improvement = "Improvement: " + getImprovement().getName() + "\n";
      rectH += yOff;
    }

    if (!features.isEmpty()) {
      sbFeatures.append("Features: \n");
      features.stream().forEach(i -> sbFeatures.append("- " + i.getName() + "\n"));
      rectH += ((features.size() + 1) * yOff);
    }

    for (Unit u : hexUnits) {
      if (u != null && u.getPosition().equals(
          new HexCoordinate(this.q, this.r, this.s))) {
        
        sbUnits.append("(" + u.getOwner().getPluralName() + ") " + u.getName() + " :\n"
            + u.getStrength() + " Strength\n" + u.getMovementPotential() + "/"
            + u.getTotalMovement() + " Movement\n");
        rectH += yOff * 3;
      }
    }

    sb.append(landscape);
    
    if (!features.isEmpty())
      sb.append(sbFeatures.toString());
    if (improvement != null)
      sb.append(improvement);
    if (hexUnits != null)
      sb.append(sbUnits.toString());
    return new Pair<>(sb.toString(), rectH);
  }
}
