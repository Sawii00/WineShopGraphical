package assegnamento3.src;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * The class {@code SingleWineController} defines the controller for the file
 * wine.fxml
 * <p>
 * Implements the ability to select a single Wine Node and interact with it.
 * <p>
 * Defines the transitions that take place when the mouse hovers the single wine
 * and the detailed information are shown.
 * <p>
 * When the wine is clicked upon, a BuyBox is displayed to accomodate the
 * request of a customer to buy a wine.
 **/
public class SingleWineController
{
	@FXML
	ImageView imageView;
	@FXML
	AnchorPane anchorPane;
	boolean selected = false;

	/**
	 * Hides the deatailed information of a wine by sliding back and fading in the
	 * ImageView.
	 * <p>
	 * Activated when the mouse hovers away from a wine.
	 **/
	public void hideInfo()
	{
		TranslateTransition translation = new TranslateTransition(new Duration(350), imageView);
		translation.setToY(0);
		translation.play();
		FadeTransition fading = new FadeTransition(new Duration(400), imageView);
		fading.setFromValue(0);
		fading.setToValue(1.0);
		fading.play();
	}

	public void initialize()
	{
	}

	/**
	 * Displays a BuyBox to allow a Customer to buy the selected wine.
	 * <p>
	 * Only wines in the Customer Page have the inner id label set, therefore if
	 * this wine is on the Homepage instead, the condition fails and no action is
	 * executed, since a logged out user cannot buy wine.
	 **/
	public void select()
	{
		Label boughtW = (Label) anchorPane.lookup("#wineId");

		/**
		 * If the parseInt fails, it means that the id was not set and that this wine is
		 * for display only.
		 * <p>
		 * No popup is displayed since this wine cannot be bought.
		 **/
		try
		{
			int wineId = Integer.parseInt(boughtW.getText());
			Wine w = new Wine(0, "", "", 0, "", "", 0, WineType.RED);
			for (Wine w1 : CustomerController.viewedWines)
			{
				if (w1.getID() == wineId)
				{
					w = w1;
					break;
				}
			}
			BuyBox buyPopup = new BuyBox(w, CustomerController.customerId);
			Pane father = (Pane) anchorPane.getParent();
			/**
			 * Fires the RefreshEvent to the father (CustomerController) to notify that the
			 * wine was bought and that it should refresh the page to display the changes.
			 **/
			father.fireEvent(new RefreshEvent(RefreshEvent.REFRESH));
		} catch (NumberFormatException e)
		{
			return;
		}
	}

	/**
	 * Shows the detailed information of a wine by sliding and fading away the
	 * ImageView.
	 * <p>
	 * Activated when the mouse hovers upon a wine.
	 **/
	public void showInfo()
	{
		TranslateTransition translation = new TranslateTransition(new Duration(350), imageView);
		translation.setToY(-180);
		translation.play();
		FadeTransition fading = new FadeTransition(new Duration(400), imageView);
		fading.setFromValue(1.0);
		fading.setToValue(0);
		fading.play();
	}
}
