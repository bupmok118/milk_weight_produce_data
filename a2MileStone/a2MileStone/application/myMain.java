

package application;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class myMain extends Application {

  private static final int WINDOW_WIDTH = 1100;
  private static final int WINDOW_HEIGHT = 800;
  private GridPane grid;
  private Scene mainScene;
  private HBox topPanel;
  private VBox rightPanel;
  private TableView<Farm> csvTable;
  private ObservableList<Farm> dataList = FXCollections.observableArrayList();
  private Desktop desktop = Desktop.getDesktop();
  
  
  public class Farm {

	    private SimpleStringProperty f1, f2;
	    private SimpleIntegerProperty f3;

	    public String getF1() {
	      return f1.get();
	    }

	    public String getF2() {
	      return f2.get();
	    }

	    public int getF3() {
	      return f3.get();
	    }

	    Farm(String farm_id, String date, int weight) {
	      this.f1 = new SimpleStringProperty(farm_id);
	      this.f2 = new SimpleStringProperty(date);
	      this.f3 = new SimpleIntegerProperty(weight);
	    }

	  }
  private void readCSVFile() {


    String CsvFile = "csv/large/2019-1.csv";
    String Delimiter = ",";

    BufferedReader br;

    try {
      br = new BufferedReader(new FileReader(CsvFile));

      String line;
      while ((line = br.readLine()) != null) {
        String[] fields = line.split(Delimiter, -1);
        if (!fields[1].equals("farm_id")) {
          String farm_id = fields[1];
          String date = fields[0];
          String pweight = fields[2];
          int weight = 0;

          farm_id = farm_id.substring(5);
          while (farm_id.length() < 3)
            farm_id = "0" + farm_id;

          weight = Integer.parseInt(pweight);

          Farm record = new Farm(farm_id, date, weight);
          dataList.add(record);
        }

      }

    } catch (FileNotFoundException ex) {
      Logger.getLogger(myMain.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(myMain.class.getName()).log(Level.SEVERE, null, ex);
    }

  }

  private void underliner(Button target, Button button_1, Button button_2, Button button_3, Button button_4, Button button_5, Button button_6) {
    if (button_1.isUnderline()) {
      button_1.setUnderline(false);
    }
    if (button_2.isUnderline()) {
      button_2.setUnderline(false);
    }
    if (button_3.isUnderline()) {
      button_3.setUnderline(false);
    }
    if (button_4.isUnderline()) {
      button_4.setUnderline(false);
    }
    if (button_5.isUnderline()) {
        button_5.setUnderline(false);
      }
    if (button_6.isUnderline()) {
        button_6.setUnderline(false);
      }
    if (!target.isUnderline()) {
      target.setUnderline(true);
    }
  }



  @Override
  public void start(Stage primaryStage) throws Exception {
    grid = new GridPane();

    mainScene = new Scene(grid, WINDOW_WIDTH, WINDOW_HEIGHT);
    topPanel = new HBox();
    // rightPanel = new VBox();
    rightPanel = new VBox();
    csvTable = new TableView<>();
    Button button_data = new Button("DATA"), button_farm = new Button("FARM"),
        button_annual = new Button("ANNUAL"), button_monthly = new Button("MONTHLY"),
        button_range = new Button("RANGE"), button_select = new Button("Select Files"), button_export = new Button("Export Files");
    Label rP_Label = new Label("Data Pressed");
    button_data.setUnderline(true);

    GridPane.setHgrow(topPanel, Priority.ALWAYS);
    GridPane.setVgrow(topPanel, Priority.ALWAYS);
    GridPane.setHgrow(csvTable, Priority.ALWAYS);
    GridPane.setVgrow(csvTable, Priority.ALWAYS);
    GridPane.setHgrow(rightPanel, Priority.ALWAYS);
    GridPane.setVgrow(rightPanel, Priority.ALWAYS);
    grid.setPadding(new Insets(10));
    grid.add(topPanel, 0, 0, 5, 1);
    grid.add(csvTable, 0, 1, 5, 10);
    grid.add(rightPanel, 5, 5, 4, 10);

    
    FileChooser fileChooser = new FileChooser();
    
    button_select.setOnAction(
    		new EventHandler<ActionEvent>() {
    		@Override
    		public void handle(ActionEvent e) {
    			File file = fileChooser.showOpenDialog(primaryStage);
    			if(file != null) {
    				openFile(file);
    			}
    		}
    			
    		});
    
    
    button_data.setOnAction(e -> {
        underliner(button_data, button_farm, button_annual, button_monthly, button_range, button_select, button_export);
        rP_Label.setText("Data Pressed");
      });
      button_farm.setOnAction(e -> {
        underliner(button_farm, button_data, button_annual, button_monthly, button_range, button_select, button_export);
        rP_Label.setText("Farm Pressed");
      });
      button_annual.setOnAction(e -> {
        underliner(button_annual, button_farm, button_data, button_monthly, button_range, button_select, button_export);
        rP_Label.setText("Annual Pressed");
      });
      button_monthly.setOnAction(e -> {
        underliner(button_monthly, button_farm, button_data, button_annual, button_range, button_select, button_export);
        rP_Label.setText("Monthly Pressed");
      });
      button_range.setOnAction(e -> {
        underliner(button_range, button_farm, button_data, button_annual, button_monthly, button_select, button_export);
        rP_Label.setText("Range Pressed");
      });

    topPanel.setPadding(new Insets(10));
    topPanel.setSpacing(10);
    topPanel.getChildren().addAll(button_data, button_farm, button_annual, button_monthly, button_range, rP_Label);

    
  //Button button_Save = new Button("Save"), button_edit = new Button("Edit"), button_year = new Button("Year"); 
    
    String combo[] = { "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020" };
	ComboBox<String> combo_Year = new ComboBox<String>(FXCollections.observableArrayList(combo));
	
	String combo2[] = { "1","2","3","4","5","6","7","8","9","10","11","12" };
	ComboBox<String> combo_Month = new ComboBox<String>(FXCollections.observableArrayList(combo2));

	String combo3[] = { "1", "2", "3", "4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21"
			,"22","23","24","25","26","27","28","29","30","31"};
	ComboBox<String> combo_Day = new ComboBox<String>(FXCollections.observableArrayList(combo3));
	
	Label label1 = new Label("Year");
	Label label2 = new Label("Month");
	Label label3 = new Label("Day");
	Label label4 = new Label("Weight");
	HBox hb = new HBox();
	TextField textField = new TextField();
	
	hb.getChildren().addAll(label1, combo_Year, label2, combo_Month, label3, combo_Day,label4, textField);
	hb.setSpacing(10);
	
	HBox hb2 = new HBox();
	Button save = new Button("Save");
	Button edit = new Button("Edit");
	Button delete = new Button("Delete");
	hb2.getChildren().addAll(save,edit,delete);
	hb2.setPadding(new Insets(10));
	hb2.setSpacing(10);
	
	Label inputData = new Label("Input Data");
	
    rightPanel.setPadding(new Insets(10));
    rightPanel.setSpacing(10);
    rightPanel.getChildren().addAll(button_select, button_export, inputData, hb, hb2);
    //rightPanel.setLayoutX(100);
    //rightPanel.setLayoutY(100);
    
   rightPanel.prefWidthProperty().bind(primaryStage.widthProperty());
    
    
    

   TableColumn f1 = new TableColumn("FARM");
    f1.setCellValueFactory(new PropertyValueFactory<>("f1"));
    TableColumn f2 = new TableColumn("DATE");
    f2.setCellValueFactory(new PropertyValueFactory<>("f2"));
    TableColumn f3 = new TableColumn("WEIGHT");
    f3.setCellValueFactory(new PropertyValueFactory<>("f3"));
    csvTable.getColumns().addAll(f1, f2, f3);
    csvTable.setItems(dataList);
    csvTable.prefHeightProperty().bind(primaryStage.heightProperty());

    readCSVFile();

    primaryStage.setResizable(false);
    primaryStage.setTitle("Milk Weight");
    primaryStage.setScene(mainScene);
    primaryStage.show();
  }
  private void openFile(File file) {
      try {
          desktop.open(file);
      } catch (IOException ex) {
          Logger.getLogger(
              myMain.class.getName()).log(
                  Level.SEVERE, null, ex
              );
      }
  }
  public static void main(String[] args) {
    launch(args);
  }
}
