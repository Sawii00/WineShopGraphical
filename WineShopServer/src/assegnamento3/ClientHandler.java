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
	private Store mainStore = null;
	
	public ClientHandler(Socket s, Store store) 
	{
		this.mainSocket = s;
		this.mainStore = store;
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
					System.out.println(r.toString());
					if (r.getMethod().equals("close"))
					{
						stop();
					}
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
