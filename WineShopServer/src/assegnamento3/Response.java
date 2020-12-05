package assegnamento3;

import java.io.Serializable;
import java.util.ArrayList;

enum StatusCode
{
	SUCCESS, INVALID_ARGUMENTS, NOT_AUTHORIZED, ERROR
}

public class Response implements Serializable
{

	private static final long serialVersionUID = -577767441985222421L;

	StatusCode returnCode;

	ArrayList<String> parameters = new ArrayList<String>();

	public Response(StatusCode code)
	{
		returnCode = code;
	}

	public void addParameter(String r)
	{
		parameters.add(r);
	}

	public void addAllParameters(String[] mex)
	{
		for (String s : mex)
		{
			parameters.add(s);
		}
	}

	public StatusCode getReturnCode()
	{
		return returnCode;
	}

	public ArrayList<String> getParameters()
	{
		return parameters;
	}

}
