package assegnamento3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseManager 
{
	
	final String WINE_TABLE = "wines";
	final String USER_TABLE = "users";
	final String ORDER_TABLE = "orders";
	final String MEX_TABLE = "messages";
	
	Connection conn = null;
	Statement st = null;
	
	String url;
	String args;
	String user;
	String password;
	
	
	public DatabaseManager(String url, String args, String user, String password)
	{
		this.url = url;
		this.args = args;
		this.user = user;
		this.password = password;
		try
		{
			open();
			
			String createTableWines = "create table if not exists wines ("
					+ "id int primary key,"
					+ "name varchar(50) not null,"
					+ "producer varchar(50) not null,"
					+ "year int not null,"
					+ "technicalNotes varchar(500) not null,"
					+ "grapeType varchar(50) not null,"
					+ "amount int not null,"
					+ "wineType int not null"
					+ ")";
			st.executeUpdate(createTableWines);
			
			String createTableUsers = "create table if not exists users ("
					+ "id int primary key,"
					+ "name varchar(50) not null,"
					+ "surname varchar(50) not null,"
					+ "email varchar(50) not null,"
					+ "password varchar(50) not null"
					+ ")";
			st.executeUpdate(createTableUsers);
			
			String createTableOrders = "create table if not exists orders ("
					+ "orderId int primary key,"
					+ "clientId int not null,"
					+ "wineId int not null,"
					+ "amount int not null"
					+ ")";
			st.executeUpdate(createTableOrders);
			
			String createTableMessages = "create table if not exists messages ("
					+ "id int primary key auto_increment,"
					+ "clientId int not null,"
					+ "wineId int not null,"
					+ "amount int not null"
					+ ")";
			st.executeUpdate(createTableMessages);
			
			
		}
		catch(SQLException e)
		{
			System.out.println("Error with SQL");
			e.printStackTrace();
		}
		
	}
	
	public void close() 
	{
		try
		{
			conn.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void open()
	{
		try
		{
			conn = DriverManager.getConnection(url + args, user, password);
			st = conn.createStatement();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		
	}
	
	
	public void saveWineList(ArrayList<Wine> wines)
	{
		try
		{
			String insert = "insert into wines values (?, ?, ?, ?, ?, ?, ?, ?)";
			String delete = "delete from wines";
			st.executeUpdate(delete);
			PreparedStatement stm = conn.prepareStatement(insert);
			
			for(Wine w: wines)
			{
				stm.setString(1, ""+w.getID());
				stm.setString(2,  w.getName());
				stm.setString(3,  w.getProducer());
				stm.setString(4,  ""+w.getYear());
				stm.setString(5,  w.getTechnicalNotes());
				stm.setString(6,  w.getGrapeType());
				stm.setString(7,  ""+w.getAmount());
				switch(w.getWineType())
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
	
	
	public ArrayList<Wine> getWineList()
	{
		ArrayList<Wine> res = new ArrayList<>();
		try
		{
			String select = "select * from wines";
			
			ResultSet set = st.executeQuery(select);
			
			while(set.next())
			{
				WineType t = null;
				
				switch(set.getInt("wineType"))
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
				
				Wine w = new Wine(set.getInt("id"), set.getString("name"), set.getString("producer"), set.getInt("year"), set.getString("technicalNotes"), set.getString("grapeType"), set.getInt("amount"), t);
				res.add(w);
			}
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
		
	}
	
	
	
	

}
