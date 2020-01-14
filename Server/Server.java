import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import javafx.util.Pair;
public class Server
{
	public ArrayList<Pair<String,Socket>> activeList;
	public ArrayList<Pair<ObjectInputStream,ObjectOutputStream>> activeUserStreams;
	public ArrayList<ClientHandler> handlers;
	public MessageManager msh;
	public Connection connection;
	Server()
	{
		activeList=new ArrayList<>();
		activeUserStreams=new ArrayList<>();
		handlers=new ArrayList<>();
		msh=new MessageManager(this);
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch(ClassNotFoundException e){
			System.out.println("JDBC Driver for SQL not found");
		}
		String url="jdbc:mysql://127.0.0.1:3306/Chat_App";
		try{
			connection=DriverManager.getConnection(url,"root","hearmeroar");
		}
		catch(SQLException e){
			System.out.println("Connection to database could not be made");
		}
	}
	public static void main(String args[])throws IOException
	{
		Server server=new Server();
		ServerSocket ss=new ServerSocket(5000);
		while(true)
		{
			Socket sc=ss.accept();
			ObjectOutputStream oos=new ObjectOutputStream(sc.getOutputStream());
			ObjectInputStream ois=new ObjectInputStream(sc.getInputStream());
			ClientHandler auth=new ClientHandler(server,oos,ois);
			Thread t=new Thread(auth);
			t.start();
		}
	}
}