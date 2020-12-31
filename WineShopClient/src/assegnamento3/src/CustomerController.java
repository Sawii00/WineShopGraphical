package assegnamento3.src;

import java.io.IOException;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The class {@code CustomerController} defines the controller for the
 * customer.fxml.
 * <p>
 * It allows the customer to have a graphical representation of the wines, and
 * of his messages box.
 * <p>
 * It gives the customer method to buy wines, notify that he needs some bottles
 * that aren't available, search wines and see his messages.
 */
public class CustomerController
{
	/**
	 * ArrayList containing the wines that are currently visible on the flowPane.
	 * <p>
	 * Updated when a search is made.
	 **/
	static ArrayList<Wine> viewedWines = new ArrayList<>();
	/**
	 * Id of the customer that has logged in.
	 * <p>
	 * Used to authenticate the requests made to the server.
	 **/
	static int customerId;
	@FXML
	private Button searchButton;
	@FXML
	private Button logoutButton;
	@FXML
	private Button mexButton;
	@FXML
	private TextField searchTextField;
	@FXML
	private FlowPane flowPane;
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private RadioButton nameRadioButton;
	@FXML
	private RadioButton yearRadioButton;
	@FXML
	private ChoiceBox<String> typeChoiceBox;
	private Pane singleWine;
	private ArrayList<Wine> wines = new ArrayList<>();
	private Image redImage = new Image(getClass().getResource("../res/red.jpg").toExternalForm());
	private Image whiteImage = new Image(getClass().getResource("../res/white.jpg").toExternalForm());
	private Image roseImage = new Image(getClass().getResource("../res/rose.jpg").toExternalForm());
	private Image mexImage = new Image(getClass().getResource("../res/message.png").toExternalForm());
	private Image mexNotificationImage = new Image(getClass().getResource("../res/not_message.png").toExternalForm());
	private Image images[] = { redImage, whiteImage, roseImage };

	/**
	 * Gives a graphical representation for the messages, creating a new MessageBox
	 * instance.
	 */
	public void displayMessages()
	{
		MessageBox msg = new MessageBox(CustomerController.customerId);
		refresh();
	}

	/**
	 * It initializes the choiceBox with the wine type and set attributes for the
	 * scrollPane.
	 */
	public void initialize()
	{
		typeChoiceBox.getItems().add("All");
		typeChoiceBox.getItems().add("Red");
		typeChoiceBox.getItems().add("White");
		typeChoiceBox.getItems().add("Rosé");
		typeChoiceBox.setValue("All");

		scrollPane.viewportBoundsProperty().addListener(new ChangeListener<Bounds>()
		{
			@Override
			public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds)
			{
				flowPane.setPrefWidth(bounds.getWidth());
				flowPane.setPrefHeight(bounds.getHeight());
			}
		});

