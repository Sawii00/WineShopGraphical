package assegnamento3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkClient
{

	private Socket mainSocket;
	private ObjectOutputStream os = null;
	private ObjectInputStream is = null;
	private boolean isConnected = false;

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

	public boolean isConnected()
	{
		return isConnected;
	}

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
