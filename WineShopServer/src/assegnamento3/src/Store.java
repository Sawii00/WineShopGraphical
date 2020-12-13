package assegnamento3.src;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The enum {@code SearchType} helps differentiate between wine searches made by
 * Year, Name or ID.
 */
enum SearchType
{
	NAME, YEAR, ID;
};

/**
 * The class {@code Store} manages the Wine Shop through methods that work on
 * the inner data structures.
 * <p>
 * It contains lists of wines, clients, sellers, orders, and notification requests.
 * <p>
 * Methods that modify the data structures are synchronized to avoid races between simultanous accesses from different clients.
 */
public class Store
{

	/**
	 * List of wines owned by the shop.
	 */
	private ArrayList<Wine> wineList = new ArrayList<Wine>();

	/**
	 * List of orders from clients.
	 */
	private ArrayList<Order> orderList = new ArrayList<Order>();

	/**
	 * List of LoggableUsers of the system. Both Clients, Sellers, and admins are stored
	 * here.
	 */
	private ArrayList<LoggableUser> userList = new ArrayList<LoggableUser>();

	/**
	 * Map of notification requests.
	 * <p>
	 * - Wine ID
	 * <p>
	 * - Client ID
	 * <p>
	 * - Amount of bottles
	 */
	private Map<Integer, Entry<Integer, Integer>> notifRequest = new HashMap<Integer, Entry<Integer, Integer>>();

	/**
	 * Class Constructor.
	 */
	public Store()
	{
	}

    /**
     * Setter for the wine list.
     *
     * @param wines list of wines
     **/
	public void setWineList(ArrayList<Wine> wines)
	{
		this.wineList.clear();
		this.wineList.addAll(wines);
	}
	
    /**
     * Setter for the user list.
     *
     * @param users list of users
     **/
	public void setUserList(ArrayList<LoggableUser> users)
	{
		this.userList.clear();
		this.userList.addAll(users);
	}
	
    /**
     * Setter for the order list.
     *
     * @param orders list of orders
     **/
	public void setOrderList(ArrayList<Order> orders)
	{
		this.orderList.clear();
		this.orderList.addAll(orders);
	}
	
    /**
     * Setter for the notification map.
     *
     * @param notifications map of notifications
     **/
	public void setNotificationList(Map<Integer, Entry<Integer, Integer>> notifications)
	{
		this.notifRequest.clear();
		this.notifRequest.putAll(notifications);
	}
	
    /**
     * Getter for the wine list.
     *
     * @return list of wines
     **/
	ArrayList<Wine> getWineList()
	{
		return wineList;
	}

    /**
     * Getter for the order list.
     *
     * @return list of orders
     **/
	ArrayList<Order> getOrderList()
	{
		return orderList;
	}

    /**
     * Getter for the user list.
     *
     * @return list of users
     **/
	ArrayList<LoggableUser> getUserList()
	{
		return userList;
	}

    /**
     * Getter for the notifications map.
     *
     * @return map of notifications
     **/
	Map<Integer, Entry<Integer, Integer>> getNotificationList()
	{
		return notifRequest;
	}

    /**
     * Returns the array of messages of the user with the specified id.
     *
     * @return array of messages
     **/
	public String[] getMessages(int id)
	{
		LoggableUser u = getLoggableUserByID(id);
        if(u instanceof Admin)return null;
		Observer o = (Observer) u;
		String mex[] = o.getMessages().split("\n");
		if (mex.length == 1 && mex[0] == "")
			return null;
		else
			return mex;
	}

    /**
     * Deletes the specified message of the user with the specified id. <p>
     * If mexId is negative, it removes all the messages for the specified user.
     *
     * @param userId id of the user
     * @param mexId id of the message (<0 for all messages)
     **/
	public void deleteMessage(int userId, int mexId)
	{
		LoggableUser u = getLoggableUserByID(userId);
        if(u instanceof Admin)return;
		Observer o = (Observer) u;
		if (mexId < 0)
			o.deleteMessages();
		else
			o.deleteMessage(mexId);
	}

