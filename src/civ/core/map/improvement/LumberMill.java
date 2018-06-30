package civ.core.map.improvement;

import java.util.ArrayList;
import java.util.List;
import civ.core.map.terrain.Feature;

public class LumberMill extends Improvement {
  private List<Feature> validFeatures = new ArrayList<>();

  public LumberMill() {
    super(0, 1, 0, 0, "Lumber Mill");

    validFeatures.add(Feature.WOODS);
    super.addAllValidFeatures(validFeatures);
  }
}
