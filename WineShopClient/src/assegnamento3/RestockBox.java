package assegnamento3;

import java.io.IOException;

import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RestockBox 
{

	private int val = 0;
	Button confirmButton;
	TextField amountTextField;
	
	
	public RestockBox()
	{
		display();
	}
	
	public int getAmount()
	{
		return val;
	}
	
	public void display()
	{
		Stage window = new Stage();

		Parent root;
		try
		{
			root = FXMLLoader.load(getClass().getResource("restock_popup.fxml"));
			confirmButton = (Button) root.lookup("#confirmButton");
			amountTextField = (TextField) root.lookup("#amountTextField");

			confirmButton.setOnAction(e ->
			{
				try {
					val = Integer.parseInt(amountTextField.getText());
				} catch (NumberFormatException e2) {
					new BasicAlertBox("Error", "Invalid parameters", 200, 100);
				}
				
				window.close();
			});
			
			
			
			window.setTitle("Restock");
			window.initModality(Modality.APPLICATION_MODAL);
			window.setScene(new Scene(root));
			window.setResizable(false);
			window.showAndWait();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
}
