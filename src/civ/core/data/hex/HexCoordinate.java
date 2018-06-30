package civ.core.data.hex;

import java.util.Random;

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

  public boolean isEqual(HexCoordinate b) {
    return (b != null) && (this.q == b.q) && (this.r == b.r) && (this.s == b.s);
  }

  public HexCoordinate add(HexCoordinate b) {
    return new HexCoordinate(q + b.q, r + b.r, s + b.s);
  }

  public HexCoordinate getRandomNeighbour() {
    return add(Hex.DIRECTIONS[rnd.nextInt(NEIGHBOURS - 1)]);
  }

  public HexCoordinate getPosition() {
    return this;
  }

  public String toString() {
    return "q:" + q + ", r:" + r + ", s:" + s;
  }
}
