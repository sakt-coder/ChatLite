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
		System.out.println("Enter root password");
		String root_password=new Scanner(System.in).nextLine();
		try{
			connection=DriverManager.getConnection(url,"root",root_password);
		}
		catch(SQLException e){
			System.out.println("Connection to database could not be made");
		}
		System.out.println("Connection to database established");
	}
	public static void main(String args[])throws Exception
	{
		Server server=new Server();
		ServerSocket ss=new ServerSocket(5000);
		while(true)
		{
			System.out.println("Waiting for client");
			Socket sc=ss.accept();
			System.out.println("Client Connected");
			ObjectOutputStream oos=new ObjectOutputStream(sc.getOutputStream());
			ObjectInputStream ois=new ObjectInputStream(sc.getInputStream());
			ClientHandler auth=new ClientHandler(server,sc,oos,ois);
			Thread t=new Thread(auth);
			t.start();
		}
	}
}