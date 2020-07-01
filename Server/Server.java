import MyTor.*;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import javafx.util.Pair;

public class Server
{
	public HashMap<String,TorSocket> activeUserMap;
	public MessageManager msh;
	public Connection connection;
	Server()throws Exception
	{
		activeUserMap=new HashMap<>();
		msh=new MessageManager(this);
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url="jdbc:mysql://127.0.0.1:3306/Chat_App";
		System.out.println("Enter root password");
		String root_password=new String(System.console().readPassword());
		
		connection=DriverManager.getConnection(url,"root",root_password);
	}
	public static void main(String args[])throws Exception
	{
		Server server=new Server();
		TorServer ss=new TorServer(5000);
		while(true)
		{
			System.out.println("Waiting for client");
			TorSocket ts=ss.accept();
			System.out.println("New Client Connected");
			ClientHandler auth=new ClientHandler(server,ts);
			Thread t=new Thread(auth);
			t.start();
		}
	}
}