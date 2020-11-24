package assegnamento3;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;



/**
 * The enum {@code SearchType} helps differentiate between wine searches made by Year, Name or ID.
 * */
enum SearchType
{
	NAME, YEAR, ID;
};



/**
* The class {@code Store} manages the Wine Shop through methods that work on the inner data structures. <p>
* It contains lists of wines, clients, sellers and orders. <p>
* {@code notifRequest} holds the requests of restock notifications made by the users. <p>
* {@code currSeller} and {@code currClient} hold the currently logged in user (Client and Sellers cannot be simultaneously logged in). <p>
* NOTE: <p> 
* This implementation does not allow for multiple users logged in simultaneously.<p>
* Modifications are required in case multiple users have to be served in parallel. <p>
* A possible solution would be to pass the user/seller id to each method to allow for authentication and logging.
*/

public class Store {
	
	/**
	 * List of wines owned by the shop.
	 * */
	private ArrayList<Wine> wineList=new ArrayList<Wine>();
	
	/**
	 * List of orders from clients.
	 * */
	private ArrayList<Order> orderList=new ArrayList<Order>();
	
	/**
	 * List of LoggableUsers of the system. Both Clients and Sellers are stored here.
	 * */
	private ArrayList<LoggableUser> userList = new ArrayList<LoggableUser>();
	
	/**
	 * Map of notification requests. <p>
	 * - Wine ID <p>
	 * - Client ID <p>
	 * - Amount of bottles
	 * */
	private Map<Integer, Entry <Integer,Integer>> notifRequest=new HashMap<Integer,Entry<Integer,Integer>>();
	
	/**
	 * Currently logged-in client.
	 * */
	private Customer currClient = null;
	
	/**
	 * Currently logged-in seller.
	 * */
	private Seller currSeller = null;
	
	/**
	* Class Constructor. <p>
	* It sets up an initial list of {@code Wines} available in the Store, some registered {@code Clients}, and one {@code Seller}.
	*/
	public Store()
	{
		userList.add(new Customer("Luca", "Neri", "l.neri@gmail.com", "1234"));
		userList.add(new Customer("Mario", "Rossi", "m.rossi@gmail.com", "1212"));
		userList.add(new Customer("Giuseppe", "Bianchi", "g.bianchi@gmail.com", "3434"));
		userList.add(new Admin("Capo", "Supremo", "p.gay", "0000"));
		userList.add(new Seller("Lucia", "Mazza", "l.mazza@gmail.com", "1111"));
		
		
		wineList.add(new Wine(100000, "Aprilia Merlot", "Cantina Violi", 2012, "Asciutto, morbido e armonico", "Merlot", 10, WineType.RED));
		wineList.add(new Wine(100001, "Lambrusco Reggiano", "Cantina Bruni", 2019, "Asciutto, frizzante e di corpo sapido", "Bacca Nera", 5, WineType.RED));
		wineList.add(new Wine(100002, "Carso Malvasia", "Cantina Gialli", 2016, "Asciutto, sapido e fresco", "Malvasia Istriana", 100, WineType.WHITE));
		wineList.add(new Wine(100003, "Franciacorta Spumante", "Cantina Gialli", 2010, "Fine ed armonico", "Pinot Bianco", 1, WineType.WHITE));
		wineList.add(new Wine(100004, "Negramaro del Salento", "Cantina Pini", 2020, "Fruttato con sentori di tabacco", "Bacca nera", 14, WineType.WHITE));
		wineList.add(new Wine(100005, "La Bioca", "Cantina di via Alba", 2000, "Scorrevole e di buon equilibrio","Bacca nera",2,WineType.RED));
		wineList.add(new Wine(100006, "Calafuria","Cantina Tromaresca", 1999, "Intenso e delicato, note fragranti di frutta bianca", "Negramaro",23,WineType.ROSE));
		wineList.add(new Wine(100007, "Charme rosé","Cantina Firriato", 2010, "Gusto è avvolgente, intenso, di sorprendente equilibrio", "Blend di vitigni autoctoni,",19,WineType.ROSE));

		
		orderList.add(new Order(userList.get(0).getID(), wineList.get(2)));
	}
	
	ArrayList<Wine> getWineList()
	{
		return wineList;
	}
	
	ArrayList<Order> getOrderList()
	{
		return orderList;
	}
	
	ArrayList<LoggableUser> getUserList()
	{
		return userList;
	}

	/**
	* Logs out the currently logged-in user by setting to {@code null} {@code currClient} and {@code currSeller}.
	*/
	private void logout()
	{
		currClient = null;
		currSeller = null;
	}
	
