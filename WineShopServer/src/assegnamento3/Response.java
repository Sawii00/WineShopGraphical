package assegnamento3;

import java.io.Serializable;
import java.util.ArrayList;

enum StatusCode
{
	SUCCESS, INVALID_ARGUMENTS, NOT_AUTHORIZED, ERROR
}

public class Response implements Serializable{

	
	private static final long serialVersionUID = -577767441985222421L;

	StatusCode returnCode;
	
	ArrayList<String> arguments = new ArrayList<String>();
	
	public Response(StatusCode code)
	{
		returnCode = code;
	}
	
	public void addArgument(String r)
	{
		arguments.add(r);
	}
	
	public StatusCode getReturnCode()
	{
		return returnCode;
	}
	
	public ArrayList<String> getArguments()
	{
		return arguments;
	}
	
	
}
