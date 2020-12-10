package assegnamento3;

import java.io.IOException;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The class {@code BuyBox} defines the controller for the buy_popup.fxml.<p>
 * It allows the customer to buy some bottles of wine or to notify to the seller that he 
 * needs more bottles than there are available.
 */
public class BuyBox
{

	Wine wine;
	
	int customerId;

	Button buyButton;

	Button notifyButton;

	Button hereButton;

	Label label1;

	TextField notificationTextField;

	ChoiceBox amountChoiceBox;

	private Stage window;

	/**
	 * Class constructor.
	 * @param w the wine selected
	 * @param customerId the id of the customer who want to buy the selected wine 
	 */
	public BuyBox(Wine w, int customerId)
	{
		this.customerId = customerId;
		this.wine = w;

		display();
	}
	
	/**
	 * If there are some bottles of a certain wine populates the amountChoiceBox with the number of 
	 * available bottles and sets not visible the notifyButton and the notificationTextField. <p>
	 * If there aren't bottles of a certain wine it set not visible the amountChoiceBox and 
	 * the buyButton.
	 */
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
			label1.setVisible(false);
			hereButton.setVisible(false);

		}
	}

	/**
	 * It shows the notifictionTextField where is possible to set the number of bottles that you
	 * want to order and the notifyButton that allows to send a notification to the seller. <p>
	 * It set not visible the buyButton and the amountChoiceBox.
	 */
	private void showNotifyBox()
	{
		notifyButton.setVisible(true);
		buyButton.setVisible(false);
		notificationTextField.setVisible(true);
		amountChoiceBox.setVisible(false);
		label1.setVisible(false);
		hereButton.setVisible(false);
	}

	/**
	 * Creates a new stage and loads the buy_popup.fxml.<p>
	 * Items are extracted from the .fxml file using id. <p> 
	 */
	private void display()
	{
		window = new Stage();

		Parent root;
		try
		{
			root = FXMLLoader.load(getClass().getResource("buy_popup.fxml"));

			buyButton = (Button) root.lookup("#buyButton");
			notifyButton = (Button) root.lookup("#notifyButton");
			hereButton = (Button) root.lookup("#hereButton");
			notificationTextField = (TextField) root.lookup("#notificationTextField");
			amountChoiceBox = (ChoiceBox) root.lookup("#amountChoiceBox");
			label1 = (Label) root.lookup("#label1");

			notifyButton.setOnAction(e ->
			{
				notification();
			});

			hereButton.setOnAction(e ->
			{
				showNotifyBox();
			});

			buyButton.setOnAction(e ->
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

	/**
	 * It allows to buy some bottles of wine.
	 * It sends a request to the server containing the number of bottles, the customerID and the
	 * wineID.
	 */
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
		} else
		{
			BasicAlertBox box = new BasicAlertBox("Success", "Succesfully bought " + val + " bottles", 200, 150, true);
		}
		window.close();
	}


	/**
	 * It allows to notify the seller that the customer needs some bottles of wine that aren't available.
	 * It sends a request to the server containing the number of bottles, the customerID and the
	 * wineID.
	 */
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
			if (res.getReturnCode() != StatusCode.SUCCESS)
			{
				BasicAlertBox box = new BasicAlertBox("Error", "The order could not be processed", 200, 100);
			} else
			{
				BasicAlertBox box = new BasicAlertBox("Success", "You will notified when the bottles will be available",
						350, 150, true);
				window.close();
			}
		} catch (NumberFormatException e)
		{
			BasicAlertBox box = new BasicAlertBox("Error", "Invalid Argument", 200, 100);
		}
	}

}
