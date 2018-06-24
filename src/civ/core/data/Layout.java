package civ.core.data;

import civ.core.data.hex.FractionalHex;
import civ.core.data.hex.HexCoordinate;
import civ.core.display.GUI;

public class Layout {
  private static final byte POLYGON_POINTS = 6;
  
  private static Orientation POINTY_TOP = new Orientation(Math.sqrt(3.0), Math.sqrt(3.0) / 2.0, 0.0,
      3.0 / 2.0, Math.sqrt(3.0) / 3.0, -1.0 / 3.0, 0.0, 2.0 / 3.0, 0.5);
  
  private final static Orientation orientation = POINTY_TOP;
  private static Point size;
  private static Point origin;


  public Layout() {
    int hexRadius = GUI.getHexRadius();
    Layout.size = new Point(hexRadius, hexRadius);
    Layout.origin = new Point(hexRadius, hexRadius);
  }

  public static Point hexToPixel(HexCoordinate h) {
    Orientation m = orientation;
    double x = (m.f0 * h.q + m.f1 * h.r) * size.x;
    double y = (m.f2 * h.q + m.f3 * h.r) * size.y;
    return new Point(x + origin.x, y + origin.y);
  }

  public static HexCoordinate pixelToHex(Point p) {
    Orientation m = orientation;
    Point pt = new Point((p.x - origin.x) / size.x, (p.y - origin.y) / size.y);
    double q = m.b0 * pt.x + m.b1 * pt.y;
    double r = m.b2 * pt.x + m.b3 * pt.y;
    return FractionalHex.hexRound(new FractionalHex(q, r, -q - r));
  }

  public static Point hexCornerOffset(int corner) {
    Orientation m = orientation;
    double angle = 2.0 * Math.PI * (m.startAngle - corner) / 6;
    return new Point(size.x * Math.cos(angle), size.y * Math.sin(angle));
  }

  public static Point[] polygonCorners(HexCoordinate h) {
    Point[] corners = new Point[POLYGON_POINTS];
    Point center = hexToPixel(h);
    for (int i = 0; i < POLYGON_POINTS; i++) {
      Point offset = hexCornerOffset(i);
      corners[i] = new Point(center.x + offset.x, center.y + offset.y);
    }
    return corners;
  }

  public static Point getPolygonPositionEstimate(HexCoordinate h) {
    return hexToPixel(h);
  }
}

