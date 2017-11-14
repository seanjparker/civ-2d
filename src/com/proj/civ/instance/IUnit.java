package com.proj.civ.instance;

import com.proj.civ.data.hex.HexCoordinate;
import com.proj.civ.map.civilization.BaseCivilization;

public class IUnit extends IData {
  protected String name;

  protected double movement;
  protected double movementTemp;
  protected double movementPotential;

  protected double strength = 0;

  protected int productionCost = 0;
  protected int health = 0;

  protected boolean isSpawned = false;
  protected boolean isMilitary = false;

  protected HexCoordinate curPos;
  protected BaseCivilization civOwner;

  protected boolean isMoving = false;
  protected boolean isAttacking = false;
}
