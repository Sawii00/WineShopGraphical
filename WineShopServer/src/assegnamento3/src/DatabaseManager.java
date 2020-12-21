package assegnamento3.src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The class {@code DatabaseManager} handles the persistence of the data.
 * <p>
 * Creates a connection to the database and implements the methods for saving
 * wine, user, order, notification, and message lists as well as loading them
 * back from main memory.
 **/
public class DatabaseManager
{
	Connection conn = null;
	Statement st = null;
	String url;
	String args;
	String user;
	String password;

	/**
	 * Class constructor.
	 * <p>
	 * Initializes the connection to the database and creates the tables if not
	 * already present.
	 * <p>
	 * A default Admin "m.rossi@gmail.com" with password "1234" is created.
	 * <p>
	 * A default Seller "g.bianchi@gmail.com" with password "1234" is created.
	 * <p>
	 * A default list of Wines is provided.
	 * 
	 * @param url      url of db
	 * @param args     arguments
	 * @param user     user
	 * @param password password
	 * 
	 * @throws SQLException Invalid sql
	 **/
	public DatabaseManager(String url, String args, String user, String password) throws SQLException
	{
		this.url = url;
		this.args = args;
		this.user = user;
		this.password = password;

		open();

		String createTableWines = "create table if not exists wines (" + "id int primary key,"
				+ "name varchar(50) not null," + "producer varchar(50) not null," + "year int not null,"
				+ "technicalNotes varchar(500) not null," + "grapeType varchar(50) not null," + "amount int not null,"
				+ "wineType int not null" + ")";
		st.executeUpdate(createTableWines);
		// The table did not exist and default wines are added.
		ResultSet res = st.executeQuery("select * from wines");
		if (!res.next())
		{
			st.executeUpdate("insert into wines values (0, 'Barolo', 'Marchesi di Barolo', 2012, "
					+ "'Profumo intenso con sentori floreali e fruttati, ricco di corpo e asciutto.', "
					+ "'Uve Nebbiolo'," + "20," + "0)");

			st.executeUpdate("insert into wines values (1, 'Amarone', 'Fratelli Vogatori', 2018, "
					+ "'Profumo di frutta matura, muschio e tabacco. Ricco e denso. Spiccato grado alcolico.', "
					+ "'Corvina, Rondinella, Molinara'," + "5," + "0)");

			st.executeUpdate("insert into wines values (2, 'Cannonau', 'Dorgali', 2020, "
					+ "'Profumo leggero di frutti rossi, sapido e caldo.', " + "'Uva Cannonau'," + "50," + "0)");

			st.executeUpdate("insert into wines values (3, 'Merlot', 'Colli Morenici', 2015, "
					+ "'Profumo intenso di ciliegia e amarena. Gusto armonico ed asciutto.'," + "'Uve Merlot'," + "12,"
					+ "0)");

			st.executeUpdate("insert into wines values (4, 'Marzemino', 'Cantina La Vis', 2018, "
					+ "'Aroma gentile di frutti di bosco e menta. Gusto secco, morbido e succoso.', "
					+ "'Uve Marzemino'," + "10," + "0)");

			st.executeUpdate("insert into wines values (5, 'Gewurztraminer', 'Cantina Tramin', 2020, "
					+ "'Aroma intenso di rose, gelsomino e frutta dolce. Gusto pieno e persistente.', "
					+ "'Uve Picolit'," + "2," + "1)");

			st.executeUpdate("insert into wines values (6, 'Picolit', 'Cantina Rocchi di Manzano', 2010, "
					+ "'Profumo di mandorla, pesca e scorza di arancia. Sentori di frutta candita e miele.', "
					+ "'Uve Marzemino'," + "10," + "1)");

			st.executeUpdate("insert into wines values (7, 'Verdicchio', 'Cantina Sartarelli', 2008, "
					+ "'Profumo di fiori di campo, pesca e mela. Gusto finale di mandorla amarognola.', "
					+ "'Uve Verdicchio'," + "15," + "1)");

			st.executeUpdate("insert into wines values (8, 'Lagrein Rosé', 'Cantina Tramin', 2017, "
					+ "'Aromi di frutta matura, fresco e leggermente vinoso.', " + "'Uve Lagrein'," + "5," + "2)");

			st.executeUpdate("insert into wines values (9, 'Vetere', 'Cantina San Salvatore', 2019, "
					+ "'Delicato profumo di fiori, fresco e sapido con sapore di agrumi finale.', " + "'Uve Aglianico',"
					+ "3," + "2)");
		}
		String createTableUsers = "create table if not exists users (" + "id int primary key,"
				+ "name varchar(50) not null," + "surname varchar(50) not null," + "email varchar(50) not null,"
				+ "password varchar(50) not null," + "type int not null" + ")";
		st.executeUpdate(createTableUsers);
		ResultSet firstAdmin = st.executeQuery("select * from users where id=0");
		// adding administrator
		if (!firstAdmin.next())
			st.executeUpdate("insert into users values (0, 'Mario', 'Rossi', 'm.rossi@gmail.com', '1234', 2)");

		ResultSet firstSeller = st.executeQuery("select * from users where id=1");
		// adding seller
		if (!firstSeller.next())
			st.executeUpdate("insert into users values (1, 'Giulio', 'Bianchi', 'g.bianchi@gmail.com', '1234', 1)");

		String createTableOrders = "create table if not exists orders (" + "orderId int primary key,"
				+ "clientId int not null," + "wineId int not null," + "amount int not null" + ")";
		st.executeUpdate(createTableOrders);

		String createTableNotifications = "create table if not exists notifications ("
				+ "id int primary key auto_increment," + "clientId int not null," + "wineId int not null,"
				+ "amount int not null" + ")";
		st.executeUpdate(createTableNotifications);

		String createTableMessages = "create table if not exists messages(" + "id int primary key auto_increment,"
				+ "userId int not null," + "text varchar(100)" + ")";
		st.executeUpdate(createTableMessages);
	}

