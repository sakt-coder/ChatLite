import java.sql.*;
import java.io.*;
import java.net.*;
import javafx.application.Platform;
//ClientReceiver is a thread that runs in the background listening for messages
public class ClientReceiver implements Runnable
{
	Client client;
	ClientWindowController controller;
	StringBuilder msb=new StringBuilder();
	ClientReceiver(Client client)
	{
		this.client=client;
		// controller=client.controller;
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
					Message ms=(Message)obj;
					msb.append(ms.toString());
					Platform.runLater(new Runnable(){
						public void run(){
							controller.MessageLabel.setText(msb.length()==0?"You have no messages":msb.toString());
						}
					});
				}
				else if(obj instanceof Authentication)
				{
					Authentication auth=(Authentication)obj;
					// To perform GUI work from this thread
					Platform.runLater(new Runnable(){
						public void run(){
							controller.LoginStatus.setText(auth.error);
							if(auth.error.equals("Login Success"))
								controller.isLogged=true;
							else if(auth.error.equals("Logged Out"))
								controller.isLogged=false;
						}
					});
					if(auth.error.equals("Logged Out")){

						client.ois.close();
						client.oos.close();
						break;
					}
				}
				else if(obj instanceof SystemMessage)
				{
					SystemMessage sm=(SystemMessage)obj;
					
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Some Error Occured");
		}
	}
}