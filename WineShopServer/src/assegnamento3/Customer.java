package assegnamento3;

import java.util.ArrayList;


/**
 *The {@code Client} class defines a Shop's Customer. <p>
 * He can: <p>
 * - Can buy wines. <p>
 * - Can order wine if it is not available in the store. <p>
 * Each client is defined by an Id. <p>
 * Implements the {@code Observer} Interface to allow for notifications receipt <p>
 * Extends the {@code LoggableUser} to implement login functionality. 
 */
public class Customer extends LoggableUser implements Observer{

	protected ArrayList<String> messages=new ArrayList<String>();

	/**
	 * Class constructor. <p>
	 * Invokes the parent {@code LoggableUser}'s constructor. 
 	 * @see LoggableUser 
 	 
 	 * The id, if not specified, is generated random.
	 * @param name of the client.
	 * @param surname of the client.
	 * @param email of the client.
	 * @param password of the client.
	 */
	public Customer(String name, String surname, String email, String password) {
		super(name, surname, email, password);
	}
	
	/**
	 * Class constructor. <p>
	 * Invokes the parent {@code LoggableUser}'s constructor. 
 	 * @see LoggableUser 
 	 
 	 * @param id of the client.
	 * @param name of the client.
	 * @param surname of the client.
	 * @param email of the client.
	 * @param password of the client.
	 */
	public Customer(int id, String name, String surname, String email, String password) {
		super(id, name, surname, email, password);
	}
	
	
	/**
	 * {@inheritDoc}
	 * */
	@Override
	public void displayMessages() 
	{
		if(messages.size() > 0)
		{
			System.out.println("NEW MESSAGES:");
			for (String m: messages) {
				System.out.println(m);
			}
			System.out.println();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * */
	@Override
	public void deleteMessages()
	{
		messages.clear();
	}
	
	/**
	 * {@inheritDoc}
	 * */
	@Override
	public void newMessage(String m)
	{
		messages.add(m);
	}
	
	/**
	 * {@inheritDoc}
	 * */
	@Override
	public String getMessages()
	{
		String res = "";
		for(String s: messages)
		{
			res += s+'\n';
			
		}
		return res;
		
	}

	@Override
	public String getUserType() {
		return "customer";
	}


	
	
}
