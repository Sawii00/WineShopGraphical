package assegnamento3.src;

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
	private static final long serialVersionUID = 4777921477023020754L;
	
	/**
	 * Message list of the seller. 
	 **/
	private ArrayList<String> messages = new ArrayList<String>();

	/**
	 * Class constructor.
	 * <p>
	 * Invokes the parent {@code LoggableUser}'s constructor.
	 * 
	 * @see LoggableUser
	 *
	 * @param name     name of the seller.
	 * @param surname  surname of the seller.
	 * @param email    email of the seller.
	 * @param password password of the seller.
	 */
	public Seller(String name, String surname, String email, String password)
	{
		super(name, surname, email, password);
	}

	/**
	 * Class constructor.
	 * <p>
	 * Invokes the parent {@code LoggableUser}'s constructor.
	 * 
	 * @see LoggableUser
	 * 
	 * @param id       id of the seller
	 * @param name     name of the seller.
	 * @param surname  surname of the seller.
	 * @param email    email of the seller.
	 * @param password password of the seller.
	 */

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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<String> getMessageList()
	{
		return this.messages;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUserType()
	{
		return "seller";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteMessage(int id)
	{
		if (id < messages.size())
			messages.remove(id);
	}
}
