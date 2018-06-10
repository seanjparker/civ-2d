package civ.core.map.civilization;

import java.awt.Color;

public class England extends BaseCivilization {
  public England() {
    super("England", "English", new Color(237, 59, 59),
        new String[] {"London", "York", "Nottingham", "Hastings", "Canterbury", "Coventry",
            "Warwick", "Newcastle", "Oxford", "Liverpool"});
  }

  @Override
  public boolean sameCivilization(int id) {
    return super.getID() == id;
  }
}
