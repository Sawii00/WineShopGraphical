package assegnamento3;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class SingleWineController 
{

	@FXML
	ImageView imageView;
	
	@FXML
	AnchorPane anchorPane;
	
	
	public void showInfo()
	{

		//TODO: setDisable
		
		TranslateTransition translation =new TranslateTransition(new Duration(350), imageView);
        translation.setToY(-180);
        translation.play();
        FadeTransition fading = new FadeTransition(new Duration(400), imageView);
        fading.setFromValue(1.0);
        fading.setToValue(0);
        fading.play();

	}
	
	public void hideInfo()
	{
		TranslateTransition translation =new TranslateTransition(new Duration(350), imageView);
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
}
