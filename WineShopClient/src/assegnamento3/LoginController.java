package assegnamento3;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    
    @FXML
    private Button signInButton;
    @FXML
    private Button registerButton;
    @FXML 
    private TextField emailTextField;
    @FXML
    private PasswordField passwordTextField;

    public void initialize() {
    	
    }

    public void login()
    {
    	System.out.println("Login Pressed");
    	
    	Request r = new Request("login");
		r.addParameter(emailTextField.getText());
		r.addParameter(passwordTextField.getText());
		
		Response res = MainClient.client.sendRequest(r);
		
		if(res.getReturnCode() == StatusCode.SUCCESS)
		{
			Stage mainStage;
	    	Parent root;
	    	
	    	mainStage = (Stage)signInButton.getScene().getWindow();
	    	
	    	try {
	    		root = FXMLLoader.load(getClass().getResource(res.getArguments().get(0)+".fxml"));
				mainStage.setScene(new Scene(root, 800, 400));
				mainStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
		{
			new BasicAlertBox("Error", "Invalid Arguments", 200, 100);
		}
    	
    	
    	
    }
    
    public void register()
    {
    	System.out.println("Register Pressed");
    	new RegistrationBox("Registration", 480, 320);
    	
    }
    
    

}