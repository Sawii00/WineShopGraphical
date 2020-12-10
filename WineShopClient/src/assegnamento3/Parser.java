package assegnamento3;

/**
 * The class {@code Parser} is a utility static class that handles the parsing of Users, Wines, and Orders received as text into actual Objects.
 **/
public class Parser
{

    /**
     * Creates a LoggableUser object based on the received string representation.
     * 
     * @param str string representation of the LoggableUser
     *
     * @return LoggableUser object
     **/
	public static LoggableUser parseUser(String str)
	{
		String vals[] = str.split("<>");
		return new LoggableUser(vals[0], vals[1], vals[2], vals[3]);
	}

    /**
     * Creates a Wine object based on the received string representation.
     *
     * @param wine string representation of the wine
     *
     * @return Wine object
     **/
	public static Wine parseWine(String wine)
	{
		String vals[] = wine.split("<>");
		return new Wine(Integer.parseInt(vals[0]), vals[1], vals[2], Integer.parseInt(vals[3]), vals[4], vals[5],
				Integer.parseInt(vals[6]), vals[7]);
	}

    /**
     * Creates a Order object based on the received string representation.
     * 
     * @param str string representation of the Order
     *
     * @return Order object
     **/

	public static Order parseOrder(String orders)
	{
		String vals[] = orders.split("<>");
		return new Order(Integer.parseInt(vals[0]), vals[1], vals[2], Integer.parseInt(vals[3]));
	}

}
