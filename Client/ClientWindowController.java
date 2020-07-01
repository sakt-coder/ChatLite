import java.net.URL;
import java.util.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.fxml.*;
import java.io.*;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import javax.crypto.*;
import java.security.*;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class ClientWindowController
{
    @FXML TextField username;
    @FXML PasswordField password;
    @FXML Label MessageLabel;
    @FXML TextArea SendMessageText;
    @FXML TextField SendTo;
    @FXML Label LoginStatus;
    @FXML Label MessageSent;
    Client client;
    int count;
    boolean isLogged;
    PublicKey publicKey;

    private String hash(String s)throws Exception
    {
        MessageDigest digest=MessageDigest.getInstance("SHA-256");
        byte[] encodedhash=digest.digest(s.getBytes(StandardCharsets.UTF_8));
        StringBuffer hexString=new StringBuffer();
        for(int i=0;i<encodedhash.length;i++)
        {
            String hex=Integer.toHexString(0xff & encodedhash[i]);
            if(hex.length()==1)
                hexString.append(hex);
        }
        return hexString.toString();
    }

    public void SignIn()throws Exception
    {
        if(isLogged==false)
        {
            User user=new User(username.getText(),hash(password.getText()));
            client=new Client(this,user.username);
            client.tos.writeObject(user);
            client.tos.flush();
        }
        else
            LoginStatus.setText("Logout First");
    }
    public void SignUp()throws Exception
    {
        if(isLogged==false)
        {
            client=new Client(this,username.getText());

            //generate keyPair
            KeyPairGenerator keyGen=KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(512);
            KeyPair keyPair=keyGen.genKeyPair();
            //convert keyPair to string
            String publicKey=Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
            String privateKey=Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
            //create database
            String table=client.username+"PrivateKey";
            String query="CREATE TABLE "+table+" ( PrivateKey varchar(2048) )";
            PreparedStatement preStat=client.connection.prepareStatement(query);
            preStat.executeUpdate();
            //store private key in database
            query="INSERT INTO "+table+" VALUES (?)";
            preStat=client.connection.prepareStatement(query);
            preStat.setString(1,privateKey);
            preStat.executeUpdate();

            SignupClass temp=new SignupClass(username.getText(),hash(password.getText()),publicKey);
            client.tos.writeObject(temp);
            client.tos.flush();
        }
        else
            LoginStatus.setText("Logout First");
    }
    public void logout()throws Exception
    {
        if(isLogged==false)
            return;
        SystemMessage sm=new SystemMessage("Logout");
        client.tos.writeObject(sm);
        client.tos.close();
        MessageLabel.setText("Messages will be displayed here");
    }
    public void SendMessage()throws Exception
    {
        //request publicKey
        Request req=new Request(SendTo.getText(),client.username);
        client.tos.writeObject(req);
        client.tos.flush();
        
        Thread.sleep(4000);
        
        //encrypt message
        String content=SendMessageText.getText();
        Cipher cipher=Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE,publicKey);
        content=Base64.getEncoder().encodeToString(cipher.doFinal(content.getBytes()));

        Message ms=new Message(client.username,SendTo.getText(),new Timestamp(System.currentTimeMillis()));
        MessageContent mc=new MessageContent(content);

        client.tos.writeObject(ms);
        client.tos.writeObject(mc);
        client.tos.flush();

        MessageSent.setText("Sent Message"+(count>0?"("+count+++")":""));
    }
}