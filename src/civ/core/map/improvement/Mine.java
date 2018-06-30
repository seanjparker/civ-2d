package civ.core.map.improvement;

import java.util.ArrayList;
import java.util.List;
import civ.core.map.terrain.Feature;

public class Mine extends Improvement {
  private List<Feature> validFeatures = new ArrayList<>();

  public Mine() {
    super(0, 1, 0, 0, "Mine");

    validFeatures.add(Feature.WOODS);
    super.addAllValidFeatures(validFeatures);
  }

}
