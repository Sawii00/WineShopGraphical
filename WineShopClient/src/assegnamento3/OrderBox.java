package assegnamento3;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class OrderBox
{
	private String value = "";
	private boolean validData = false;

	public OrderBox()
	{
		display();
	}

	public OrderBox(String defaultValue)
	{
		value = defaultValue;
		display();
	}

	public String getValue()
	{
		return value;
	}

	private void display()

	{
		Stage window = new Stage();

		Parent root;
		try
		{
			root = FXMLLoader.load(getClass().getResource("edit_order.fxml"));
			Button btn = (Button) root.lookup("#confirmButton");
			ChoiceBox chbox = (ChoiceBox) root.lookup("#amountChoiceBox");

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

	public boolean isValid()
	{
		return validData;
	}
}
