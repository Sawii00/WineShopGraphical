package assegnamento3.src;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The class {@code NetworkServer} is responsible to create a persistent Server
 * that accepts clients and creates instances of {@code ClientHandler} to handle
 * them.
 * <p>
 * Implements Runnable to allow it to be executed in background.
 **/
public class NetworkServer implements Runnable
{
	private ServerSocket mainSocket;
	private boolean isRunning = true;
	/**
	 * Store instance that handles the subsystem for Wines, Logins, Registrations.
	 * <p>
	 * Static for easy access outside.
	 **/
	public static Store mainStore = new Store();

	/**
	 * Class constructor.
	 * <p>
	 * Creates the server socket from which to accept incoming connections.
	 * 
	 * @param port port to listen from
	 * @throws IOException Could not connect socket
	 **/
	public NetworkServer(int port) throws IOException
	{
		mainSocket = new ServerSocket(port);
		System.out.println("Started on port: " + port);
	}

	/**
	 * Start the execution of the server.
	 **/
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
	}

	/**
	 * Stops the execution of the server.
	 **/
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