		searchTextField.textProperty().addListener(new ChangeListener<String>()
		{
			// Allows to updates the search for each textField change, thus guaranteeing the
			// search in real time.
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2)
			{
				searchTextField.setText(arg2);
				search();
			}
		});

		typeChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2)
			{
				// forcing choicebox text change... it does not happen until out of this handler
				typeChoiceBox.setValue(typeChoiceBox.getItems().get((int) arg2));
				search();
			}
		});

		populateFullList();
	}

	/**
	 * Allows to logout the current customer.
	 * <p>
	 * It loads the home stage.
	 */
	public void logout()
	{
		Stage mainStage;
		Parent root;

		mainStage = (Stage) logoutButton.getScene().getWindow();
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

	/*
	 * Sends a request to the server to get the wineLis. Clears the current list and
	 * updates thanks to the response received by the server.
	 */
	private void populateFullList()
	{
		Request r = new Request("getWinesList");
		Response res = MainClient.client.sendRequest(r);
		wines.clear();
		for (String wine : res.getParameters())
		{
			wines.add(Parser.parseWine(wine));
		}
		searchWine("", SearchType.NAME);
	}

	/**
	 * Synchronizes the graphical representation of messages and wines.
	 * <p>
	 * It sends a request to the server to get new messages and changes the message
	 * image according to whether there are new messages or not
	 * <p>
	 * It clears the wine grid to synchronize the updated list of wines.
	 */
	public void refresh()
	{
		if (CustomerController.customerId != 0)
		{
			Request r = new Request("getMessages");
			r.addParameter("" + CustomerController.customerId);
			Response res = MainClient.client.sendRequest(r);

			if (res.getParameters().size() != 0)
				((ImageView) mexButton.getGraphic()).setImage(mexNotificationImage);
			else
				((ImageView) mexButton.getGraphic()).setImage(mexImage);
		}
		flowPane.getChildren().clear();
		for (Wine w : viewedWines)
		{
			try
			{
				singleWine = FXMLLoader.load(getClass().getResource("../res/wine.fxml"));
				/*
				 * Sets the image according to the type of wine since that images are called as
				 * the type of wines.
				 */
				((ImageView) ((StackPane) ((VBox) singleWine.getChildren().get(0)).getChildren().get(0)).getChildren()
						.get(1)).setImage(images[w.getWineType().ordinal()]);

				singleWine.addEventHandler(RefreshEvent.REFRESH, (e) ->
				{
					populateFullList();
					refresh();
				});
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			Label name = (Label) singleWine.lookup("#nameLabel");
			Label year = (Label) singleWine.lookup("#yearLabel");
			Label producer = (Label) singleWine.lookup("#producerLabel");
			Label techNotes = (Label) singleWine.lookup("#notesLabel");
			Label grapeType = (Label) singleWine.lookup("#grapeTypeLabel");
			Label amount = (Label) singleWine.lookup("#amountLabel");
			Label wineId = (Label) singleWine.lookup("#wineId");
			name.setText(w.getName());
			year.setText("" + w.getYear());
			producer.setText(w.getProducer());
			techNotes.setText(w.getTechnicalNotes());
			grapeType.setText(w.getGrapeType());
			amount.setText("" + w.getAmount());
			wineId.setText("" + w.getID());
			flowPane.getChildren().add(singleWine);
		}
	}

	/**
	 * Handle the search function according to the SearchTYpe and calls the
	 * serachWine function.
	 * <p>
	 * The search can be done by name or by year.
	 */
	public void search()
	{
		// If the serachTextField is empty, it call the popilateFullList to update the
		// list of wines.
		if (searchTextField.getText().equals(""))
		{
			populateFullList();
			return;
		}
		if (nameRadioButton.isSelected())
		{
			searchWine(searchTextField.getText(), SearchType.NAME);
		} else
		{
			searchWine(searchTextField.getText(), SearchType.YEAR);
		}
	}

	/**
	 * Allows to search a wine. The search is done in real time.
	 * 
	 * @param text the key word used to search a wine
	 * @param type the customer can search any wine, or specifying if is red, white
	 *             or rosé.
	 */
	private void searchWine(String text, SearchType type)
	{
		viewedWines.clear();
		if (type == SearchType.NAME)
		{
			for (Wine w : wines)
			{
				/**
				 * To have a correct comparison between the keyword entered in the
				 * searchTextField and the name of the wine, all letters are transformed into
				 * lowercase.
				 * <p>
				 * It does the same thing regarding the comparison between the type of wine and
				 * the text in the typeChoiceBox and then replaces accents on the 'e' of 'rosé'.
				 */
				if (w.getName().toLowerCase().startsWith(text.toLowerCase())
						&& (typeChoiceBox.getValue().equals("All") || w.getWineType().toString().toLowerCase()
								.equals(typeChoiceBox.getValue().toLowerCase().replace("é", "e"))))
				{
					viewedWines.add(w);
				}
			}
		} else
		{
			int year;
			try
			{
				year = Integer.parseInt(text);
			} catch (NumberFormatException e)
			{
				new BasicAlertBox("Error", "Invalid year", 200, 100);
				return;
			}
			for (Wine w : wines)
			{
				/**
				 * It controls if the year searched is contained in a wine year.
				 * <p>
				 * To have a correct comparison between the type of wine and the text in the
				 * typeChoiceBox, all letters are transformed into lowercase.
				 * <p>
				 * Then replaces accents on the 'e' of 'rosé'.
				 */
				if (String.valueOf(w.getYear()).startsWith(String.valueOf(year))
						&& (typeChoiceBox.getValue().equals("All") || w.getWineType().toString().toLowerCase()
								.contains(typeChoiceBox.getValue().toLowerCase().replace("é", "e"))))
				{
					viewedWines.add(w);
				}
			}
		}
		refresh();
	}

	/**
	 * Setter for the customer's id.
	 * 
	 * @param id of the customer
	 */
	public void setCustomerID(int id)
	{
		customerId = id;
	}
};

/**
 * The enum {@code SearchType1} defines two research fees for the wines.
 */
enum SearchType1
{
	NAME, YEAR
}
