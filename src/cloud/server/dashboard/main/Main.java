
package cloud.server.dashboard.main;

import cloud.server.dashboard.database.DatabaseHandler;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author cmprbigg
 */
public class Main extends Application {
    
        
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Cloud Server Dashboard");
        stage.show();
        stage.setOnCloseRequest(e -> Platform.exit());
        
        //Run the Database Handler on a seperate thread to allow for decreased loading times
        new Thread (() -> {
            DatabaseHandler.getInstance();
        }).start();
      
    }

    public static void main(String[] args) {
        launch(args);
    }
    

}

    