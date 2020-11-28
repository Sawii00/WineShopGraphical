package assegnamento3;


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

public class SellerController {

	ObservableList<Wine> wines = FXCollections.<Wine>observableArrayList();	
	ObservableList<Order> orders = FXCollections.<Order>observableArrayList();	
	
	@FXML
	ChoiceBox<String> sellerChoiceBox;

	@FXML
	Button addButton;
	
	@FXML
	Button editButton;
	
	@FXML
	Button removeButton;

	@FXML
	Button refreshButton;
	
	@FXML
	TableView<Wine> sellerWineTable;
	
	@FXML
	TableView<Order> sellerOrderTable;
	

	public void add() 
	{
		WineBox box = new WineBox("Add Wine");
		if(box.isValid())
		{
			String[] vals = box.getValues();
			Request r = new Request("addWine");
			r.addAllParameters(vals);
			Response res = MainClient.client.sendRequest(r);
			
			if(res.getReturnCode() == StatusCode.INVALID_ARGUMENTS)
	    		new BasicAlertBox("Error", "Invalid Arguments", 200, 100);
			else
				refresh();
		}
	}
	
	public void edit()
	{
		if(sellerWineTable.isVisible())
		{
			Wine w = sellerWineTable.getSelectionModel().getSelectedItem();
			String vals[] = {w.getName(), w.getProducer(), ""+w.getYear(), w.getTechnicalNotes(), w.getGrapeType(), ""+w.getNumber(), w.getWineType().toString()};
			WineBox box = new WineBox("Edit Wine", vals);
			if(box.isValid())
			{
				vals = box.getValues();
				Request r = new Request("editWine");
				r.addParameter(""+w.getID());
				r.addAllParameters(vals);
				Response res = MainClient.client.sendRequest(r);
				if(res.getReturnCode() == StatusCode.INVALID_ARGUMENTS)
		    		new BasicAlertBox("Error", "Invalid Arguments", 200, 100);
				else
				{
					//TODO
					sellerWineTable.getItems().get(sellerWineTable.getItems().indexOf(w)).setName(vals[0]);
					sellerWineTable.getItems().get(sellerWineTable.getItems().indexOf(w)).setProducer(vals[1]);
					sellerWineTable.getItems().get(sellerWineTable.getItems().indexOf(w)).setYear(Integer.parseInt(vals[2]));
					sellerWineTable.getItems().get(sellerWineTable.getItems().indexOf(w)).setTechnicalNotes(vals[3]);
					sellerWineTable.getItems().get(sellerWineTable.getItems().indexOf(w)).setGrapeType(vals[4]);
					sellerWineTable.getItems().get(sellerWineTable.getItems().indexOf(w)).setNumber(Integer.parseInt(vals[5]));
					sellerWineTable.getItems().get(sellerWineTable.getItems().indexOf(w)).setWineType(vals[6]);
					sellerWineTable.refresh();
				}
				sellerWineTable.getSelectionModel().clearSelection();
	
			}
		}
		else
		{
			//decide if we want to edit only amount or all the info
			//you cannot arbitarily change the customer email or wine name... should go for id instead -->they have to exist
			Order o = sellerOrderTable.getSelectionModel().getSelectedItem();
			String vals[] = {o.getCustomerEmail(), o.getWineName(), ""+o.getAmount()};
			OrderBox box = new OrderBox("Edit Order", vals);
			if(box.isValid())
			{
				vals = box.getValues();
				Request r = new Request("editWine");
				r.addParameter(""+o.getOrderID());
				r.addAllParameters(vals);
				Response res = MainClient.client.sendRequest(r);
				if(res.getReturnCode() == StatusCode.INVALID_ARGUMENTS)
		    		new BasicAlertBox("Error", "Invalid Arguments", 200, 100);
				else
				{
					//TODO
					sellerOrderTable.getItems().get(sellerOrderTable.getItems().indexOf(o)).setCustomerEmail(vals[1]);
					sellerOrderTable.getItems().get(sellerOrderTable.getItems().indexOf(o)).setWineName(vals[2]);
					sellerOrderTable.getItems().get(sellerOrderTable.getItems().indexOf(o)).setAmount(Integer.parseInt(vals[3]));
					sellerOrderTable.refresh();
				}
				sellerOrderTable.getSelectionModel().clearSelection();
	
			}
				
			
			
		}
	}
	
	public void remove() 
	{
		if(sellerWineTable.isVisible())
		{
			Wine w = sellerWineTable.getSelectionModel().getSelectedItem();
			Request r = new Request("removeWine");
			r.addParameter(""+w.getID());
			Response res = MainClient.client.sendRequest(r);
			sellerWineTable.getItems().remove(w);
			sellerWineTable.refresh();
			sellerWineTable.getSelectionModel().clearSelection();
		}
		else
		{
			Order o = sellerOrderTable.getSelectionModel().getSelectedItem();
			Request r = new Request("removeWine");
			r.addParameter(""+o.getOrderID());
			Response res = MainClient.client.sendRequest(r);
			sellerOrderTable.getItems().remove(o);
			sellerOrderTable.refresh();
			sellerOrderTable.getSelectionModel().clearSelection();
			
		}
	}

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
			for(String ord: res.getArguments())
			{
				orders.add(Parser.parseOrder(ord));
			}				
			sellerOrderTable.getItems().clear();
			sellerOrderTable.getItems().addAll(orders);
		}
		else 
		{
			sellerWineTable.setVisible(true);
			sellerOrderTable.setVisible(false);
			addButton.setDisable(false);
			editButton.setDisable(true);
			removeButton.setDisable(true);
			Request r = new Request("getWinesList");
			Response res = MainClient.client.sendRequest(r);
			wines.clear();
			for(String wine: res.getArguments())
			{
				wines.add(Parser.parseWine(wine));
			}
			sellerWineTable.getItems().clear();
			sellerWineTable.getItems().addAll(wines);
		}
	}	
		
	

	
	/*public void editSeller()
	{
		LoggableUser usr = sellerPersonTable.getSelectionModel().getSelectedItem();
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
				sellerPersonTable.getItems().get(sellerPersonTable.getItems().indexOf(usr)).setName(vals[0]);
				sellerPersonTable.getItems().get(sellerPersonTable.getItems().indexOf(usr)).setSurname(vals[1]);
				sellerPersonTable.getItems().get(sellerPersonTable.getItems().indexOf(usr)).setEmail(vals[2]);
				sellerPersonTable.getItems().get(sellerPersonTable.getItems().indexOf(usr)).setPassword(vals[3]);
				sellerPersonTable.refresh();
			}
			sellerPersonTable.getSelectionModel().clearSelection();

		}
		
	}*/
	
	
	
	
    public void initialize()
    {
    	String vals[] = {"Wines","Orders"};
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
    	selectedWines.addListener(new ListChangeListener<Wine>() 
    	{

			@Override
			public void onChanged(Change<? extends Wine> arg0) 
			{
				if(arg0.getList().size() != 0)
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
    	
    	ObservableList<Order> selectedOrders = selectionModelOrder.getSelectedItems();
    	selectedOrders.addListener(new ListChangeListener<Order>() 
    	{

			@Override
			public void onChanged(Change<? extends Order> arg0) 
			{
				if(arg0.getList().size() != 0)
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
  
    	
    	sellerChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() 
    	{
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				sellerChoiceBox.getSelectionModel().select((int)arg2);
				refresh();
			}
    	});
    	
		sellerChoiceBox.getSelectionModel().select(1);
		sellerChoiceBox.getSelectionModel().select(0);

    	
    }
}