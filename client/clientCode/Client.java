package clientCode;

import java.awt.datatransfer.StringSelection;
import java.awt.print.Printable;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import clientUI.userFrameControl;

public class Client {
	private static String serverIP = "172.18.157.1";
	private static int serverPort = 12345;
	public static Socket socket;

	private final String splitSign = "\n\r";
	public static User user;
	private OutputStream out;
	private InputStream in;
	private boolean connective;

	public Client() {
		connective = false;
		user = new User();
	}

	public void run() throws UnknownHostException, IOException {
		socket = new Socket(serverIP, serverPort);
		out = socket.getOutputStream();
		in = socket.getInputStream();
		connective = true;
	}

	public boolean getConnective() {
		return connective;
	}

	public User getUser() {
		return user;
	}

	public void leave() {
		try {
			if (socket != null)
				socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getMessageFromServer() throws IOException {
		byte[] data = new byte[3000000];
		int count = 0;
		while ((data[count] = (byte) in.read()) != -1 && count < 300000) {
			if (count >= 3 && data[count - 3] == '\n'
					&& data[count - 2] == '\r' && data[count - 1] == '\n'
					&& data[count] == '\r')
				break;
			count++;
		}
		String outstring = new String(data, "UTF-8");
		return outstring;
	}

	public void sendMessageToServer(String message)
			throws UnsupportedEncodingException, IOException {
		out.write(message.getBytes("UTF-8"));
		out.flush();
	}

	public String login(String name, String password) throws IOException {
		String loginMessage = "login" + splitSign + name + " " + password
				+ splitSign + splitSign;
		sendMessageToServer(loginMessage);
		String strings[] = getMessageFromServer().split(splitSign);
		if (strings[0].equals("loginSuccess")) {
			String userInfor[] = strings[1].split(" ");
			user = new User(userInfor[0], userInfor[1], password, userInfor[2],
					userInfor[3]);
		}
		return strings[0];
	}

	public String register(String name, String password, String mail,
			String telephone) throws IOException {
		String registerString = "register" + splitSign + name + " " + password
				+ " " + telephone + " " + mail + splitSign + splitSign;
		sendMessageToServer(registerString);
		String strings[] = getMessageFromServer().split(splitSign);

		return strings[0];
	}

	public String serverDateToClientDate(String date) {
		String strings[] = date.split(" ");
		String fontStrings[] = strings[0].split("-");
		String backStrings[] = strings[1].split(":");
		String returnString = fontStrings[0] + fontStrings[1] + fontStrings[2]
				+ backStrings[0] + backStrings[1]
				+ backStrings[2].substring(0, 2);
		return returnString;
	}

	public boolean earlierSystem(String stime) {
		int year = Integer.parseInt(stime.substring(0, 4));
		int month = Integer.parseInt(stime.substring(4, 6));
		int day = Integer.parseInt(stime.substring(6, 8));
		int hour = Integer.parseInt(stime.substring(8, 10));
		int minute = Integer.parseInt(stime.substring(10, 12));

		Calendar tempCalendar = Calendar.getInstance();
		if (year < tempCalendar.get(Calendar.YEAR))
			return true;
		else if (year > tempCalendar.get(Calendar.YEAR))
			return false;
		else {
			if (month < tempCalendar.get(Calendar.MONTH))
				return true;
			else if (month > tempCalendar.get(Calendar.MONTH))
				return false;
			else {
				if (day < tempCalendar.get(Calendar.DAY_OF_MONTH))
					return true;
				else if (day > tempCalendar.get(Calendar.DAY_OF_MONTH))
					return false;
				else {
					if (hour < tempCalendar.get(Calendar.HOUR_OF_DAY))
						return true;
					else if (hour > tempCalendar.get(Calendar.HOUR_OF_DAY))
						return false;
					else {
						if (minute < tempCalendar.get(Calendar.MINUTE))
							return true;
						else if (minute > tempCalendar.get(Calendar.MINUTE))
							return false;
						else
							return false;
					}
				}
			}
		}
	}

	public String listMeetings() throws UnsupportedEncodingException,
			IOException {
		String listMeetingsString = "listMeetings" + splitSign + user.getId()
				+ splitSign + splitSign;
		sendMessageToServer(listMeetingsString);
		String strings[] = getMessageFromServer().split(splitSign);

		if (strings[0].equals("listMeetingSuccess")) {
			for (int i = 1; i < strings.length - 2; i++) {
				String meetingInfor[] = strings[i].split(" ");
				String stime = serverDateToClientDate(meetingInfor[3] + " "
						+ meetingInfor[4]);
				String etime = serverDateToClientDate(meetingInfor[5] + " "
						+ meetingInfor[6]);
				Meeting tempMeeting = new Meeting(meetingInfor[0],
						meetingInfor[1], meetingInfor[2], stime, etime,
						meetingInfor[7], meetingInfor[8]);
				if (earlierSystem(stime))
					user.addHistoryMeeting(tempMeeting);
				else
					user.addNowMeeting(tempMeeting);
			}
			Collections.sort(user.getNowMeeting(), new Comparator<Meeting>() {
				public int compare(Meeting m1, Meeting m2) {
					return m1.getStime().compareTo(m2.getStime());
				}
			});
			Collections.sort(user.getHistoryMeeting(),
					new Comparator<Meeting>() {
						public int compare(Meeting m1, Meeting m2) {
							return m1.getStime().compareTo(m2.getStime());
						}
					});
		}
		return strings[0];
	}

	public String getNowMeetingInfo(String id)
			throws UnsupportedEncodingException, IOException {
		String getMeetingInfoString = "getMeetingInfo" + splitSign + id
				+ splitSign + splitSign;
		sendMessageToServer(getMeetingInfoString);
		String strings[] = getMessageFromServer().split(splitSign);
		if (strings[0].equals("getMeetingInfoSuccess")) {
			for (int i = 0; i < user.getNowMeeting().size(); i++) {
				if (user.getNowMeeting().get(i).getId().equals(id))
					user.getNowMeeting().get(i).setContent(strings[1]);
			}
		}
		return strings[0];
	}

	public String addMeeting(String uid, String participatorString,
			String stime, String etime, String title, String position,
			String content) throws UnsupportedEncodingException, IOException {
		String addMeetingsString = "addMeeting" + splitSign + uid + " "
				+ participatorString + " " + stime + " " + etime + " " + title
				+ " " + position + " " + content + splitSign + splitSign;
		sendMessageToServer(addMeetingsString);
		String strings[] = getMessageFromServer().split(splitSign);
		if (strings[0].equals("addMeetingSuccess")) {
			Meeting tempMeeting = new Meeting(strings[1], user.getName(),
					participatorString, stime, etime, title, position, content);
			if (earlierSystem(stime)) {
				user.addHistoryMeeting(tempMeeting);
				Collections.sort(user.getHistoryMeeting(),
						new Comparator<Meeting>() {
							public int compare(Meeting m1, Meeting m2) {
								return m1.getStime().compareTo(m2.getStime());
							}
						});
			} else {
				user.addNowMeeting(tempMeeting);
				Collections.sort(user.getNowMeeting(),
						new Comparator<Meeting>() {
							public int compare(Meeting m1, Meeting m2) {
								return m1.getStime().compareTo(m2.getStime());
							}
						});
			}
		}
		return strings[0];
	}

	public String updateMeeting(String mid, String participatorString,
			String stime, String etime, String title, String position,
			String content) throws UnsupportedEncodingException, IOException {
		String updateMeetingString = "updateMeeting" + splitSign + mid + " "
				+ userFrameControl.getClient().getUser().getId() + " "
				+ participatorString + " " + stime + " " + etime + " " + title
				+ " " + position + " " + content + splitSign + splitSign;
		sendMessageToServer(updateMeetingString);
		String strings[] = getMessageFromServer().split(splitSign);
		if (strings[0].equals("updateMeetingSuccess")) {
			for (int i = 0; i < user.getNowMeeting().size(); i++) {
				if (user.getNowMeeting().get(i).getId().equals(mid)) {
					user.getNowMeeting().get(i)
							.setParticipator(participatorString);
					user.getNowMeeting().get(i).setStime(stime);
					user.getNowMeeting().get(i).setEtime(etime);
					user.getNowMeeting().get(i).setTitle(title);
					user.getNowMeeting().get(i).setPosition(position);
					user.getNowMeeting().get(i).setContent(content);
				}
			}
		}
		Collections.sort(user.getNowMeeting(), new Comparator<Meeting>() {
			public int compare(Meeting m1, Meeting m2) {
				return m1.getStime().compareTo(m2.getStime());
			}
		});
		return strings[0];
	}

	public String deleteNowMeeting(String deleteMeetingId)
			throws UnsupportedEncodingException, IOException {
		String deleteMeetingsString = "deleteMeeting" + splitSign
				+ deleteMeetingId + splitSign + splitSign;
		sendMessageToServer(deleteMeetingsString);
		String strings[] = getMessageFromServer().split(splitSign);
		if (strings[0].equals("deleteMeetingSuccess")) {
			for (int i = 0; i < user.getNowMeeting().size(); i++) {
				if (user.getNowMeeting().get(i).getId().equals(deleteMeetingId)) {
					user.getNowMeeting().remove(i);
					break;
				}
			}
		}
		Collections.sort(user.getNowMeeting(), new Comparator<Meeting>() {
			public int compare(Meeting m1, Meeting m2) {
				return m1.getStime().compareTo(m2.getStime());
			}
		});
		return strings[0];
	}

	public String getHistoryMeetingInfo(String id)
			throws UnsupportedEncodingException, IOException {
		String getMeetingInfoString = "getMeetingInfo" + splitSign + id
				+ splitSign + splitSign;
		sendMessageToServer(getMeetingInfoString);
		String strings[] = getMessageFromServer().split(splitSign);
		if (strings[0].equals("getMeetingInfoSuccess")) {
			for (int i = 0; i < user.getHistoryMeeting().size(); i++) {
				if (user.getHistoryMeeting().get(i).getId().equals(id))
					user.getHistoryMeeting().get(i).setContent(strings[1]);
			}
		}
		return strings[0];
	}

	public String deleteHistoryMeeting(String deleteMeetingId)
			throws UnsupportedEncodingException, IOException {
		String deleteMeetingsString = "deleteMeeting" + splitSign
				+ deleteMeetingId + splitSign + splitSign;
		sendMessageToServer(deleteMeetingsString);
		String strings[] = getMessageFromServer().split(splitSign);
		if (strings[0].equals("deleteMeetingSuccess")) {
			for (int i = 0; i < user.getHistoryMeeting().size(); i++) {
				if (user.getHistoryMeeting().get(i).getId()
						.equals(deleteMeetingId)) {
					user.getHistoryMeeting().remove(i);
					break;
				}
			}
		}
		return strings[0];
	}

	public String listFriends() throws UnsupportedEncodingException,
			IOException {
		String listFriendString = "listFriend" + splitSign + user.getId()
				+ splitSign + splitSign;
		sendMessageToServer(listFriendString);
		String strings[] = getMessageFromServer().split(splitSign);
		if (strings[0].equals("listFriendSuccess")) {
			for (int i = 1; i < strings.length - 2; i++) {
				String info[] = strings[i].split(" ");
				User friend = new User(info[0], info[1], info[2], info[3]);
				user.getFriend().add(friend);
			}
		}
		return strings[0];
	}

	public String addFriends(String newFriendName)
			throws UnsupportedEncodingException, IOException {
		String addFriendString = "addFriend" + splitSign
				+ userFrameControl.getClient().getUser().getId() + " "
				+ newFriendName + splitSign + splitSign;
		sendMessageToServer(addFriendString);
		String strings[] = getMessageFromServer().split(splitSign);
		if (strings[0].equals("addFriendSuccess")) {
			String info[] = strings[1].split(" ");
			User friend = new User(info[0], info[1], info[2], info[3]);
			user.getFriend().add(friend);
		}
		return strings[0];
	}

	public String deleteFriend(String deleteFriendId)
			throws UnsupportedEncodingException, IOException {
		String deleteFriend = "deleteFriends" + splitSign
				+ userFrameControl.getClient().getUser().getId() + " "
				+ deleteFriendId + splitSign + splitSign;
		sendMessageToServer(deleteFriend);
		String strings[] = getMessageFromServer().split(splitSign);
		if (strings[0].equals("deleteFriendSuccess")) {
			for (int i = 0; i < user.getFriend().size(); i++) {
				if (user.getFriend().get(i).getId().equals(deleteFriendId)) {
					user.getFriend().remove(i);
					break;
				}
			}
		}
		return strings[0];
	}

	public String updateUser(String password)
			throws UnsupportedEncodingException, IOException {
		String updateUserString = "updateUser" + splitSign + user.getId() + " "
				+ user.getName() + " " + password + " " + user.getMail() + " "
				+ user.getTelePhone() + splitSign + splitSign;
		sendMessageToServer(updateUserString);
		String strings[] = getMessageFromServer().split(splitSign);
		if (strings[0].equals("updateUserSuccess")) {
			user.setPassword(password);
		}
		return strings[0];
	}

	public String HasCollision(LinkedList<Meeting> meetings) {
		String returnstr = "";
		int len = meetings.size();
		int flag = 0;
		for (int i = 0; i < len; i++) {
			for (int j = i + 1; j < len; j++) {
				if (meetings.get(i).getEtime()
						.compareTo(meetings.get(j).getStime()) <= 0)
					break;
				else {// 结束时间大于开始时间
					if (j + 1 < len) {
						if (meetings.get(i).getEtime()
								.compareTo(meetings.get(j + 1).getStime()) <= 0) {
							int temp = i;
							flag = 1;
							while (temp <= j) {
								returnstr += temp;
								returnstr += "号会议,";
								temp++;
							}
							returnstr += "时间安排有冲突。" + "\n\r";
						}
					} else {
						int temp = i;
						flag = 1;
						while (temp <= j) {
							returnstr += temp;
							returnstr += "号会议,";
							temp++;
						}
						returnstr += "时间安排有冲突。" + "\n\r";
					}
				}
			}
		}
		if (flag == 1)
			returnstr += "请合理安排时间。";
		return returnstr;
	}
}