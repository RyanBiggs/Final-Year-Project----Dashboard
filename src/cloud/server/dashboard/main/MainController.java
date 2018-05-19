
package cloud.server.dashboard.main;

import cloud.server.dashboard.database.DatabaseHandler;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author cmprbigg
 */
public class MainController implements Initializable {
    
    //Create an observable list for clouds to be stored
    ObservableList<Cloud> cloud = FXCollections.observableArrayList();
    
    //Create new instance of Database Handler
    DatabaseHandler handler = DatabaseHandler.getInstance();
    
    //Declare FXML 
    @FXML
    private Label IDExist;
    @FXML
    private Label nameExist;
    @FXML
    private Label dateTime;
    @FXML
    private HBox IDHbox;
    @FXML
    private HBox nameHbox;
    @FXML
    private BarChart<?, ?> barCPUGoal;
    @FXML
    private JFXTextField txtCloudID;
    @FXML
    private JFXTextField txtCloudName;
    @FXML
    private BarChart<?, ?> barWattRun;
    @FXML
    private BarChart<?, ?> barWattGoal;
    @FXML
    private BarChart<?, ?> barCPURun;
    @FXML
    private StackPane stackPane;


    
    /*TODO
        
        ---Serve error if the ID or Name does not exist--
        ---Add an alert menu allowing a user to check an alert and dismiss it---
    */
    
    
    //******************************************
    //  initialize - Called upon initialisation
    //******************************************
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JFXDepthManager.setDepth(nameHbox, 1);//Adds some styling to the HBox that can only be done with the Jfoenix library 
        JFXDepthManager.setDepth(IDHbox, 1);
        clearCharts();//Clear charts on run
        dateTime();//Activate clock on run
        
    }    
    
    //FXML event for opening add cloud window
    @FXML
    private void loadAddCloud (ActionEvent event) {
     loadWindow("/cloud/server/dashboard/ui/addCloud/add_cloud.fxml", "Add New Cloud");   
    }
    
        //FXML event for opening add cloud window
    @FXML
    private void loadDeleteCloud (ActionEvent event) {
     loadWindow("/cloud/server/dashboard/deleteCloud/delete_cloud.fxml", "Delete Cloud");   
    }
    
    //FXML event for opening cloud list window
    @FXML
    private void loadCloudList (ActionEvent event) {
        loadWindow("/cloud/server/dashboard/listcloud/cloud_list.fxml", "List of Avaliable Clouds"); 
    }
    
    //FXML event for clearing charts of data
    @FXML
    private void clearChartsButton (ActionEvent event) {
            clearCharts();
        }
    
    //FXML event for closing application
    @FXML
    private void close (ActionEvent event) {
             ((Stage) stackPane.getScene().getWindow()).close();
             
        }

    
    //**********************************************************
    //  FXML event for checking if ID exists
    //**********************************************************    
    @FXML
    private void checkID (ActionEvent event) throws SQLException {
            //Grab what is written in the ID box...
            String id = txtCloudID.getText();
            //Create SQL query...
            String qu = "SELECT * FROM CLOUD WHERE id = '" + id + "'";
            ResultSet rs = handler.execQuery(qu);
            Boolean flag = false;
            try {
                //While the ID exists in the DB...
                while(rs.next()){
                    
                  //flag is true do nothing
                    flag = true;
                }
                //if flag is not true...
                if(!flag){
                    //Set label
                    IDExist.setText("No Such ID Exists");
                }
            } catch (SQLException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
    //**********************************************************
    //  FXML event for checking if name exists
    //**********************************************************    
    @FXML
    private void checkName (ActionEvent event) {
            //Grab what is written in the name box...
            String name = txtCloudName.getText();
            //Create SQL query...
            String qu = "SELECT * FROM CLOUD WHERE id = '" + name + "'";
            ResultSet rs = handler.execQuery(qu);
            Boolean flag = false;
            try {
                //While the name exists in the DB...
                while(rs.next()){
                    
                    //flag is true...
                    flag = true;
                }
                //if flag is not true...
                if(!flag){
                    //Set label
                    nameExist.setText("No Such Name Exists");
                }
            } catch (SQLException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    
    //**********************************************************
    //  FXML event for adding data to charts
    //**********************************************************
    @FXML
    private void addCloudData(ActionEvent event) throws SQLException {
        
        /*on button click, take the id and add the data to the charts on the next tab*/
        
        //Select cloud that matches given ID
        String id = txtCloudID.getText();
        String cname = txtCloudName.getText();
        String qu = "SELECT * FROM CLOUD WHERE id = '" + id + "' OR name = '" + cname + "'";
        ResultSet rs = handler.execQuery(qu);
        Boolean flag = false;
        //Loop through Result Set
        try{
            while(rs.next()){
                String name = rs.getString("name");
                String ID = rs.getString("id");
                Boolean alert = rs.getBoolean("hasAlert");
                Double runWatt = rs.getDouble("runWatt");
                Double wattGoal = rs.getDouble("wattGoal");
                Double runCPU = rs.getDouble("runCpu");
                Double cpuGoal = rs.getDouble("cpuGoal");
                
                //Add cloud to the list
                cloud.add(new Cloud(name, ID, //date,
                     alert, cpuGoal, wattGoal ,runWatt, runCPU ));
                
                //todo BUG hides the names after more than a couple of clouds have been added
                
                //Create a new series of data for each chart
                XYChart.Series series1 = new XYChart.Series();
                XYChart.Series series2 = new XYChart.Series();
                XYChart.Series series3 = new XYChart.Series();
                XYChart.Series series4 = new XYChart.Series();
                
                //for each cloud in the cloud list...
                for (Cloud clouds : cloud) {
                    //if the given cloudID matches the one from the DB...
                    if (clouds.getID().equals(id)){
                        //Gather the data..
                        series1.getData().add(new XYChart.Data<>(clouds.getID(), clouds.getRunCpu()));
                        //Set the name..
                        series1.setName(name);
                        //and apply it to the chart
                        barCPURun.getData().add(series1);
                    }
                    if (clouds.getID().equals(id)){
                        series2.getData().add(new XYChart.Data<>(clouds.getID(), clouds.getCpuGoal()));
                        series2.setName(name);
                        barCPUGoal.getData().add(series2);
                    }
                    if (clouds.getID().equals(id)){
                        series3.getData().add(new XYChart.Data<>(clouds.getID(), clouds.getRunWatt()));
                        series3.setName(name);
                        barWattRun.getData().add(series3);
                    }
                        
                    if (clouds.getID().equals(id)){
                        series4.getData().add(new XYChart.Data<>(clouds.getID(), clouds.getWattGoal()));
                        series4.setName(name);
                        barWattGoal.getData().add(series4);
                    }
                }                                                        
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
     //method for clearing charts
    private void clearCharts() {
        barWattRun.getData().clear();
        barWattGoal.getData().clear();
        barCPUGoal.getData().clear();
        barCPURun.getData().clear();
        cloud.clear();
    }

    
    //**********************************************************
    //  dateTime - Fetches local date and time, and displays it
    //**********************************************************
    private void dateTime(){
        Task Task1 = new Task<Void>() {
            @Override
            public Void call() throws InterruptedException {
                while (true) {
                    // Break operation if cancelled
                    if (isCancelled()) 
                        break;

                    // Get the local date time formatted as "dd MMM yyyy hh:mm:ss" ( 17 Mar 2018 15:06:25 )
                    this.updateMessage(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm:ss"))); 
                }

                return null;
            }
        };
        
        Task1.messageProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue o, Object ov, Object nv) {
                // Set the text of the dateTime label to the above formatted data/time.
                dateTime.setText((String) o.getValue());
            }
        });

        // Start the clock on a separate thread
        new Thread(Task1).start();
    }
    
    
    
    //**********************************************************
    //  Method for loading new windows like cloud list and add cloud
    //**********************************************************
    
    void loadWindow(String loc, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
                    } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
