package application;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;

/**
 * This is written for managing data
 *
 */
public class FarmReport {
  HashSet<Farm> dataSet; 
  int totalWeight; 
  Alert a; 
  TextArea message; 
  
  /**
   * Constructor of FarmReport
   */
  public FarmReport() {
    dataSet = new HashSet<Farm>();
    totalWeight = 0;
    a = new Alert(AlertType.ERROR);
    message = new TextArea();
    message.setWrapText(true);
    a.getDialogPane().setContent(message);
  }

  /**
   * 
   * @param farm - Farm node
   */
  public void add(Farm farm) {
    for (Farm f : dataSet) {
      if (f.equals(farm))
        return;
    }
    dataSet.add(farm);
  }

  /**
   * 
   * @param file - CSV file to read
   */
  public void readCSV(File file) {
    String FieldDelimiter = ",";
    BufferedReader br;
    try {
      br = new BufferedReader(new FileReader(file));
      String line;
      while ((line = br.readLine()) != null) {
        try {
          String[] fields = line.split(FieldDelimiter, -1);
          if (!fields[1].equals("farm_id")) {
            String farm_id = fields[1];
            String date = fields[0];
            String pweight = fields[2];
            int weight = 0;
            farm_id = farm_id.substring(5);
            Integer.parseInt(farm_id);
            while (farm_id.length() < 3) {
              farm_id = "0" + farm_id;
            }
            String[] tempDate = date.split("-");
            while (tempDate[1].length() < 2)
              tempDate[1] = "0" + tempDate[1];
            while (tempDate[2].length() < 2)
              tempDate[2] = "0" + tempDate[2];
            String inDate = tempDate[0] + "-" + tempDate[1] + "-" + tempDate[2];
            weight = Integer.parseInt(pweight);
            totalWeight += weight;
            add(new Farm(farm_id, inDate, weight + ""));
          }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException
            | StringIndexOutOfBoundsException ex) {
          if (!a.isShowing()) {
            message.setText("File contains missing or invalid data: " + ex.getMessage());
            a.show();
          } else {
            message.setText(
                message.getText() + "\nFile contains missing or invalid data: " + ex.getMessage());
          }
        }
      }

    } catch (FileNotFoundException ex) {
      a.setContentText("File(s) canno be found. Program halted.\n" + ex.getMessage());
      a.show();
    } catch (IOException ex) {
      a.setContentText("Program halted.\n" + ex.getMessage());
      a.show();
    }
  }

  /**
   * 
   * @param farm_id - starting farm id
   * @param syear - starting year
   * @param smonth - starting month
   * @param sday - starting day
   * @param did - ending farm id
   * @param dyear - ending year
   * @param dmonth - ending month
   * @param dday - ending day
   * @return list of targeted farms (farm_id, date, total weight)
   */
  public List<Farm> getRangeReport(String farm_id, String syear, String smonth, String sday, String did,
      String dyear, String dmonth, String dday) {
    ArrayList<Farm> output = new ArrayList<Farm>();
    HashMap<String, String> map = new HashMap<String, String>();
    String s_id, s_year, s_month, s_day, d_id, d_year, d_month, d_day;
    if (farm_id == null)
      s_id = "000";
    else
      s_id = farm_id;
    if (syear == null)
      s_year = "2000";
    else
      s_year = syear;
    if (smonth == null)
      s_month = "01";
    else
      s_month = smonth;
    if (sday == null)
      s_day = "01";
    else
      s_day = sday;
    if (did == null)
      d_id = "999";
    else
      d_id = did;
    if (dyear == null)
      d_year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
    else
      d_year = dyear;
    if (dmonth == null)
      d_month = "12";
    else
      d_month = dmonth;
    if (dday == null)
      d_day = "31";
    else
      d_day = dday;
    String skey = s_year + "-" + s_month + "-" + s_day;
    String dkey = d_year + "-" + d_month + "-" + d_day;
    for (Farm f : dataSet) {
      String key = f.getF1();
      String datekey = f.getF2();
      if ((key.compareTo(s_id) >= 0 && datekey.compareTo(skey) >= 0)
          && (key.compareTo(d_id) <= 0 && datekey.compareTo(dkey) <= 0)) {
        if (!map.containsKey(key))
          map.put(key + "-" + datekey, f.getF3());
        else
          map.replace(key + "-" + datekey,
              Integer.parseInt(map.get(key + "-" + datekey)) + Integer.parseInt(f.getF3()) + "");
      }
    }
    for (Map.Entry<String, String> e : map.entrySet()) {
      output.add(new Farm(e.getKey().substring(0, 3), e.getKey().substring(4),
          Integer.parseInt(e.getValue()) + ""));
    }
    return output;
  }

