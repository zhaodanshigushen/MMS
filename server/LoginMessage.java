import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;


public class LoginMessage extends MessageBase{
	
	private String name;
	private String passwd;
	
	public LoginMessage(String name, String passwd){
		this.name = name;
		this.passwd = passwd;
	}
	
	public String getName(){
		return name;
	}
	
	public String getPassword(){
		return passwd;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void	setPassword(String passwd){
		this.passwd = passwd;
	}

	@Override
	public MessageType getType() {
		// TODO Auto-generated method stub
		return MessageType.LOGIN;
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
		
		String msg = "login\n\r"
					+ name + " "+ passwd 
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
