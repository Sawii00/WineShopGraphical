package assegnamento3.src;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * The class {@code HomeController} defines the controller for the home.fxml.
 * <p>
 * It allows the user to have a graphical representation of the wines.
 */
public class HomeController implements Initializable
{
	@FXML
	Button searchButton;
	@FXML
	Button loginRegisterButton;
	@FXML
	TextField searchTextField;
	@FXML
	FlowPane flowPane;
	@FXML
	ScrollPane scrollPane;
	@FXML
	RadioButton nameRadioButton;
	@FXML
	RadioButton yearRadioButton;
	@FXML
	Label centerLabel1;
	@FXML
	AnchorPane layer1;
	@FXML
	ImageView labelWallpaper;
	@FXML
	VBox layer2;
	@FXML
	Label centerLabel2;
	@FXML
	Label topLabel;
	@FXML
	Button enterButton;
	@FXML
	Button swipeButton;
	@FXML
	ChoiceBox<String> typeChoiceBox;
	Pane singleWine;
	ArrayList<Wine> viewedWines = new ArrayList<>();
	ArrayList<Wine> wines = new ArrayList<>();
	Image redImage = new Image(getClass().getResource("../res/red.jpg").toExternalForm());
	Image whiteImage = new Image(getClass().getResource("../res/white.jpg").toExternalForm());
	Image roseImage = new Image(getClass().getResource("../res/rose.jpg").toExternalForm());
	Image images[] = { redImage, whiteImage, roseImage };

	/**
	 * It initializes the choiceBox with the wine type and set attributes for the
	 * scrollPane.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
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
			@Override
			// Allows to updates the search for each textField change, thus guaranteeing the
			// search in real time.
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
				// Forcing choicebox text change... it does not happen until out of this handler
				typeChoiceBox.setValue(typeChoiceBox.getItems().get((int) arg2));
				search();
			}
		});

		populateFullList();
	}

	/**
	 * Allows to login or register.
	 * <p>
	 * It loads the LoginRegister scene.
	 */
	public void loginRegister()
	{
		Stage mainStage = (Stage) loginRegisterButton.getScene().getWindow();
		Parent root;
		try
		{
			root = FXMLLoader.load(getClass().getResource("../res/log_reg.fxml"));
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
	private void refresh()
	{
		flowPane.getChildren().clear();
		for (Wine w : viewedWines)
		{
			try
			{
				singleWine = FXMLLoader.load(getClass().getResource("../res/wine.fxml"));
				((ImageView) ((StackPane) ((VBox) singleWine.getChildren().get(0)).getChildren().get(0)).getChildren()
						.get(1)).setImage(images[w.getWineType().ordinal()]);
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
			wineId.setText("");
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
	 * Handles the animation that allows the scene displayed when the program is
	 * opened to scroll up.
	 * 
	 * @param event the event generated on mouse click
	 */
	@FXML
	public void swipe(MouseEvent event)
	{
		TranslateTransition slide = new TranslateTransition();
		slide.setDuration(Duration.seconds(2.0));
		slide.setNode(layer1);
		slide.setToY(-700);
		slide.play();
	}
};

/**
 * The enum {@code SearchType1} defines two research fees for the wines.
 */
enum SearchType
{
	NAME, YEAR
}
