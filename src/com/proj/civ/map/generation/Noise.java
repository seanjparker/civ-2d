package com.proj.civ.map.generation;

// OpenSimplex by Kurt Spencer
public class Noise {
  private static final double STRETCH_CONSTANT_2D = -0.211324865405187; // (1/Math.sqrt(2+1)-1)/2;
  private static final double SQUISH_CONSTANT_2D = 0.366025403784439; // (Math.sqrt(2+1)-1)/2;
  private static final double NORM_CONSTANT_2D = 47;

  private final double REDISTRIBUTION_FACTOR = 4.0;

  private short[] perm;

  // Gradients for 2D. They approximate the directions to the
  // vertices of an octagon from the center.
  private static byte[] gradients2D =
      new byte[] {5, 2, 2, 5, -5, 2, -2, 5, 5, -2, 2, -5, -5, -2, -2, -5,};

  // Initializes the class using a permutation array generated from a 64-bit seed.
  // Generates a proper permutation (i.e. doesn't merely perform N successive pair swaps on a base
  // array)
  // Uses a simple 64-bit LCG.
  public Noise(long seed) {
    perm = new short[256];
    short[] source = new short[256];
    for (short i = 0; i < 256; i++)
      source[i] = i;
    seed = seed * 6364136223846793005l + 1442695040888963407l;
    seed = seed * 6364136223846793005l + 1442695040888963407l;
    seed = seed * 6364136223846793005l + 1442695040888963407l;
    for (int i = 255; i >= 0; i--) {
      seed = seed * 6364136223846793005l + 1442695040888963407l;
      int r = (int) ((seed + 31) % (i + 1));
      if (r < 0)
        r += (i + 1);
      perm[i] = source[r];
      source[r] = source[i];
    }
  }

  public double noise1(double x, double y, double w, double h, int octaves, boolean isPangaea) {
    double freq = 1.0;
    double e = 1.0;
    double v = 0.0;
    for (int i = 0; i < octaves; i++) {
      v += e * eval(freq * x, freq * y);

      freq *= 2.0;
      e = 1.0 / freq;
    }
    // return isPangaea ? reshape(redistribute(scale(v)), x, y, w, h) : redistribute(scale(v));
    return redistribute(scale(v));
  }

  public double noise2(double x, double y, int octaves) {
    double freq = 1.0;
    double e = 1.0;
    double v = 0.0;
    for (int i = 0; i < octaves; i++) {
      v += e * eval(freq * x, freq * y);

      freq *= 2.0;
      e = 1.0 / freq;
    }
    return scale(v);
  }

  private double manhattanDistance(double x, double y, double w, double h) {
    double cx = w / 2;
    double cy = h / 2;
    double nx = x - cx;
    double ny = cy - y;
    return 2 * Math.max(Math.abs(nx), Math.abs(ny));
  }

  private double reshape(double e, double x, double y, double w, double h) {
    // e = e + a - b * d ^ c
    final double a = 0.46;
    final double b = 0.54;
    final double c = 0.60;
    final double d = manhattanDistance(x, y, w, h); // Manhattan distance
    return e + a - b * Math.pow(d, c);
  }

  private double scale(double x) {
    return (x / 2.0) + 0.5;
  }

  private double redistribute(double x) {
    return Math.pow(x, REDISTRIBUTION_FACTOR);
  }

  // private double fade(double x) {
  // return x * x * x * (x * (6 * x - 15) + 10);
  // }

