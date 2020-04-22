import java.io.Serializable;
public class SignupClass implements Serializable
{
	String username,password,publicKey;
	public SignupClass(String username,String password,String pubicKey)
	{
		this.username=username;
		this.password=password;
		this.publicKey=publicKey;
	}
}