import java.net.URL;
import java.util.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.fxml.*;
import java.io.*;
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
    public void SignIn()throws Exception {
       User user=new User(username.getText(),password.getText());
       client.oos.writeObject(user);
    }
    public void SignUp()throws Exception{
        SignupClass temp=new SignupClass(username.getText(),password.getText());
        client.oos.writeObject(temp);
    }
    public void SendMessage()
    {
        System.out.println(SendMessageText.getText()+" sent to "+SendTo.getText());
        MessageSent.setText("Message Sent");
    }
}