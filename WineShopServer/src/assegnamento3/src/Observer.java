package assegnamento3.src;

import java.util.ArrayList;

/**
 * The {@code Observer} interface is used to implement the Observer Design
 * Pattern.
 * <p>
 * {@code Client} and {@code Seller} implement the Interface to enable the
 * receipt of notifications.
 */
public interface Observer
{
	/**
	 * Prints to the standard output the messages' list.
	 */
	public void displayMessages();

	/**
	 * Clears all messages.
	 */
	public void deleteMessages();

	/**
	 * Adds a new message to the list.
	 * 
	 * @param m the message.
	 */
	public void newMessage(String m);

	/**
	 * Returns the string representation of all the messages. Each message is on a
	 * new line.
	 * 
	 * @return messages as a single String
	 */

	public String getMessages();

	/**
	 * Deletes a specific message given by id.
	 * 
	 * @param id id of the message to be removed
	 **/
	public void deleteMessage(int id);

	/**
	 * Returns the messages as a list.
	 * 
	 * @return message list
	 **/
	ArrayList<String> getMessageList();
}
