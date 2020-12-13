package assegnamento3.src;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * The class {@code NetworkClient} is responsible for establishing a connection
 * to the server, sending requests, and receive responses.
 **/
public class NetworkClient
{

	private Socket mainSocket;
	private ObjectOutputStream os = null;
	private ObjectInputStream is = null;
	private boolean isConnected = false;

	/**
	 * Class constructor.
	 * <p>
	 * Creates the socket and initializes the I/O streams.
	 *
	 * @param host server address
	 * @param port server port
	 **/
	public NetworkClient(String host, int port)
	{
		try
		{
			mainSocket = new Socket(host, port);
			System.out.println(mainSocket);
			os = new ObjectOutputStream(mainSocket.getOutputStream());
			is = new ObjectInputStream(mainSocket.getInputStream());
			this.isConnected = true;
		} catch (IOException e)
		{
			this.isConnected = false;
			new BasicAlertBox("Error", "Could not connect to Server on port: " + port, 300, 150);
		}
	}

	/**
	 * Returns whether the client is connected to the server or not.
	 *
	 * @return true if the client is connected, false otherwise
	 **/
	public boolean isConnected()
	{
		return isConnected;
	}

	/**
	 * Sends a request to the server and waits for a response.
	 * <p>
	 * This method is blocking, thus if no response is received it hangs the caller.
	 *
	 * @param r request to be sent
	 *
	 * @return response from server
	 **/
	public Response sendRequest(Request r)
	{
		try
		{
			if (os != null && is != null)
			{
				System.out.println("Sending Request");
				os.writeObject(r);
				os.flush();
				Object o = is.readObject();
				if (o instanceof Response)
				{
					Response res = (Response) o;
					return res;
				}
			}
		} catch (IOException | ClassNotFoundException e)
		{
			System.out.println("No longer connected to server");
		}

		return new Response(StatusCode.ERROR);

	}
}
