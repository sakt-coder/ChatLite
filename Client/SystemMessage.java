import java.sql.Timestamp;
import java.io.Serializable;
public class SystemMessage implements Serializable
{
	String note;
	public SystemMessage(String note)
	{
		this.note=note;
	}
}