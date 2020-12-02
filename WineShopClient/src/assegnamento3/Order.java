package assegnamento3;

public class Order
{
	private int orderID;
	private String customerEmail;
	private String wineName;
	private int amount;

	public Order(int id, String email, String wine, int amount)
	{
		this.orderID = id;
		this.customerEmail = email;
		this.wineName = wine;
		this.amount = amount;
	}

	public int getOrderID()
	{
		return orderID;
	}

	public String getCustomerEmail()
	{
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail)
	{
		this.customerEmail = customerEmail;
	}

	public String getWineName()
	{
		return wineName;
	}

	public void setWineName(String wineName)
	{
		this.wineName = wineName;
	}

	public int getAmount()
	{
		return amount;
	}

	public void setAmount(int amount)
	{
		this.amount = amount;
	}

}
