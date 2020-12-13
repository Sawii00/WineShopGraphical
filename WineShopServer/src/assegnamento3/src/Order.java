package assegnamento3.src;

/**
 * The {@code Order} class represents a purchase order.
 * <p>
 * Contains a unique id, the id of the client, the id of the wine, and the amount that was bought.
 */
public class Order
{

	private int orderID;
	private int clientID;
	private int wineID;
	private int amount;

	/**
	 * Class constructor.
	 * 
	 * @param c the client who buy the wine
	 * @param w the wine that is bought.
	 */
	public Order(int id, int c, int w, int a)
	{
		orderID = id;
		clientID = c;
		wineID = w;
		amount = a;
	}

    /**
     * Getter for the order id.
     *
     * @return order id  
     **/
	public int getOrderID()
	{
		return orderID;
	}

	/**
	 * Getter for the client.
	 * 
	 * @return the client that ordered the wine.
	 */
	public int getClient()
	{
		return clientID;
	}

	/**
	 * Setter for the client.
	 * 
	 * @param clientID who ordered the wine.
	 */
	public void setClient(int clientID)
	{
		this.clientID = clientID;
	}

	/**
	 * Getter for the wine bought.
	 * 
	 * @return wine the wine that was ordered.
	 */
	public int getWineID()
	{
		return wineID;
	}

	/**
	 * Setter for the wine bought.
	 * 
	 * @param wine the wine ordered.
	 */
	public void setWineID(int wine)
	{
		this.wineID = wine;
	}

    /**
     * Getter for the amount.
     *
     * @return amount of bottles that were bought
     **/
	public int getAmount()
	{
		return amount;
	}

    /**
     * Setter for the amount of bottles that were bought.
     *
     * @param n number of bottles
     **/
	public void setAmount(int n)
	{
		this.amount = n;
	}
}
