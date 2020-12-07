package assegnamento3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.collections.ListChangeListener.Change;
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
import javafx.util.Duration;

enum SearchType1
{
	NAME, YEAR
};

public class CustomerController
{

	@FXML
	Button searchButton;

	@FXML
	Button logoutButton;

	@FXML
	Button mexButton;

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
	ChoiceBox<String> typeChoiceBox;

	Pane singleWine;

	static ArrayList<Wine> viewedWines = new ArrayList<>();
	ArrayList<Wine> wines = new ArrayList<>();

	Image redImage = new Image(getClass().getResource("red.jpg").toExternalForm());
	Image whiteImage = new Image(getClass().getResource("white.jpg").toExternalForm());
	Image roseImage = new Image(getClass().getResource("rose.jpg").toExternalForm());

	Image mexImage = new Image(getClass().getResource("message.png").toExternalForm());
	Image mexNotificationImage = new Image(getClass().getResource("not_message.png").toExternalForm());
	
	
	Image images[] = { redImage, whiteImage, roseImage };

	static int customerId;

	public void setCustomerID(int id)
	{
		customerId = id;
	}

	public void displayMessages()
	{
		MessageBox msg = new MessageBox(this.customerId);
		refresh();

	}
	
	

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

	public void refresh()
	{
		if(this.customerId != 0)
		{
			Request r = new Request("getMessages");
			r.addParameter("" + this.customerId);
			Response res = MainClient.client.sendRequest(r);
			
			if(res.getParameters().size() != 0)
				((ImageView)mexButton.getGraphic()).setImage(mexNotificationImage);
			else 
				((ImageView)mexButton.getGraphic()).setImage(mexImage);
		}
		
		flowPane.getChildren().clear();
		for (Wine w : viewedWines)
		{
			try
			{
				// TODO: duplicate instead of reloading each time
				singleWine = FXMLLoader.load(getClass().getResource("wine.fxml"));
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
			amount.setText("" + w.getNumber());
			wineId.setText("" + w.getID());
			flowPane.getChildren().add(singleWine);

		}
	}

	public void logout()
	{
		Stage mainStage;
		Parent root;

		mainStage = (Stage) logoutButton.getScene().getWindow();

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

	private void searchWine(String text, SearchType type)
	{
		viewedWines.clear();
		if (type == SearchType.NAME)
		{
			for (Wine w : wines)
			{
				if (w.getName().toLowerCase().contains(text.toLowerCase())
						&& (typeChoiceBox.getValue().equals("All") || w.getWineType().toString().toLowerCase()
								.equals(typeChoiceBox.getValue().toLowerCase().replace("�", "e"))))
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
				if (String.valueOf(w.getYear()).contains(String.valueOf(year))
						&& (typeChoiceBox.getValue().equals("All") || w.getWineType().toString().toLowerCase()
								.contains(typeChoiceBox.getValue().toLowerCase().replace("�", "e"))))
				{
					viewedWines.add(w);
				}
			}
		}

		refresh();

	}

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

	public void initialize() throws IOException
	{

		typeChoiceBox.getItems().add("All");
		typeChoiceBox.getItems().add("Red");
		typeChoiceBox.getItems().add("White");
		typeChoiceBox.getItems().add("Ros�");
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

		// retrieve wineList from server

	}

}
