package assegnamento3;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PortBox
{

	public PortBox(String title)
	{

		display(title);

	}

	private int port = -1;

	public int getPort()
	{
		return port;
	}

	private void display(String title)
	{
		Stage window = new Stage();
		Parent root;
		try
		{
			root = FXMLLoader.load(getClass().getResource("port_popup.fxml"));
			TextField portTextField = (TextField) root.lookup("#portTextField");
			Button connectButton = (Button) root.lookup("#connectButton");

			Scene scene = new Scene(root);

			connectButton.setOnMouseClicked(e ->
			{

				try
				{
					port = Integer.parseInt(portTextField.getText());
					if (port < 1000 || port >= 65535)
					{
						new BasicAlertBox("Error", "Invalid port (1000 - 65535)", 200, 150);
						return;
					}
					window.close();

				} catch (NumberFormatException e2)
				{
					new BasicAlertBox("Error", "Invalid port (1000 - 65535)", 200, 150);
					return;
				}

			});

			window.initModality(Modality.APPLICATION_MODAL);
			window.setTitle(title);
			window.setResizable(false);
			window.setScene(scene);
			window.showAndWait();
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}

	}
}