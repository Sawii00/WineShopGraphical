package assegnamento3.src;

/**
 * The class {@code Message} represents a user message.
 * <p>
 * Each message is made up of a unique id and a text.
 **/
public class Message
{
	private int id;
	private String message;

	/**
	 * Class constructor.
	 *
	 * @param id  id of the message
	 * @param mex text of the message
	 **/
	public Message(int id, String mex)
	{
		this.id = id;
		this.message = mex;
	}

	/**
	 * Getter for the message id.
	 *
	 * @return id of the message
	 **/
	public int getID()
	{
		return this.id;
	}

	/**
	 * Getter the the text message.
	 *
	 * @return content of the message
	 **/
	public String getMessage()
	{
		return this.message;
	}

	/**
	 * Setter for the text message.
	 *
	 * @param m content of the message
	 **/
	public void setMessage(String m)
	{
		this.message = m;
	}
};
