package civ.core.data.utils;

import static civ.core.instance.IData.*;
/*
 * One turn is
40 Years, until 1000 BC (75 turns)
25 Years, until 500 AD (60 turns)
20 Years, until 1000 AD (25 turns)
any culture buildings after this point will not generate extra culture/turn, before a realistic deadline
10 Years, until 1500 AD (50 turns)
5 Years, until 1800 AD (60 turns)
2 Years, until 1920 AD (60 turns)
1 Years, until 2050 AD (130 turns) -> time victory condition
 */

public class TurnTimer {
  private static int currentYear = -4000;
  
  public static void nextTurn() {
    if (currentYear < -1000) {
      //+40 years
      currentYear += 40;
    } else if (currentYear >= -1000 && currentYear < 500) {
      //+25 years
      currentYear += 25;
    } else if (currentYear >= 500 && currentYear < 1000) {
      //+20 years
      currentYear += 20;
    } else if (currentYear >= 1000 && currentYear < 1500) {
      //+10 years
      currentYear += 10;
    } else if (currentYear >= 1500 && currentYear < 1800) {
      //+5 years
      currentYear += 5;
    } else if (currentYear >= 1800 && currentYear < 1920) {
      //+2 years
      currentYear += 2;
    } else if (currentYear >= 1920) {
      //+1 year
      currentYear += 1;
    }
  }
  
  public static int getYear() {
    return currentYear;
  }
  
  public static String getFormattedYear() {
    boolean isBC = currentYear < 0;
    return (isBC ? Integer.toString(-currentYear) : Integer.toString(currentYear))
        + (isBC ? " BC" : " AD");
  }
  
}
