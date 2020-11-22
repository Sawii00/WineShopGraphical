package assegnamento3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainClient extends Application {

    static NetworkClient client = new NetworkClient("127.0.0.1", 4926);
	
	@Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("WineShop");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
        
       
        
        
        
        primaryStage.setOnCloseRequest(e->{
			e.consume();
			new AreYouSureAlertBox("Confirmation", "Are you sure you want to close the client?", 300, 200, ()->{
				Request r = new Request("close");
				client.sendRequest(r);
				System.exit(0);
			});
		});
        
        
        
        
        
    }


    public static void main(String[] args) {
        launch(args);
    }
}