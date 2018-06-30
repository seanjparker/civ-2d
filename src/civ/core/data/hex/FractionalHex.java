package civ.core.data.hex;

public class FractionalHex {
  public final double q;
  public final double r;
  public final double s;

  public FractionalHex(double q, double r, double s) {
    this.q = q;
    this.r = r;
    this.s = s;
  }

  public static HexCoordinate hexRound(FractionalHex h) {
    int q = (int) Math.round(h.q);
    int r = (int) Math.round(h.r);
    int s = (int) Math.round(h.s);
    double qDiff = Math.abs(q - h.q);
    double rDiff = Math.abs(r - h.r);
    double sDiff = Math.abs(s - h.s);
    if ((qDiff > rDiff) && (qDiff > sDiff)) {
      q = -r - s;
    } else if (rDiff > sDiff) {
      r = -q - s;
    }
    return new HexCoordinate(q, r, -q - r);
  }

}
