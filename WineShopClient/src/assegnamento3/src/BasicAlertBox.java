package assegnamento3.src;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The class {@code BasicAlertBox} displays a basic popup in which title and
 * message can be customized.
 **/
public class BasicAlertBox
{
	/**
	 * Class constructor.
	 * <p>
	 * Invokes the method display that is responsible to show it.
	 * 
	 * @param title   title of the window
	 * @param message message to be displayed
	 * @param width   width of the window
	 * @param height  height of the window
	 **/
	public BasicAlertBox(String title, String message, int width, int height)
	{
		display(title, message, width, height, false);
	}

	/**
	 * Class constructor.
	 * <p>
	 * Invokes the method display that is responsible to show it.
	 * 
	 * @param title   title of the window
	 * @param message message to be displayed
	 * @param width   width of the window
	 * @param height  height of the window
	 * @param red     apply theme to the window
	 **/
	public BasicAlertBox(String title, String message, int width, int height, boolean red)
	{
		display(title, message, width, height, red);
	}

	/**
	 * Displays the popup.
	 * 
	 * @param title   title of the window
	 * @param message message to be displayed
	 * @param width   width of the popup
	 * @param height  height of the popup
	 **/
	private void display(String title, String message, int width, int height, boolean red)
	{
		Stage window = new Stage();

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(width);
		window.setMaxWidth(width);
		window.setMinHeight(height);
		window.setMaxHeight(height);
		window.setResizable(false);

		Label label = new Label();
		label.setText(message);

		Button closeButton = new Button("Close");
		if (red)
		{
			closeButton.setStyle(
					"-fx-background-color: #5b0417; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: white;");
			label.setTextFill(Color.WHITE);
		}
		closeButton.setOnAction(e -> window.close());
		VBox layout = new VBox();
		layout.getChildren().addAll(label, closeButton);
		layout.setAlignment(Pos.CENTER);

		VBox.setMargin(closeButton, new Insets(20, 0, 0, 0));
		if (red)
			layout.setStyle("-fx-background-color: #5b0417");

		Scene scene = new Scene(layout);
		window.setScene(scene);

		window.showAndWait();
	}
}
