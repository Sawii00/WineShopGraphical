package assegnamento3.src;


/**
 * The class {@code Admin} represents an instance of an Admin of the WineShop. <p>
 * He can see all information about customers, sellers, wines, and orders. <p>
 * Admins can add sellers and edit their information. <p>
 * It extends LoggableUser to implement the functionalitity to be logged in.
 * */
public class Admin extends LoggableUser
{

    /**
     * Class constructor. <p>
     * Creates a LoggableUser by calling the super constructor.
     * @param name name of the Admin
     * @param surname surname of the Admin
     * @param email email of the Admin
     * @param password password of the Admin
     **/
	public Admin(String name, String surname, String email, String password)
	{
		super(name, surname, email, password);
	}
	
    /**
     * Class constructor. <p>
     * Creates a LoggableUser by calling the super constructor.
     * @param name name of the Admin
     * @param surname surname of the Admin
     * @param email email of the Admin
     * @param password password of the Admin
     **/
	public Admin(int id, String name, String surname, String email, String password)
	{
		super(id, name, surname, email, password);
	}

    /**
     * {@inheritDoc}
     **/
	@Override
	public String getUserType()
	{
		// TODO Auto-generated method stub
		return "admin";
	}

}
