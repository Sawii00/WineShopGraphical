package assegnamento3.tests;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.BeforeAll;

import assegnamento3.src.*;


public class StoreTest 
{

	static Store store = new Store();
	
	//change tests if you change amount of bottles
	@BeforeAll
	static void setupStore()
	{
		Wine w1 = new Wine("wine1", "prod1", 2000, "good", "uva", 10,"red");
		Wine w2 = new Wine("wine2", "prod2", 2010, "good", "uva", 5, "red");
		store.addWine(w1);
		store.addWine(w2);
		Seller s1 = new Seller("Seller", "Seller", "email", "password");
		store.register(s1);
		Customer usr = new Customer("Customer", "Customer", "customeremail", "customerpassword");
		store.register(usr);
	}
	
	private boolean toBoolean(String res)
	{
		if(res.equals("true"))return true;
		else return false;
	}
	

	
	@ParameterizedTest
	@CsvSource(value={"user,user,useremail,userpassword,true", "user,user,useremail,userpassword,false","user2,user2,useremail,password2,false"})
	public void testRegistration(String name, String surname, String email, String pwd, String res)
	{
		Customer usr = new Customer(1, name,surname,email,pwd);
		assertTrue(store.register(usr) == toBoolean(res));
	}
	
	@ParameterizedTest
	@CsvSource(value={"customeremail,customerpassword,false", "customeremail,wrongpassword,true","wrongemail,customerpassword,true"})
	public void testLogin(String email, String pwd, String res)
	{
		assertTrue((store.login(email,pwd) == null) == toBoolean(res));		
	}
	
	private boolean checkOrder(int wine, int customer, int amount)
	{
		ArrayList<Order> orderList = store.getOrderList();
		if(orderList.size() > 0)
		{
			Order order = orderList.get(orderList.size() - 1);
			return(order.getWineID() == wine && order.getAmount() == amount && order.getClient() == customer);
		}
		return false;
	}
	
	@Test
	public void testBuy()
	{
		Wine validWine = store.getWineByID(100000);
		int prevAmount = validWine.getAmount();
		//Valid buy
		assertTrue(store.buy(validWine.getID(), 1, 5));
		//Check order is placed
		assertTrue(checkOrder(validWine.getID(), 1, 5));
		//Check bottles update
		assertTrue(store.getWineByID(validWine.getID()).getAmount() == prevAmount - 5);
		
		int orderListSize = store.getOrderList().size();
		//Invalid Buy, Invalid wine.
		assertFalse(store.buy(50000, 1, 5));
		assertTrue(orderListSize == store.getOrderList().size());
		//Not existing user
		prevAmount = store.getWineByID(100000).getAmount();
		assertFalse(store.buy(validWine.getID(), 123, 2));
		assertTrue(orderListSize == store.getOrderList().size());
		assertTrue(prevAmount == store.getWineByID(100000).getAmount());
		
		//Too many bottles
		assertFalse(store.buy(validWine.getID(), 1, 200));
		assertTrue(orderListSize == store.getOrderList().size());
		assertTrue(prevAmount == store.getWineByID(100000).getAmount());
		
		ArrayList<String> mex = ((Observer)store.getLoggableUserByID(0)).getMessageList();
		int messageListSize = mex.size();
		//Valid buy with message to Sellers
		assertTrue(store.buy(validWine.getID(), 1, prevAmount));
		//Check order is placed
		assertTrue(checkOrder(validWine.getID(), 1, prevAmount));
		//Check bottles update
		assertTrue(store.getWineByID(validWine.getID()).getAmount() == 0);
		//Check messages
		assertTrue(mex.size() == messageListSize + 1);
		
	}
	
	private boolean checkValidRequest(int wine, int customer, int amount)
	{
		Map<Integer, Entry<Integer, Integer>> reqs = store.getNotificationList();
		if(reqs.isEmpty())return false;
		for(Integer i : reqs.keySet())
		{
			if(i == wine)
			{
				Entry<Integer, Integer> e = reqs.get(i);
				return e.getKey() == customer && e.getValue() == amount;
			}
			
		}
		return false;
	}
	
	
	@Test
	public void testRequestWine()
	{
		Customer c = store.getClientByID(1);
		Wine w = store.getWineByID(100001);
		//Valid request
		assertTrue(store.requestWine(w.getID(), c.getID(), w.getAmount() + 10));
		assertTrue(checkValidRequest(w.getID(), c.getID(), w.getAmount() + 10));
		
		int notificationSize = store.getNotificationList().size();
		//Invalid request. Not existing wine
		assertFalse(store.requestWine(2000, c.getID(), 10));
		assertTrue(notificationSize == store.getNotificationList().size());
		assertFalse(checkValidRequest(2000, c.getID(), 10));
		
		//Invalid request. Not existing user
		assertFalse(store.requestWine(w.getID(), 123, w.getAmount() + 10));
		assertTrue(notificationSize == store.getNotificationList().size());
		assertFalse(checkValidRequest(2000, c.getID(), w.getAmount() + 10));
		
		//Invalid request. Wine Already Present
		int prevSize = ((Observer)c).getMessageList().size();
		assertFalse(store.requestWine(w.getID(), c.getID(), w.getAmount()));
		assertTrue(notificationSize == store.getNotificationList().size());
		assertFalse(checkValidRequest(w.getID(), c.getID(), w.getAmount()));
		assertTrue(((Observer)c).getMessageList().size() == prevSize + 1);
		
	}
	
	
	
	
}