  // 2D OpenSimplex Noise.
  private double eval(double x, double y) {
    // Place input coordinates onto grid.
    double stretchOffset = (x + y) * STRETCH_CONSTANT_2D;
    double xs = x + stretchOffset;
    double ys = y + stretchOffset;

    // Floor to get grid coordinates of rhombus (stretched square) super-cell origin.
    int xsb = fastFloor(xs);
    int ysb = fastFloor(ys);

    // Skew out to get actual coordinates of rhombus origin. We'll need these later.
    double squishOffset = (xsb + ysb) * SQUISH_CONSTANT_2D;
    double xb = xsb + squishOffset;
    double yb = ysb + squishOffset;

    // Compute grid coordinates relative to rhombus origin.
    double xins = xs - xsb;
    double yins = ys - ysb;

    // Sum those together to get a value that determines which region we're in.
    double inSum = xins + yins;

    // Positions relative to origin point.
    double dx0 = x - xb;
    double dy0 = y - yb;

    // We'll be defining these inside the next block and using them afterwards.
    double dx_ext, dy_ext;
    int xsv_ext, ysv_ext;

    double value = 0;

    // Contribution (1,0)
    double dx1 = dx0 - 1 - SQUISH_CONSTANT_2D;
    double dy1 = dy0 - 0 - SQUISH_CONSTANT_2D;
    double attn1 = 2 - dx1 * dx1 - dy1 * dy1;
    if (attn1 > 0) {
      attn1 *= attn1;
      value += attn1 * attn1 * extrapolate(xsb + 1, ysb + 0, dx1, dy1);
    }

    // Contribution (0,1)
    double dx2 = dx0 - 0 - SQUISH_CONSTANT_2D;
    double dy2 = dy0 - 1 - SQUISH_CONSTANT_2D;
    double attn2 = 2 - dx2 * dx2 - dy2 * dy2;
    if (attn2 > 0) {
      attn2 *= attn2;
      value += attn2 * attn2 * extrapolate(xsb + 0, ysb + 1, dx2, dy2);
    }

    if (inSum <= 1) { // We're inside the triangle (2-Simplex) at (0,0)
      double zins = 1 - inSum;
      if (zins > xins || zins > yins) { // (0,0) is one of the closest two triangular vertices
        if (xins > yins) {
          xsv_ext = xsb + 1;
          ysv_ext = ysb - 1;
          dx_ext = dx0 - 1;
          dy_ext = dy0 + 1;
        } else {
          xsv_ext = xsb - 1;
          ysv_ext = ysb + 1;
          dx_ext = dx0 + 1;
          dy_ext = dy0 - 1;
        }
      } else { // (1,0) and (0,1) are the closest two vertices.
        xsv_ext = xsb + 1;
        ysv_ext = ysb + 1;
        dx_ext = dx0 - 1 - 2 * SQUISH_CONSTANT_2D;
        dy_ext = dy0 - 1 - 2 * SQUISH_CONSTANT_2D;
      }
    } else { // We're inside the triangle (2-Simplex) at (1,1)
      double zins = 2 - inSum;
      if (zins < xins || zins < yins) { // (0,0) is one of the closest two triangular vertices
        if (xins > yins) {
          xsv_ext = xsb + 2;
          ysv_ext = ysb + 0;
          dx_ext = dx0 - 2 - 2 * SQUISH_CONSTANT_2D;
          dy_ext = dy0 + 0 - 2 * SQUISH_CONSTANT_2D;
        } else {
          xsv_ext = xsb + 0;
          ysv_ext = ysb + 2;
          dx_ext = dx0 + 0 - 2 * SQUISH_CONSTANT_2D;
          dy_ext = dy0 - 2 - 2 * SQUISH_CONSTANT_2D;
        }
      } else { // (1,0) and (0,1) are the closest two vertices.
        dx_ext = dx0;
        dy_ext = dy0;
        xsv_ext = xsb;
        ysv_ext = ysb;
      }
      xsb += 1;
      ysb += 1;
      dx0 = dx0 - 1 - 2 * SQUISH_CONSTANT_2D;
      dy0 = dy0 - 1 - 2 * SQUISH_CONSTANT_2D;
    }

    // Contribution (0,0) or (1,1)
    double attn0 = 2 - dx0 * dx0 - dy0 * dy0;
    if (attn0 > 0) {
      attn0 *= attn0;
      value += attn0 * attn0 * extrapolate(xsb, ysb, dx0, dy0);
    }

    // Extra Vertex
    double attn_ext = 2 - dx_ext * dx_ext - dy_ext * dy_ext;
    if (attn_ext > 0) {
      attn_ext *= attn_ext;
      value += attn_ext * attn_ext * extrapolate(xsv_ext, ysv_ext, dx_ext, dy_ext);
    }

    return value / NORM_CONSTANT_2D;
  }

  private double extrapolate(int xsb, int ysb, double dx, double dy) {
    int index = perm[(perm[xsb & 0xFF] + ysb) & 0xFF] & 0x0E;
    return gradients2D[index] * dx + gradients2D[index + 1] * dy;
  }

  private static int fastFloor(double x) {
    int xi = (int) x;
    return x < xi ? xi - 1 : xi;
  }
}
