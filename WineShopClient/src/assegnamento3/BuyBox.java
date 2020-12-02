package assegnamento3;

import java.io.IOException;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BuyBox
{

	Wine wine;
	int customerId;


	Button buyButton;

	Button notifyButton;

	TextField notificationTextField;

	ChoiceBox amountChoiceBox;

	private Stage window;
	
	private void populateChoiceBox()
	{
		if (wine.getNumber() > 0)
		{
			for (int i = 1; i <= wine.getNumber(); i++)
			{
				amountChoiceBox.getItems().add(i);
			}
			amountChoiceBox.setValue(1);
		} else
		{
			notifyButton.setVisible(true);
			buyButton.setVisible(false);
			notificationTextField.setVisible(true);
			amountChoiceBox.setVisible(false);

		}
	}

	private void display()
	{
		window = new Stage();

		Parent root;
		try
		{
			root = FXMLLoader.load(getClass().getResource("buy_popup.fxml"));
			
			
			buyButton = (Button)root.lookup("#buyButton");
			notifyButton = (Button)root.lookup("#notifyButton");
			notificationTextField = (TextField)root.lookup("#notificationTextField");
			amountChoiceBox = (ChoiceBox)root.lookup("#amountChoiceBox");
			
			notifyButton.setOnAction(e->
			{
				notification();
			});

			buyButton.setOnAction(e->
			{
				buy();
			});
			
			populateChoiceBox();

			window.initModality(Modality.APPLICATION_MODAL);
			window.setScene(new Scene(root));
			window.setResizable(false);

			window.showAndWait();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public BuyBox(Wine w, int customerId)
	{
		this.customerId = customerId;
		this.wine = w;
		
		display();

	
	}
	
	public void buy()
	{
		int val = (int) amountChoiceBox.getValue();
		Request r = new Request("buy");
		r.addParameter("" + customerId);
		r.addParameter("" + wine.getID());
		r.addParameter("" + val);
		Response res = MainClient.client.sendRequest(r);
		if (res.getReturnCode() != StatusCode.SUCCESS)
		{
			BasicAlertBox box = new BasicAlertBox("Error", "The order could not be processed", 200, 100);
		}
		else
		{
			BasicAlertBox box = new BasicAlertBox("Success", "Succesfully bought " + val+ " bottles", 200, 150);
		}
		window.close();
	}

	public void notification()
	{
		try
		{
			int val = Integer.parseInt(notificationTextField.getText());
			Request r = new Request("notification");
			r.addParameter("" + customerId);
			r.addParameter("" + wine.getID());
			r.addParameter("" + val);
			Response res = MainClient.client.sendRequest(r);
		} catch (NumberFormatException e)
		{
			BasicAlertBox box = new BasicAlertBox("Error", "Invalid Argument", 200, 100);
		}
		window.close();
	}

	
}
