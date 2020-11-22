package assegnamento3;

import javax.swing.event.ChangeEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;

public class AdminController {

	@FXML
	ChoiceBox<String> adminChoiceBox;

	@FXML
	Button addButton;
	
	@FXML
	Button editButton;
	
	@FXML
	TableView<LoggableUser> adminPersonTable;
	
	@FXML
	TableView<Wine> adminWineTable;

	
    public void initialize()
    {
    	String vals[] = {"Customers", "Sellers", "Wines"};
    	adminChoiceBox.getItems().addAll(vals);
    	adminChoiceBox.setValue(vals[0]);
    	
    	
    	
    }
}