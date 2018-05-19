/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.server.dashboard.ui.addCloud;

import cloud.server.dashboard.database.DatabaseHandler;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author cmprbigg
 */
public class AddCloudController implements Initializable {
    
    //Declare FXML

    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField name;
    @FXML
    private JFXSlider watts;
    @FXML
    private JFXSlider cpu;
    @FXML
    private AnchorPane addCloudPane;

    DatabaseHandler databaseHandler;
    //******************************************
    //  initialize - Called upon initialisation
    //******************************************
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Declare new instance of DB handler
        databaseHandler = DatabaseHandler.getInstance();
        //Run check data method
        checkData();
    }    

    //FXML click event to add cloud
    @FXML
    private void addCloud(ActionEvent event) {
        
        /*Add Date created to database*/
//        java.util.Date javaDate = new java.util.Date();
//        long javaTime = javaDate.getTime();
//        java.sql.Date sqlDate = new java.sql.Date(javaTime);
//        java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());


        //Generate a random double for testing purposes
        Random random = new Random();
        double randomWatt = 150 + (300 - 150) * random.nextDouble();
        double randomCpu = 1500 + (3000 - 1500) * random.nextDouble();
        
            //Declare variables to collect the data given through textboxes
        String cloudID = id.getText();
        String cloudName = name.getText();
        double cloudWatts = watts.getValue();
        double cloudCPU = cpu.getValue();
        double cloudWattRun = randomWatt;//////For the purpose of testing, clouds are given a random running wattage of between 150 and 300w
        double cloudCPURun = randomCpu;//////For the purpose of testing, clouds are giving a random running clock speed between 1500 and 3000mhz
        String hasAlert = "";
        

        //If either Cloud ID or Cloud Name textboxes are empty, serve error and ask the user to fill both out
        if (cloudID.isEmpty() || cloudName.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Fill Out Both Fields!");
            alert.showAndWait();
            return;
        }
        
        //If the running number of either watts or cpu is higher than the given goal, alert = true
        if (cloudWattRun > cloudWatts || cloudCPURun > cloudCPU){
    
            hasAlert = "true";
        }else{
            
            hasAlert = "false";
        }
        
        
        //SQL query -- Insert into cloud table the values given
        String qu = "INSERT INTO CLOUD VALUES (" +
                "'" + cloudID +"'," +
                "'" + cloudName + "'," +
                "" + cloudWattRun + "," +
                "" + cloudCPURun + "," +
                "" + cloudWatts + "," +
                "" + cloudCPU + "," +
                "" + hasAlert + "" +
               
                ")";

        //If there is an error serve error alert, else serve Success alert
        System.out.print(qu);
        if(databaseHandler.execAction(qu)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Success!");
            alert.showAndWait();
            Stage stage = (Stage) addCloudPane.getScene().getWindow();
            stage.close();
        }else{ //error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failed!");
            alert.showAndWait();
        }
    }

    //Click event to close window on click 
    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) addCloudPane.getScene().getWindow();
        stage.close();
    }

    //Debug purposes -- when form is opened print out all names of clouds to check DB connectivity.
    private void checkData() {
        String qu = "SELECT * FROM CLOUD";
        ResultSet rs = databaseHandler.execQuery(qu);
        try {
            while(rs.next()){
            String namex = rs.getString("name");
            System.out.println(namex);
        }
    } catch (SQLException ex) {
        Logger.getLogger(AddCloudController.class.getName()).log(Level.SEVERE, null, ex);
    }
    
} 
}




