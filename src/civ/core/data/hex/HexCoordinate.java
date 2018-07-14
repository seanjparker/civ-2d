package civ.core.data.hex;

import static civ.core.instance.IData.hexMap;
import java.util.Random;
import civ.core.data.map.HexMap;

public class HexCoordinate {
  public static final int NEIGHBOURS = 6;

  public final int q;
  public final int r;
  public final int s;

  public final Random rnd;
  
  public HexCoordinate(int q, int r, int s) {
    this.q = q;
    this.r = r;
    this.s = s;
    
    rnd = new Random();
  }

  public HexCoordinate(int q, int r) {
    this(q, r, -q - r);
  }
  
  @Override
  public boolean equals(Object b) {
    if (b == null)
      return false;
    
    HexCoordinate h = (HexCoordinate) b;
    return this.q == h.q && this.r == h.r;
  }
  
  @Override
  public int hashCode() {
    return HexMap.hash(this);
  }

  public HexCoordinate add(HexCoordinate b) {
    return new HexCoordinate(q + b.q, r + b.r, s + b.s);
  }

  //This can get stuck in an infinte loop but we can deal with it later
  public HexCoordinate getValidRandomNeighbour(boolean isMilitary) {
    HexCoordinate newHex;
    do {
      newHex = add(Hex.DIRECTIONS[rnd.nextInt(NEIGHBOURS - 1)]);
    } while (hexMap.getHex(newHex) == null
        || ((isMilitary && !hexMap.getHex(newHex).canSetMilitary())
            || (!isMilitary && !hexMap.getHex(newHex).canSetCivilian())));
    return newHex;
  }

  public HexCoordinate getPosition() {
    return this;
  }

  public String toString() {
    return "q:" + q + ", r:" + r + ", s:" + s;
  }
}
