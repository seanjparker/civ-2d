package civ.core.data.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GFXUtils {
  
  private GFXUtils() {
    throw new IllegalStateException("Utility class");
  }
  
  public static BufferedImage loadImage(String path) {
    try {
      return ImageIO.read(new File(path)); 
    } catch (IOException e) {
      System.err.println(e);
      return null;
    }
  }
  public static BufferedImage loadImageAndScale(String path, double targetW, double targetH) {
    BufferedImage image = loadImage(path);
    BufferedImage dbi = null;
    if (image != null) {
      dbi = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = dbi.createGraphics();
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

      AffineTransform at = AffineTransform.getScaleInstance(targetW / image.getWidth(), targetH / image.getHeight());
      g.drawRenderedImage(image, at);
    }
    return dbi;
  }
  
  public static BufferedImage scale(BufferedImage sbi, int imageType, int dWidth, int dHeight, double fWidth, double fHeight) {
    BufferedImage dbi = null;
    if(sbi != null) {
        dbi = new BufferedImage(dWidth, dHeight, imageType);
        Graphics2D g = dbi.createGraphics();
        AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
        g.drawRenderedImage(sbi, at);
    }
    return dbi;
  }
  
  public static Color getColourForReadableText(Color bgColour) {
    double weightedR = bgColour.getRed() * 0.299;
    double weightedG = bgColour.getGreen() * 0.587;
    double weightedB = bgColour.getBlue() * 0.114;
    
    return weightedR + weightedG + weightedB > 187D ? Color.BLACK : Color.WHITE;
  }
  
  public static String getViewableColour(Color bgColour) {
    double weightedR = bgColour.getRed() * 0.299;
    double weightedG = bgColour.getGreen() * 0.587;
    double weightedB = bgColour.getBlue() * 0.114;
    
    return weightedR + weightedG + weightedB > 187D ? "BLACK" : "WHITE";
  }
  
}
