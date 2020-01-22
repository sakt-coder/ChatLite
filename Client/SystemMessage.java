import java.sql.Timestamp;
import java.io.Serializable;
public class SystemMessage implements Serializable
{
	String sender;
	int id;
	Timestamp time;
	public SystemMessage(String sender,int id,Timestamp time)
	{
		this.sender=sender;
		this.id=id;
		this.time=time;
	}
}