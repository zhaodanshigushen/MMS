import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;


public class RegisterMessage extends MessageBase{
	
	private String name;
	private String passwd;
	private String mail;
	private String telephone;
	
	public RegisterMessage(){
		
	}
	
	public RegisterMessage(String name, String passwd, String mail, String telephone){
		this.name = name;
		this.passwd = passwd;
		this.mail = mail;
		this.telephone = telephone;
	}
	
	public String getName(){
		return name;
	}
	
	public String getPassword(){
		return passwd;
	}
	
	public String getMail(){
		return mail;
	}
	
	public String getTelephone(){
		return telephone;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setPassword(String passwd){
		this.passwd = passwd;
	}
	
	public void setMail(String mail){
		this.mail = mail;
	}
	
	public void setTelephone(String tel){
		this.telephone = tel;
	}

	@Override
	public MessageType getType() {
		// TODO Auto-generated method stub
		return MessageType.REGISTER;
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
		
		String msg = "register\n\r"
					+ name + " "+ passwd + " " + mail + " " + telephone
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
