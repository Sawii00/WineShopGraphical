package assegnamento3.src;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The class {@code RegistrationBox} displays a popup that allows the
 * registration of a new Seller or the modification of its data.
 * <p>
 * It checks the validity of the provided input.
 **/
public class SellerBox
{
	private String values[] = { "", "", "", "" };
	private boolean validData = false;

	/**
	 * Class constructor.
	 * 
	 * @param title title of the window
	 **/
	public SellerBox(String title)
	{
		display(title);
	}

	/**
	 * Class constructor.
	 * <p>
	 * Sets the default values that will be used to populate the fields in case the
	 * seller is being edited.
	 *
	 * @param defaultValues default values of the fields
	 * @param title         title of the window
	 **/
	public SellerBox(String title, String[] defaultValues)
	{
		if (defaultValues.length != 4)
			throw new IllegalArgumentException("Invalid Arguments to Regitration Box");
		values = defaultValues;
		display(title);
	}

	/**
	 * Displays the popup.
	 * 
	 * @param title title of the window
	 **/
	private void display(String title)

	{
		Stage window = new Stage();

		Parent root;
		try
		{
			root = FXMLLoader.load(getClass().getResource("../res/reg_popup.fxml"));
			Button btn = (Button) root.lookup("#regConfirmButton");
			TextField name = (TextField) root.lookup("#regNameTextField");
			TextField surname = (TextField) root.lookup("#regSurnameTextField");
			TextField email = (TextField) root.lookup("#regEmailTextField");
			PasswordField password = (PasswordField) root.lookup("#regPasswordTextField");
			Label title_label = (Label) root.lookup("#title");
			title_label.setText(title);
			name.setText(values[0]);
			surname.setText(values[1]);
			email.setText(values[2]);
			password.setText(values[3]);

			btn.setOnAction(e ->
			{
				values[0] = name.getText();
				values[1] = surname.getText();
				values[2] = email.getText();
				values[3] = password.getText();

				Pattern namePattern = Pattern.compile("[^0-9]+");
				Pattern surnamePattern = Pattern.compile("[^0-9]+");
				Pattern emailPattern = Pattern.compile("[a-zA-Z0-9.\\_]+@[a-zA-Z]+\\.[a-zA-Z]+");
				Matcher matcherName = namePattern.matcher(values[0]);
				Matcher matcherSurname = surnamePattern.matcher(values[1]);
				Matcher matcherEmail = emailPattern.matcher(values[2]);
				if (!(matcherName.matches() && matcherSurname.matches() && matcherEmail.matches())
						|| values[3].equals(""))
				{
					new BasicAlertBox("Error", "Invalid parameters", 300, 100);
					return;
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

	/**
	 * Getter for the input values.
	 *
	 * @return array of the provided input
	 **/
	public String[] getValues()
	{
		return values;
	}

	/**
	 * Returns whether the inserted data is valid.
	 *
	 * @return data is valid
	 **/
	public boolean isValid()
	{
		return validData;
	}
}
