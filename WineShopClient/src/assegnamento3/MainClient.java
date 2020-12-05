package assegnamento3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainClient extends Application
{

	static NetworkClient client;

	@Override
	public void start(Stage primaryStage) throws Exception
	{

		PortBox pb = new PortBox("Server Port");

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
		Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
		primaryStage.setTitle("WineShop");
		primaryStage.setScene(new Scene(root));
		primaryStage.setResizable(false);
		primaryStage.show();

		primaryStage.setOnCloseRequest(e ->
		{
			e.consume();
			new AreYouSureAlertBox("Confirmation", "Are you sure you want to close the client?", 300, 200, () ->
			{
				Request r = new Request("close");
				client.sendRequest(r);
				System.exit(0);
			});
		});

	}

	public static void main(String[] args)
	{
		launch(args);
	}
}