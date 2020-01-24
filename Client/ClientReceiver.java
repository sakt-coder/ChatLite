import java.sql.*;
import java.io.*;
import java.net.*;
import javafx.application.Platform;
//ClientReceiver is a thread that runs in the background listening for messages
public class ClientReceiver implements Runnable
{
	Client client;
	ClientWindowController controller;
	ClientReceiver(Client client)
	{
		this.client=client;
	}
	public void setController(ClientWindowController controller)
	{
		this.controller=controller;
	}
	public void run()
	{
		try
		{
			while(true)
			{
				Object obj=client.ois.readObject();
				if(obj instanceof Message)
				{

				}
				else if(obj instanceof Authentication)
				{
					Authentication auth=(Authentication)obj;
					// To perform GUI work from this thread
					Platform.runLater(new Runnable(){
						public void run(){
							controller.LoginStatus.setText(auth.error);
						}
					});
				}
				else if(obj instanceof SystemMessage)
				{

				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Some Error Occured");
		}
	}
}