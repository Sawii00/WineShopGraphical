package assegnamento3.src;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The class {@code OrderBox} displays a popup that allows a Seller to change
 * details of an order.
 * <p>
 * Only the amount of bottles in an order can be changed.
 **/
public class OrderBox
{
	/**
	 * Contains the initial value of amount and will contain the new value as well
	 **/
	private String value = "";
	/**
	 * Contains whether the data in {@code value} is valid
	 **/
	private boolean validData = false;

	/**
	 * Class constructor.
	 **/
	public OrderBox()
	{
		display();
	}

	/**
	 * Class costructor.
	 * <p>
	 * Sets the default value of "amount" to the specified one.
	 *
	 * @param defaultValue current value of amount
	 **/
	public OrderBox(String defaultValue)
	{
		value = defaultValue;
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
			root = FXMLLoader.load(getClass().getResource("../res/edit_order.fxml"));
			Button btn = (Button) root.lookup("#confirmButton");
			ChoiceBox chbox = (ChoiceBox) root.lookup("#amountChoiceBox");

			/**
			 * Populating the choicebox with the possible amount values.
			 **/
			for (int i = 1; i <= Integer.parseInt(value); i++)
			{
				chbox.getItems().add(i);
			}
			btn.setOnAction(e ->
			{
				if (!("" + chbox.getValue()).equals(""))
				{
					value = "" + chbox.getValue();
					validData = true;
				}
				window.close();
			});

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
	 * Getter for the amount value.
	 *
	 * @return amount inserted by the Seller
	 **/
	public String getValue()
	{
		return value;
	}

	/**
	 * Returns whether the data in {@code value} is valid or not.
	 *
	 * @return data is valid
	 **/
	public boolean isValid()
	{
		return validData;
	}
}
