package assegnamento3.src;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The class {@code MainServer} handles the main execution of the Wineshop
 * Server.
 * <p>
 * Sets up the connection to the database manager and opens a server connection
 * to accept clients.
 **/
public class MainServer extends Application
{
	/**
	 * Main instance of the NetworkServer that handles incoming new connections.<p>
	 * Executed on a separated thread.
	 **/
	NetworkServer server = null;
	/**
	 * Thread that handles the NetworkServer. 
	 **/
	Thread mainServerThread = null;
	/**
	 * Creates the Database Manager that handles the persistency of the data. 
	 **/
	DatabaseManager db = null;

	/**
	 * Starts the Primary Stage with the prompt for the server port. <p>
	 * If the start button is pressed the server is setup to listen for incoming connections and the connection to the DB manager is open. 
	 **/
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		Parent root = FXMLLoader.load(getClass().getResource("../res/main_server.fxml"));
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

			/* Dumps the content of the main lists to the database */
			saveAllLists();
			db.close();
		});

		startButton.setOnAction(e ->
		{
			try
			{
				int port = Integer.parseInt(portTextField.getText());
				server = new NetworkServer(port);
				db = new DatabaseManager("jdbc:mysql://localhost:3306/wineShopNasturzioPindozzi?",
						"createDatabaseIfNotExist=true", "root", "");
				mainServerThread = new Thread(server);
				mainServerThread.start();
				startButton.setDisable(true);
				stopButton.setDisable(false);
				portTextField.setEditable(false);
				db.open();

				/* Loads the content of the database to the main lists */
				loadAllLists();
			} catch (NumberFormatException | IOException e2)
			{
				new BasicAlertBox("Error", "Invalid Port", 200, 150);
			} catch (SQLException e1)
			{
				new BasicAlertBox("Error", "Could not connect to db", 200, 150);
				if (server != null)
					server.stop();
				// primaryStage.close();
			}
		});

		/**
		 * Periodic task that saves the lists in the database as backup.
		 **/
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(() ->
		{
			if (db.isOpen())
				saveAllLists();
		}, 10, 10, TimeUnit.SECONDS);

		primaryStage.setOnCloseRequest(e ->
		{
			e.consume();
			if (server != null)
				server.stop();
			if (db != null && db.isOpen())
			{
				saveAllLists();
				db.close();
			}
			System.exit(0);
		});

		primaryStage.show();
	}

	/**
	 * Loads all the lists from the database to memory
	 **/
	private void loadAllLists()
	{
		server.mainStore.setWineList(db.getWineList());
		server.mainStore.setUserList(db.getUserList());
		server.mainStore.setOrderList(db.getOrderList());
		server.mainStore.setNotificationList(db.getNotificationList());
	}

	/**
	 * Saves all the lists onto the database
	 **/
	private void saveAllLists()
	{
		db.saveWineList(server.mainStore.getWineList());
		db.saveUserList(server.mainStore.getUserList());
		db.saveOrderList(server.mainStore.getOrderList());
		db.saveNotificationList(server.mainStore.getNotificationList());
	}

	/**
	 * Launches the Software. 
	 **/
	public static void main(String[] args)
	{
		launch(args);
	}
}
