import java.io.*;
import java.sql.*;
public class Request implements Serializable
{
	String username,from;
	public Request(String username,String from)
	{
		this.username=username;
		this.from=from;
	}
}