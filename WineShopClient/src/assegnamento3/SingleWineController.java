package assegnamento3;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.animation.FadeTransition;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SingleWineController
{


	@FXML
	ImageView imageView;

	@FXML
	AnchorPane anchorPane;

	boolean selected = false;

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

	public void select()
	{
		Label boughtW = (Label) anchorPane.lookup("#wineId");
		try {
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
			Pane father = (Pane)anchorPane.getParent();
			father.fireEvent(new RefreshEvent(RefreshEvent.REFRESH));
		} catch (NumberFormatException e) {
			//id is not available --> its the home page
			return;
		}
		
		
	}

	public void initialize()
	{

	}
}
