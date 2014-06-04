import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;


public abstract class MessageBase {
	
	public static enum MessageType{CONNECTION, LOGIN, REGISTER, UPDATE_USER, UPDATE_MEETING,
								   ADD_MEETING, DELETE_MEETING, GET_MEETING, ADD_FRIEND,
								   GET_FRIENDLIST, DELETE_FRIEND};
	

	public abstract MessageType getType();
	
	public abstract boolean operate(Connection conn, OutputStream out);
	
	public abstract void send(OutputStream out);
	

}
