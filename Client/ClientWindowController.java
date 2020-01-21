import java.net.URL;
import java.util.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.fxml.*;
public class ClientWindowController implements Initializable
{
    @FXML TextField username;
    @FXML PasswordField password;
    @FXML Label MessageLabel;
    @FXML TextArea SendMessageText;
    @FXML TextField SendTo;
    @FXML Label LoginStatus;
    @FXML Label MessageSent;
    public void SignIn() {
       System.out.println(username.getText()+" signed in with password "+password.getText());
       LoginStatus.setText(username.getText()+" signed in");
       //add SHA-256 encryption
    }
    public void SignUp(){
        System.out.println(username.getText()+" signed up with password "+password.getText());
        LoginStatus.setText("New User "+username.getText()+" created");
    }
    public void SendMessage()
    {
        System.out.println(SendMessageText.getText()+" sent to "+SendTo.getText());
        MessageSent.setText("Message Sent");
    }
    public void initialize(URL url,ResourceBundle rb)
    {

    }
}