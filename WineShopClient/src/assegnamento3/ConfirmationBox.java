package assegnamento3;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmationBox
{

	public ConfirmationBox(String title, String message, Action todo)
	{
		display(title, message, todo);
	}

	private void display(String title, String message, Action todo)
	{
		Stage window = new Stage();
		Parent root;
		try
		{
			root = FXMLLoader.load(getClass().getResource("confirmation_box.fxml"));
			window.initModality(Modality.APPLICATION_MODAL);
			window.setTitle(title);
			window.setResizable(false);

			Label messageLabel = (Label) root.lookup("#messageLabel");
			messageLabel.setText(message);

			Button yes = (Button) root.lookup("#yesButton");
			Button no = (Button) root.lookup("#noButton");
			
			yes.setOnAction(e ->
			{
				todo.act();
				window.close();
			});
			
			no.setOnAction(e ->
			{
				window.close();
			});

			
			Scene scene = new Scene(root);
			window.setScene(scene);

			window.showAndWait();
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
	}

	

}
