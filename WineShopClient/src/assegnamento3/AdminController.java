package assegnamento3;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeEvent;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminController {

	
	ObservableList<LoggableUser> customers = FXCollections.<LoggableUser>observableArrayList();
	ObservableList<LoggableUser> sellers = FXCollections.<LoggableUser>observableArrayList();
	ObservableList<Wine> wines = FXCollections.<Wine>observableArrayList();	
	ObservableList<Order> orders = FXCollections.<Order>observableArrayList();	
	boolean sellerTableOn = false;
	
	
	@FXML
	ChoiceBox<String> adminChoiceBox;

	@FXML
	Button addButton;
	
	@FXML
	Button editButton;
	
	@FXML
	Button removeButton;

	@FXML
	Button refreshButton;
	
	@FXML
	TableView<LoggableUser> adminPersonTable;
	
	@FXML
	TableView<Wine> adminWineTable;
	
	@FXML
	TableView<Order> adminOrderTable;
	

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
			for(String usr: res.getArguments())
			{
				customers.add(Parser.parseUser(usr));
			}
			adminPersonTable.getItems().clear();
			adminPersonTable.getItems().addAll(customers);
			
		}
		else if (selectedTable.equals("Sellers")) 
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
			for(String usr: res.getArguments())
			{
				sellers.add(Parser.parseUser(usr));
			}				
			adminPersonTable.getItems().clear();
			adminPersonTable.getItems().addAll(sellers);
		}
		else if (selectedTable.equals("Orders"))
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
			for(String ord: res.getArguments())
			{
				orders.add(Parser.parseOrder(ord));
			}				
			adminOrderTable.getItems().clear();
			adminOrderTable.getItems().addAll(orders);
		}
		else 
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
			for(String wine: res.getArguments())
			{
				wines.add(Parser.parseWine(wine));
			}
			adminWineTable.getItems().clear();
			adminWineTable.getItems().addAll(wines);
		}
	}	
		
	
	
	public void addSeller()
	{
		//TODO fixed dimensions...no need to specify them
		RegistrationBox box = new RegistrationBox("Add Seller", 480, 320);
		if(box.isValid())
		{
			String[] vals = box.getValues();
			Request r = new Request("registerSeller");
			r.addAllParameters(vals);
			Response res = MainClient.client.sendRequest(r);
			
			if(res.getReturnCode() == StatusCode.INVALID_ARGUMENTS)
	    		new BasicAlertBox("Error", "Invalid Arguments", 200, 100);
			else
				adminPersonTable.getItems().add(new LoggableUser(vals[0], vals[1], vals[2], vals[3]));
		}
	}
	
	public void editSeller()
	{
		LoggableUser usr = adminPersonTable.getSelectionModel().getSelectedItem();
		String vals[] = {usr.getName(), usr.getSurname(), usr.getEmail(), usr.getPassword()};
		RegistrationBox box = new RegistrationBox("Edit Seller", 480, 320, vals);
		if(box.isValid())
		{
			vals = box.getValues();
			Request r = new Request("editSeller");
			r.addParameter(usr.getEmail());
			r.addAllParameters(vals);
			Response res = MainClient.client.sendRequest(r);
			if(res.getReturnCode() == StatusCode.INVALID_ARGUMENTS)
	    		new BasicAlertBox("Error", "Invalid Arguments", 200, 100);
			else
			{
				//TODO
				adminPersonTable.getItems().get(adminPersonTable.getItems().indexOf(usr)).setName(vals[0]);
				adminPersonTable.getItems().get(adminPersonTable.getItems().indexOf(usr)).setSurname(vals[1]);
				adminPersonTable.getItems().get(adminPersonTable.getItems().indexOf(usr)).setEmail(vals[2]);
				adminPersonTable.getItems().get(adminPersonTable.getItems().indexOf(usr)).setPassword(vals[3]);
				adminPersonTable.refresh();
			}
			adminPersonTable.getSelectionModel().clearSelection();

		}
		
	}
	
	
	
	
    public void initialize()
    {
    	String vals[] = {"Customers", "Sellers", "Wines","Orders"};
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
    	adminWineTable.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("number"));
    	
    	adminOrderTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("customerEmail"));
    	adminOrderTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("wineName"));
    	adminOrderTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("amount"));
    	
    	TableViewSelectionModel<LoggableUser> selectionModelPerson = adminPersonTable.getSelectionModel();
    	selectionModelPerson.setSelectionMode(SelectionMode.SINGLE);
    	
    	ObservableList<LoggableUser> selectedItems = selectionModelPerson.getSelectedItems();
    	selectedItems.addListener(new ListChangeListener<LoggableUser>() 
    	{

			@Override
			public void onChanged(Change<? extends LoggableUser> arg0) 
			{
				if(sellerTableOn && arg0.getList().size() != 0)
				{
					editButton.setDisable(false);
					removeButton.setDisable(false);
				}
				else
				{
					editButton.setDisable(true);
					removeButton.setDisable(true);
				}
			}
    		
    	});
    	
    	adminChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() 
    	{
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				adminChoiceBox.getSelectionModel().select((int)arg2);
				refresh();
			}
    	});
    	
		adminChoiceBox.getSelectionModel().select(1);
		adminChoiceBox.getSelectionModel().select(0);

    	
    }
}