package assegnamento3.src;

/**
 * The class {@code Order} contains the information regarding a Customer order.
 * <p>
 * It has a unique ID, the email of the customer, the name of the wine that was
 * ordered, and the amount that was bought.
 * <p>
 * It is only used to contain the information to be displayed in the table.
 **/
public class Order
{
	private int orderID;
	private String customerEmail;
	private String wineName;
	private int amount;

	/**
	 * Class constructor.
	 **/
	public Order(int id, String email, String wine, int amount)
	{
		this.orderID = id;
		this.customerEmail = email;
		this.wineName = wine;
		this.amount = amount;
	}

	/**
	 * Getter for the amount of wine in the order.
	 *
	 * @return amount of bottles in the order
	 **/
	public int getAmount()
	{
		return amount;
	}

	/**
	 * Getter for the customer's email.
	 *
	 * @return email of the customer
	 **/
	public String getCustomerEmail()
	{
		return customerEmail;
	}

	/**
	 * Getter for the order id.
	 *
	 * @return id of the order
	 **/
	public int getOrderID()
	{
		return orderID;
	}

	/**
	 * Getter for the wine name.
	 *
	 * @return name of the wine
	 **/
	public String getWineName()
	{
		return wineName;
	}

	/**
	 * Setter for the amount of wine in the order.
	 *
	 * @param amount amount of bottles of the order
	 **/
	public void setAmount(int amount)
	{
		this.amount = amount;
	}

	/**
	 * Setter for the customer's email.
	 *
	 * @param customerEmail email of the customer
	 **/
	public void setCustomerEmail(String customerEmail)
	{
		this.customerEmail = customerEmail;
	}

	/**
	 * Setter for the wine name.
	 *
	 * @param wineName name of the wine
	 **/
	public void setWineName(String wineName)
	{
		this.wineName = wineName;
	}
}
