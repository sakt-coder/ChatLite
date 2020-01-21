import java.sql.Timestamp;
public class SystemMessage
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