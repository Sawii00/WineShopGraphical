package assegnamento3.src;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.animation.TranslateTransition;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Duration;

/**
 * The class {@code LoginRegisterController} defines the controller for the
 * log_reg.fxml.
 * <p>
 * It allows the user to have a graphical representation of the login and of the
 * registration.
 */
public class LoginRegisterController implements Initializable
{

	@FXML
	private AnchorPane layerFather;

	@FXML
	private AnchorPane layerSign;

	@FXML
	private Label labelCreateAccount;

	@FXML
	private Label orUseEmailLabelReg;

	@FXML
	private Label labelSignInToWineShop;

	@FXML
	private TextField regNameTextField;

	@FXML
	private TextField regSurnameTextField;

	@FXML
	private TextField regEmailTextField;

	@FXML
	private PasswordField regPasswordField;

	@FXML
	private Button signUpButton;

	@FXML
	private Label orUseEmailLabelLog;

	@FXML
	private Button signInButton;

	@FXML
	private TextField emailLogTextField;

	@FXML
	private PasswordField logPasswordField;

	@FXML
	private AnchorPane layerRed;

	@FXML
	private Label WelcomeBackLabel;

	@FXML
	private Label wineShopLabel;

	@FXML
	private Label winesfromLabel;

	@FXML
	private Button signInTransButton;

	@FXML
	private Button signUpTransButton;

	@FXML
	private Label helloFriendLabel;

	@FXML
	private Label enterYourDetailLabel;

	@FXML
	private Label andStartJourneyLabel;

	@FXML
	private Button exitButton;

