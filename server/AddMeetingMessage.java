import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.List;


public class AddMeetingMessage extends MessageBase{
	
	private String sponsor;
	private List<String> participators;
	private String stime;
	private String etime;
	private String title;
	private String content;
	private String position;

	@Override
	public MessageType getType() {
		// TODO Auto-generated method stub
		return MessageType.ADD_MEETING;
	}

	@Override
	public boolean operate(Connection conn, OutputStream out) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void send(OutputStream out) {
		// TODO Auto-generated method stub
		if(out == null)
			return;
		
		String ps = new String();
		for(int i = 0; i < participators.size(); i++){
			ps = ps + "\\"+ participators.get(i);
		}
		
		String msg = "addMeeting\n\r"
					+ sponsor + " "+ ps + " "
					+ stime + " " + etime + " "
					+ title + " " + content + " " + position
					+ "\n\r\n\r";
		
		try {
			out.write(msg.getBytes());
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
