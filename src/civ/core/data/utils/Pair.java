package civ.core.data.utils;

public class Pair<T1, T2> {
  T1 t1;
  T2 t2;
  
  public Pair(T1 t1, T2 t2) {
    this.t1 = t1;
    this.t2 = t2;
  }
  
  public T1 getFirst() {
    return this.t1;
  }
  
  public T2 getSecond() {
    return this.t2;
  }
}
