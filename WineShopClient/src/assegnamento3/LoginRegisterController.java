package assegnamento3;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class LoginRegisterController implements Initializable
{

	@FXML
	private AnchorPane layersignup;

	@FXML
	private AnchorPane layer1;

	@FXML
	private Label a2;

	@FXML
	private Label a1;

	@FXML
	private Label b2;

	@FXML
	private TextField r1;

	@FXML
	private TextField r2;

	@FXML
	private TextField r3;

	@FXML
	private PasswordField r4;

	@FXML
	private Button signUpButton;

	@FXML
	private Label b1;

	@FXML
	private Button signInButton;

	@FXML
	private TextField l1;

	@FXML
	private PasswordField l2;

	@FXML
	private AnchorPane layer2;

	@FXML
	private Label s1;

	@FXML
	private Label s2;

	@FXML
	private Label s3;

	@FXML
	private Button signInSButton;

	@FXML
	private Button signUpSButton;

	@FXML
	private Label t1;

	@FXML
	private Label t2;

	@FXML
	private Label t3;

	@FXML
	private Button exitButton;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		t1.setVisible(false);
		t2.setVisible(false);
		t3.setVisible(false);
		signUpSButton.setVisible(false);
		b1.setVisible(false);
		b2.setVisible(false);
		s2.setVisible(true);
		signInButton.setVisible(false);
		l1.setVisible(false);
		l2.setVisible(false);
		r1.setVisible(true);
		r2.setVisible(true);
		r3.setVisible(true);
		r4.setVisible(true);
		signInButton.setDefaultButton(false);
		signUpButton.setDefaultButton(true);

	}

	@FXML
	private void btn(MouseEvent event)
	{
		TranslateTransition slide = new TranslateTransition();
		slide.setDuration(Duration.seconds(0.7));
		slide.setNode(layer2);

		slide.setToX(491);
		slide.play();

		layer1.setTranslateX(-309);
		signInButton.setVisible(true);
		b1.setVisible(true);
		b2.setVisible(true);

		t1.setVisible(true);
		t2.setVisible(true);
		t3.setVisible(true);
		signUpSButton.setVisible(true);
		s1.setVisible(false);
		s2.setVisible(false);
		s3.setVisible(false);
		signInSButton.setVisible(false);
		a1.setVisible(false);
		a2.setVisible(false);
		signUpButton.setVisible(false);
		l1.setVisible(true);
		l2.setVisible(true);
		r1.setVisible(false);
		r2.setVisible(false);
		r3.setVisible(false);
		r4.setVisible(false);
		signInButton.setDefaultButton(true);
		signUpButton.setDefaultButton(false);

		slide.setOnFinished((e ->
		{

		}));
	}

	@FXML
	private void btn2(MouseEvent event)
	{
		TranslateTransition slide = new TranslateTransition();
		slide.setDuration(Duration.seconds(0.7));
		slide.setNode(layer2);

		slide.setToX(0);
		slide.play();

		layer1.setTranslateX(0);
		signInButton.setVisible(false);
		b1.setVisible(false);
		b2.setVisible(false);

		t1.setVisible(false);
		t2.setVisible(false);
		t3.setVisible(false);
		signUpSButton.setVisible(false);
		s1.setVisible(true);
		s2.setVisible(true);
		s3.setVisible(true);
		signInSButton.setVisible(true);
		a1.setVisible(true);
		a2.setVisible(true);
		signUpButton.setVisible(true);
		l1.setVisible(false);
		l2.setVisible(false);
		r1.setVisible(true);
		r2.setVisible(true);
		r3.setVisible(true);
		r4.setVisible(true);
		signInButton.setDefaultButton(false);
		signUpButton.setDefaultButton(true);

		slide.setOnFinished((e ->
		{

		}));
	}

	public void login()
	{
		System.out.println("Login Pressed");

		Request r = new Request("login");
		r.addParameter(l1.getText());
		r.addParameter(l2.getText());

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

	private String vals[] = { "", "", "", "" };

	public String[] getValues()
	{
		return vals;
	}

	public void register()
	{
		System.out.println("Register Pressed");

		Request r = new Request("register");
		vals[0] = r1.getText();
		vals[1] = r2.getText();
		vals[2] = r3.getText();
		vals[3] = r4.getText();

		for (String s : vals)
		{
			if (s.equals(""))
			{
				new BasicAlertBox("Error", "Fill-in all the information", 300, 100);
				return;
			}
		}
		r.addAllParameters(vals);
		Response res = MainClient.client.sendRequest(r);
		r1.clear();
		r2.clear();
		r3.clear();
		r4.clear();
		if (res.getReturnCode() == StatusCode.INVALID_ARGUMENTS)
			new BasicAlertBox("Error", "Invalid Arguments", 200, 100);
	}

	public void exit()
	{
		Stage mainStage = (Stage) exitButton.getScene().getWindow();
		Parent root;

		try
		{
			root = FXMLLoader.load(getClass().getResource("home.fxml"));
			mainStage.setScene(new Scene(root));
			mainStage.show();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
