package assegnamento3;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The enum {@code StatusCode} encodes the possible return values of a server request.
 **/
enum StatusCode
{
	SUCCESS, INVALID_ARGUMENTS
}

/**
 * The class {@code Response} represents a serializable response of the server to a client's request. <p>
 * Contains a return code and a list of result parameters.
 **/
public class Response implements Serializable
{

	private static final long serialVersionUID = -577767441985222421L;

	StatusCode returnCode;

	ArrayList<String> parameters = new ArrayList<String>();

    /**
     * Class constructor.
     **/
	public Response(StatusCode code)
	{
		returnCode = code;
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
     * Getter for the return code.
     *
     * @return status code
     **/
	public StatusCode getReturnCode()
	{
		return returnCode;
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

}
