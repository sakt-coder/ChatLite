import java.io.Serializable;
public class SignupClass implements Serializable
{
	String username,password,publicKey;
	public SignupClass(String username,String password,String publicKey)
	{
		this.username=username;
		this.password=password;
		this.publicKey=publicKey;
	}
}