	/**
	 * @return true if the connection is open, false otherwise.
	 **/
	public boolean isOpen()
	{
		return conn != null;
	}

	/**
	 * Closes the connection to the database.
	 **/
	public void close()
	{
		try
		{
			conn.close();
			conn = null;
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Opens the connection to the database.
	 * <p>
	 * Delegates possible errors to the invoking method to allow for the
	 * disconnection of the server.
	 * 
	 * @throws SQLException Could not connect to db
	 **/
	public void open() throws SQLException
	{
		conn = DriverManager.getConnection(url + args, user, password);
		st = conn.createStatement();
	}

	/**
	 * Saves the given wineList to the database.
	 * 
	 * @param wines list of wines
	 **/
	public void saveWineList(ArrayList<Wine> wines)
	{
		try
		{
			String insert = "insert into wines values (?, ?, ?, ?, ?, ?, ?, ?)";
			String delete = "delete from wines";
			st.executeUpdate(delete);
			PreparedStatement stm = conn.prepareStatement(insert);
			for (Wine w : wines)
			{
				stm.setString(1, "" + w.getID());
				stm.setString(2, w.getName());
				stm.setString(3, w.getProducer());
				stm.setString(4, "" + w.getYear());
				stm.setString(5, w.getTechnicalNotes());
				stm.setString(6, w.getGrapeType());
				stm.setString(7, "" + w.getAmount());
				switch (w.getWineType())
				{
				case RED:
					stm.setString(8, "0");
					break;
				case ROSE:
					stm.setString(8, "1");
					break;
				case WHITE:
					stm.setString(8, "2");
					break;
				default:
					throw new IllegalArgumentException("Unexpected value: " + w.getWineType());
				}
				stm.addBatch();
			}
			stm.executeBatch();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves the winelist from the database.
	 * 
	 * @return list of the stored wines
	 **/
	public ArrayList<Wine> getWineList()
	{
		ArrayList<Wine> res = new ArrayList<>();
		try
		{
			String select = "select * from wines";

			ResultSet set = st.executeQuery(select);
			while (set.next())
			{
				WineType t = null;
				switch (set.getInt("wineType"))
				{
				case 0:
					t = WineType.RED;
					break;
				case 1:
					t = WineType.WHITE;
					break;
				case 2:
					t = WineType.ROSE;
					break;
				}
				Wine w = new Wine(set.getInt("id"), set.getString("name"), set.getString("producer"),
						set.getInt("year"), set.getString("technicalNotes"), set.getString("grapeType"),
						set.getInt("amount"), t);
				res.add(w);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * Saves the given userList to the database and for each user all the personal
	 * messages.
	 * 
	 * @param users list of users
	 **/
	public void saveUserList(ArrayList<LoggableUser> users)
	{
		try
		{
			String insert = "insert into users values (?, ?, ?, ?, ?, ?)";
			String insertMex = "insert into messages (userId, text) values (?, ?)";

			st.executeUpdate("delete from users");
			st.executeUpdate("delete from messages");

			PreparedStatement stm = conn.prepareStatement(insert);
			PreparedStatement stmMex = conn.prepareStatement(insertMex);
			for (LoggableUser u : users)
			{
				stm.setString(1, "" + u.getID());
				stm.setString(2, u.getName());
				stm.setString(3, u.getSurname());
				stm.setString(4, u.getEmail());
				stm.setString(5, u.getPassword());
				if (u instanceof Customer)
				{
					stm.setString(6, "0");
					ArrayList<String> mex = ((Customer) u).getMessageList();
					for (String m : mex)
					{
						stmMex.setString(1, "" + u.getID());
						stmMex.setString(2, m);
						stmMex.addBatch();
					}
					stmMex.executeBatch();
				} else if (u instanceof Seller)
				{
					stm.setString(6, "1");
					ArrayList<String> mex = ((Seller) u).getMessageList();
					for (String m : mex)
					{
						stmMex.setString(1, "" + u.getID());
						stmMex.setString(2, m);
						stmMex.addBatch();
					}
					stmMex.executeBatch();
				} else
					stm.setString(6, "2");
				stm.addBatch();
			}
			stm.executeBatch();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves the userList from the database and loads all the personal messages.
	 * 
	 * @return list of the stored users
	 **/
	public ArrayList<LoggableUser> getUserList()
	{
		ArrayList<LoggableUser> res = new ArrayList<>();
		try
		{
			String select = "select * from users";
			String selectMex = "select * from messages where userId = ";
			ResultSet set = st.executeQuery(select);
			Statement st2 = conn.createStatement();
			while (set.next())
			{
				LoggableUser u = null;
				ResultSet mexSet = st2.executeQuery(selectMex + set.getInt("id"));
				switch (set.getInt("type"))
				{
				case 0:
					u = new Customer(set.getInt("id"), set.getString("name"), set.getString("surname"),
							set.getString("email"), set.getString("password"));
					while (mexSet.next())
						((Observer) u).newMessage(mexSet.getString("text"));

					break;
				case 1:
					u = new Seller(set.getInt("id"), set.getString("name"), set.getString("surname"),
							set.getString("email"), set.getString("password"));
					while (mexSet.next())
						((Observer) u).newMessage(mexSet.getString("text"));
					break;
				case 2:
					u = new Admin(set.getInt("id"), set.getString("name"), set.getString("surname"),
							set.getString("email"), set.getString("password"));
					break;
				}
				res.add(u);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * Saves the given orderlist to the database.
	 * 
	 * @param orders list of orders
	 **/
	public void saveOrderList(ArrayList<Order> orders)
	{
		try
		{
			String insert = "insert into orders values (?, ?, ?, ?)";
			String delete = "delete from orders";
			st.executeUpdate(delete);
			PreparedStatement stm = conn.prepareStatement(insert);
			for (Order o : orders)
			{
				stm.setString(1, "" + o.getOrderID());
				stm.setString(2, "" + o.getClient());
				stm.setString(3, "" + o.getWineID());
				stm.setString(4, "" + o.getAmount());
				stm.addBatch();
			}
			stm.executeBatch();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves the orderList from the database.
	 * 
	 * @return list of the stored orders
	 **/
	public ArrayList<Order> getOrderList()
	{
		ArrayList<Order> res = new ArrayList<>();
		try
		{
			String select = "select * from orders";

			ResultSet set = st.executeQuery(select);
			while (set.next())
			{
				Order o = new Order(set.getInt("orderId"), set.getInt("clientId"), set.getInt("wineId"),
						set.getInt("amount"));
				res.add(o);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * Saves the given notificationMap to the database.
	 * 
	 * @param notifications map of notifications
	 **/
	public void saveNotificationList(Map<Integer, Entry<Integer, Integer>> notifications)
	{
		try
		{
			String insert = "insert into notifications (clientId, wineId, amount) values (?, ?, ?)";
			String delete = "delete from notifications";
			st.executeUpdate(delete);
			PreparedStatement stm = conn.prepareStatement(insert);
			for (Integer clientId : notifications.keySet())
			{
				Entry<Integer, Integer> wine = notifications.get(clientId);
				stm.setString(1, "" + clientId);
				stm.setString(2, "" + wine.getKey());
				stm.setString(3, "" + wine.getValue());
				stm.addBatch();
			}
			stm.executeBatch();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves the notificationMap from the database.
	 * 
	 * @return list of the stored notifications
	 **/
	public Map<Integer, Entry<Integer, Integer>> getNotificationList()
	{
		Map<Integer, Entry<Integer, Integer>> notifications = new HashMap<>();
		try
		{
			String select = "select * from notifications";

			ResultSet set = st.executeQuery(select);
			while (set.next())
			{
				notifications.put(set.getInt("clientId"),
						new AbstractMap.SimpleEntry<Integer, Integer>(set.getInt("wineId"), set.getInt("amount")));
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return notifications;
	}
}
