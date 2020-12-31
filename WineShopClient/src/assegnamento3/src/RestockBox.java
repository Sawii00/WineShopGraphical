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
 * The class {@code RestockBox} displays a popup that prompts the Seller for the
 * amount of bottles of a wine to be restocked.
 **/
public class RestockBox
{
	private int val = 0;
	private Button confirmButton;
	private TextField amountTextField;

	/**
	 * Class constructor.
	 **/
	public RestockBox()
	{
		display();
	}

	/**
	 * Displays the popup.
	 **/
	public void display()
	{
		Stage window = new Stage();

		Parent root;
		try
		{
			root = FXMLLoader.load(getClass().getResource("../res/restock_popup.fxml"));
			confirmButton = (Button) root.lookup("#confirmButton");
			amountTextField = (TextField) root.lookup("#amountTextField");

			confirmButton.setOnAction(e ->
			{
				try
				{
					val = Integer.parseInt(amountTextField.getText());
				} catch (NumberFormatException e2)
				{
					new BasicAlertBox("Error", "Invalid parameters", 200, 100);
				}
				window.close();
			});

			window.setTitle("Restock");
			window.initModality(Modality.APPLICATION_MODAL);
			window.setScene(new Scene(root));
			window.setResizable(false);
			window.showAndWait();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Getter for the amount of wine to be restocked.
	 *
	 * @return amount of bottles
	 **/
	public int getAmount()
	{
		return val;
	}
}
