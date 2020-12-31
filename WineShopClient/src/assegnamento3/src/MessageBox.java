package assegnamento3.src;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The class {@code MessageBox} displays a popup with the inbox messages of a
 * user (Customer or Seller) in a table.
 * <p>
 * The user can view, delete single messages, as well as remove them all from
 * memory.
 **/
public class MessageBox
{
	private Button removeMex;
	private Button removeAll;
	private TableView<Message> table;
	private int customerID;
	private ObservableList<Message> messages = FXCollections.<Message>observableArrayList();

	/**
	 * Class constructor.
	 *
	 * @param id id of the current user
	 **/
	public MessageBox(int id)
	{
		this.customerID = id;
		display();
	}

	/**
	 * Displays the popup.
	 **/
	public void display()
	{
		Stage window = new Stage();

		Parent root;
		try
		{
			root = FXMLLoader.load(getClass().getResource("../res/mex_popup.fxml"));
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

			// Adds the listener to changes in the selection model of the table
			table.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Message>()
			{
				@Override
				public void onChanged(Change<? extends Message> arg0)
				{
					// The remove button is enabled only if a message is selected
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

	/**
	 * Refreshes the messages by retrieving them from the server.
	 **/
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

	/**
	 * Removes all the messages both locally and on the server.
	 **/
	private void removeAll()
	{
		Request r = new Request("deleteAllMessages");
		r.addParameter("" + this.customerID);
		Response res = MainClient.client.sendRequest(r);
		if (res.getReturnCode() != StatusCode.SUCCESS)
			new BasicAlertBox("Error", "Invalid Arguments", 200, 100);
		refreshMessages();
	}

	/**
	 * Removes the selected message both locally and on the server.
	 **/
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
}
