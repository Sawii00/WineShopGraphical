package assegnamento3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable
{
	private Socket mainSocket = null;
	private ObjectOutputStream os = null;
	private ObjectInputStream is = null;
	private boolean isRunning = true;
	
	public ClientHandler(Socket s) 
	{
		this.mainSocket = s;
		try 
		{
			this.os = new ObjectOutputStream(mainSocket.getOutputStream());
			this.is = new ObjectInputStream(mainSocket.getInputStream());
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void sendResponse(Response r)
	{
		try
		{
			if (os != null)
			{
				os.writeObject(r);
				os.flush();
			}
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private Response handleRequest(Request request)
	{
		String method = request.getMethod();
		Response response;
		switch (method)
		{
			case "close": 
			{
				stop();
				return new Response(StatusCode.SUCCESS);

			}
			case "login":
			{
				if(request.getParameters().size() != 2)
					return new Response(StatusCode.INVALID_ARGUMENTS);
				String email = request.getParameters().get(0);
				String password = request.getParameters().get(1);
				
				LoggableUser obs = NetworkServer.mainStore.login(email, password);
				if(obs != null)
				{
					response = new Response(StatusCode.SUCCESS);
					response.addArgument(obs.getUserType());
					if(obs instanceof Observer)
					{
						String mex = ((Observer)obs).getMessages();
						response.addArgument(mex);
					}
				}
				else
				{
					response = new Response(StatusCode.INVALID_ARGUMENTS);
				}
				return response;
			}
			case "register":
			{
				if(request.getParameters().size() != 4)
					return new Response(StatusCode.INVALID_ARGUMENTS);
				String name = request.getParameters().get(0);
				String surname = request.getParameters().get(1);
				String email = request.getParameters().get(2);
				String password = request.getParameters().get(3);
				
				Customer c = new Customer(name, surname, email, password);
				boolean succesfullyRegistered = NetworkServer.mainStore.register(c);
				
				if(succesfullyRegistered)
				{
					response = new Response(StatusCode.SUCCESS);
				}
				else
				{
					response = new Response(StatusCode.INVALID_ARGUMENTS);					
				}
				
				return response;
			}
			case "getCustomersList":
			{
				response = new Response(StatusCode.SUCCESS);
				for(LoggableUser w: NetworkServer.mainStore.getUserList())
				{
					if(w instanceof Customer)
						response.addArgument(w.serializedString());
				}
				return response;
			}
			case "getSellersList":
			{
				response = new Response(StatusCode.SUCCESS);
				for(LoggableUser w: NetworkServer.mainStore.getUserList())
				{
					if(w instanceof Seller)
						response.addArgument(w.serializedString());
				}
				return response;
			}
			case "getWinesList":
			{
				response = new Response(StatusCode.SUCCESS);
				for(Wine w: NetworkServer.mainStore.getWineList())
				{
					response.addArgument(w.serializedString());
				}
				return response;
			}
			case "getOrderList":
			{
				response = new Response(StatusCode.SUCCESS);
				for(Order o: NetworkServer.mainStore.getOrderList())
				{
					Customer c = NetworkServer.mainStore.getClientByID(o.getClient());
					response.addArgument(o.getOrderID()+"<>"+c.getEmail() + "<>"+o.getWine().getName()+"<>"+o.getWine().getNumber());
				}
				return response;
			}
			case "registerSeller":
			{
				if(request.getParameters().size() != 4)
					return new Response(StatusCode.INVALID_ARGUMENTS);
				String name = request.getParameters().get(0);
				String surname = request.getParameters().get(1);
				String email = request.getParameters().get(2);
				String password = request.getParameters().get(3);
				
				Seller s = new Seller(name, surname, email, password);
				boolean succesfullyRegistered = NetworkServer.mainStore.register(s);
				
				if(succesfullyRegistered)
				{
					response = new Response(StatusCode.SUCCESS);
				}
				else
				{
					response = new Response(StatusCode.INVALID_ARGUMENTS);					
				}
				
				return response;
			}
			case "editSeller":
			{
				if(request.getParameters().size() != 5)
					return new Response(StatusCode.INVALID_ARGUMENTS);
				
				String oldEmail = request.getParameters().get(0);
				String name = request.getParameters().get(1);
				String surname = request.getParameters().get(2);
				String newEmail = request.getParameters().get(3);
				String password = request.getParameters().get(4);
				
				boolean ok = NetworkServer.mainStore.editUser(oldEmail, new LoggableUser(name, surname, newEmail, password));
				Response res;
				if(ok)
					res = new Response(StatusCode.SUCCESS);
				else
					res = new Response(StatusCode.INVALID_ARGUMENTS);
				return res;
			}
			case "searchWineName":
			{
				if(request.getParameters().size() != 1)
					return new Response(StatusCode.INVALID_ARGUMENTS);
				
				ArrayList<Wine> result = NetworkServer.mainStore.search(request.getParameters().get(0), SearchType.NAME);
				response = new Response(StatusCode.SUCCESS);
				for(Wine w: result)
				{
					response.addArgument(w.serializedString());
				}
				return response;
			}
			case "searchWineYear":
			{
				if(request.getParameters().size() != 1)
					return new Response(StatusCode.INVALID_ARGUMENTS);
				ArrayList<Wine> result = NetworkServer.mainStore.search(request.getParameters().get(0), SearchType.YEAR);
				response = new Response(StatusCode.SUCCESS);
				for(Wine w: result)
				{
					response.addArgument(w.serializedString());
				}
				return response;
			}
			case "removeSeller":
			{
				if(request.getParameters().size() != 1)
					return new Response(StatusCode.INVALID_ARGUMENTS);
				NetworkServer.mainStore.removeSeller(request.getParameters().get(0));
				return new Response(StatusCode.SUCCESS);
			}
			case "addWine":
			{
				if(request.getParameters().size() != 7)
					return new Response(StatusCode.INVALID_ARGUMENTS);
				String name = request.getParameters().get(0);
				String producer = request.getParameters().get(1);
				String year = request.getParameters().get(2);
				String notes = request.getParameters().get(3);
				String grape = request.getParameters().get(4);
				String number = request.getParameters().get(5);
				String type = request.getParameters().get(6);
				
				Wine w = new Wine(name, producer, Integer.parseInt(year), notes, grape, Integer.parseInt(number), type);
				NetworkServer.mainStore.addWine(w);
				response = new Response(StatusCode.SUCCESS);
				return response;
			}
			case "removeWine":
			{
				if(request.getParameters().size() != 1)
					return new Response(StatusCode.INVALID_ARGUMENTS);
				NetworkServer.mainStore.removeWine(Integer.parseInt(request.getParameters().get(0)));
				return new Response(StatusCode.SUCCESS);
			}
			case "editWine":
			{
				if(request.getParameters().size() != 8)
					return new Response(StatusCode.INVALID_ARGUMENTS);
				
				String id = request.getParameters().get(0);
				String name = request.getParameters().get(1);
				String producer = request.getParameters().get(2);
				String year = request.getParameters().get(3);
				String notes = request.getParameters().get(4);
				String grape = request.getParameters().get(5);
				String number = request.getParameters().get(6);
				String type = request.getParameters().get(7);
				
				boolean ok = NetworkServer.mainStore.editWine(Integer.parseInt(id), new Wine(name, producer, Integer.parseInt(year), notes, grape, Integer.parseInt(number), type));
				Response res;
				if(ok)
					res = new Response(StatusCode.SUCCESS);
				else
					res = new Response(StatusCode.INVALID_ARGUMENTS);
				return res;
			}
			case "editOrder":
			{
				
				return new Response(StatusCode.SUCCESS);
			}
			case "removeOrder":
			{
				return new Response(StatusCode.SUCCESS);
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + method);
		}
		
	}
	
	
	public void run()
	{
		while(isRunning)
		{
			Request r;
			Object i;
			try 
			{
				i = is.readObject();
				if (i instanceof Request)
				{
					r = (Request)i;
					Response res = handleRequest(r);
					os.writeObject(res);
					os.flush();
				}
				else
				{
					System.err.println("Server could not read the request");
				}
			} catch (ClassNotFoundException | IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public void stop()
	{
		isRunning = false;
		try 
		{
			Response r = new Response(StatusCode.SUCCESS);
			sendResponse(r);
			mainSocket.close();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
