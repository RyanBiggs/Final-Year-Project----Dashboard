/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.server.dashboard.deleteCloud;


import cloud.server.dashboard.database.DatabaseHandler;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
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
public class DeleteCloudController implements Initializable {
    
    //Declare FXML

    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField name;

    @FXML
    private AnchorPane deleteCloudPane;

    DatabaseHandler databaseHandler;
    //******************************************
    //  initialize - Called upon initialisation
    //******************************************
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Declare new instance of DB handler
        databaseHandler = DatabaseHandler.getInstance();
  
    }    

    //FXML click event to add cloud
    @FXML
    private void deleteCloud(ActionEvent event) {
              
        //Declare variables to collect the data given through textboxes
        String cloudID = id.getText();
        String cloudName = name.getText();
        
        //If either Cloud ID or Cloud Name textboxes are empty, serve error and ask the user to fill both out
        if (cloudID.isEmpty() || cloudName.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Fill Out Both Fields!");
            alert.showAndWait();
            return;
        }
                  
        //SQL query -- Delete the cloud from the DB that matches the Cloud ID given
        String qu = "DELETE FROM CLOUD WHERE id='" +  cloudID + "'";


        //If there is an error serve error alert, else serve Success alert
        System.out.print(qu);
        if(databaseHandler.execAction(qu)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Success!");
            alert.showAndWait();
            Stage stage = (Stage) deleteCloudPane.getScene().getWindow();
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
        Stage stage = (Stage) deleteCloudPane.getScene().getWindow();
        stage.close();
    }


    
} 





