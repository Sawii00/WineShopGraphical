package assegnamento3;

public class Order 
{
	private String customerEmail;
	private String wineName;
	private int amount;
	
	public Order(String email, String wine, int amount) 
	{
		this.customerEmail=email;
		this.wineName=wine;
		this.amount=amount;
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
