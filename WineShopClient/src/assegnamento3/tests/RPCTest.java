package assegnamento3.tests;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import assegnamento3.src.*;

public class RPCTest
{

	static NetworkClient client = new NetworkClient("127.0.0.1", 1500);
	
	@BeforeAll
	static void initTests()
	{
		Request req = new Request("initializeDatabase");
		Response res = client.sendRequest(req);
		assertTrue(res.getReturnCode().toString().equals("SUCCESS"));
		//give time to initialize everything
		try
		{
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		Request req2 = new Request("register");
		String arr[] = {"defaultUser","defaultUser","defaultEmail","defaultPassword"};
		req2.addAllParameters(arr);
		res = client.sendRequest(req2);
	}
	
	
	@Ignore
	@ParameterizedTest
	@CsvSource(value= {"John,Black,juser,jpwd,SUCCESS",
			"Pippus,Baudus,juser,ppwd,INVALID_ARGUMENTS",
			"Mario,Rossi,juser1,jpwd,SUCCESS",
			"Pippus,Baudus,juser3,ppwd,SUCCESS"})
	public void testRegister(String name, String surname, String email, String pwd, String result)
	{
		//Valid Register
		Request req = new Request("register");
		req.addParameter(name);
		req.addParameter(surname);
		req.addParameter(email);
		req.addParameter(pwd);
		Response res = client.sendRequest(req);
		assertTrue(res.getReturnCode().toString().equals(result));
	}
	
	@ParameterizedTest
	@CsvSource(value= {"defaultEmail,defaultPassword,SUCCESS",
			"juser2,jpwd,INVALID_ARGUMENTS",
			"juser,jpwd2,INVALID_ARGUMENTS",
			"smonk,jpwd,INVALID_ARGUMENTS"})
	public void testLogin(String email, String pwd, String result)
	{
		//Valid Register
		Request req = new Request("login");
		req.addParameter(email);
		req.addParameter(pwd);
		Response res = client.sendRequest(req);
		assertTrue(res.getReturnCode().toString().equals(result));
	}
	
	
	@ParameterizedTest
	@CsvSource(value= {"2,0,5,SUCCESS",
			"2,0,20,INVALID_ARGUMENTS",
			"10,0,5,INVALID_ARGUMENTS",
			"2,20,5,INVALID_ARGUMENTS",
			"2,0,-5,INVALID_ARGUMENTS"})
	public void testBuy(String customer, String wine, String amount, String result)
	{
		Request req = new Request("buy");
		req.addParameter(customer);
		req.addParameter(wine);
		req.addParameter(amount);
		Response res = client.sendRequest(req);
		assertTrue(res.getReturnCode().toString().equals(result));	
	}
	
	@ParameterizedTest
	@CsvSource(value= {"2,0,5,INVALID_ARGUMENTS", //wine already present
			"2,0,30,SUCCESS", //not enough wine
			"10,0,5,INVALID_ARGUMENTS", //invalid person
			"2,20,5,INVALID_ARGUMENTS", //invalid wine
			"2,0,-5,INVALID_ARGUMENTS"})  //invalid amount
	public void testNotificationRequest(String customer, String wine, String amount, String result)
	{
		Request req = new Request("notification");
		req.addParameter(customer);
		req.addParameter(wine);
		req.addParameter(amount);
		Response res = client.sendRequest(req);
		assertTrue(res.getReturnCode().toString().equals(result));	
	}
	
}
