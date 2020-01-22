import java.io.Serializable;
public class SignupClass implements Serializable
{
	String username,password;
	public SignupClass(String username,String password)
	{
		this.username=username;
		this.password=password;
	}
}