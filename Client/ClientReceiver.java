import java.sql.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.security.spec.*;
import java.security.*;
import javax.crypto.*;
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
					//get privatekey
					String table=client.username+"PrivateKey";
					String query="SELECT * FROM "+table;
					PreparedStatement preStat=client.connection.prepareStatement(query);
					ResultSet result=preStat.executeQuery();
					String privateKey=null;
					while(result.next())
						privateKey=result.getString("privateKey");
					KeyFactory factory=KeyFactory.getInstance("RSA");
            		PrivateKey prk=(PrivateKey)factory.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));

					//decrypt message
					Message ms=(Message)obj;
					String content=ms.getContent();
					System.out.println(content);
					Cipher cipher=Cipher.getInstance("RSA");
					cipher.init(Cipher.DECRYPT_MODE,prk);
					content=new String(cipher.doFinal(Base64.getDecoder().decode(content)));
					
					//Append message to screen
					String s=ms.getFrom()+": "+content+" "+ms.getSentTime()+"\n";
					System.out.println("Decrypted");
					System.out.println(content);
					msb.append(s);
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
				else if(obj instanceof PublicKey)
				{
			          try{
			            controller.publicKey=(PublicKey)obj;
			        System.out.println("Public Key Received");

			        }catch(Exception e){
			            System.out.println("No Public Key Received");
			        }

				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}
}