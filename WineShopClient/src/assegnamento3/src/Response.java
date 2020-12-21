package assegnamento3.src;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The class {@code Response} represents a serializable response of the server
 * to a client's request.
 * <p>
 * Contains a return code and a list of result parameters.
 **/
public class Response implements Serializable
{
	private static final long serialVersionUID = -577767441985222421L;
	StatusCode returnCode;
	ArrayList<String> parameters = new ArrayList<String>();

	/**
	 * Class constructor.
	 *
	 * @param code status code of the response
	 **/
	public Response(StatusCode code)
	{
		returnCode = code;
	}

	/**
	 * Adds all parameters contained in the specified array to the parameter list.
	 *
	 * @param mex array of parameters
	 **/
	public void addAllParameters(String[] mex)
	{
		for (String s : mex)
		{
			parameters.add(s);
		}
	}

	/**
	 * Adds a parameter to the parameter list.
	 *
	 * @param r parameter
	 **/
	public void addParameter(String r)
	{
		parameters.add(r);
	}

	/**
	 * Getter for the parameter list.
	 *
	 * @return parameter list
	 **/
	public ArrayList<String> getParameters()
	{
		return parameters;
	}

	/**
	 * Getter for the return code.
	 *
	 * @return status code
	 **/
	public StatusCode getReturnCode()
	{
		return returnCode;
	}
}

/**
 * The enum {@code StatusCode} encodes the possible return values of a server
 * request.
 **/
enum StatusCode
{
	SUCCESS, INVALID_ARGUMENTS, NOT_AUTHORIZED, ERROR
}
