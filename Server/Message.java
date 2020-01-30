import java.io.*;
import java.sql.*;
public class Message implements Serializable
{
	private String from;
	private String to;
	private String content;
	private Timestamp sentTime;
	private Timestamp receivedTime;
	private Timestamp seenTime;

	public Message(String from,String to,String content,Timestamp sentTime)
	{
		this.from=from;
		this.to=to;
		this.content=content;
		this.sentTime=sentTime;
	}
	public String getFrom()
	{
		return from;
	}
	public String getTo()
	{
		return to;
	}
	public String getContent()
	{
		return content;
	}
	public Timestamp getSentTime()
	{
		return sentTime;
	}
	public Timestamp getReceivedTime()
	{
		return receivedTime;
	}
	public Timestamp getSeenTime()
	{
		return seenTime;
	}
	public void setReceivedTime(Timestamp t)
	{
		receivedTime=t;
	}
	public void setSeenTime(Timestamp t)
	{
		seenTime=t;
	}
	public String toString()
	{
		String s=from+": "+content+" "+sentTime+"\n";
		return s;
	}
}