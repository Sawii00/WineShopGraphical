package assegnamento3.src;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The class {@code PortBox} displays a popup that prompts the user for the
 * server port.
 * <p>
 * The input is checked against the restricted ports and invalid characters.
 **/
public class PortBox
{
	private int port = -1;

	/**
	 * Class constructor.
	 **/
	public PortBox()
	{
		display();
	}

	/**
	 * Displays the popup.
	 **/
	private void display()
	{
		Stage window = new Stage();
		Parent root;
		try
		{
			root = FXMLLoader.load(getClass().getResource("../res/port_popup.fxml"));
			TextField portTextField = (TextField) root.lookup("#portTextField");
			portTextField.requestFocus();
			Button connectButton = (Button) root.lookup("#connectButton");
			connectButton.setDefaultButton(true);
			Scene scene = new Scene(root);

			connectButton.setOnMouseClicked(e ->
			{
				try
				{
					/**
					 * Checks whether the input are valid numbers.
					 **/
					port = Integer.parseInt(portTextField.getText());
					/**
					 * Ports before 1000 are reserved and cannot be used.
					 **/
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
			window.setTitle("Port");
			window.setResizable(false);
			window.setScene(scene);
			window.showAndWait();
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
	}

	/**
	 * Getter for the server port.
	 *
	 * @return port of the server
	 **/
	public int getPort()
	{
		return port;
	}
}
