
package cloud.server.dashboard.listcloud;

import cloud.server.dashboard.database.DatabaseHandler;
import cloud.server.dashboard.main.Cloud;
import cloud.server.dashboard.ui.addCloud.AddCloudController;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author cmprbigg
 */


 
public class Cloud_listController implements Initializable {
    //Create an observable list for clouds to be stored
    ObservableList<Cloud> list = FXCollections.observableArrayList();
    
    //Declare FXML
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Cloud> tableView;
    @FXML
    private TableColumn<Cloud, String> nameCol;
    @FXML
    private TableColumn<Cloud, String> IDCol;
     // @FXML
    //private TableColumn<Cloud, Date> dateCol;      /*Try to get date created working*/
    @FXML
    private TableColumn<Cloud, String> alertCol = new TableColumn<>("Yes");
  
    


    //******************************************
    //  initialize - Called upon initialisation
    //******************************************
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //This should be set in FXML file but a bug with net beans
                                                                              //is causing a compile error with this line being there. 
        //run the initialise columns method
        initCol();
        
        
        //run the initialise columns method
        loadData();
    }    

    private void initCol() {
        //Create Columns for tables
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        IDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        //dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        //alertCol.setCellValueFactory(new PropertyValueFactory<>("alert"));
          alertCol.setCellValueFactory(cellData ->{
        boolean alert = cellData.getValue().getAlert();
        String alertAsString;
        //If there is an alert, show Yes or No rather than true or false
        if (alert == true) {
        alertAsString = "This Cloud is Exceeding Usage Goals and Requires Attention Immediately";
    
    }
            else{
        alertAsString = "This Cloud is Running Under Usage Goals";
    }
            return new ReadOnlyStringWrapper(alertAsString);
        });
        }

    //Load data from DB
    private void loadData() {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        //Select all from Cloud Table
        String qu = "SELECT * FROM CLOUD";
        ResultSet rs = handler.execQuery(qu);
        try {
            //Loop through DB and select all for each row in cloud table...
            while(rs.next()){
            String name = rs.getString("name");
            String ID = rs.getString("id");
          //  Date date = rs.getDate("dateCreated");
            Boolean alert = rs.getBoolean("hasAlert");
             Double runWatt = rs.getDouble("runWatt");
                Double wattGoal = rs.getDouble("wattGoal");
                Double runCpu = rs.getDouble("runCpu");
                Double cpuGoal = rs.getDouble("cpuGoal");
            

            //Than create a new cloud in the list for each row
            list.add(new Cloud(name, ID, //date,
                     alert, cpuGoal, wattGoal, runWatt, runCpu ));
        }
    } catch (SQLException ex) {
        Logger.getLogger(AddCloudController.class.getName()).log(Level.SEVERE, null, ex);
    }
        //Once collected, apply data to the table
        tableView.getItems().setAll(list);
    }
    

    
}
