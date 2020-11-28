package assegnamento3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkClient {

	private Socket mainSocket;
	private ObjectOutputStream os = null;
	private ObjectInputStream is = null;
	
	public NetworkClient(String host, int port)
	{
		try
		{
			mainSocket = new Socket(host, port);
			System.out.println(mainSocket);
			os = new ObjectOutputStream(mainSocket.getOutputStream());
			is = new ObjectInputStream(mainSocket.getInputStream());
		}
		catch (IOException e)
		{
			new BasicAlertBox("Error", "Could not connect to Server on port: "+port, 300, 100);
		}
	}
	
	public Response sendRequest (Request r) 
	{
		System.out.println("Sending Request");
		try
		{
			if (os != null && is != null)
			{
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
			e.printStackTrace();
		}
		
		return new Response(StatusCode.ERROR);
		
	}
}
