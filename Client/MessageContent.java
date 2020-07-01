import java.io.*;
import java.sql.*;
public class MessageContent implements Serializable
{
	private String content;

	public MessageContent(String content)
	{
		this.content=content;
	}
	public String getContent()
	{
		return content;
	}
	public String toString()
	{
		return content;
	}
}