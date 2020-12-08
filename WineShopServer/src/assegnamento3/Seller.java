package assegnamento3;

import java.util.ArrayList;

/**
 * The {@code Seller} class defines a Shop's Employee.
 * <p>
 * He can:
 * <p>
 * - Can manage the store through wine adding or removal
 * <p>
 * - Can re-stock the bottles of wines that are not available.
 * <p>
 * - Receives a notification if the store has run out of bottles of a specific
 * wine.
 * <p>
 * Implements the {@code Observer} Interface to allow for notification receipts.
 * <p>
 * Extends the {@code LoggableUser} to implement login functionality.
 */

public class Seller extends LoggableUser implements Observer
{

	protected ArrayList<String> messages = new ArrayList<String>();

	/**
	 * Class constructor.
	 * <p>
	 * Invokes the parent {@code LoggableUser}'s constructor.
	 * 
	 * @see LoggableUser
	 * 
	 * @param name     of the seller.
	 * @param surname  of the seller.
	 * @param email    of the seller.
	 * @param password of the seller.
	 */
	public Seller(String name, String surname, String email, String password)
	{
		super(name, surname, email, password);
	}
	
	public Seller(int id, String name, String surname, String email, String password)
	{
		super(id, name, surname, email, password);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void displayMessages()
	{
		if (messages.size() > 0)
		{
			System.out.println("NEW MESSAGES:");
			for (String m : messages)
			{
				System.out.println(m);
			}
			System.out.println();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteMessages()
	{
		messages.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void newMessage(String m)
	{
		messages.add(m);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMessages()
	{
		String res = "";
		for (String s : messages)
		{
			res += s + '\n';

		}
		return res;

	}

	@Override
	public String getUserType()
	{
		return "seller";
	}

	@Override
	public void deleteMessage(int id)
	{
		if (id < messages.size())
			messages.remove(id);
	}

}
