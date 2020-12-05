package assegnamento3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * list of threads to be join
 * */

public class NetworkServer implements Runnable
{

	private ServerSocket mainSocket;
	private boolean isRunning = true;
	public static Store mainStore = new Store();

	public NetworkServer(int port) throws IOException
	{

		mainSocket = new ServerSocket(port);
		System.out.println("Started on port: " + port);
		// mainStore.loadData() --> read sql database

	}

	public void run()
	{
		isRunning = true;
		while (isRunning)
		{
			try
			{
				Socket s = mainSocket.accept();

				Runnable r = new ClientHandler(s);
				new Thread(r).start();
				// s.close();
			} catch (IOException e)
			{
				System.out.println("Closing server");
				isRunning = false;
			}
		}

		// save all lists
		// periodic timer saves the lists

	}

	public void stop()
	{
		isRunning = false;
		try
		{
			mainSocket.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
