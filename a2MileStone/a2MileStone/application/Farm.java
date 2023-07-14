package application;

import javafx.beans.property.SimpleStringProperty;

/**
 * @author sunghoon
 *
 */
public class Farm {
  private SimpleStringProperty farm1, farm2, farm3;

  /**
   * @return farm1
   */
  public String getF1() {
    return farm1.get();
  }

  /**
   * @return farm2
   */
  public String getF2() {
    return farm2.get();
  }

  /**
   * @return farm3
   */
  public String getF3() {
    return farm3.get();
  }

  @Override
  public boolean equals(Object o) {
    if(o instanceof Farm) {
      Farm f = (Farm)o;
      if(f.getF1().equals(farm1.get()) && f.getF2().equals(farm2.get()) && f.getF3().equals(farm3.get()))
        return true;
      else return false;
    }
    else
      return false;
  }
  
  /**
   * @param farm1 - first string
   * @param farm2 - second string
   * @param farm3 - third string
   */
  public Farm(String farm1, String farm2, String farm3) {
    this.farm1 = new SimpleStringProperty(farm1);
    this.farm2 = new SimpleStringProperty(farm2);
    this.farm3 = new SimpleStringProperty(farm3);
  }
}
