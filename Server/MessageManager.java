import java.io.*;
import java.sql.*;
import javafx.util.*;
public class MessageManager
{
	Server server;
	MessageManager(Server server)
	{
		this.server=server;
	}
	public void insertUser(SignupClass sc)throws SQLException
	{
		//UserTable contains the username and password for all users
		String query="INSERT INTO UserTable VALUES (?, ?)";
		PreparedStatement preStat=server.connection.prepareStatement(query);
		preStat.setString(1,sc.username);
		preStat.setString(2,sc.password);
		preStat.executeUpdate();
		//we create a new table in our database
		String table=sc.username+"Table";
		query="CREATE TABLE "+table+" ( Sender varchar(11) , Valid int(255) , Message text(2000) , Time timestamp)";
		preStat=server.connection.prepareStatement(query);
		preStat.executeUpdate();
	}
	//if some user is offline, the message sent to him is stored in his database
	public void insertMessage(String receiver,Message ms)throws SQLException
	{
		String query="INSERT INTO "+receiver+"Table VALUES (?, ?, ?, ?)";
		PreparedStatement preStat=server.connection.prepareStatement(query);
		preStat.setString(1,ms.getFrom());
		preStat.setInt(2,0);
		preStat.setString(3,ms.getContent());
		preStat.setTimestamp(4,ms.getSentTime());
		preStat.executeUpdate();
	}
	//returns all the messages sent to this user when he was offline
	public void remove(String username,ObjectOutputStream oos)throws Exception
	{
		//we connect to the table of the user
		String table=username+"Table";
		String query="SELECT * FROM "+table;
		PreparedStatement preStat=server.connection.prepareStatement(query);
		ResultSet result=preStat.executeQuery();
		while(result.next())
		{
			String sender=result.getString("Sender");
			int valid=result.getInt("Valid");
			Timestamp time=result.getTimestamp("Time");
			String content=result.getString("Message");
			Message ms=new Message(sender,username,content,time);
			oos.writeObject(ms);
			oos.flush();
		}
	}
	
}