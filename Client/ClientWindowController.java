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
    public void SignIn()throws Exception {
       User user=new User(username.getText(),password.getText());
       client.oos.writeObject(user);
    }
    public void SignUp()throws Exception{
        SignupClass temp=new SignupClass(username.getText(),password.getText());
        client.oos.writeObject(temp);
    }
    public void logout()throws Exception{
        SystemMessage sm=new SystemMessage(username.getText(),-1,new Timestamp(System.currentTimeMillis()));
        System.out.println("Logged out");
        client.oos.writeObject(sm);
        System.out.println("Sent System Message");
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