import java.net.URL;
import java.util.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.fxml.*;
import java.io.*;
import java.sql.Timestamp;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
//this class interacts with the client window
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
    private String hash(String s)throws NoSuchAlgorithmException{
        MessageDigest digest=MessageDigest.getInstance("SHA-256");
        byte[] encodedhash=digest.digest(s.getBytes(StandardCharsets.UTF_8));
        StringBuffer hexString=new StringBuffer();
        for(int i=0;i<encodedhash.length;i++){
            String hex=Integer.toHexString(0xff & encodedhash[i]);
            if(hex.length()==1)
                hexString.append(hex);
        }
        return hexString.toString();
    }
    public void SignIn()throws Exception {
        if(isLogged==false){
            User user=new User(username.getText(),hash(password.getText()));
            client=new Client(this,user.username);
            client.oos.writeObject(user);
        }
        else{
            LoginStatus.setText("Logout First");
        }
    }
    public void SignUp()throws Exception{
        if(isLogged==false){
            SignupClass temp=new SignupClass(username.getText(),hash(password.getText()));
            client=new Client(this,temp.username);
            client.oos.writeObject(temp);
        }
        else{
            LoginStatus.setText("Logout First");
        }
    }
    public void logout()throws Exception{
        if(isLogged==false)
            return;
        SystemMessage sm=new SystemMessage(client.username,-1,new Timestamp(System.currentTimeMillis()));
        System.out.println("Logged out");
        client.oos.writeObject(sm);
        MessageLabel.setText("Messages will be displayed here");
    }
    public void SendMessage()
    {
        Message ms=new Message(client.username,SendTo.getText(),SendMessageText.getText(),new Timestamp(System.currentTimeMillis()));
        try{
            client.oos.writeObject(ms);
        }catch(Exception e){
            System.out.println("Could Not Send Message");
        }
        MessageSent.setText("Sent Message"+(count>0?"("+count+++")":""));
    }
}