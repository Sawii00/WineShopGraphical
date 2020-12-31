package assegnamento3.src;

/**
 * The class {@code Person} defines a generic person described by a name and a
 * surname.
 */
public class Person
{
	/**
	 * Name of the person. 
	 **/
	protected String name;
	/**
	 * Surname of the person. 
	 **/
	protected String surname;

	/**
	 * Class Constructor.
	 * 
	 * @param name    name of the person
	 * @param surname surname of the person
	 */
	public Person(String name, String surname)
	{
		this.name = name;
		this.surname = surname;
	}

	/**
	 * Getter for the person's name.
	 * 
	 * @return the person's name.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Setter for the person's name.
	 * 
	 * @param name of the person.
	 */
	protected void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Getter for the person's surname.
	 * 
	 * @return surname of the person.
	 */
	public String getSurname()
	{
		return surname;
	}

	/**
	 * Setter for the person's surname.
	 * 
	 * @param surname of the person.
	 */
	protected void setSurname(String surname)
	{
		this.surname = surname;
	}

	/**
	 * Gives a textual representation of the person.
	 * 
	 * @return the person's name and his surname.
	 */
	public String toString()
	{
		return this.name + " " + this.surname;
	}
}
