package assegnamento3.src;

import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * The class {@code SellerController} defines the controller for seller.fxml
 * <p>
 * Defines methods that handle adding, editing, and removing wines, as well as
 * those for handling orders.
 **/
public class SellerController
{

	ObservableList<Wine> wines = FXCollections.<Wine>observableArrayList();
	ObservableList<Order> orders = FXCollections.<Order>observableArrayList();

	@FXML
	ChoiceBox<String> sellerChoiceBox;

	@FXML
	Button addButton;

	@FXML
	Button editButton;

	@FXML
	Button mexButton;

	@FXML
	Button logoutButton;

	@FXML
	Button removeButton;

	@FXML
	Button refreshButton;

	@FXML
	TableView<Wine> sellerWineTable;

	@FXML
	TableView<Order> sellerOrderTable;

	@FXML
	Button restockButton;

	private int sellerId;

	/**
	 * Opens a WineBox popup to allow the Seller to add a new wine to the
	 * collection.
	 **/
	public void add()
	{
		WineBox box = new WineBox("Add Wine");
		if (box.isValid())
		{
			String[] vals = box.getValues();
			Request r = new Request("addWine");
			r.addAllParameters(vals);
			Response res = MainClient.client.sendRequest(r);

			if (res.getReturnCode() == StatusCode.INVALID_ARGUMENTS)
				new BasicAlertBox("Error", "Invalid Arguments", 200, 100);
			else
				refresh();
		}
	}

	/**
	 * Displays the MessageBox to show the inbox messages.
	 **/
	public void displayMessages()
	{
		MessageBox msg = new MessageBox(this.sellerId);
	}

	/**
	 * Displays a WineBox or Orderbox to allow a Seller to edit wines or orders.
	 * <p>
	 * The functionality is based upon the table that is currently visible.
	 **/
	public void edit()
	{
		if (sellerWineTable.isVisible())
		{
			Wine w = sellerWineTable.getSelectionModel().getSelectedItem();
			String vals[] = { w.getName(), w.getProducer(), "" + w.getYear(), w.getTechnicalNotes(), w.getGrapeType(),
					"", w.getWineType().toString() };
			WineBox box = new WineBox("Edit Wine", vals);
			if (box.isValid())
			{
				vals = box.getValues();
				Request r = new Request("editWine");
				r.addParameter("" + w.getID());
				for (int i = 0; i < 7; i++)
				{
					if (i != 5)
						r.addParameter(vals[i]);
				}
				Response res = MainClient.client.sendRequest(r);
				if (res.getReturnCode() == StatusCode.INVALID_ARGUMENTS)
					new BasicAlertBox("Error", "Invalid Arguments", 200, 100);
				else
				{
					/**
					 * Modifies the table to display changes.
					 **/
					sellerWineTable.getItems().get(sellerWineTable.getItems().indexOf(w)).setName(vals[0]);
					sellerWineTable.getItems().get(sellerWineTable.getItems().indexOf(w)).setProducer(vals[1]);
					sellerWineTable.getItems().get(sellerWineTable.getItems().indexOf(w))
							.setYear(Integer.parseInt(vals[2]));
					sellerWineTable.getItems().get(sellerWineTable.getItems().indexOf(w)).setTechnicalNotes(vals[3]);
					sellerWineTable.getItems().get(sellerWineTable.getItems().indexOf(w)).setGrapeType(vals[4]);
					sellerWineTable.getItems().get(sellerWineTable.getItems().indexOf(w)).setWineType(vals[6]);
					sellerWineTable.refresh();
				}
				sellerWineTable.getSelectionModel().clearSelection();

			}
		} else
		{

			Order o = sellerOrderTable.getSelectionModel().getSelectedItem();
			String val = "";

			/**
			 * Retrieves the amount of bottles currently available to clamp the max number
			 * of bottles.
			 **/
			for (Wine w : wines)
			{
				if (w.getName().equals(o.getWineName()))
				{
					val = "" + (w.getAmount() + o.getAmount());
					break;
				}
			}

			OrderBox box = new OrderBox(val);
			if (box.isValid())
			{
				val = box.getValue();
				Request r = new Request("editOrder");
				r.addParameter("" + o.getOrderID());
				r.addParameter(val);
				Response res = MainClient.client.sendRequest(r);
				if (res.getReturnCode() == StatusCode.INVALID_ARGUMENTS)
					new BasicAlertBox("Error", "Invalid Arguments", 200, 100);
				else
				{
					sellerOrderTable.getItems().get(sellerOrderTable.getItems().indexOf(o))
							.setAmount(Integer.parseInt(val));
					sellerOrderTable.refresh();
				}
				sellerOrderTable.getSelectionModel().clearSelection();

			}

		}
	}

