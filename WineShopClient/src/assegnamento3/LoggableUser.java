package assegnamento3;

import java.io.Serializable;
import java.util.Random;

/**
 * The {@code LoggableUser} class defines a generic person with log-in functionality. <p>
 * This class will be extended by the Seller Class and Client Class to implement log-in to the store<p>
 * It defines getters and setters for email, password and Id. <p>
 * It extends {@code Person} to add name and surname.
 */

public class LoggableUser extends Person implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4878874717365268921L;


	/**
	 * Class constructor.
	 * @param id id of the user.
	 * @param name name of the user.
	 * @param surname surname of the user.
	 * @param email email of the user.
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
	 * Class constructor. <p> 
	 * ID is automatically generated since not provided.
	 * @param name name of the user.
	 * @param surname surname of the user.
	 * @param email email of the user.
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
	
	protected int id;
	protected String email;
	protected String password;
	 

	/**
	 * Getter for the user's Id.
	 * @return the user's Id.
	 */
	public int getID()
	{
		return id;
	}
	
	/**
	 * Setter for the user's Id.
	 * @param id user's Id.
	 */
	protected void setID(int id)
	{
		this.id = id;
	}
	

	/**
	 * Getter for the user's email.
	 * @return email of the user.
	 */
	public String getEmail() 
	{
		return email;
	}
	
	/**
	 * Getter for the user's password.
	 * @return password of the user.
	 */

	public String getPassword()
	{
		return password;
	}
	
	/**
	 * Setter for the user's email.
	 * @param email of the user.
	 */
	protected void setEmail(String email)
	{
		this.email = email;
	}
	
	/**
	 * Setter for the user's password.
	 * @param password of the user.
	 */
	
	protected void setPassword(String password)
	{
		this.password = password;
	}
	
	/**
	 * Gives a textual representation of the user.
	 * @return the user's id, name, his surname and his email.
	 */
	
	public String toString()
	{
		return this.id + ", "+super.toString()+", "+this.email;
	}
	
	public String getUserType()
	{
		return "user";
	}
	
	
	public String serializedString()
	{
		return this.name+"<>"+this.surname+"<>"+this.email+"<>"+this.password;
	}
	
}
	