package assegnamento3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
