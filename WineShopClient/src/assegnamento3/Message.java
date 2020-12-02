package assegnamento3;

public class Message
{
	private int id;
	private String message;
	
	public Message(int id, String mex)
	{
		this.id = id;
		this.message = mex;
	}
	
	public String getMessage()
	{
		return this.message;
	}
	
	public void setMessage(String m)
	{
		this.message = m;
	}
	
	public int getID()
	{
		return this.id;
	}

};
