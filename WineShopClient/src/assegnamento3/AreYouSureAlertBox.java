package assegnamento3;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AreYouSureAlertBox {

	public AreYouSureAlertBox(String title, String message, int width, int height, ITodo todo) {
		display(title, message, width, height, todo);
	}

	private void display(String title, String message, int width, int height, ITodo todo) {
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

		
		
		Button yes = new Button("Yes");
		yes.setDefaultButton(true);
		yes.setOnAction(e -> {
			onClose(todo);
			window.close();
		});
		Button no = new Button("No");
		no.setOnAction(e -> {
			window.close();
		});

		VBox layout = new VBox();
		layout.getChildren().addAll(label, yes, no);
		layout.setAlignment(Pos.CENTER);

		VBox.setMargin(yes, new Insets(20, 0, 0, 0));
		VBox.setMargin(no, new Insets(20, 0, 0, 0));

		Scene scene = new Scene(layout);
		window.setScene(scene);

		window.showAndWait();
	}

	void onClose(ITodo todo) {
		todo.todo();
	}

}
