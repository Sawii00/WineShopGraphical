package assegnamento3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeController 
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
	
	Pane singleWine;
	
	ObservableList<Wine> wines = FXCollections.<Wine>observableArrayList();	
	

	
	
	
	public void search()
	{
		if(searchTextField.getText().equals(""))
		{
			populateFullList();
			return;
		}
		
		if(nameRadioButton.isSelected())
		{
			Request rq = new Request("searchWineName");
			rq.addParameter(searchTextField.getText());
			Response res = MainClient.client.sendRequest(rq);
			wines.clear();
			for(String s: res.getArguments())
			{
				wines.add(Parser.parseWine(s));
			}
		}
		else
		{
			int year;
			try
			{
				year = Integer.parseInt(searchTextField.getText());
			}
			catch(NumberFormatException e)
			{
				new BasicAlertBox("Error", "Invalid Year", 200, 100);
				return;
			}
			Request rq = new Request("searchWineYear");
			rq.addParameter(""+year);
			Response res = MainClient.client.sendRequest(rq);
			wines.clear();
			for(String s: res.getArguments())
			{
				wines.add(Parser.parseWine(s));
			}
		}
	}

	public void loginRegister()
	{
		Stage mainStage = (Stage)loginRegisterButton.getScene().getWindow();

    	Parent root;
    	
    	try {
    		root = FXMLLoader.load(getClass().getResource("login.fxml"));
			mainStage.setScene(new Scene(root));
			mainStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void populateFullList()
	{
		Request r = new Request("getWinesList");
		Response res = MainClient.client.sendRequest(r);
		wines.clear();
		for(String wine: res.getArguments())
		{
			wines.add(Parser.parseWine(wine));
		}
	}
	
	public void initialize() throws IOException
	{
		
	


		scrollPane.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
	            @Override
	            public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
	                flowPane.setPrefWidth(bounds.getWidth());
	                flowPane.setPrefHeight(bounds.getHeight());
	            }
	        });
		
		
		wines.addListener(new ListChangeListener<Wine>() 
    	{

			@Override
			public void onChanged(Change<? extends Wine> arg0) 
			{
				flowPane.getChildren().clear();
				for(Wine w: wines)
				{
					//Pane wineBox = new Pane(singleWine);
					try {
						singleWine = FXMLLoader.load(getClass().getResource("wine.fxml"));
						((ImageView)((VBox)singleWine.getChildren().get(0)).getChildren().get(0)).setImage(new Image(getClass().getResource(w.getWineType().toString().toLowerCase()+".jpg").toExternalForm()));
					} catch (IOException e) {
						e.printStackTrace();
					}
					Label name = (Label)singleWine.lookup("#nameLabel");
					Label year = (Label)singleWine.lookup("#yearLabel");
					name.setText(w.getName());
					year.setText(""+w.getYear());
					flowPane.getChildren().add(singleWine);
					
				}
			}
    		
    	});
		
		populateFullList();
		
		
		
		//retrieve wineList from server
		
	}
	
}
