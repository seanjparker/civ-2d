package civ.core.data;

public interface Producable {
  public String getName();
  public int getProductionCost();
  public int getCurrentProduction();
  public void addProductionToBuild(int productionToAdd);
}
