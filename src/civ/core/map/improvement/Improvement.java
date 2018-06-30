package civ.core.map.improvement;

import java.util.ArrayList;
import java.util.List;
import civ.core.map.terrain.Feature;
import civ.core.map.terrain.Landscape;
import civ.core.map.terrain.Yield;

public class Improvement extends Yield {
  private List<Landscape> validLandscapes = new ArrayList<>();
  private List<Feature> validFeatures = new ArrayList<>();
  private String name;

  public Improvement(int food, int production, int science, int gold, String name) {
    super(food, production, science, gold);
    this.name = name;
  }

  public void addAllValidLandscapes(List<Landscape> vl) {
    validLandscapes.addAll(vl);
  }

  public boolean validLandscape(Landscape c) {
    return validLandscapes.contains(c);
  }

  public void addAllValidFeatures(List<Feature> vf) {
    validFeatures.addAll(vf);
  }

  public boolean validFeature(Feature c) {
    return validFeatures.contains(c);
  }

  public String toString() {
    return "Food:" + getFood() + ", Production:" + getProduction() + ", Science:" + getScience()
        + ", Gold:" + getGold();
  }

  public String getName() {
    return name;
  }
}
