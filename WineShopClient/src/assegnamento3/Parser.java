package assegnamento3;

public class Parser 
{
	
	public static LoggableUser parseUser(String str)
	{
		String vals[] = str.split("<>");
		return new LoggableUser(vals[0], vals[1], vals[2], vals[3]);
	}
	
	
	
	public static Wine parseWine(String wine)
	{
		String vals[] = wine.split("<>");
		return new Wine(Integer.parseInt(vals[0]),vals[1], vals[2], Integer.parseInt(vals[3]), vals[4], vals[5], Integer.parseInt(vals[6]), vals[7]);
	}
	
	public static Order parseOrder(String orders)
	{
		String vals[] = orders.split("<>");
		return new Order(Integer.parseInt(vals[0]), vals[1], vals[2], Integer.parseInt(vals[3]));
	}
	
	
	
}
