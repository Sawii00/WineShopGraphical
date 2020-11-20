package assegnamento3;

import java.io.Serializable;
import java.util.ArrayList;


public class Request implements Serializable{
	
	private static final long serialVersionUID = -3573650597397447309L;
	
	String method;
	ArrayList<String> parameters = new ArrayList<String>();
	
	public Request(String m)
	{
		method = m;
	}
	
	public void addParameter(String s)
	{
		parameters.add(s);
	}
	
	public String getMethod()
	{
		return method;
	}
	
	public ArrayList<String> getParameters()
	{
		return parameters;
	}
	
	public String toString() 
	{
		String res = method + ": ";
		for(var i: parameters)
		{
			res += i + ", ";
		}
		return res;
	}
	
	
	
	
	
}
