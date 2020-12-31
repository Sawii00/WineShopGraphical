package assegnamento3.src;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class for the client-side application.
 * <p>
 * Tries to establish a connection to the server after prompting the user for a
 * port.
 * <p>
 * If no connection can be established, the application is terminated.
 **/
public class MainClient extends Application
{
	/**
	 * Instance of NetworkClient that handles the connection with the server. 
	 **/
	static NetworkClient client;

	/**
	 * Launches the Software. 
	 **/
	public static void main(String[] args)
	{
		launch(args);
	}

	/**
	 * Starts the NetworkClient after prompring the user for a Port and populates the Primary Stage of the software. 
	 **/
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		PortBox pb = new PortBox();
		if (pb.getPort() < 0)
		{
			primaryStage.close();
			return;
		}
		client = new NetworkClient("127.0.0.1", pb.getPort());
		if (!client.isConnected())
		{
			primaryStage.close();
			return;
		}
		Parent root = FXMLLoader.load(getClass().getResource("../res/home.fxml"));
		primaryStage.setTitle("WineShop");
		primaryStage.setScene(new Scene(root));
		primaryStage.setResizable(false);
		primaryStage.show();
		primaryStage.setOnCloseRequest(e ->
		{
			e.consume();
			new ConfirmationBox("Confirmation", "Are you sure you want to close the client?", () ->
			{
				Request r = new Request("close");
				client.sendRequest(r);
				System.exit(0);
			});
		});
	}
}
