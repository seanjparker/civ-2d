package civ.core.map.improvement;

import java.util.ArrayList;
import java.util.List;
import civ.core.map.terrain.Feature;
import civ.core.map.terrain.Landscape;

public class TradingPost extends Improvement {
  private List<Feature> validFeatures = new ArrayList<>();
  private List<Landscape> validLandscapes = new ArrayList<>();

  public TradingPost() {
    super(0, 0, 0, 1, "Trading Post");

    validLandscapes.add(Landscape.PLAINS);
    validLandscapes.add(Landscape.GRASSLAND);
    validLandscapes.add(Landscape.DESERT);
    validLandscapes.add(Landscape.TUNDRA);
    super.addAllValidLandscapes(validLandscapes);

    validFeatures.add(Feature.HILLS);
    super.addAllValidFeatures(validFeatures);
  }

}
