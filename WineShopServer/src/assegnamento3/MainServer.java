package assegnamento3;

import java.io.IOException;

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
		});

		startButton.setOnAction(e ->
		{

			try
			{
				int port = Integer.parseInt(portTextField.getText());
				server = new NetworkServer(port);
				mainServerThread = new Thread(server);
				mainServerThread.start();
				startButton.setDisable(true);
				stopButton.setDisable(false);

			} catch (NumberFormatException | IOException e2)
			{
				new BasicAlertBox("Error", "Invalid Port", 200, 100);
			}

		});

		primaryStage.show();

		primaryStage.setOnCloseRequest(e ->
		{
			e.consume();
			new AreYouSureAlertBox("Confirmation", "Are you sure you want to close the server?", 300, 200, () ->
			{

				if (server != null)
					server.stop();
				System.exit(0);
			});
		});

	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
