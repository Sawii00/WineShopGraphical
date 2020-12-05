package assegnamento3;

import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MessageBox
{

	private Button removeMex;
	private Button removeAll;
	private TableView<Message> table;
	private int customerID;

	ObservableList<Message> messages = FXCollections.<Message>observableArrayList();

	public MessageBox(int id)
	{
		this.customerID = id;
		display();
	}

	private void removeMex()
	{
		Request r = new Request("deleteMessage");
		int mexID = table.getSelectionModel().getSelectedIndex();
		r.addParameter("" + this.customerID);
		r.addParameter("" + mexID);
		Response res = MainClient.client.sendRequest(r);
		if (res.getReturnCode() != StatusCode.SUCCESS)
			new BasicAlertBox("Error", "Invalid Arguments", 200, 100);
		refreshMessages();
	}

	private void removeAll()
	{

		Request r = new Request("deleteAllMessages");
		r.addParameter("" + this.customerID);
		Response res = MainClient.client.sendRequest(r);
		if (res.getReturnCode() != StatusCode.SUCCESS)
			new BasicAlertBox("Error", "Invalid Arguments", 200, 100);
		refreshMessages();

	}

	private void refreshMessages()
	{
		Request r = new Request("getMessages");
		r.addParameter("" + this.customerID);
		Response res = MainClient.client.sendRequest(r);
		if (res.getReturnCode() != StatusCode.SUCCESS)
			new BasicAlertBox("Error", "Invalid Arguments", 200, 100);
		else
		{
			messages.clear();
			table.getItems().clear();
			if (res.getParameters().size() != 0)
			{
				for (int i = 0; i < res.getParameters().size(); ++i)
				{
					String[] mex = res.getParameters().get(i).split("<>");
					messages.add(new Message(Integer.parseInt(mex[0]), mex[1]));
				}
				table.getItems().addAll(messages);
			}

		}
	}

	public void display()
	{
		Stage window = new Stage();

		Parent root;
		try
		{
			root = FXMLLoader.load(getClass().getResource("mex_popup.fxml"));
			removeMex = (Button) root.lookup("#removeMexButton");
			removeAll = (Button) root.lookup("#removeAllButton");
			table = (TableView) root.lookup("#mexTable");

			removeMex.setOnAction(e ->
			{
				removeMex();
			});

			removeAll.setOnAction(e ->
			{
				removeAll();
			});

			table.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("message"));

			TableViewSelectionModel<Message> selectionTable = table.getSelectionModel();
			selectionTable.setSelectionMode(SelectionMode.SINGLE);

			table.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Message>()
			{

				@Override
				public void onChanged(Change<? extends Message> arg0)
				{
					removeMex.setDisable(false);
				}

			});

			refreshMessages();

			window.setTitle("Messages");
			window.initModality(Modality.APPLICATION_MODAL);
			window.setScene(new Scene(root));
			window.setResizable(false);

			window.showAndWait();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

}