	/**
	 * Generic method that returns whether an object is included in a list based on
	 * the string representation.
	 * <p>
	 * It allows to check if a list contains an object without having to match the
	 * object's memory address (it only confronts inner data).
	 *
	 * @param <T>  Any data type. Specifically used with Wines and LoggableUsers
	 * @param list the list in which to search the object.
	 * @param obj  the object to find in the list.
	 *
	 * @return true if the object is found, false otherwise.
	 */
	private <T> boolean contains(ArrayList<T> list, T obj)
	{
		for (T t : list)
		{
			if (obj.toString().equals(t.toString()))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if a user has already been registered into the store.
	 * 
	 * @param usr user to be registered.
	 * @return true if already existing, false otherwise.
	 */
	private boolean alreadyRegistered(LoggableUser usr)
	{

		for (LoggableUser u : userList)
		{
			if (u.getEmail().equals(usr.getEmail()))
				return true;
		}
		return false;
	}

	/**
	 * Generic method that returns the object reference contained in the specified
	 * list of the specified object.
	 * <p>
	 * It allows to retrieve an object without having to match the object's memory
	 * address by confronting the inner data.
	 * <p>
	 * Employs the {@code contains} method to check if a suitable match is found.
	 * <p>
	 * If no matching object is found, it returns {@code null}.
	 *
	 * @param <T>  Any data type. Specifically used with Wines and LoggableUsers
	 * @param list the list in which to search the object.
	 * @param obj  the object to find in the list.
	 *
	 * @return the object if found, or null.
	 */
	private <T> T get(ArrayList<T> list, T obj)
	{
		for (T t : list)
		{
			if (obj.toString().equals(t.toString()))
			{
				return t;
			}
		}
		return null;
	}

	/**
	 * It retrieves the Wine that matches the specified id.
	 *
	 * @param wineId id of the wine
	 *
	 * @return wine if found, null otherwise
	 */
	public Wine getWineByID(int wineId)
	{
		for (Wine w : wineList)
		{
			if (w.getID() == wineId)
				return w;
		}
		return null;
	}

    /**
     * Retrieves the Order that matches the specified id.
     *
     * @param id id of the order
     *
     * @return order if found, null otherwise
     **/
	private Order getOrderByID(int id)
	{
		for (Order o : orderList)
		{
			if (o.getOrderID() == id)
				return o;
		}
		return null;
	}
    
    /**
     * Edits the specified user with the info provided.
     *
     * @param email email of the user to be modified
     * @param newUser object containing the new data for the user
     **/
	public synchronized boolean editUser(String email, LoggableUser newUser)
	{
		if (alreadyRegistered(newUser) && !email.equals(newUser.getEmail()))
			return false;
		for (LoggableUser usr : userList)
		{
			if (usr.getEmail().equals(email))
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
	 * Retrieves the Customer that matches the specified id.
     *
	 * @param clientId the id of the Customer to be retrieved.
	 *
	 * @return the customer if found, null otherwise.
	 */
	public Customer getClientByID(int clientId)
	{
        LoggableUser res = getLoggableUserByID(clientId);
        if(res != null)
            return (Customer)res;
        else
            return null;
	}

    /**
     * Retrieves the LoggableUser that matches the specified id.
     *
     * @param id id of the user
     *
     * @return user if found, null otherwise
     **/
	public LoggableUser getLoggableUserByID(int id)
	{
		for (LoggableUser w : userList)
		{
			if (w.getID() == id)
				return w;
		}
		return null;
	}

	/**
	 * It allows the registration of a new {@code LoggableUser} to the Store. <p>
     * Synchronized among different threads.
	 * 
	 * @param usr the client or seller to be registered.
	 */
	synchronized public boolean register(LoggableUser usr)
	{
		if (!alreadyRegistered(usr))
		{
			usr.setID(userList.size());
			userList.add(usr);
			return true;
		}
		return false;
	}

	/**
	 * Adds a wine to the wineList. <p>
     * Synchronized among different threads.
	 * If the wine passed as parameter is not in the list it is added to the
	 * wineList.<p>
	 * If it is already present in the list, the number of bottles is updated.
	 * 
	 * @param w the wine 
	 */
	synchronized public void addWine(Wine w)
	{
		if (!contains(wineList, w))
		{
			w.setID(100000 + wineList.size());
			wineList.add(w);
		} else
		{
			Wine cur = get(wineList, w);
			cur.setAmount(w.getAmount() + cur.getAmount());
		}
	}

	/**
	 * Restocks {@code extraN} numbers of bottles of the specified wine. <p>
     * Synchronized among different threads.
     *
	 * @param wineId id of the wine to be restocked.
	 * @param extraN number of bottles to be added.
	 */
	synchronized public boolean restockWine(int wineId, int extraN)
	{

		Wine w = getWineByID(wineId);
		if (w != null)
		{
			w.setAmount(w.getAmount() + extraN);
			for (Integer i : notifRequest.keySet())
			{
				if (i == wineId)
				{
					Entry<Integer, Integer> e = notifRequest.get(i);
					if ((Integer) e.getValue() <= w.getAmount())
					{
						Customer c = getClientByID((Integer) e.getKey());
						c.newMessage("Wine: " + w.getName() + " is available.");
					}
				}
			}
			return true;
		} else
			return false;
	}

	/**
	 * It allows the login of a {@code LoggableUser} to the Store. <p>
	 * 
	 * @param email    the user's email.
	 * @param password the user's password.
     *
	 * @return true if email and password are correct, false otherwise.
	 */
	public LoggableUser login(String email, String password)
	{
		for (LoggableUser usr : userList)
		{
			if (usr.getEmail().equals(email) && usr.getPassword().equals(password))
			{
				return (LoggableUser) usr;
			}
		}
		return null;
	}

	/**
	 * Searches by Year or Name or ID through the list of wines.
	 * 
	 * @param searchText text to be searched for
	 * @param searchType type of search
     *
	 * @return list of wines that match the query
	 */
	public ArrayList<Wine> search(String searchText, SearchType searchType)
	{
		ArrayList<Wine> res = new ArrayList<Wine>();

		if (searchType == SearchType.NAME)
		{
			for (Wine w : wineList)
				if (w.getName().toLowerCase().contains(searchText.toLowerCase()))
					res.add(w);

		} else if (searchType == SearchType.YEAR)
		{
			for (Wine w : wineList)
				if (w.getYear() == Integer.parseInt(searchText))
					res.add(w);
		} else
		{
			for (Wine w : wineList)
				if (w.getID() == Integer.parseInt(searchText))
					res.add(w);
		}

		return res;
	}

	/**
	 * Sells {@code amount} bottles of the wine specified by the {@code wineId} to the user specified by the {@code customerId}. <p>
     * Synchronized among different Threads.
	 *
	 * @param wineId                id of the wine
     * @param customerId            id of the customer
	 * @param amount                amount of bottles
	 *
	 * @return true if the wine is sold, false otherwise.
	 */
	synchronized public boolean buy(int wineId, int customerId, int amount)
	{
		Wine w = getWineByID(wineId);
		if (w != null)
		{
			if (w.getAmount() > amount)
			{
				w.setAmount(w.getAmount() - amount);
				orderList.add(new Order(orderList.size(), customerId, w.getID(), amount));
				return true;
			} else if (w.getAmount() == amount)
			{
				for (LoggableUser s : userList)
				{
					if (s instanceof Seller)
						((Seller) s).newMessage("Wine: " + w.getName() + " needs to be restocked.");
				}

				w.setAmount(w.getAmount() - amount);
				orderList.add(new Order(orderList.size(), customerId, w.getID(), amount));
				return true;
			} else
			{
				return false;
			}
		} else
		{
			return false;
		}

	}

	/**
	 * A notification request for the specified amount of wine associated with the specified client is saved. <p>
	 * When the amount is available, the client will be warned with a message. <p>
     * Sychronized among different Threads.
	 * 
	 * @param wineId id of the wine
     * @param customerId id of the customer
	 * @param amount numbers of bottles requested
	 */
	synchronized public void requestWine(int wineId, int customerId, int amount)
	{
		Customer c = getClientByID(customerId);
		Wine w = getWineByID(wineId);
		if (w.getAmount() >= amount)
		{
			// wine already available
			((Observer) c).newMessage("Wine " + w.getName() + " is available.");
		} else
		{
            //wine is not available, request is placed
			notifRequest.put(wineId, new AbstractMap.SimpleEntry<Integer, Integer>(customerId, amount));
			for (LoggableUser s : userList)
			{
				if (s instanceof Seller)
					((Seller) s).newMessage("Customer: " + c.getName() + " " + c.getSurname() + " requested " + amount
							+ " bottles of " + w.getName() + ".");
			}
		}
	}

    /**
     * Removes a seller from the user list. <p>
     * Synchronized among different Threads.
     *
     * @param email email of the seller
     **/
	synchronized public void removeSeller(String email)
	{
		for (LoggableUser usr : userList)
		{
			if (usr.getEmail().equals(email))
			{
				userList.remove(usr);
				return;
			}
		}
	}

    /**
     * Removes a wine from the wine list. <p>
     * Synchronized among different Threads.
     *
     * @param id id of the wine
     * */
	synchronized public void removeWine(int id)
	{
		wineList.remove(getWineByID(id));
	}

    /**
     * Edits the information of the specified wine. <p>
     * Synchronized among different Threads.
     *
     * @param id id of the wine
     * @param newWine Wine object containing the new data
     *
     **/
	synchronized public void editWine(int id, Wine newWine)
	{
		Wine w = getWineByID(id);
		w.setName(newWine.getName());
		w.setProducer(newWine.getProducer());
		w.setYear(newWine.getYear());
		w.setTechnicalNotes(newWine.getTechnicalNotes());
		w.setGrapeType(newWine.getGrapeType());
		if (newWine.getAmount() >= 0)
			w.setAmount(newWine.getAmount());
		w.setWineType(newWine.getWineType());
	}

    /**
     * Removes the order with the specified id from the list. <p>
     * Synchronized among different Threads.
     *
     * @param id id of the order
     **/
	synchronized public void removeOrder(int id)
	{
		Order o = getOrderByID(id);
		if(o != null)
		{
			Wine w = getWineByID(o.getWineID());
			if(w != null)
			{
				w.setAmount(w.getAmount() + o.getAmount());
			}
			orderList.remove(o);				
		}
	}

    /**
     * Edits the amount of bottles of a specified order. <p>
     * Synchronized among differen Threads.
     *
     * @param id id of the order
     * @param amount new amount of ordered wine
     *
     * @return true if there is enough wine to satisfy the change, false otherwise
     **/
	synchronized public boolean editOrder(int id, int amount)
	{
		Order o = getOrderByID(id);
		Wine w = getWineByID(o.getWineID());
		if ((w.getAmount() + o.getAmount()) < amount)
			return false;
		w.setAmount(w.getAmount() + o.getAmount() - amount);
		o.setAmount(amount);
		return true;
	}

}
