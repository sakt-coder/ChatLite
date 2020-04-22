import java.io.*;
import java.net.*;
import java.sql.*;
public class Client
{
	public Socket socket;
	public ObjectOutputStream oos;
	public ObjectInputStream ois;
	public ClientWindowController controller;
	public String username;
	public Connection connection;
	Client(ClientWindowController controller,String username)
	{
		//connecting to server and initializing client
		try{
			socket=new Socket("localhost",5000);
			oos=new ObjectOutputStream(socket.getOutputStream());
			ois=new ObjectInputStream(socket.getInputStream());
		}catch(Exception e){
			System.out.println("Could not connect to Server");
		}
		System.out.println("Connected to Server");
		//connecting to mysql
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

		ClientReceiver clientReceiver=new ClientReceiver(this);
		clientReceiver.setController(controller);
		this.username=username;
        Thread t=new Thread(clientReceiver);
        t.start();
	}
}