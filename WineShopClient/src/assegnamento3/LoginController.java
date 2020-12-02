package assegnamento3;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController
{

	@FXML
	private Button signInButton;
	@FXML
	private Button registerButton;
	@FXML
	private TextField emailTextField;
	@FXML
	private PasswordField passwordTextField;

	public void initialize()
	{

	}

	public void login()
	{
		System.out.println("Login Pressed");

		Request r = new Request("login");
		r.addParameter(emailTextField.getText());
		r.addParameter(passwordTextField.getText());

		Response res = MainClient.client.sendRequest(r);

		if (res.getReturnCode() == StatusCode.SUCCESS)
		{
			Stage mainStage;
			Parent root;

			mainStage = (Stage) signInButton.getScene().getWindow();

			try
			{
				FXMLLoader loader = new FXMLLoader(getClass().getResource(res.getParameters().get(0) + ".fxml"));
				root = loader.load();
				if (res.getParameters().get(0).equals("customer"))
				{
					CustomerController controller = loader.getController();
					controller.setCustomerID(Integer.parseInt(res.getParameters().get(1)));
				}
				else if(res.getParameters().get(0).equals("seller"))
				{
					SellerController controller = loader.getController();
					controller.setSellerID(Integer.parseInt(res.getParameters().get(1)));
				}
				mainStage.setScene(new Scene(root));
				mainStage.show();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		} else
		{
			new BasicAlertBox("Error", "Invalid Arguments", 200, 100);
		}

	}

	public void register()
	{
		System.out.println("Register Pressed");
		RegistrationBox rb = new RegistrationBox("Registration");
		if (rb.isValid())
		{
			String[] vals = rb.getValues();
			Request r = new Request("register");
			r.addAllParameters(vals);

			Response res = MainClient.client.sendRequest(r);

			if (res.getReturnCode() == StatusCode.INVALID_ARGUMENTS)
				new BasicAlertBox("Error", "Invalid Arguments", 200, 100);
		}

	}

}