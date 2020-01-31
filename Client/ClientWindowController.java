import java.net.URL;
import java.util.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.fxml.*;
import java.io.*;
import java.sql.Timestamp;
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
    public void SignIn()throws Exception {
        if(isLogged==false){
            User user=new User(username.getText(),password.getText());
            client=new Client(this);
            client.oos.writeObject(user);
            isLogged=true;
        }
        else{
            LoginStatus.setText("Logout First");
        }
    }
    public void SignUp()throws Exception{
        if(isLogged==false){
            SignupClass temp=new SignupClass(username.getText(),password.getText());
            client.oos.writeObject(temp);
        }
        else{
            LoginStatus.setText("Logout First");
        }
    }
    public void logout()throws Exception{
        SystemMessage sm=new SystemMessage(username.getText(),-1,new Timestamp(System.currentTimeMillis()));
        System.out.println("Logged out");
        client.oos.writeObject(sm);
        isLogged=false;
        MessageLabel.setText("Messages will be displayed here");
    }
    public void SendMessage()
    {
        Message ms=new Message(username.getText(),SendTo.getText(),SendMessageText.getText(),new Timestamp(System.currentTimeMillis()));
        try{
            client.oos.writeObject(ms);
        }catch(Exception e){
            System.out.println("Could Not Send Message");
        }
        MessageSent.setText("Sent Message"+(count>0?"("+count+++")":""));
    }
}