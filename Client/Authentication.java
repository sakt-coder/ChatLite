import java.io.Serializable;
public class Authentication implements Serializable
{
	boolean flag;
	String error;
	public Authentication(boolean flag,String error)
	{
		this.flag=flag;
		this.error=error;
	}
}