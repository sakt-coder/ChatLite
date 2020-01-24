import java.io.*;
import java.net.*;
public class Client
{
	public Socket socket;
	public ObjectOutputStream oos;
	public ObjectInputStream ois;
	Client()
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
	}
}