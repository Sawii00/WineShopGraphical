package assegnamento3;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RegistrationBox
{
	private String values[] = new String[4];
	private boolean validData = false;
	
	public RegistrationBox(String title, int width, int height) {
		display(title, width, height);
	}

	public String[] getValues()
	{
		return values;
	}
	
	private void display(String title, int width, int height)
	
	{
		Stage window = new Stage();

		Parent root;
		try
		{
			root = FXMLLoader.load(getClass().getResource("reg_popup.fxml"));
			Button btn = (Button)root.lookup("#regConfirmButton");
			TextField name = (TextField)root.lookup("#regNameTextField");
			TextField surname = (TextField)root.lookup("#regSurnameTextField");
			TextField email = (TextField)root.lookup("#regEmailTextField");
			PasswordField password = (PasswordField)root.lookup("#regPasswordTextField");
			Label title_label = (Label)root.lookup("#title");
			title_label.setText(title);
			
			
			
			
			btn.setOnAction(e -> {
				values[0] = name.getText();
				values[1] = surname.getText();
				values[2] = email.getText();
				values[3] = password.getText();
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
	        window.setScene(new Scene(root, width, height));
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
