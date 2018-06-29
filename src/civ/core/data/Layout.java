package civ.core.data;

import civ.core.data.hex.FractionalHex;
import civ.core.data.hex.HexCoordinate;

public class Layout {
  private final byte POLYGON_POINTS = 6;
  public final Orientation orientation;
  public final Point size;
  public final Point origin;

  public Layout(Orientation orientation, Point size, Point origin) {
    this.orientation = orientation;
    this.size = size;
    this.origin = origin;
  }

  public static Orientation POINTY_TOP = new Orientation(Math.sqrt(3.0), Math.sqrt(3.0) / 2.0, 0.0,
      3.0 / 2.0, Math.sqrt(3.0) / 3.0, -1.0 / 3.0, 0.0, 2.0 / 3.0, 0.5);

  public Point hexToPixel(HexCoordinate h) {
    double x = (orientation.f0 * h.q + orientation.f1 * h.r) * size.x;
    double y = (orientation.f2 * h.q + orientation.f3 * h.r) * size.y;
    return new Point(x + origin.x, y + origin.y);
  }

  public HexCoordinate pixelToHex(Point p) {
    Point pt = new Point((p.x - origin.x) / size.x, (p.y - origin.y) / size.y);
    double q = orientation.b0 * pt.x + orientation.b1 * pt.y;
    double r = orientation.b2 * pt.x + orientation.b3 * pt.y;
    return FractionalHex.hexRound(new FractionalHex(q, r, -q - r));
  }

  public Point hexCornerOffset(int corner) {
    double angle = 2.0 * Math.PI * (orientation.startAngle - corner) / 6;
    return new Point(size.x * Math.cos(angle), size.y * Math.sin(angle));
  }

  public Point[] polygonCorners(HexCoordinate h) {
    Point[] corners = new Point[POLYGON_POINTS];
    Point center = hexToPixel(h);
    for (int i = 0; i < POLYGON_POINTS; i++) {
      Point offset = hexCornerOffset(i);
      corners[i] = new Point(center.x + offset.x, center.y + offset.y);
    }
    return corners;
  }

  public Point getPolygonPositionEstimate(HexCoordinate h) {
    return hexToPixel(h);
  }
}

