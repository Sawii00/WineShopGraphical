package assegnamento3;

public class Parser 
{
	
	public static LoggableUser parseUser(String str)
	{
		String vals[] = str.split("<>");
		return new LoggableUser(vals[0], vals[1], vals[2], vals[3]);
	}
	
	public static WineType parseWineType(String type)
	{
		switch (type)
		{
		case "RED":
		{
			return WineType.RED;
		}
		case "WHITE":
		{
			return WineType.WHITE;
		}
		case "ROSE":
		{
			return WineType.ROSE;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + type);
		}
	}
	
	public static Wine parseWine(String wine)
	{
		String vals[] = wine.split("<>");
		return new Wine(vals[0], vals[1], Integer.parseInt(vals[2]), vals[3], vals[4], Integer.parseInt(vals[5]), parseWineType(vals[6]));
	}
	
	public static Order parseOrder(String orders)
	{
		String vals[] = orders.split("<>");
		return new Order(vals[0], vals[1], Integer.parseInt(vals[2]));
	}
	
	
	
}
