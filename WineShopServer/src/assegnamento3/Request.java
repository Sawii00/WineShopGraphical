package assegnamento3;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The class {@code Request} represents a serializable rpc-like request to be sent to the server. <p>
 * It contains a method name and a list of String parameters.
 **/
public class Request implements Serializable
{

	private static final long serialVersionUID = -3573650597397447309L;

	String method;
	ArrayList<String> parameters = new ArrayList<String>();

    /**
     * Class constructor.
     *
     * @param m request method
     **/
	public Request(String m)
	{
		method = m;
	}

    /**
     * Adds all the parameters of an array to the parameter list.
     *
     * @param arr array of parameters
     **/
	public void addAllParameters(String[] arr)
	{
		for (String s : arr)
		{
			parameters.add(s);
		}
	}

    /**
     * Adds a parameter to the list.
     **/
	public void addParameter(String s)
	{
		parameters.add(s);
	}

    /**
     * Getter for the method.
     *
     * @return request method
     **/
	public String getMethod()
	{
		return method;
	}

    /**
     * Getter for the parameters.
     *
     * @return parameter list
     **/
	public ArrayList<String> getParameters()
	{
		return parameters;
	}

}
