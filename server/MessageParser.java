import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class MessageParser {
	
	private InputStream in;
	private OutputStream out;
	private Connection sqlConn;
	
	public MessageParser(InputStream in, OutputStream out, Connection sqlConn){
		this.in = in;
		this.out = out;
		this.sqlConn = sqlConn;
	}
	
	public void parse(){
		
		int count = 0;
		byte [] input = new byte[2048];
		
		/** 读入一条报文 */
		try {
			while((input[count] = (byte) in.read()) != -1 && count < 1024){
				if(count >= 3 && input[count-3] == '\n' && input[count-2] == '\r' 
				&& input[count-1] == '\n' && input[count] == '\r'){
					break;
				}						
					count++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String mesgStr = null;
		try {
			mesgStr = new String(input,"UTF-8");
			System.out.println(mesgStr);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		/** 注册 */
		if(mesgStr.startsWith("register")){
			registerHandler(mesgStr);
			return;
		}
		
		/** 登录 */
		if(mesgStr.startsWith("login")){
			loginHandler(mesgStr);
			return;
		}
		
		/** 更新用户 */
		if(mesgStr.startsWith("updateUser")){
			updateUserHandler(mesgStr);
			return;
		}
		
		/** 获取用户信息 */
		if(mesgStr.startsWith("getUserInfo")){
			getUserInfoHandler(mesgStr);
			return;
		}

		/** 添加好友 */
		if(mesgStr.startsWith("addFriend")){
			addFriendHandler(mesgStr);
			return;
		}
		
		/** 删除好友 */
		if(mesgStr.startsWith("deleteFriend")){
			deleteFriendHandler(mesgStr);
			return;
		}
		
		/** 列出好友 */
		if(mesgStr.startsWith("listFriend")){
			listFriendHandler(mesgStr);
			return;
		}
		
		/** 添加会议 */
		if(mesgStr.startsWith("addMeeting")){
			addMeetingHandler(mesgStr);
			return;
		}
		
		/** 删除会议 */
		if(mesgStr.startsWith("deleteMeeting")){
			deleteMeetingHandler(mesgStr);
			return;
		}
		
		/** 更新会议 */
		if(mesgStr.startsWith("updateMeeting")){
			updateMeetingHandler(mesgStr);
			return;
		}
		
		/** 获取所有会议 */
		if(mesgStr.startsWith("listMeetings")){
			listMeetingsHandler(mesgStr);
			return;
		}
		
		/** 获取会议信息 */
		if(mesgStr.startsWith("getMeetingInfo")){
			getMeetingInfoHandler(mesgStr);
			return;
		}
		
	}
	
	/** 获取单个会议信息的操作 */
	private void getMeetingInfoHandler(String mesgStr) {
		// TODO Auto-generated method stub
		int begin, end;
		begin = mesgStr.indexOf("\n\r");
		begin += 2;
		
		end = mesgStr.indexOf("\n\r\n\r", begin);
		String mid = mesgStr.substring(begin, end);
		mid = mid.trim();
		
		try {
			if(sqlConn == null || sqlConn.isClosed()){
				System.out.println("There is something wrong with connetion to mysql");
				return;
			}
			
			//检测用户是否存在
			Statement statement = sqlConn.createStatement();
			String sql = "select * from meeting where mid = '" + mid + "';";
			System.out.println(sql);
			ResultSet rs = statement.executeQuery(sql);
			if(rs.next()){//用户存在,获取用户信息发送信息
				String sp = rs.getString("sponsor");
				String participators = rs.getString("participator");
				String stime = rs.getString("stime");
				String etime = rs.getString("etime");
				String title = rs.getString("title");
				String position = rs.getString("position");
				String cont = rs.getString("content");
				
				//查询sponsor的用户名
				sql = "SELECT username FROM user where uid='"+ sp +"'";
				ResultSet rs2 = statement.executeQuery(sql);
				rs2.next();
				sp = sp + "-" + rs2.getString("username");
				
				//获取参与者的用户名
				String [] ps = participators.split("/");
				for(int i = 0; i < ps.length; i++){
					String id = ps[i];
					sql = "SELECT username FROM user where uid='"+id+"'";
					ResultSet r = statement.executeQuery(sql);
					if(!r.next()){
						String mesg = "getMeetingInfoFail\n\r SQL error!\n\r\n\r";
						out.write(mesg.getBytes("UTF-8"));
						out.flush();
						return;
					}
					else{
						String username = r.getString("username");
						ps[i] = ps[i] + "-" + username;
					}
				}
				
				//构建会议信息
				String mesg = "getMeetingInfoSuccess\n\r" + sp + " " + ps[0];
				for(int i = 1; i < ps.length; i++){
					mesg = mesg + "/" + ps[i];
				}
				mesg = mesg + " " + stime + " " + etime + " " + title + " " + position
						    + " " + cont + "\n\r\n\r";
				
				out.write(mesg.getBytes("UTF-8"));
				out.flush();
			}
			else{//用户不存在
				System.out.println("getMeetingInfo failed! The meeting doesn't exist!");
				String mesg = "getUserMeetingFail\n\r The meeting doesn't exist \n\r\n\r";
				out.write(mesg.getBytes("UTF-8"));
				out.flush();
			}
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** 获取与用户相关的所有会议信息 */
	private void listMeetingsHandler(String mesgStr) {
		// TODO Auto-generated method stub
		int begin, end;
		begin = mesgStr.indexOf("\n\r");
		begin += 2;
		
		end = mesgStr.indexOf("\n\r\n\r", begin);
		String uid = mesgStr.substring(begin, end);
		uid = uid.trim();
		
		try {
			if(sqlConn == null || sqlConn.isClosed()){
				System.out.println("There is something wrong with connetion to mysql");
				return;
			}
			
			//检测用户是否存在
			Statement statement = sqlConn.createStatement();
			String sql = "select * from user where uid = '" + uid + "';";
			ResultSet userRs = statement.executeQuery(sql);
			System.out.println(sql);
			
			if(userRs.next()){//用户存在,获取用户信息发送信息
				
				String mesg = "getMeetingInfoSuccess\n\r";
				
				sql = "select * from user_meeting where uid = '" + uid + "';";
				System.out.println(sql);
				//statement.execute(sql);
				//ResultSet mrs = statement.getResultSet();
				ResultSet mrs = statement.executeQuery(sql);
				
				/*String mids = mrs.getString("mid");
				System.out.println(mids);*/
				
				//获取含有该用户的所有会议
				while(mrs.next()){
					//获取会议id
					String mid = mrs.getString("mid");
					sql = "select * from meeting where mid = '" + mid + "';";
					Statement statement1 = sqlConn.createStatement();
					ResultSet rs = statement1.executeQuery(sql);
					
					if(rs.next()){
						String sp = rs.getString("sponsor");
						String participators = rs.getString("participator");
						String stime = rs.getString("stime");
						String etime = rs.getString("etime");
						String title = rs.getString("title");
						String position = rs.getString("position");
						String cont = rs.getString("content");
						
						//查询sponsor的用户名
						sql = "SELECT username FROM user where uid='"+ sp +"'";
						ResultSet rs2 = statement1.executeQuery(sql);
						rs2.next();
						sp = sp + "-" + rs2.getString("username");
						
						//获取参与者的用户名
						String [] ps = participators.split("/");
						for(int i = 0; i < ps.length; i++){
							String id = ps[i];
							sql = "SELECT username FROM user where uid='"+id+"'";
							ResultSet r = statement1.executeQuery(sql);
							if(!r.next()){
								mesg = "listMeetingFail\n\r SQL error!\n\r\n\r";
								out.write(mesg.getBytes("UTF-8"));
								out.flush();
								return;
							}
							else{
								String username = r.getString("username");
								ps[i] = ps[i] + "-" + username;
							}
						}
						
						//构建会议信息
						mesg = mesg +mid+" " + sp + " " + ps[0];
						for(int i = 1; i < ps.length; i++){
							mesg = mesg + "/" + ps[i];
						}
						mesg = mesg + " " + stime + " " + etime + " " + title + " " + position
								    + " " + cont + "\n\r";
					}
				}
				mesg = mesg + "\n\r";
				out.write(mesg.getBytes("UTF-8"));
				out.flush();
			}
			else{//用户不存在
				System.out.println("listMeeting failed! The user doesn't exist!");
				String mesg = "listMeetingFail\n\r The user doesn't exist \n\r\n\r";
				out.write(mesg.getBytes("UTF-8"));
				out.flush();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** 获取用户信息的操作 */
	private void getUserInfoHandler(String mesgStr) {
		// TODO Auto-generated method stub
		int begin, end;
		begin = mesgStr.indexOf("\n\r");
		begin += 2;
		
		end = mesgStr.indexOf("\n\r\n\r", begin);
		String uid = mesgStr.substring(begin, end);
		uid = uid.trim();
		
		try {
			if(sqlConn == null || sqlConn.isClosed()){
				System.out.println("There is something wrong with connetion to mysql");
				return;
			}
			
			//检测用户是否存在
			Statement statement = sqlConn.createStatement();
			String sql = "select * from user where uid = '" + uid + "';";
			System.out.println(sql);
			ResultSet rs = statement.executeQuery(sql);
			if(rs.next()){//用户存在,获取用户信息发送信息
				String name = rs.getString("username");
				String password = rs.getString("password");
				String email = rs.getString("email");
				String tel = rs.getString("phone");
				
				String mesg = "getUserInfoSuccess\n\r" + uid + " " + password + " " 
				                                       + email + " " + tel + "\n\r\n\r";
				out.write(mesg.getBytes("UTF-8"));
				out.flush();
			}
			else{//用户不存在
				System.out.println("getUserInfo failed! The name has been used!");
				String mesg = "getUserInfoFail\n\r The user doesn't exist \n\r\n\r";
				out.write(mesg.getBytes("UTF-8"));
				out.flush();
			}
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** 注册的操作 */
	private void registerHandler(String mesgStr){
		int begin, end;
		begin = mesgStr.indexOf("\n\r");
		begin += 2;
		
		end = mesgStr.indexOf("\n\r", begin);
		String content = mesgStr.substring(begin, end);
		content = content.trim();
		
		//解析协议字符串
		int sp1 = 0;
		int sp2 =  content.indexOf(" ");
		String name = content.substring(sp1, sp2);
		
		sp1 = sp2 + 1;
		sp2 = content.indexOf(' ', sp1 + 1);
		String password = content.substring(sp1, sp2);
		
		sp1 = sp2 + 1;
		sp2 = content.indexOf(' ', sp1 + 1);
		String mail = content.substring(sp1, sp2);
		
		sp1 = sp2 + 1;
		String tel = content.substring(sp1);
		
		try {
			if(sqlConn == null || sqlConn.isClosed()){
				System.out.println("There is something wrong with connetion to mysql");
				return;
			}
			
			//检测用户是否存在
			Statement statement = sqlConn.createStatement();
			String sql = "select username from user where username = '" + name + "' or email = '"+mail+"';";
			System.out.println(sql);
			ResultSet rs = statement.executeQuery(sql);
			if(!rs.next()){//用户不存在
				sql = "INSERT INTO user VALUES(null, '" + name + "','" + password + "','" + mail + "','" + tel + "');";
				System.out.println(sql);
				//向数据库插入用户
				if(statement.executeUpdate(sql) > 0){
					String mesg = "registerSuccess\n\r Register successfully \n\r\n\r";
					out.write(mesg.getBytes("UTF-8"));
					out.flush();
				}
				else{
					String mesg = "registerFail\n\r Register failed \n\r\n\r";
					out.write(mesg.getBytes("UTF-8"));
					out.flush();
				}
			}
			else{//用户存在
				System.out.println("Register failed! The name has been used!");
				String mesg = "registerFail\n\r The name has been used \n\r\n\r";
				out.write(mesg.getBytes("UTF-8"));
				out.flush();
			}
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** 登录的操作 */
	private void loginHandler(String mesgStr){
		int begin, end;
		begin = mesgStr.indexOf("\n\r");
		begin += 2;
		
		end = mesgStr.indexOf("\n\r", begin);
		String content = mesgStr.substring(begin, end);
		content = content.trim();
		
		int sp1 = 0;
		int sp2 =  content.indexOf(" ");
		String name = content.substring(sp1, sp2);
		
		sp1 = sp2 + 1;
		String pas = content.substring(sp1);
		
		try {
			if(sqlConn == null || sqlConn.isClosed()){
				System.out.println("There is something wrong with connetion to mysql");
				return;
			}
			
			Statement statement = sqlConn.createStatement();
			String sql = "select username from user where username = '" + name + "';";
			System.out.println(sql);
			ResultSet rs = statement.executeQuery(sql);
			if(rs.next()){
				sql = "select * from user where username='" + name + "' and password='" + pas + "';" ;
				System.out.println(sql);
				if(statement.execute(sql)){//要修改
					ResultSet result = statement.getResultSet();
					if(result.next()){
						String uid = result.getString("uid");
						String uname = result.getString("username");
						String email = result.getString("email");
						String tel = result.getString("phone");
						System.out.println(name + " logins successfully!");
						String mesg = "loginSuccess\n\r" + uid + " " + uname + " " 
														 + email + " " + tel + "\n\r\n\r";
						out.write(mesg.getBytes("UTF-8"));
						out.flush();
					}
					else{
						System.out.println(name + " logins failed!");
						String mesg = "loginFail\n\r Incorrect password! \n\r\n\r";
						out.write(mesg.getBytes("UTF-8"));
						out.flush();
					}
				}
				
			}
			else{
				System.out.println("Login failed! The name doesn't exist!");
				String mesg = "loginFail\n\r The name doesn't exist! \n\r\n\r";
				out.write(mesg.getBytes("UTF-8"));
				out.flush();
			}
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** 添加会议的操作 */
	private void addMeetingHandler(String mesgStr){
		int begin, end;
		begin = mesgStr.indexOf("\n\r");
		begin += 2;
		
		//获取主要内容
		end = mesgStr.indexOf("\n\r", begin);
		String content = mesgStr.substring(begin, end);
		content = content.trim();
		
		//获取主要内容的各个字段
		int sp1 = 0;
		int sp2 =  content.indexOf(" ");
		String uid = content.substring(sp1, sp2);
		
		sp1 = sp2 + 1;
		sp2 = content.indexOf(" ",sp1);
		String participators = content.substring(sp1,sp2);
		
		sp1 = sp2 + 1;
		sp2 = content.indexOf(" ",sp1);
		String stime = content.substring(sp1,sp2);
		
		sp1 = sp2 + 1;
		sp2 = content.indexOf(" ",sp1);
		String etime = content.substring(sp1,sp2);
		
		sp1 = sp2 + 1;
		sp2 = content.indexOf(" ",sp1);
		String title = content.substring(sp1,sp2);
		
		sp1 = sp2 + 1;
		sp2 = content.indexOf(" ",sp1);
		String position = content.substring(sp1,sp2);
		
		sp1 = sp2 + 1;
		String cont = content.substring(sp1);
		
		try {
			if(sqlConn == null || sqlConn.isClosed()){
				System.out.println("There is something wrong with connetion to mysql");
				return;
			}
			
			//判断user表里面是否有存在对应的用户
			Statement statement = sqlConn.createStatement();
			String sql = "select * from user where uid = '" + uid + "';";
			System.out.println(sql);
			String sponsor = "";
			ResultSet rs = statement.executeQuery(sql);
			if(rs.next()){//用户存在
				//验证每个参与者是否都是已注册用户，若存在不是的，则添加失败
				sponsor = rs.getString("username");
				String [] ps = participators.split("/");
				List<String> toaddresses = new ArrayList<String>();
				toaddresses.clear();
				for(int i = 0; i < ps.length; i++){
					String id = ps[i];
					sql = "SELECT email FROM user where uid='"+id+"';";
					ResultSet r = statement.executeQuery(sql);
					if(!r.next()){
						String mesg = "addMeetingFail\n\r Participator error!\n\r\n\r";
						out.write(mesg.getBytes("UTF-8"));
						out.flush();
						return;
					}
					else{
							toaddresses.add(r.getString("email"));
							while(r.next())
								toaddresses.add(r.getString("email"));
					}
				}
				
				sql = "INSERT INTO meeting values(null,'" + title 
						                        + "','" + uid 
						                        + "','" + participators
						                        + "','" + stime
						                        + "','" + etime
						                        + "','" + position
						                        + "','" + cont
						                        + "');";
				System.out.println(sql);
				MyAuthenticator.addintoSendingList(sponsor, toaddresses, stime, etime, title, position);
				String mid = new String();
				if(statement.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS) > 0){
					//获取自动生成的mid
					ResultSet key = statement.getGeneratedKeys();
					if (key != null && key.next()) {
						mid = key.getString(1);
						System.out.println("============"+mid);
						//向user_meeting插入用户与会议关系
						sql = "INSERT INTO user_meeting values('" + uid + "','" + mid + "','" + "1');";
						System.out.println(sql);
						statement.executeUpdate(sql);
						for(int i = 0; i < ps.length; i++){
							sql = "INSERT INTO user_meeting values('" + ps[i] + "','" + mid + "','" + "0');";
							System.out.println(sql);
							statement.executeUpdate(sql);
						}
					}
				}
				
				sql = "INSERT INTO meeting values(null,'" + title 
                        + "','" + uid 
                        + "','" + participators
                        + "','" + stime
                        + "','" + etime
                        + "','" + position
                        + "','" + cont
                        + "';";
				
				String mesg = "addMeetingSuccess\n\r"+mid+"\n\r";
				out.write(mesg.getBytes("UTF-8"));
				out.flush();
				
			}
			else{//用户不存在
				System.out.println("User doesn't exit!");
				String mesg = "addMeetingFail\n\r Sponsor doesn't exit!\n\r\n\r";
				out.write(mesg.getBytes("UTF-8"));
				out.flush();
			}
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	/** 删除会议的操作 */
	private void deleteMeetingHandler(String mesgStr){
		
		//解析协议
		int begin, end;
		begin = mesgStr.indexOf("\n\r");
		begin += 2;
		
		end = mesgStr.indexOf("\n\r", begin);
		String mid = mesgStr.substring(begin, end);
		mid = mid.trim();
		
		try {
			if(sqlConn == null || sqlConn.isClosed()){
				System.out.println("There is something wrong with connetion to mysql");
				return;
			}
			
			//判断meeting表里面是否有存在要删除的会议
			Statement statement = sqlConn.createStatement();
			String sql = "select * from meeting where mid = '" + mid + "';";
			System.out.println(sql);
			ResultSet rs = statement.executeQuery(sql);
			if(rs.next()){//会议存在
				//从数据库中删除对应的数据
				sql = "DELETE FROM meeting where mid = '" + mid + "';" ;
				String sql2 = "DELETE FROM user_meeting where mid = '" + mid + "';" ;
				System.out.println(sql);
				if(statement.executeUpdate(sql2) <= 0)
					System.out.println("Something wrong with table user_meeting.");
				
				if(statement.executeUpdate(sql) > 0 ){//删除成功
					System.out.println("Delete meeting "+mid+" successfully!");
					String mesg = "deleteMeetingSuccess\n\r DeleteMeeeting successfully \n\r\n\r";
					out.write(mesg.getBytes("UTF-8"));
					out.flush();
				}
				else{//删除失败
					System.out.println("Delete meeting failed!");
					String mesg = "deleteMeetingFail\n\r\n\r";
					out.write(mesg.getBytes("UTF-8"));
					out.flush();
				}
			}
			else{//会议不存在
				System.out.println("Meeting doesn't exit!");
				String mesg = "deleteMeetingFail\n\r Meeting doesn't exit!\n\r\n\r";
				out.write(mesg.getBytes("UTF-8"));
				out.flush();
			}
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/** 删除好友的操作 */
 	private void deleteFriendHandler(String mesgStr){
		int begin, end;
		begin = mesgStr.indexOf("\n\r");
		begin += 2;
		
		end = mesgStr.indexOf("\n\r", begin);
		String content = mesgStr.substring(begin, end);
		content = content.trim();
		
		int sp1 = 0;
		int sp2 =  content.indexOf(" ");
		String uid = content.substring(sp1, sp2);
		
		sp1 = sp2 + 1;
		String fid = content.substring(sp1);
		
		try {
			if(sqlConn == null || sqlConn.isClosed()){
				System.out.println("There is something wrong with connetion to mysql");
				return;
			}
			
			//判断user表里面是否有存在对应的用户
			Statement statement = sqlConn.createStatement();
			String sql = "select * from user where uid = '" + uid + "';";
			System.out.println(sql);
			ResultSet rs = statement.executeQuery(sql);
			if(rs.next()){//用户存在
				//从数据库中获取好友列表数据
				sql = "DELETE FROM friends where uid = '" + uid + "' and fid='"+ fid + "';" ;				
				System.out.println(sql);
				if(statement.executeUpdate(sql) > 0){
					String mesg = "deleteFriendSuccess\n\r\n\r";
					out.write(mesg.getBytes("UTF-8"));
					out.flush();
				}
				else{
					String mesg = "deleteFriendFail\n\r Maybe friend doesn't exsit!\n\r\n\r";
					out.write(mesg.getBytes("UTF-8"));
					out.flush();
				}
			}
			else{//用户不存在
				System.out.println("User doesn't exit!");
				String mesg = "deleteFriendFail\n\r User doesn't exit!\n\r\n\r";
				out.write(mesg.getBytes("UTF-8"));
				out.flush();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** 添加好友的操作 */
	private void addFriendHandler(String mesgStr){
		
		//解析协议
		int begin, end;
		begin = mesgStr.indexOf("\n\r");
		begin += 2;
		
		end = mesgStr.indexOf("\n\r", begin);
		String content = mesgStr.substring(begin, end);
		content = content.trim();
	
		//把信息中的朋友信息分离出来，存在ArrayList里面
		int sp1 = 0;
		int sp2 =  content.indexOf(" ");
		
		String uid = content.substring(0, sp2);
		
		sp1 = sp2 + 1;
		String name = content.substring(sp1);
		
		try {
			if(sqlConn == null || sqlConn.isClosed()){
				System.out.println("There is something wrong with connetion to mysql");
				return;
			}
			
			//判断user表里面是否有存在对应的用户
			Statement statement = sqlConn.createStatement();
			String sql = "select * from user where uid = '" + uid + "';";
			System.out.println(sql);
			ResultSet rs = statement.executeQuery(sql);
			if(rs.next()){//用户存在
				//从数据库中获取好友列表数据
				sql = "SELECT * FROM user where username = '" + name + "';" ;				
				System.out.println(sql);
				ResultSet query = statement.executeQuery(sql);
				
				if(query.next()){
					String fid = query.getString(1);
					String email = query.getString(4);
					String phone = query.getString(5);
					sql = "SELECT * FROM friends where uid='" + uid + "' and fid='"+ fid+"';";
					ResultSet rs2 = statement.executeQuery(sql);
					if(rs2.next()){
						System.out.println("Add friends failed!");
						String mesg = "addFriendFail\n\r You two have been friends.\n\r\n\r";
						out.write(mesg.getBytes("UTF-8"));
						out.flush();
						return;
					}
					
					sql = "INSERT INTO friends values('" + uid + "', '" + fid + "')";
					if(statement.executeUpdate(sql) > 0){
						System.out.println("Add friends successfully!");
						String mesg = "addFriendSuccess\n\r" + fid + " " + name + " " 
															 + email + " " + phone + "\n\r\n\r";
						out.write(mesg.getBytes("UTF-8"));
						out.flush();
					}
					else{
						System.out.println("Add friends failed!");
						String mesg = "addFriendFail\n\r SQL insert error!\n\r\n\r";
						out.write(mesg.getBytes("UTF-8"));
						out.flush();
					}
				}
				else{
					System.out.println("Add friends failed!");
					String mesg = "addFriendFail\n\r The user you want to make friends with doesn't exist!\n\r\n\r";
					out.write(mesg.getBytes("UTF-8"));
					out.flush();
				}
				
			}
			else{//用户不存在
				System.out.println("Add friends failed!");
				String mesg = "addFriendFail\n\r User doesn't exist!\n\r\n\r";
				out.write(mesg.getBytes("UTF-8"));
				out.flush();
			}
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/** 获取好友列表的操作 */
	private void listFriendHandler(String mesgStr){
		int begin, end;
		begin = mesgStr.indexOf("\n\r");
		begin += 2;
		
		end = mesgStr.indexOf("\n\r", begin);
		String uid = mesgStr.substring(begin, end);
		uid = uid.trim();
		
		
		try {
			if(sqlConn == null || sqlConn.isClosed()){
				System.out.println("There is something wrong with connetion to mysql");
				return;
			}
			
			//判断user表里面是否有存在对应的用户
			Statement statement = sqlConn.createStatement();
			String sql = "select * from user where uid = '" + uid + "';";
			System.out.println(sql);
			ResultSet rs = statement.executeQuery(sql);
			if(rs.next()){//用户存在
				//从数据库中获取好友列表数据
				sql = "SELECT fid FROM friends where uid='" + uid + "';" ;				
				System.out.println(sql);
				ResultSet query = statement.executeQuery(sql);
				
				String fs = "";
				Statement statement1 = sqlConn.createStatement();
				while(query.next()){
					String id = query.getString(1);
					sql = "SELECT * FROM user where uid='"+ id +"';";
					ResultSet rs1 = statement1.executeQuery(sql);
					rs1.next();
					String n = rs1.getString(2);
					String email = rs1.getString(4);
					String phone = rs1.getString(5);
					fs = fs + id + " " + n + " " + email + " "  + phone + "\n\r";
				}
				
				String mesg = "listFriendSuccess\n\r" + fs.trim() + "\n\r\n\r";
				out.write(mesg.getBytes("UTF-8"));
				out.flush();
				
			}
			else{//用户不存在
				System.out.println("User doesn't exit!");
				String mesg = "listFriendFail\n\r User doesn't exit!\n\r\n\r";
				out.write(mesg.getBytes("UTF-8"));
				out.flush();
			}
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** 更新用户信息的操作 */
	private void updateUserHandler(String mesgStr){
		int begin, end;
		begin = mesgStr.indexOf("\n\r");
		begin += 2;
		
		end = mesgStr.indexOf("\n\r", begin);
		String content = mesgStr.substring(begin, end);
		
		int sp1 = 0;
		int sp2 =  content.indexOf(" ");
		String uid = content.substring(sp1, sp2);
		
		sp1 = sp2 + 1;
		sp2 =  content.indexOf(" ", sp1);
		String name = content.substring(sp1,sp2);
		
		sp1 = sp2 + 1;
		sp2 =  content.indexOf(" ", sp1);
		String passwd = content.substring(sp1,sp2);
		
		sp1 = sp2 + 1;
		sp2 = content.indexOf(' ', sp1);
		String mail = content.substring(sp1, sp2);
		
		sp1 = sp2 + 1;
		String tel = content.substring(sp1);
		
		try {
			if(sqlConn == null || sqlConn.isClosed()){
				System.out.println("There is something wrong with connetion to mysql");
				return;
			}
			
			//检测用户是否存在
			Statement statement = sqlConn.createStatement();
			String sql = "select username from user where username = '" + name + "' and uid='" + uid +"';";
			System.out.println(sql);
			ResultSet rs = statement.executeQuery(sql);
			if(rs.next()){
				sql = "UPDATE user SET username='" + name 
						           + "',password='" +passwd
						           + "',email='" + mail 
						           + "',phone='" + tel
						           + "' where uid ='"+uid+"';";
				System.out.println(sql);
				//向数据库插入用户
				if(statement.executeUpdate(sql) > 0){
					String mesg = "updateUserSuccess\n\r Update user successfully \n\r\n\r";
					out.write(mesg.getBytes("UTF-8"));
					out.flush();
				}
				else{
					String mesg = "updateUserFail\n\r Update user failed \n\r\n\r";
					out.write(mesg.getBytes("UTF-8"));
					out.flush();
				}
			}
			else{
				System.out.println("Update user failed! The user doesn't exist!");
				String mesg = "updateUserFail\n\r The user doesn't exist! \n\r\n\r";
				out.write(mesg.getBytes("UTF-8"));
				out.flush();
			}
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/** 更新会议信息的操作 */
	private void updateMeetingHandler(String mesgStr){
		int begin, end;
		begin = mesgStr.indexOf("\n\r");
		begin += 2;
		
		//获取主要内容
		end = mesgStr.indexOf("\n\r", begin);
		String content = mesgStr.substring(begin, end);
		content = content.trim();
		
		//获取主要内容的各个字段
		int sp1 = 0;
		int sp2 =  content.indexOf(" ");
		String mid = content.substring(sp1, sp2);
		
		sp1 = sp2 + 1;
		sp2 = content.indexOf(" ",sp1);
		String uid = content.substring(sp1,sp2);
		
		sp1 = sp2 + 1;
		sp2 = content.indexOf(" ",sp1);
		String participators = content.substring(sp1,sp2);
		
		sp1 = sp2 + 1;
		sp2 = content.indexOf(" ",sp1);
		String stime = content.substring(sp1,sp2);
		
		sp1 = sp2 + 1;
		sp2 = content.indexOf(" ",sp1);
		String etime = content.substring(sp1,sp2);
		
		sp1 = sp2 + 1;
		sp2 = content.indexOf(" ",sp1);
		String title = content.substring(sp1,sp2);
		
		sp1 = sp2 + 1;
		sp2 = content.indexOf(" ",sp1);
		String position = content.substring(sp1,sp2);
		
		sp1 = sp2 + 1;
		String cont = content.substring(sp1);
		
		try {
			if(sqlConn == null || sqlConn.isClosed()){
				System.out.println("There is something wrong with connetion to mysql");
				return;
			}
			
			//判断meeting表里面是否有存在对应的会议
			Statement statement = sqlConn.createStatement();
			String sql = "select * from meeting where mid = '" + mid + "';";
			System.out.println(sql);
			ResultSet rs = statement.executeQuery(sql);
			if(rs.next()){//用户存在
				
				//验证每个参与者是否都是已注册用户，若存在不是的，则添加失败
				String [] ps = participators.split("/");
				for(int i = 0; i < ps.length; i++){
					String id = ps[i];
					sql = "SELECT username FROM user where uid='"+id+"'";
					ResultSet r = statement.executeQuery(sql);
					if(!r.next()){
						String mesg = "updateMeetingFail\n\r Participator error!\n\r\n\r";
						out.write(mesg.getBytes("UTF-8"));
						out.flush();
						return;
					}
				}
				
				//获取原会议的参与者
				sql = "select participator from meeting where mid='" + mid + "';";
				String [] pss = null;
				if(statement.execute(sql)){
					ResultSet rs1 = statement.getResultSet();
					rs1.next();
					String p = rs1.getString("participator");
					pss = p.split("/");
				}
				
				//与更新进行比较，如果是更新中没有的人则删除
				for(int i = 0; i < pss.length; i++){
					boolean delflag = true;
					for(int k = 0; k < ps.length; k++){
						if(ps[k] == pss[i]){
							delflag = false;
							break;
						}		
					}
					if(delflag){
						sql = "DELETE FROM user_meeting where mid='" + mid 
								                     + "' and uid='" + pss[i]
								                     + "' and rel=0;";
						statement.executeUpdate(sql);			                             
					}
				}
				
				//与更新进行比较，如果是更新新增的人则添加
				for(int i = 0; i < ps.length; i++){
					boolean addflag = true;
					for(int k = 0; k < pss.length; k++){
						if(ps[i] == pss[k]){
							addflag = false;
							break;
						}		
					}
					if(addflag){
						sql = "INSERT INTO user_meeting values('" + ps[i] + "','" + mid + "','" + "0');";
						System.out.println(sql);
						statement.executeUpdate(sql);			                             
					}
				}
				
				//更新meeting表的对应meeting
				sql = "UPDATE meeting set title='" + title + "',"
						                + "participator='" + participators + "',"
						                + "stime='" + stime + "',"
						                + "etime='" + etime + "',"
						                + "position='" + position  + "',"
						                + "content='" + cont + "'"
						                + "where mid='" + mid + "';"; 
                      
				if(statement.executeUpdate(sql) > 0){
					String mesg = "updateMeetingSuccess\n\r\n\r";
					out.write(mesg.getBytes("UTF-8"));
					out.flush();
				}
				else{
					String mesg = "updateMeetingFail\n\r sql error\n\r\n\r";
					out.write(mesg.getBytes("UTF-8"));
					out.flush();
				}
				
			}
			else{//用户不存在
				System.out.println("Meeting doesn't exit!");
				String mesg = "updateMeetingFail\n\r Meeting doesn't exit!\n\r\n\r";
				out.write(mesg.getBytes("UTF-8"));
				out.flush();
			}
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			String mesg = "updateMeetingFail\n\r SQL error!\n\r\n\r";
			try {
				out.write(mesg.getBytes("UTF-8"));
				out.flush();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
