package assegnamento3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * list of threads to be join
 * */

public class NetworkServer {

	private ServerSocket mainSocket;
	private boolean isRunning = true;
	private static Store mainStore = new Store();

	
	public NetworkServer(int port)
	{
		try
		{
			mainSocket = new ServerSocket(port);
			System.out.println("Started on port: "+port);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void run() 
	{
		isRunning = true;
		while (isRunning) 
		{
			try 
			{
				Socket s = mainSocket.accept();
				
				Runnable r = new ClientHandler(s, mainStore);
				new Thread(r).start();
				//s.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void stop()
	{
		isRunning = false;
	}
}