	/**
	 * Initializes the Scene by setting up the table, populating the ChoiceBox, and
	 * installing the listeners to changes in the Scene.
	 **/
	public void initialize()
	{
		String vals[] = { "Wines", "Orders" };
		sellerChoiceBox.getItems().addAll(vals);
		sellerChoiceBox.setValue(vals[0]);

		sellerWineTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
		sellerWineTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("producer"));
		sellerWineTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("year"));
		sellerWineTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("technicalNotes"));
		sellerWineTable.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("grapeType"));
		sellerWineTable.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("number"));

		sellerOrderTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("customerEmail"));
		sellerOrderTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("wineName"));
		sellerOrderTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("amount"));

		TableViewSelectionModel<Wine> selectionModelWine = sellerWineTable.getSelectionModel();
		selectionModelWine.setSelectionMode(SelectionMode.SINGLE);

		TableViewSelectionModel<Order> selectionModelOrder = sellerOrderTable.getSelectionModel();
		selectionModelOrder.setSelectionMode(SelectionMode.SINGLE);

		ObservableList<Wine> selectedWines = selectionModelWine.getSelectedItems();

		/**
		 * Listener to changes of currently selected wine.
		 **/
		selectedWines.addListener(new ListChangeListener<Wine>()
		{
			@Override
			public void onChanged(Change<? extends Wine> arg0)
			{
				if (arg0.getList().size() != 0)
				{
					editButton.setDisable(false);
					removeButton.setDisable(false);
					restockButton.setDisable(false);
				} else
				{
					editButton.setDisable(true);
					removeButton.setDisable(true);
					restockButton.setDisable(true);
				}
			}
		});

		ObservableList<Order> selectedOrders = selectionModelOrder.getSelectedItems();

		/**
		 * Listener to changes to currently selected orders.
		 **/
		selectedOrders.addListener(new ListChangeListener<Order>()
		{
			@Override
			public void onChanged(Change<? extends Order> arg0)
			{
				if (arg0.getList().size() != 0)
				{
					editButton.setDisable(false);
					removeButton.setDisable(false);
				} else
				{
					editButton.setDisable(true);
					removeButton.setDisable(true);
				}
			}
		});

		/**
		 * Listener to changes in the ChoiceBox.
		 **/
		sellerChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2)
			{
				sellerChoiceBox.getSelectionModel().select((int) arg2);
				refresh();
			}
		});

		sellerChoiceBox.getSelectionModel().select(1);
		sellerChoiceBox.getSelectionModel().select(0);

	}

	/**
	 * Logs out the Seller by loading back the home page.
	 **/
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

	/**
	 * Refreshes the content of the table by requesting the update list from the
	 * Server.
	 * <p>
	 * The functionality is based upon the table that is currently visible.
	 **/
	public void refresh()
	{
		String selectedTable = sellerChoiceBox.getSelectionModel().getSelectedItem();

		if (selectedTable.equals("Orders"))
		{
			sellerWineTable.setVisible(false);
			sellerOrderTable.setVisible(true);
			addButton.setDisable(true);
			editButton.setDisable(true);
			removeButton.setDisable(true);
			Request r = new Request("getOrderList");
			Response res = MainClient.client.sendRequest(r);
			orders.clear();
			for (String ord : res.getParameters())
			{
				orders.add(Parser.parseOrder(ord));
			}
			sellerOrderTable.getItems().clear();
			sellerOrderTable.getItems().addAll(orders);
		} else
		{
			sellerWineTable.setVisible(true);
			sellerOrderTable.setVisible(false);
			addButton.setDisable(false);
			editButton.setDisable(true);
			removeButton.setDisable(true);
			Request r = new Request("getWinesList");
			Response res = MainClient.client.sendRequest(r);
			wines.clear();
			for (String wine : res.getParameters())
			{
				wines.add(Parser.parseWine(wine));
			}
			sellerWineTable.getItems().clear();
			sellerWineTable.getItems().addAll(wines);
		}
	}

	/**
	 * Allows a Seller to remove a wine or order.
	 * <p>
	 * The functionality is based upon the table that is currently visible.
	 **/
	public void remove()
	{
		if (sellerWineTable.isVisible())
		{
			Wine w = sellerWineTable.getSelectionModel().getSelectedItem();
			Request r = new Request("removeWine");
			r.addParameter("" + w.getID());
			Response res = MainClient.client.sendRequest(r);
			if (res.getReturnCode() == StatusCode.SUCCESS)
			{
				sellerWineTable.getItems().remove(w);
				sellerWineTable.refresh();
				sellerWineTable.getSelectionModel().clearSelection();
			}
		} else
		{
			Order o = sellerOrderTable.getSelectionModel().getSelectedItem();
			Request r = new Request("removeOrder");
			r.addParameter("" + o.getOrderID());
			Response res = MainClient.client.sendRequest(r);
			if (res.getReturnCode() == StatusCode.SUCCESS)
			{
				sellerOrderTable.getItems().remove(o);
				sellerOrderTable.refresh();
				sellerOrderTable.getSelectionModel().clearSelection();
			}

		}
	}

	/**
	 * Opens a RestockBox to allow the Seller to restock the selected wine with a
	 * number of new bottles.
	 **/
	public void restock()
	{
		RestockBox box = new RestockBox();
		int val = box.getAmount();
		Wine w = sellerWineTable.getSelectionModel().getSelectedItem();
		Request r = new Request("restockWine");
		r.addParameter("" + w.getID());
		r.addParameter("" + val);
		Response res = MainClient.client.sendRequest(r);
		if (res.getReturnCode() != StatusCode.SUCCESS)
		{
			new BasicAlertBox("Error", "Invalid parameter", 200, 100);
		} else
		{
			sellerWineTable.getItems().get(sellerWineTable.getItems().indexOf(w)).setAmount(w.getAmount() + val);
			sellerWineTable.refresh();
		}
	}

	/**
	 * Setter for the seller id.
	 *
	 * @param id id of the seller
	 **/
	public void setSellerID(int id)
	{
		this.sellerId = id;
	}
}
