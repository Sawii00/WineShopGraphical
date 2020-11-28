package assegnamento3;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WineBox
{
	private String values[] = {"","","","","","",""};
	private boolean validData = false;
	
	public WineBox(String title)
	{
		display(title);
	}
	
	public WineBox(String title, String[] defaultValues) 
	{
		if(defaultValues.length != 7)
			throw new IllegalArgumentException("Invalid Arguments to Wine Box");
		values = defaultValues;
		display(title);
	}

	public String[] getValues()
	{
		return values;
	}
	
	private void display(String title)
	
	{
		Stage window = new Stage();

		Parent root;
		try
		{
			root = FXMLLoader.load(getClass().getResource("wine_popup.fxml"));
			Button btn = (Button)root.lookup("#confirmButton");
			TextField name = (TextField)root.lookup("#nameTextField");
			TextField producer = (TextField)root.lookup("#producerTextField");
			TextField year = (TextField)root.lookup("#yearTextField");
			TextArea notes = (TextArea)root.lookup("#notesTextArea");
			TextField grape = (TextField)root.lookup("#grapeTextField");
			TextField number = (TextField)root.lookup("#numberTextField");
			ChoiceBox<String> type = (ChoiceBox)root.lookup("#typeChoiceBox");



			Label title_label = (Label)root.lookup("#title");
			title_label.setText(title);
			name.setText(values[0]);
			producer.setText(values[1]);
			year.setText(values[2]);
			notes.setText(values[3]);
			grape.setText(values[4]);
			number.setText(values[5]);
			type.setValue(values[6]);

			type.getItems().add("Red");
			type.getItems().add("White");
			type.getItems().add("Rosé");
			
			btn.setOnAction(e -> {
				values[0] = name.getText();
				values[1] = producer.getText();
				values[2] = year.getText();
				values[3] = notes.getText();
				values[4] = grape.getText();
				values[5] = number.getText();
				values[6] = type.getValue();
				 try 
				 {
					 int y=Integer.parseInt(year.getText());
					 int n=Integer.parseInt(number.getText());

				 } catch (NumberFormatException e1)
				 {
					 new BasicAlertBox("Error", "Invalid data", 300, 100);
					 return;
				 }
				 
				 
				for(String s: values)
				{
					if(s.equals(""))
					{
						new BasicAlertBox("Error", "Fill-in all the information", 300, 100);
						return;
					}
				}
				
				validData = true;
				window.close();
			});
			
			window.setTitle(title);
	        window.initModality(Modality.APPLICATION_MODAL);
	        window.setScene(new Scene(root));
	        window.setResizable(false);

			window.showAndWait();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
       
	
		
		
	}

	public boolean isValid()
	{
		return validData;
	}
}
