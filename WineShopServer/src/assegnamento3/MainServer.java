package assegnamento3;

import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MainServer extends Application
{

	// does not close underlying server

	NetworkServer server = null;
	Thread mainServerThread = null;
	DatabaseManager db = null;
	
	@Override
	public void start(Stage primaryStage) throws Exception
	{

		
		
		Parent root = FXMLLoader.load(getClass().getResource("main_server.fxml"));
		primaryStage.setTitle("WineshopServer");
		primaryStage.setScene(new Scene(root));
		primaryStage.setResizable(false);

		Button startButton = (Button) root.lookup("#startButton");
		Button stopButton = (Button) root.lookup("#stopButton");
		TextField portTextField = (TextField) root.lookup("#portTextField");

		stopButton.setOnAction(e ->
		{
			server.stop();
			startButton.setDisable(false);
			stopButton.setDisable(true);
			portTextField.setEditable(true);

			db.saveWineList(server.mainStore.getWineList());
			db.saveUserList(server.mainStore.getUserList());
			db.saveOrderList(server.mainStore.getOrderList());
			db.saveNotificationList(server.mainStore.getNotificationList());
			db.close();

			
		});

		startButton.setOnAction(e ->
		{

			try
			{
				int port = Integer.parseInt(portTextField.getText());
				server = new NetworkServer(port);
				db = new DatabaseManager("jdbc:mysql://localhost:3306/wineShop?", "createDatabaseIfNotExist=true","root", "");
				mainServerThread = new Thread(server);
				mainServerThread.start();
				startButton.setDisable(true);
				stopButton.setDisable(false);
				portTextField.setEditable(false);
				db.open();
				server.mainStore.setWineList(db.getWineList());
				server.mainStore.setUserList(db.getUserList());
				server.mainStore.setOrderList(db.getOrderList());
				server.mainStore.setNotificationList(db.getNotificationList());

			} catch (NumberFormatException | IOException e2)
			{
				new BasicAlertBox("Error", "Invalid Port", 200, 150);
			} catch (SQLException e1)
			{
				new BasicAlertBox("Error", "Could not connect to db", 200, 150);
				if (server != null)
					server.stop();
			}

		});

		primaryStage.show();

		primaryStage.setOnCloseRequest(e ->
		{
			e.consume();
			if (server != null)
				server.stop();
			if(db != null && db.isOpen())
			{
				db.saveWineList(server.mainStore.getWineList());
				db.saveUserList(server.mainStore.getUserList());
				db.saveOrderList(server.mainStore.getOrderList());
				db.saveNotificationList(server.mainStore.getNotificationList());
				db.close();
			}
			System.exit(0);
		});

	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
