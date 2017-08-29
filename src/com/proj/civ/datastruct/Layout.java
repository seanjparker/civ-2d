package com.proj.civ.datastruct;

import com.proj.civ.datastruct.hex.FractionalHex;
import com.proj.civ.datastruct.hex.HexCoordinate;

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

	public static Orientation POINTY_TOP = new Orientation(Math.sqrt(3.0), Math.sqrt(3.0) / 2.0, 0.0, 3.0 / 2.0, Math.sqrt(3.0) / 3.0, -1.0 / 3.0, 0.0, 2.0 / 3.0, 0.5);
	
	public Point hexToPixel(Layout l, HexCoordinate h) {
		Orientation m = l.orientation;
		double x = (m.f0 * h.q + m.f1 * h.r) * l.size.x;
		double y = (m.f2 * h.q + m.f3 * h.r) * l.size.y;
		return new Point(x + l.origin.x, y + l.origin.y);
	}
	
	public HexCoordinate pixelToHex(Layout l, Point p) {
		Orientation m = l.orientation;
		Point pt = new Point((p.x - l.origin.x) / l.size.x, (p.y - l.origin.y) / l.size.y);
		double q = m.b0 * pt.x + m.b1 * pt.y;
		double r = m.b2 * pt.x + m.b3 * pt.y;
		return FractionalHex.hexRound(new FractionalHex(q, r, -q - r));
	}
	
    public Point hexCornerOffset(Layout layout, int corner) {
        Orientation m = layout.orientation;
        Point size = layout.size;
        double angle = 2.0 * Math.PI * (m.startAngle - corner) / 6;
        return new Point(size.x * Math.cos(angle), size.y * Math.sin(angle));
    }

    public Point[] polygonCorners(Layout layout, HexCoordinate h) {
        Point[] corners = new Point[POLYGON_POINTS];
        Point center = hexToPixel(layout, h);
        for (int i = 0; i < POLYGON_POINTS; i++)
        {
            Point offset = hexCornerOffset(layout, i);
            corners[i] = new Point(center.x + offset.x, center.y + offset.y);
        }
        return corners;
    }
    public Point getPolygonPositionEstimate(Layout layout, HexCoordinate h) {
    		return hexToPixel(layout, h);
    }
}

