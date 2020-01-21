import java.io.*;
import java.net.*;
import java.sql.*;
import javafx.util.Pair;

public class ClientHandler implements Runnable
{
	Server server;
	Socket socket;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	User user;
	ClientHandler(Server server,Socket socket,ObjectOutputStream oos,ObjectInputStream ois)
	{
		this.server=server;
		this.socket=socket;
		this.oos=oos;
		this.ois=ois;
	}
	public void run()
	{
		//Reading Login Details of Client
		Object obj=null;
		try{
			obj=ois.readObject();
		}
		catch(Exception e){
			System.out.println("No response from client");
		}

		//If existing user login, then we receive a User object
		if(obj instanceof User)
		{
			user=(User)obj;
			if(authenticate())
			{
				//Send successful login message to client
				sendAuthentication(true,"Login Succes");
				//Empty the table of username into oos
				server.msh.remove(username,oos);
				startService();
			}
			else
			{
				//Send failed authentication message to client
				sendAuthentication(false,"Invalid Login");
			}
			try{
				this.oos.close();
				this.ois.close();
				this.socket.close();
			}catch(Exception e){
				System.out.println("Some Error Occured");
			}
		}
		//If new user login, then we receive a SignupClass Object
		else
		{
			SignupClass temp=(SignupClass)obj;
			try{
				server.msh.insertUser(temp);
			}catch(Exception e){
				System.out.println("Could not add user");
			}
		}
	}

	public void startService()
	{
		Timestamp time=null;
		//add user to server lists
		server.activeList.add(new Pair<String,Socket>(user.username,socket));
		server.activeUserStreams.add(new Pair<>(ois,oos));
		server.handlers.add(this);
		while(true)
		{
			//receive messages or system messages
			Object obj;
			try{
				obj=ois.readObject();
			}catch(Exception e){
				System.out.println("Some Error Occured");
				break;
			}
			if(obj instanceof Message)
			{
				Message ms=(Message)obj;
				String receiver = ms.getTo();
				ObjectOutputStream oosTo=find(receiver);
				if(oosTo!=null)//If user is online
				{
					//send message to receiver
					ms.setReceivedTime(ms.getSentTime());
					try{
						oosTo.writeObject(ms);
						oosTo.flush();
					}catch(Exception e){
						System.out.println("Some Error Occured");
					}
					//sent success system message
					SystemMessage sm=new SystemMessage(ms.getTo(),1,ms.getSentTime());
					try{
						oos.writeObject(sm);
						oos.flush();
					}catch(Exception e){
						System.out.println("Some Error Occured");
					}
				}
				else //If user is offline 
					server.msh.insertMessage(receiver,ms);
			}
			else if(obj instanceof SystemMessage)
			{
				//If we receive a system message to logout
				SystemMessage sm=(SystemMessage)obj;
				if(sm.id==-1){
					time=sm.time;
					break;
				}
			}
			else
			{
				System.out.println("Some Error Occured");
				break;
			}
		}
		//logging out
		logout(time);
	}

	public ObjectOutputStream find(String receiver)
	{
		for(int i=0;i<server.activeList.size();i++)
		{
			if(server.activeList.get(i).getKey().equals(receiver))
			{
				return server.activeUserStreams.get(i).getValue();
			}
		}
		return null;
	}

	public void logout(Timestamp time)
	{
		//find user and remove him from server lists
		for(int i=0;i<server.activeList.size();i++)
		{
			if(server.activeList.get(i).getKey().equals(user.username))
			{
				server.activeList.remove(i);
				server.activeUserStreams.remove(i);
				server.handlers.remove(i);
				break;
			}
		}
	}
	public void sendAuthentication(boolean flag,String error)
	{
		//send authentication message of invalid or successful login
		Authentication auth=new Authentication(flag,error);
		try{
			oos.writeObject(auth);
			oos.flush();
		}catch(Exception e){
			System.out.println("Some Error Occured");
		}
	}
	public boolean authenticate()
	{
		//we find the password of this user
		try{
			String query="SELECT Password FROM UserTable WHERE UserName='"+user.username+"'";
			PreparedStatement preStat=server.connection.prepareStatement(query);
			ResultSet rs=preStat.executeQuery(query);
			if(rs.next())
			{
				//match the passwords
				if(user.password.equals(rs.getString("Password")))
					return true;
				else
					return false;
			}
		}catch(Exception e){
			System.out.println("Some Error Occured");
		}
		return false;
	}
}