import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.List;


public class UpdateUserMessage extends MessageBase{
	
	private String name;
	private String id;
	private String passwd;
	private String mail;
	private String telephone;
	private List<String> friends;
	
	
	public UpdateUserMessage(String id, String name, String passwd
							, String mail, String tel, List<String> friends){
		this.name = name;
		this.id = id;
		this.passwd = passwd;
		this.mail = mail;
		this.telephone = tel;
		this.friends = friends;
	}
	
	public UpdateUserMessage(){
		
	}
	
	public String getName(){
		return name;
	}
	
	public String getId(){
		return id;
	}
	
	public String getMail(){
		return mail;
	}
	
	public String getTelephone(){
		return telephone;
	}
	
	public String getPassword(){
		return passwd;
	}
	
	public List<String> getFriends(){
		return friends;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public void setPassword(String passwd){
		this.passwd = passwd;
	}

	
	public void setMail(String mail){
		this.mail = mail;
	}
	
	public void setFriends(List<String> friends){
		this.friends = friends;
	}
	
	@Override
	public MessageType getType() {
		// TODO Auto-generated method stub
		return MessageType.UPDATE_USER;
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
		
		String fs = new String();
		for(int i = 0; i < friends.size(); i++){
			fs = fs + "\\"+ friends.get(i);
		}
		
		String msg = "updateMeeting\n\r" 
					+ name + " " + passwd + " " + mail + " " + telephone + " " 
					+ fs + "\n\r\n\r";
		
		try {
			out.write(msg.getBytes());
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void getUserInfo(InputStream in){
		
	}
}
