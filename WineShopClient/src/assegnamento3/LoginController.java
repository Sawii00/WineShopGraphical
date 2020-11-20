package assegnamento3;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class LoginController {

    
    @FXML
    private Button signInButton;
    @FXML
    private Button registerButton;
    @FXML 
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField;

    public void initialize() {
    	
    }

    public void login()
    {
    	System.out.println("Login Pressed");
    	
    	Stage mainStage;
    	Parent root;
    	
    	mainStage = (Stage)signInButton.getScene().getWindow();
    	try {
			root = FXMLLoader.load(getClass().getResource("admin.fxml"));
			mainStage.setScene(new Scene(root, 600, 400));
			mainStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
    public void register()
    {
    	System.out.println("Register Pressed");
    	new RegistrationBox("Registration", 480, 320);
    	
    }
    
    

}