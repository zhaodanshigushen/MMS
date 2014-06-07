
import java.util.ArrayList;
import java.util.List;

import javax.mail.*;   

public class MyAuthenticator extends Authenticator implements Runnable{   
    private String userName=null;   
    private String password=null;   
    private static List<Mails> sendingList = new ArrayList<Mails>();
    private static class Mails{
    	public Mails(String sponsor, List<String>particapators, String stime, String etime, String title, String position){
    		spon = sponsor;
    		copyList(particapators, this.toemail);
    		this.stime = stime;
    		this.etime = etime;
    		this.title = title;
    		this.posi = position;
    	}
    	String spon;
    	List<String> toemail = new ArrayList<String>();
    	String stime;
    	String etime;
    	String title;
    	String posi;
		private String gettime(String stime){
			return stime.substring(0,4)+"年"+stime.substring(4,6)+"月"+stime.substring(6, 8)+"日"+stime.substring(8,10)+"时"+stime.substring(10,12)+"分";
		}
    	private void copyList(List<String> from, List<String> to){
    		to.clear();
    		int length = from.size();
    		for(int i=0;i<length;i++)
    			to.add(from.get(i));
    	}
    }
    public MyAuthenticator(){   
    }   
    public MyAuthenticator(String username, String password) {    
        this.userName = username;    
        this.password = password;    
    }    
    protected PasswordAuthentication getPasswordAuthentication(){   
        return new PasswordAuthentication(userName, password);   
    }
    public synchronized static void addintoSendingList(String sponsor, List<String>particapators, String stime, String etime, String title, String position){
    	Mails newmail = new Mails(sponsor,particapators,stime,etime,title,position);
    	sendingList.add(newmail);
    }
    public void run(){
    	System.out.println("enter sending email thread!");
    	MailSenderInfo mailInfo = new MailSenderInfo();    
	    mailInfo.setMailServerHost("smtp.163.com");    
	    mailInfo.setMailServerPort("25");    
	    mailInfo.setValidate(true);    
	    mailInfo.setUserName("Meeting__Manage@163.com");    
	    mailInfo.setPassword("MeetingManage");   
	    mailInfo.setFromAddress("Meeting__Manage@163.com");       
	    mailInfo.setSubject("来自MMS(会议管理系统)：您有新的会议");
	    while(true){
	    	while(!sendingList.isEmpty()){
		    	System.out.println("begin send emails;");
		    	Mails tosend = sendingList.get(0);
		    	String content = tosend.spon+"邀请您于"+tosend.gettime(tosend.stime)+"在"+tosend.posi+"参加会议。会议主题为："+tosend.title+"将持续到"+tosend.gettime(tosend.etime)+",请及时参加。";
			    mailInfo.setContent(content);  
			    System.out.println("start send emails");
			    System.out.println(content);
			    List<String> tomails = new ArrayList<String>();
			    tomails = tosend.toemail;
			    int tomails_size = tomails.size();
			    System.out.println("----------------------"+tomails_size);
			    for(String toaddress : tomails){
			    	mailInfo.setToAddress(toaddress);
			    	System.out.println("===================="+toaddress);
			    	SimpleMailSender.sendHtmlMail(mailInfo);
			    }
			    System.out.println("end of sending");
			    sendingList.remove(0);
	    	}
	    	System.out.print("");
	    }
	    
    }
}   
