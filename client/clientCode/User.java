package clientCode;

import java.util.HashMap;
import java.util.LinkedList;

import javax.security.auth.kerberos.KerberosKey;

import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;

import clientUI.userMeetingManager.AddMeetingAction;

public class User {
	private String id;
	private String name;
	private String password;
	private String telePhone;
	private String mail;
	private LinkedList<Meeting> nowMeeting;
	private LinkedList<Meeting> historyMeeting;
	private LinkedList<User> friend;

	public User() {
		name = "";
		id = "-1";
		telePhone = "";
		id = "";
		password = "";
		nowMeeting = new LinkedList<Meeting>();
		historyMeeting = new LinkedList<Meeting>();
		friend = new LinkedList<User>();
	}

	public User(String uid, String newName, String newPassword, String newMail,
			String newTelephone) {
		name = newName;
		mail = newMail;
		telePhone = newTelephone;
		id = uid;
		password = newPassword;
		nowMeeting = new LinkedList<Meeting>();
		historyMeeting = new LinkedList<Meeting>();
		friend = new LinkedList<User>();
	}

	public User(String uid, String newName, String newMail, String newTelephone) {
		name = newName;
		mail = newMail;
		telePhone = newTelephone;
		id = uid;
		password = "";
		nowMeeting = new LinkedList<Meeting>();
		historyMeeting = new LinkedList<Meeting>();
		friend = new LinkedList<User>();
	}

	public LinkedList<Meeting> getNowMeeting() {
		return nowMeeting;
	}

	public LinkedList<Meeting> getHistoryMeeting() {
		return historyMeeting;
	}

	public LinkedList<User> getFriend() {
		return friend;
	}

	public String getTelePhone() {
		return telePhone;
	}

	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void addNowMeeting(Meeting m) {
		nowMeeting.add(m);
	}

	public void addHistoryMeeting(Meeting m) {
		historyMeeting.add(m);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}