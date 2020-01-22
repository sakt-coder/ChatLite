import java.io.*;
import java.net.*;
public class testClient
{
	static Socket socket;
	public static void main(String args[])throws Exception
	{
		try{
			socket=new Socket("localhost",5000);
		}catch(Exception e){
			System.out.println("Could not connect to Server");
		}
		System.out.println("Connected to Server");
		ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
		User obj=new User("Nikhil","123456");
		oos.writeObject(obj);
		socket.close();
	}
}