  /**
   * 
   * @param year - target year
   * @param month - target month
   * @return List<Farm>
   */
  public List<Farm> getAnnualMonthlyReport(String year, String month) {
    List<Farm> list = getRangeReport(null, year, month, null, null, year, month, null);
    ArrayList<Farm> output = new ArrayList<Farm>();
    HashMap<String, String> map = new HashMap<String, String>();
    for (Farm f : list) {
      String key = f.getF1();
      if (!map.containsKey(key))
        map.put(key, f.getF3());
      else
        map.replace(key, Integer.parseInt(map.get(key)) + Integer.parseInt(f.getF3()) + "");
    }
    for (Map.Entry<String, String> e : map.entrySet()) {
      output.add(new Farm(e.getKey(),
          Double.toString(Double.parseDouble(e.getValue()) / getTargetWeight(year, month) * 100)
              .substring(0, 5) + "%",
          e.getValue()));
    }
    return output;
  }


  /**
   * 
   * @param farm_id - target id (null for all id)
   * @param year - target year (null for all year)
   * @return List<Farm> where Farm(Month, Percent, Total Weight of Month)
   */
  public List<Farm> getFarmReport(String farm_id, String year) {
    List<Farm> list = getRangeReport(farm_id, year, null, null, farm_id, year, null, null);
    HashMap<String, String> map = new HashMap<String, String>();
    ArrayList<Farm> output = new ArrayList<Farm>();
    for (Farm f : list) {
      String key = f.getF2().substring(5, 7);
      if (!map.containsKey(key))
        map.put(key, f.getF3());
      else
        map.replace(key, Integer.parseInt(map.get(key)) + Integer.parseInt(f.getF3()) + "");
    }
    for (Map.Entry<String, String> e : map.entrySet()) {
      output.add(new Farm(e.getKey(),
          Double
              .toString(Double.parseDouble(e.getValue()) / getTargetWeight(year, e.getKey()) * 100)
              .substring(0, 5) + "%",
          e.getValue()));
    }
    return output;
  }

  /**
   * 
   * @return List<Farm> where Farm(Year, , Total Weight of Year)
   */
  public List<Farm> getAnnualData() {
    List<Farm> list = getRangeReport(null, null, null, null, null, null, null, null);
    HashMap<String, String> map = new HashMap<String, String>();
    ArrayList<Farm> output = new ArrayList<Farm>();
    for (Farm f : list) {
      String key = f.getF2().substring(0, 4);
      if (!map.containsKey(key))
        map.put(key, f.getF3());
      else
        map.replace(key, Integer.parseInt(map.get(key)) + Integer.parseInt(f.getF3()) + "");
    }
    for (Map.Entry<String, String> e : map.entrySet()) {
      output.add(new Farm(e.getKey(), "", e.getValue()));
    }
    return output;
  }

  /**
   * 
   * @return Total Weight
   */
  public int getTotalWeight() {
    return totalWeight;
  }


  /**
   * 
   * @return List<Farm>
   */
  public List<Farm> getAllList() {
    return new ArrayList<Farm>(dataSet);
  }

  /**
   * 
   * @param year - target year
   * @param month - target month
   * @return Total Weight
   */
  private int getTargetWeight(String year, String month) {
    int output = 0;
    List<Farm> list = getRangeReport(null, year, month, null, null, year, month, null);
    for (Farm f : list) {
      if ((month == null || f.getF2().substring(5, 7).equals(month))
          && (year == null || f.getF2().substring(0, 4).equals(year)))
        output += Integer.parseInt(f.getF3());
    }
    return output;
  }

  /**
   * 
   * @param syear - starting year
   * @param smonth - starting month
   * @param sday - starting day
   * @param dyear - ending year
   * @param dmonth - ending month
   * @param dday - ending day
   * @return Daily Average Weight
   */
  public int getDailyAverage(String syear, String smonth, String sday, String dyear, String dmonth,
      String dday) {
    List<Farm> list = getRangeReport(null, syear, smonth, sday, null, dyear, dmonth, dday);
    int output = 0;
    for (Farm f : list)
      output += Integer.parseInt(f.getF3());
    if (list.size() != 0)
      return output / list.size();
    else
      return 0;
  }

  /**
   * 
   * @param year - target year (null for all year)
   * @param month - target month (null for all month)
   * @return Farm Average Weight
   */
  public int getFarmAverage(String year, String month) {
    List<Farm> list = getAnnualMonthlyReport(year, month);
    int output = 0;
    for (Farm f : list)
      output += Integer.parseInt(f.getF3());
    if (list.size() != 0)
      return output / list.size();
    else
      return 0;
  }

  /**
   * 
   * @param farm_id - target id (null for all id)
   * @param year - target year (null for all year)
   * @return Monthly Average Weight
   */
  public int getMonthlyAverage(String farm_id, String year) {
    List<Farm> list = getFarmReport(farm_id, year);
    int output = 0;
    for (Farm f : list)
      output += Integer.parseInt(f.getF3());
    if (list.size() != 0)
      return output / list.size();
    else
      return 0;
  }

  /**
   * 
   * @return Annual Average Weight
   */
  public int getAnnualAverage() {
    List<Farm> list = getAnnualData();
    int output = 0;
    for (Farm f : list)
      output += Integer.parseInt(f.getF3());
    if (list.size() != 0)
      return output / list.size();
    else
      return 0;
  }
}
