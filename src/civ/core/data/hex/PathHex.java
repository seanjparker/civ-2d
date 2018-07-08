package civ.core.data.hex;

public class PathHex extends HexCoordinate {
  private final boolean canMoveOver;
  private final boolean canSwitch;

  public PathHex(int q, int r, boolean passable) {
    super(q, r);
    this.canMoveOver = passable;
    this.canSwitch = false;
  }

  public PathHex(HexCoordinate h, boolean passable) {
    super(h.q, h.r, h.s);
    this.canMoveOver = passable;
    this.canSwitch = false;
  }

  public PathHex(HexCoordinate h, boolean passable, boolean canSwitch) {
    super(h.q, h.r, h.s);
    this.canMoveOver = passable;
    this.canSwitch = canSwitch;
  }

  public boolean getPassable() {
    return canMoveOver;
  }

  public boolean getCanSwitch() {
    return this.canSwitch;
  }
}
