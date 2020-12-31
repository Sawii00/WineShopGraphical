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
 * The class {@code AdminController} defines the controller for the admin.fxml.
 * <p>
 * It allows the admin to have a graphical representation of the sellerList,
 * customerList, wineList and orderList.
 * <p>
 * It gives the admin methods to add, edit or remove sellers.
 */
public class AdminController
{
	/**
	 * Observable list containing the Customer objects to be displayed in the table. 
	 **/
	ObservableList<LoggableUser> customers = FXCollections.<LoggableUser>observableArrayList();
	/**
	 * Observable list containing the Seller objects to be displayed in the table. 
	 **/
	ObservableList<LoggableUser> sellers = FXCollections.<LoggableUser>observableArrayList();
	/**
	 * Observable list containing the Wines objects to be displayed in the table. 
	 **/
	ObservableList<Wine> wines = FXCollections.<Wine>observableArrayList();
	/**
	 * Observable list containing the Orders objects to be displayed in the table. 
	 **/
	ObservableList<Order> orders = FXCollections.<Order>observableArrayList();
	/**
	 * True if the seller table is the currently visible one.
	 **/
	private boolean sellerTableOn = false;
	@FXML
	private ChoiceBox<String> adminChoiceBox;
	@FXML
	private Button addButton;
	@FXML
	private Button editButton;
	@FXML
	private Button logoutButton;
	@FXML
	private Button removeButton;
	@FXML
	private Button refreshButton;
	@FXML
	private TableView<LoggableUser> adminPersonTable;
	@FXML
	private TableView<Wine> adminWineTable;
	@FXML
	private TableView<Order> adminOrderTable;

	/**
	 * Allows to add a new seller.
	 * <p>
	 * It creates a new pop up using the RegistraionBox Class and allows the admin
	 * to fill in his fields.
	 * <p>
	 * It sends a request to the server and and populates the table with the
	 * elements extracted from the response.
	 */
	public void addSeller()
	{
		SellerBox box = new SellerBox("Add Seller");
		if (box.isValid())
		{
			String[] vals = box.getValues();
			Request r = new Request("registerSeller");
			r.addAllParameters(vals);
			Response res = MainClient.client.sendRequest(r);

			if (res.getReturnCode() == StatusCode.INVALID_ARGUMENTS)
				new BasicAlertBox("Error", "Invalid Arguments", 200, 100);
			else
				adminPersonTable.getItems().add(new LoggableUser(vals[0], vals[1], vals[2], vals[3]));
		}
	}

	/**
	 * Allows to edit seller.
	 * <p>
	 * It creates a new pop up using the RegistraionBox Class filling in the fields
	 * with the seller's already existing data, giving the admin the possibility to
	 * change them.
	 * <p>
	 * It sends a request to the server and populates the table with the new
	 * elements extracted from the response.
	 */
	public void editSeller()
	{
		LoggableUser usr = adminPersonTable.getSelectionModel().getSelectedItem();
		String vals[] = { usr.getName(), usr.getSurname(), usr.getEmail(), usr.getPassword() };
		SellerBox box = new SellerBox("Edit Seller", vals);
		if (box.isValid())
		{
			vals = box.getValues();
			Request r = new Request("editSeller");
			r.addParameter(usr.getEmail());
			r.addAllParameters(vals);
			Response res = MainClient.client.sendRequest(r);
			if (res.getReturnCode() == StatusCode.INVALID_ARGUMENTS)
				new BasicAlertBox("Error", "Invalid Arguments", 200, 100);
			else
			{
				adminPersonTable.getItems().get(adminPersonTable.getItems().indexOf(usr)).setName(vals[0]);
				adminPersonTable.getItems().get(adminPersonTable.getItems().indexOf(usr)).setSurname(vals[1]);
				adminPersonTable.getItems().get(adminPersonTable.getItems().indexOf(usr)).setEmail(vals[2]);
				adminPersonTable.getItems().get(adminPersonTable.getItems().indexOf(usr)).setPassword(vals[3]);
				adminPersonTable.refresh();
			}
			adminPersonTable.getSelectionModel().clearSelection();
		}
	}

