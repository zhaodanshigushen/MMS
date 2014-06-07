package clientCode;

import java.util.LinkedList;

import clientUI.userFrameControl;

public class Meeting {
	private String sponsor;
	private LinkedList<String> participator;
	private String position;
	private String stime;
	private String etime;
	private String title;
	private String content;
	private String id;

	public Meeting() {
		stime = "";
		etime = "";
		title = "";
		content = "";
		sponsor = "";
		position = "";
		id = "-1";
		participator = new LinkedList<String>();
	}

	public Meeting(String newid, String newSP, String newPa, String newStime,
			String newEtime, String newTitle, String newPo) {
		id = newid;
		String string[] = newSP.split("-");
		sponsor = string[1];
		String paString[] = newPa.split("/");
		participator = new LinkedList<String>();
		int i;
		for (i = 0; i < paString.length; i++) {
			String s[] = paString[i].split("-");
			participator.add(s[1]);
		}
		position = newPo;
		stime = newStime;
		etime = newEtime;
		title = newTitle;
		content = "";
	}

	public Meeting(String newid, String newSP, String newPa, String newStime,
			String newEtime, String newTitle, String newPo, String newContent) {
		id = newid;
		sponsor = newSP;
		participator = new LinkedList<String>();
		int i;
		String paString[] = newPa.split("/");
		for (i = 0; i < paString.length; i++) {
			for (int j = 0; j < userFrameControl.getClient().getUser()
					.getFriend().size(); j++) {
				if (paString[i].equals(userFrameControl.getClient().getUser()
						.getFriend().get(j).getId())) {
					this.participator.add(userFrameControl.getClient()
							.getUser().getFriend().get(j).getName());
					break;
				}
			}
		}
		position = newPo;
		stime = newStime;
		etime = newEtime;
		title = newTitle;
		content = newContent;
	}

	public String getStime() {
		return stime;
	}

	public void setStime(String stime) {
		this.stime = stime;
	}

	public String getEtime() {
		return etime;
	}

	public void setEtime(String etime) {
		this.etime = etime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSponsor() {
		return sponsor;
	}

	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public LinkedList<String> getParticipator() {
		return participator;
	}

	public void setParticipator(String participator) {
		String paString[] = participator.split("/");
		this.participator.clear();
		for (int i = 0; i < paString.length; i++) {
			for (int j = 0; j < userFrameControl.getClient().getUser()
					.getFriend().size(); j++) {
				if (paString[i].equals(userFrameControl.getClient().getUser()
						.getFriend().get(j).getId())) {
					this.participator.add(userFrameControl.getClient()
							.getUser().getFriend().get(j).getName());
					break;
				}
			}
		}
	}

	public String getStringParticipator() {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < participator.size(); i++) {
			stringBuffer.append(participator.get(i));
			stringBuffer.append(" ");
		}
		return stringBuffer.toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}