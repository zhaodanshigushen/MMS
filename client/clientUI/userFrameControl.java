package clientUI;

import java.io.IOException;
import java.net.UnknownHostException;

import clientCode.Client;
import clientCode.User;

public class userFrameControl {
	private static userRegister frameControlUserRegister;
	private static userLogin frameControlUserLogin;
	private static userMeetingManager frameControluserMeetingManager;
	private static Client myClient;

	public userFrameControl() throws IOException {
		frameControlUserLogin = new userLogin();
		frameControluserMeetingManager = new userMeetingManager();
		myClient = new Client();
	}

	public static Client getClient() {
		return myClient;
	}

	public static userRegister getFrameControlUserRegister() {
		return frameControlUserRegister;
	}

	public static userMeetingManager getFrameControlUserMeetingManager() {
		return frameControluserMeetingManager;
	}

	public static userLogin getFrameControlUserLogin() {
		frameControlUserRegister = new userRegister();
		return frameControlUserLogin;
	}

	public static void main(String argc[]) throws UnknownHostException,
			IOException {
		userFrameControl frameControl = new userFrameControl();
		userFrameControl.getFrameControlUserLogin().run();
	}
}