	/**
	 * Initializes the stage.
	 * <p>
	 * Is populated the ChoiceBox that allows to see the different tables.
	 * <p>
	 * Are set the columns of the tables and is specified that is possible to select
	 * only one row at a time.
	 * <p>
	 * It contains the selection handler.
	 */
	public void initialize()
	{
		String vals[] = { "Customers", "Sellers", "Wines", "Orders" };
		adminChoiceBox.getItems().addAll(vals);
		adminChoiceBox.setValue(vals[0]);
		adminPersonTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
		adminPersonTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("surname"));
		adminPersonTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("email"));
		adminPersonTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("password"));

		adminWineTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
		adminWineTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("producer"));
		adminWineTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("year"));
		adminWineTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("technicalNotes"));
		adminWineTable.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("grapeType"));
		adminWineTable.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("amount"));

		adminOrderTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("customerEmail"));
		adminOrderTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("wineName"));
		adminOrderTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("amount"));

		TableViewSelectionModel<LoggableUser> selectionModelPerson = adminPersonTable.getSelectionModel();
		selectionModelPerson.setSelectionMode(SelectionMode.SINGLE);

		/**
		 * Allows to enable or disable the buttons depending the selected row.
		 */
		ObservableList<LoggableUser> selectedItems = selectionModelPerson.getSelectedItems();
		selectedItems.addListener(new ListChangeListener<LoggableUser>()
		{
			@Override
			public void onChanged(Change<? extends LoggableUser> arg0)
			{
				if (sellerTableOn && arg0.getList().size() != 0)
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
		 * Update the tables after they are selected thanks to the choiceBox.
		 */
		adminChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2)
			{
				adminChoiceBox.getSelectionModel().select((int) arg2);
				refresh();
			}
		});

		refresh();
	}

	/**
	 * Allows to logout the current admin.
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

	/**
	 * Refresh the tableView.
	 * <p>
	 * It sends a request to the server in order to get the update lists that
	 * populate the tables.
	 * <p>
	 * Before populating the tables it clears them to set the update ones.
	 * <p>
	 * It also sets enable or disable the different buttons depending on the
	 * selected tables or tables' rows.
	 */
	public void refresh()
	{
		String selectedTable = adminChoiceBox.getSelectionModel().getSelectedItem();
		adminPersonTable.getSelectionModel().clearSelection();
		if (selectedTable.equals("Customers"))
		{
			sellerTableOn = false;
			adminPersonTable.setVisible(true);
			adminWineTable.setVisible(false);
			adminOrderTable.setVisible(false);
			addButton.setDisable(true);
			editButton.setDisable(true);
			removeButton.setDisable(true);
			Request r = new Request("getCustomersList");
			Response res = MainClient.client.sendRequest(r);
			customers.clear();
			for (String usr : res.getParameters())
			{
				customers.add(Parser.parseUser(usr));
			}
			adminPersonTable.getItems().clear();
			adminPersonTable.getItems().addAll(customers);
		} else if (selectedTable.equals("Sellers"))
		{
			adminPersonTable.getSelectionModel().clearSelection();
			sellerTableOn = true;
			adminPersonTable.setVisible(true);
			adminWineTable.setVisible(false);
			adminOrderTable.setVisible(false);
			addButton.setDisable(false);
			editButton.setDisable(true);
			removeButton.setDisable(true);
			Request r = new Request("getSellersList");
			Response res = MainClient.client.sendRequest(r);
			sellers.clear();
			for (String usr : res.getParameters())
			{
				sellers.add(Parser.parseUser(usr));
			}
			adminPersonTable.getItems().clear();
			adminPersonTable.getItems().addAll(sellers);
		} else if (selectedTable.equals("Orders"))
		{
			sellerTableOn = false;
			adminPersonTable.setVisible(false);
			adminWineTable.setVisible(false);
			adminOrderTable.setVisible(true);
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
			adminOrderTable.getItems().clear();
			adminOrderTable.getItems().addAll(orders);
		} else
		{
			sellerTableOn = false;
			adminPersonTable.setVisible(false);
			adminWineTable.setVisible(true);
			adminOrderTable.setVisible(false);
			addButton.setDisable(true);
			editButton.setDisable(true);
			removeButton.setDisable(true);
			Request r = new Request("getWinesList");
			Response res = MainClient.client.sendRequest(r);
			wines.clear();
			for (String wine : res.getParameters())
			{
				wines.add(Parser.parseWine(wine));
			}
			adminWineTable.getItems().clear();
			adminWineTable.getItems().addAll(wines);
		}
	}

	/**
	 * Allows to remove a seller.
	 * <p>
	 * It sends a request to the server and when it receives a response it removes
	 * the selected seller from the tableView.
	 */
	public void removeSeller()
	{
		LoggableUser usr = adminPersonTable.getSelectionModel().getSelectedItem();
		Request r = new Request("removeSeller");
		r.addParameter(usr.getEmail());
		Response res = MainClient.client.sendRequest(r);
		adminPersonTable.getItems().remove(usr);
		adminPersonTable.refresh();
		adminPersonTable.getSelectionModel().clearSelection();
	}
}