	/**
	 * Allows to go out from the loginRegistration scene.
	 * <p>
	 * It loads the home stage.
	 */
	public void exit()
	{
		Stage mainStage = (Stage) exitButton.getScene().getWindow();
		Parent root;

		try
		{
			root = FXMLLoader.load(getClass().getResource("../res/home.fxml"));
			mainStage.setScene(new Scene(root));
			mainStage.show();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Sets the elements that are shown when the stage is opened.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		helloFriendLabel.setVisible(false);
		enterYourDetailLabel.setVisible(false);
		andStartJourneyLabel.setVisible(false);
		signUpTransButton.setVisible(false);
		orUseEmailLabelLog.setVisible(false);
		labelSignInToWineShop.setVisible(false);
		wineShopLabel.setVisible(true);
		signInButton.setVisible(false);
		emailLogTextField.setVisible(false);
		logPasswordField.setVisible(false);
		regNameTextField.setVisible(true);
		regSurnameTextField.setVisible(true);
		regEmailTextField.setVisible(true);
		regPasswordField.setVisible(true);
		signInButton.setDefaultButton(false);
		signUpButton.setDefaultButton(true);
	}

	/**
	 * Allows the user to log in.
	 * <p>
	 * Takes and sends to the server the parameters from the fields and loads a new
	 * stage depending on the response.
	 */
	public void login()
	{
		System.out.println("Login Pressed");

		Request r = new Request("login");
		r.addParameter(emailLogTextField.getText());
		r.addParameter(logPasswordField.getText());

		Response res = MainClient.client.sendRequest(r);

		if (res.getReturnCode() == StatusCode.SUCCESS)
		{
			Stage mainStage;
			Parent root;

			mainStage = (Stage) signInButton.getScene().getWindow();

			try
			{
				FXMLLoader loader = new FXMLLoader(getClass().getResource("../res/" + res.getParameters().get(0) + ".fxml"));
				root = loader.load();
				if (res.getParameters().get(0).equals("customer"))
				{
					CustomerController controller = loader.getController();
					controller.setCustomerID(Integer.parseInt(res.getParameters().get(1)));
					controller.refresh();
				} else if (res.getParameters().get(0).equals("seller"))
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

	/**
	 * Allows the user to be registered as Customer.
	 * <p>
	 * Takes and sends to the server the parameters from the fields and then clear
	 * the them.
	 */
	public void register()
	{
		Request r = new Request("register");

		String vals[] = new String[4];

		vals[0] = regNameTextField.getText();
		vals[1] = regSurnameTextField.getText();
		vals[2] = regEmailTextField.getText();
		vals[3] = regPasswordField.getText();

		Pattern namePattern = Pattern.compile("[^0-9]+");
		Pattern surnamePattern = Pattern.compile("[^0-9]+");
		Pattern emailPattern = Pattern.compile("[a-zA-Z0-9.\\_]+@[a-zA-Z]+\\.[a-zA-Z]+");
		Matcher matcherName = namePattern.matcher(vals[0]);
		Matcher matcherSurname = surnamePattern.matcher(vals[1]);
		Matcher matcherEmail = emailPattern.matcher(vals[2]);

		if (!(matcherName.matches() && matcherSurname.matches() && matcherEmail.matches()) || vals[3].equals(""))
		{
			new BasicAlertBox("Error", "Invalid parameters", 300, 100);
			return;
		}

		r.addAllParameters(vals);
		Response res = MainClient.client.sendRequest(r);
		regNameTextField.clear();
		regSurnameTextField.clear();
		regEmailTextField.clear();
		regPasswordField.clear();
		if (res.getReturnCode() == StatusCode.INVALID_ARGUMENTS)
			new BasicAlertBox("Error", "Invalid Arguments", 200, 100);
	}

	/**
	 * Handles the animation that allows the layerRed to move to left in order to
	 * set not visible the fields and buttons linked to login and to set visible the
	 * registration ones.
	 * 
	 * @param event the event generated on mouse click
	 */
	@FXML
	private void transToLeft(MouseEvent event)
	{
		TranslateTransition slide = new TranslateTransition();
		slide.setDuration(Duration.seconds(0.7));
		slide.setNode(layerRed);

		slide.setToX(0);
		slide.play();

		layerSign.setTranslateX(0);
		signInButton.setVisible(false);
		orUseEmailLabelLog.setVisible(false);
		labelSignInToWineShop.setVisible(false);

		helloFriendLabel.setVisible(false);
		enterYourDetailLabel.setVisible(false);
		andStartJourneyLabel.setVisible(false);
		signUpTransButton.setVisible(false);
		WelcomeBackLabel.setVisible(true);
		wineShopLabel.setVisible(true);
		winesfromLabel.setVisible(true);
		signInTransButton.setVisible(true);
		orUseEmailLabelReg.setVisible(true);
		labelCreateAccount.setVisible(true);
		signUpButton.setVisible(true);
		emailLogTextField.setVisible(false);
		logPasswordField.setVisible(false);
		regNameTextField.setVisible(true);
		regSurnameTextField.setVisible(true);
		regEmailTextField.setVisible(true);
		regPasswordField.setVisible(true);
		signInButton.setDefaultButton(false);
		signUpButton.setDefaultButton(true);

	}

	/**
	 * Handles the animation that allows the layerRed to move to right in order to
	 * set not visible the fields and buttons linked to registration and to set
	 * visible the login ones.
	 * 
	 * @param event the event generated on mouse click
	 */
	@FXML
	private void transToRight(MouseEvent event)
	{
		TranslateTransition slide = new TranslateTransition();
		slide.setDuration(Duration.seconds(0.7));
		slide.setNode(layerRed);

		slide.setToX(491);
		slide.play();

		layerSign.setTranslateX(-309);
		signInButton.setVisible(true);
		orUseEmailLabelLog.setVisible(true);
		labelSignInToWineShop.setVisible(true);

		helloFriendLabel.setVisible(true);
		enterYourDetailLabel.setVisible(true);
		andStartJourneyLabel.setVisible(true);
		signUpTransButton.setVisible(true);
		WelcomeBackLabel.setVisible(false);
		wineShopLabel.setVisible(false);
		winesfromLabel.setVisible(false);
		signInTransButton.setVisible(false);
		orUseEmailLabelReg.setVisible(false);
		labelCreateAccount.setVisible(false);
		signUpButton.setVisible(false);
		emailLogTextField.setVisible(true);
		logPasswordField.setVisible(true);
		regNameTextField.setVisible(false);
		regSurnameTextField.setVisible(false);
		regEmailTextField.setVisible(false);
		regPasswordField.setVisible(false);
		signInButton.setDefaultButton(true);
		signUpButton.setDefaultButton(false);

	}
}
