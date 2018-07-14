package civ.core.instance;

import civ.core.data.hex.HexCoordinate;
import civ.core.map.civilization.BaseCivilization;
import civ.core.unit.Scout;
import civ.core.unit.Settler;
import civ.core.unit.Unit;
import civ.core.unit.Warrior;
import civ.core.unit.Worker;

public class IUnit {
  protected String name;

  protected double movement;
  protected double movementTemp;
  protected double movementPotential;

  protected double strength = 0;

  protected int productionCost = 0;
  protected int currentProduction = 0;
  protected int health = 0;

  protected boolean isSpawned = false;
  protected boolean isMilitary = false;

  protected HexCoordinate curPos;
  protected BaseCivilization civOwner;

  protected boolean isMoving = false;
  protected boolean isAttacking = false;
  protected boolean hasTerrainCost = true;
  
  public enum UnitEnum {
    SETTLER,
    WORKER,
    WARRIOR,
    SCOUT;
    
    public Unit get() {
      switch (this) {
        case SETTLER: return new Settler();
        case WORKER:  return new Worker();
        case WARRIOR: return new Warrior();
        case SCOUT:   return new Scout();
        default:      return new Scout();
      }
    }
  }
}
