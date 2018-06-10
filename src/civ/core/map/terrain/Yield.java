package civ.core.map.terrain;

public class Yield {
  private int food;
  private int production;
  private int science;
  private int gold;

  public Yield(int food, int production, int science, int gold) {
    this.food = food;
    this.production = production;
    this.science = science;
    this.gold = gold;
  }

  public int getFood() {
    return this.food;
  }

  public int getProduction() {
    return this.production;
  }

  public int getScience() {
    return this.science;
  }

  public int getGold() {
    return this.gold;
  }
}
