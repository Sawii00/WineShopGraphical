package assegnamento3.src;

import java.io.Serializable;
import java.util.Random;

/**
 * The {@code LoggableUser} class defines a generic person with log-in
 * functionality.
 * <p>
 * This class will be extended by the Seller Class and Client Class to implement
 * log-in to the store
 * <p>
 * It defines getters and setters for email, password and Id.
 * <p>
 * It extends {@code Person} to add name and surname.
 */

public class LoggableUser extends Person implements Serializable
{
	private static final long serialVersionUID = 4878874717365268921L;
	/**
	 * Id of the user.
	 */
	protected int id;
	/**
	 * Email of the user.
	 **/
	protected String email;
	/**
	 * Password of the user.
	 **/
	protected String password;

	/**
	 * Class constructor.
	 * 
	 * @param id       id of the user.
	 * @param name     name of the user.
	 * @param surname  surname of the user.
	 * @param email    email of the user.
	 * @param password password of the user.
	 */
	public LoggableUser(int id, String name, String surname, String email, String password)
	{
		super(name, surname);
		this.email = email;
		this.password = password;
		this.id = id;
	}

	/**
	 * Class constructor.
	 * <p>
	 * ID is automatically generated since not provided.
	 * 
	 * @param name     name of the user.
	 * @param surname  surname of the user.
	 * @param email    email of the user.
	 * @param password password of the user.
	 */
	public LoggableUser(String name, String surname, String email, String password)
	{
		super(name, surname);
		this.email = email;
		this.password = password;
		Random r = new Random();
		this.id = r.nextInt(1000000);
	}

	/**
	 * Getter for the user's email.
	 * 
	 * @return email of the user.
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * Getter for the user's Id.
	 * 
	 * @return the user's Id.
	 */
	public int getID()
	{
		return id;
	}

	/**
	 * Getter for the user's password.
	 * 
	 * @return password of the user.
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * Getter for the user's type.
	 * 
	 * @return String encoding of the user's type.
	 */
	public String getUserType()
	{
		return "user";
	}

	/**
	 * Returns a representation of the user as a String with fields divided by a
	 * separator.
	 * <p>
	 * Allows for easy construction and deconstruction of users between Client /
	 * Server.
	 * 
	 * @return String encoding of the user.
	 */
	public String serializedString()
	{
		return this.name + "<>" + this.surname + "<>" + this.email + "<>" + this.password;
	}

	/**
	 * Setter for the user's email.
	 * 
	 * @param email of the user.
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}

	/**
	 * Setter for the user's Id.
	 * 
	 * @param id user's Id.
	 */
	public void setID(int id)
	{
		this.id = id;
	}

	/**
	 * Setter for the user's password.
	 * 
	 * @param password of the user.
	 */

	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * Gives a textual representation of the user.
	 * 
	 * @return the user's id, name, his surname and his email.
	 */

	@Override
	public String toString()
	{
		return this.id + ", " + super.toString() + ", " + this.email;
	}
}