	/**
	* Generic method that returns whether an object is included in a list based on the string representation. <p>
	* It allows to check if a list contains an object without having to match the object's memory address (it only confronts inner data).
	*
	* @param <T> Any data type. Specifically used with Wines and LoggableUsers
	* @param list the list in which to search the object.
	* @param obj the object to find in the list.
	*
	* @return true if the object is found, false otherwise.
	*/
	private <T> boolean contains(ArrayList<T> list, T obj)
	{
		for(T t: list)
		{
			if(obj.toString().equals(t.toString()))
			{
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Checks if a user has already been registered into the store.
	 * @param usr user to be registered.
	 * @return true if already existing, false otherwise.
	 * */
	private boolean alreadyRegistered(LoggableUser usr)
	{
		
		for(LoggableUser u: userList)
		{
			if(u.getEmail().equals(usr.getEmail()))
				return true;
		}
		return false;
	}
	
	
	/**
	* Generic method that returns the object reference contained in the specified list of the specified object. <p>
	* It allows to retrieve an object without having to match the object's memory address by confronting the inner data. <p>
	* Employs the {@code contains} method to check if a suitable match is found. <p>
	* If no matching object is found, it returns {@code null}.
	*
	* @param <T> Any data type. Specifically used with Wines and LoggableUsers
	* @param list the list in which to search the object.
	* @param obj the object to find in the list.
	*
	* @return the object if found, or null.
	*/
	private <T> T get(ArrayList<T> list, T obj)
	{
		for(T t: list)
		{
			if(obj.toString().equals(t.toString()))
			{
				return t;
			}
		}
		return null;
	}
	
	/**
	* It retrieves the Wine that matches the specified id.
	*
	* @param wineId the id of the wine to be retrieved.
	*
	* @return the wine if found, null otherwise.
	*/
	private Wine getWineByID(int wineId)
	{
		for(Wine w: wineList)
		{
			if(w.getID() == wineId)
				return w;
		}
		return null;
	}
	
	public boolean editUser(String email, LoggableUser newUser)
	{
		if(alreadyRegistered(newUser) && !email.equals(newUser.getEmail()))
			return false;
		for(LoggableUser usr: userList)
		{
			if(usr.getEmail().equals(email))
			{
				usr.setEmail(newUser.getEmail());
				usr.setName(newUser.getName());
				usr.setSurname(newUser.getSurname());
				usr.setPassword(newUser.getPassword());
				return true;
			}
		}
		return false;
	}
	
	/**
	* It retrieves the Client that matches the specified id. <p>
	*
	* @param clientId the id of the client to be retrieved.
	*
	* @return the client if found, null otherwise.
	*/
	public Customer getClientByID(int clientId)
	{
		for(LoggableUser w: userList)
		{
			if(w instanceof Customer && ((Customer)w).getID() == clientId)
				return (Customer)w;
		}
		return null;
	}
	

	/**
	* It allows the registration of a new {@code LoggableUser} to the Store. <p>
	* @param usr the client or seller to be registered.
	*/
	
	synchronized public boolean register(LoggableUser usr)
	{
		if(!alreadyRegistered(usr))
		{
			usr.setID(userList.size());
			userList.add(usr);
			return true;
		}
		return false;
	}

	/**
	 * Adds a wine to the wineList. <p>
	 * Precondition: a seller is logged in.
	 * If a seller is logged in it check if the wine you want to add is in wineList. <p>
	 * If the wine passed as parameter is not in the list it is added to the wineList e 
	 * added a wineId. <p>
	 * If it is already present in the list, the number of bottles is updated.
	 * @param w the wine you want to add to the wineList.
	 */
	synchronized public void addWine(Wine w)
	{
		if(currSeller == null)
		{
			System.out.println("No Seller is logged in.");
			return;
		}
		
		if(!contains(wineList, w))
		{
			w.setID(100000+wineList.size());
			wineList.add(w);
			
		}
		else
		{
			Wine cur = get(wineList, w);
			cur.setNumber(w.getNumber() + cur.getNumber());
		}
	}
	
	/**
	* Restocks {@code extraN} numbers of bottles of the specified wine. <p>
	* Precondition: a seller is logged in.
	* @param wineId id of the wine to be restocked.
	* @param extraN number of bottles to be added.
	*/
	synchronized public void restockWine(int wineId, int extraN)
	{
		if(currSeller == null)
		{
			System.out.println("No Seller is logged in.");
			return;
		}
		Wine w=getWineByID(wineId);
		if(w!=null)
		{
			w.setNumber(w.getNumber() + extraN);
			System.out.println("Restocking wine: "+w.getID() + " with "+extraN+" bottles\n");
			for (Integer i: notifRequest.keySet())
			{
				if (i==wineId) 
				{
					Entry<Integer, Integer> e = notifRequest.get(i);
					if((Integer)e.getValue() <= extraN)
					{
						Customer c = getClientByID((Integer)e.getKey());
						System.out.println("Notifying user " + c.getID()+" that the wine is available\n");
						c.newMessage("Wine: "+wineId+" is available.");
					}
				}
			}
		}
	}
	
	/**
	* It allows the login of a {@code LoggableUser} to the Store. <p>
	* It logs out any previous logged-in user.
	* @param email the user's email.
	* @param password the user's password.
	* @return true if email and password are correct, false otherwise.
	*/
	public LoggableUser login(String email, String password)
	{
		for(LoggableUser usr: userList)
		{
			if(usr.getEmail().equals(email) && usr.getPassword().equals(password))
			{
				return (LoggableUser)usr;
			}
		}
		return null;
	}
	
	
	/**
	 * Displays all the wines. <p>
	 * Precondition: there are clients or sellers logged in. <p>
	 * If there are any, it prints to the standard output the list, otherwise displays an error message.
	 */
	public void displayWines()
	{
		if(currClient == null && currSeller == null)
		{
			System.out.println("No user is logged in.");
			return;
		}
		
		for(Wine w: wineList)
		{
			System.out.println(w.toString());
		}
		System.out.println();
	}
	
	/**
	* Displays only the wines that match the specified query. <p>
	* Precondition: there are clients or sellers logged in. <p>
	* It employs the search method and therefore allows to show all the wines that match a specified name or year. <p>
	*
	* @param searchText text to be searched
	* @param searchType type of search (YEAR, NAME, ID)
	*/
	public void displayWines(String searchText, SearchType searchType)
	{
		if(currClient == null && currSeller == null)
		{
			System.out.println("No user is logged in.");
			return;
		}
		
		for(Wine w: search(searchText, searchType))
		{
			System.out.println(w.toString());
		}
		System.out.println();

	}
	
	/**
	* Searches by Year or Name or ID through the list of wines.
	* @param searchText text to be searched for
	* @param searchType type of search
	* @return list of wines that match the query
	*/
	public ArrayList<Wine> search(String searchText, SearchType searchType)
	{
		ArrayList<Wine> res = new ArrayList<Wine>();
		
		if(searchType == SearchType.NAME)
		{
			for(Wine w: wineList)
				if(w.getName().toLowerCase().contains(searchText.toLowerCase()))
					res.add(w);
			
		}
		else if(searchType == SearchType.YEAR)
		{
			for(Wine w: wineList)
				if(w.getYear() == Integer.parseInt(searchText))
					res.add(w);
		}
		else
		{
			for(Wine w: wineList)
				if(w.getID() == Integer.parseInt(searchText))
					res.add(w);
		}
		
		return res;
		
	}
	

	/**
	* Sells {@code amount} bottles of the wine specified by the {@code wineId} to the currently logged-in client. <p>
	*
	* Precondition: a client is logged in.
	*
	* @param wineId id of the wine to be bought
	* @param amount amount of bottles to be bought
	* @param requestIfNotAvailable express willingness to be notified in case of the absence of the needed amount
	*
	* @return true if the wine is available to be bought, false otherwise.
	*/
	synchronized public boolean buy(int wineId, int amount, boolean requestIfNotAvailable)
	{
		if(currClient == null)
		{
			System.out.println("No user is logged in.");
			return false;
		}
		
		Wine w = getWineByID(wineId);
		if(w != null)
		{
			if(w.getNumber() > amount)
			{
				System.out.println("User: " + currClient.toString() + " is buying "+amount+ " bottles of "+ w.getName() + "\n");
				w.setNumber(w.getNumber() - amount);
				orderList.add(new Order(currClient.getID(), new Wine(w.getName(), w.getProducer(), w.getYear(), w.getTechnicalNotes(), w.getGrapeType(), amount, w.getWineType())));
				return true;
			}
			else if (w.getNumber()==amount)
			{
				System.out.println("User: " + currClient.toString() + " is buying all the bottles of "+ w.getName() + "\n");
				System.out.println("Notifying all Sellers about this. \n");
				for (LoggableUser s: userList)
				{
					if(s instanceof Seller)
						((Seller)s).newMessage("Wine: "+w.getID()+" needs to be restocked.");
				}
				
				w.setNumber(w.getNumber() - amount);
				orderList.add(new Order(currClient.getID(), new Wine(w.getName(), w.getProducer(), w.getYear(), w.getTechnicalNotes(), w.getGrapeType(), amount,w.getWineType())));
				return true;			
			}
			else
			{
				System.out.println("User " + currClient.toString() +" is trying to buy "+w.getName()+ " but there is not enough wine.");

				if(requestIfNotAvailable)
				{
					requestWine(wineId, amount);
				}
				return false;
			}
		}
		else
		{
			System.out.println("User " + currClient.toString() + "is trying to buy a wine that does not exist.");
			return false;
		}
		
	}
	
	/**
	 * A notification request for the specified amount of wine associated with the current client is saved. <p>
	 * When the amount is available, the client will be warned with a message. <p>
     * Precondition: a client is logged in.
	 * @param wineId the id of the wine that allows to uniquely identify it.
	 * @param amount the numbers of bottles requested. 
	 */
	synchronized public void requestWine(int wineId, int amount)
	{
		if(currClient == null)return;
		System.out.println("\nUser "+currClient.getName() + " " + currClient.getSurname() + "(" + currClient.getID() + ") has requested to be notified.\n");
		notifRequest.put(wineId, new AbstractMap.SimpleEntry<Integer, Integer>(currClient.getID(),amount));
	}
}
