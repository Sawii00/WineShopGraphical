package assegnamento3;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RegistrationBox
{

	public RegistrationBox(String title, int width, int height) {
		display(title, width, height);
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
			TextField password = (TextField)root.lookup("#regPasswordTextField");
			
			btn.setOnAction(e -> {
				
				Request r = new Request("register");
				r.addParameter(name.getText());
				r.addParameter(surname.getText());
				r.addParameter(email.getText());
				r.addParameter(password.getText());
				Response res = MainClient.client.sendRequest(r);
				
				if(res.getReturnCode() == StatusCode.SUCCESS)
				{
					window.close();
				}
				else
				{
					new BasicAlertBox("Error", "Invalid Arguments", 200, 100);
				}
				
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

	void onClose(ITodo todo) {
		todo.todo();
	}

}
