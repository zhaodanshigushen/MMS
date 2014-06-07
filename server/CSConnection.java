import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class CSConnection implements Runnable{

	private Socket socket;
	private boolean logined;
	private String name; 
	private InputStream in;
	private OutputStream out;
	
	public CSConnection(Socket socket){
		super();
		this.socket = socket;
		try {
			in = socket.getInputStream();
			out = socket.getOutputStream();
		} catch (IOException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		String defaultDomain = "127.0.0.1";  
        String defaultDatabase = "MMS";  
        String defaultUser = "root";  
        String defaultPass = "1234abcd";  
        Connection sqlConn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//Class.forName("java.sql.Driver");
			//sqlConn = DriverManager.getConnection("jdbc:mysql://" + defaultDomain + "/" + defaultDatabase, 
			//										defaultUser, defaultPass);
			//2.获取数据库的连接 
	        sqlConn=DriverManager.getConnection("jdbc:mysql://"+defaultDomain +"/"
	        											+ defaultDatabase
	        											+ "?user=" + defaultUser
	        											+ "&password=" + defaultPass
	        											+ "&useUnicode=true&characterEncoding=utf-8"); 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MessageParser parser = new MessageParser(in, out, sqlConn);
		while(true){
			try{
				socket.sendUrgentData(0xFF);
			}catch(Exception ex){
				break;
			}
			
			parser.parse();
		}
	}

}
