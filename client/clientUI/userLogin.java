package clientUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

import clientCode.Client;

public class userLogin extends javax.swing.JFrame implements ActionListener {
	private JTextField nameField, passwordField;
	private JButton loginJButton, registerJButton;
	// private JLabel label;
	private JLabel nameLabel, passwordLabel;
	private JFrame frame;

	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() == loginJButton) {
				try {
					if (!userFrameControl.getClient().getConnective())
						userFrameControl.getClient().run();
					String name = nameField.getText();
					String password = passwordField.getText();
					if (name.isEmpty()) {
						JOptionPane.showMessageDialog(null, "用户名不能为空！", "登录错误",
								JOptionPane.WARNING_MESSAGE);
					} else if (password.isEmpty()) {
						JOptionPane.showMessageDialog(null, "密码不能为空！", "登录错误",
								JOptionPane.WARNING_MESSAGE);
					} else {
						String loginString = new String();
						try {
							loginString = userFrameControl.getClient().login(
									name, password);
							if (loginString.equals("loginSuccess")) {
								String listMeetingsString = userFrameControl
										.getClient().listMeetings();
								String listFriendsString = userFrameControl
										.getClient().listFriends();
								if (listMeetingsString
										.equals("listMeetingSuccess")
										&& listFriendsString
												.equals("listFriendSuccess")) {
									userFrameControl.getFrameControlUserLogin()
											.getFrame().setVisible(false);
									userFrameControl
											.getFrameControlUserMeetingManager()
											.run();
								} else if (listMeetingsString
										.equals("getMeetingInfoFail"))
									JOptionPane.showMessageDialog(null,
											"获取会议信息失败", "登录错误",
											JOptionPane.WARNING_MESSAGE);
								else if (listFriendsString
										.equals("listFriendFail"))
									JOptionPane.showMessageDialog(null,
											"获取好友失败", "登录错误",
											JOptionPane.WARNING_MESSAGE);
							} else if (loginString.equals("loginFail")) {
								JOptionPane.showMessageDialog(null, "用户名或密码错误",
										"登录错误", JOptionPane.WARNING_MESSAGE);
								nameField.setText("");
								passwordField.setText("");
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				} catch (UnknownHostException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}

			else if (e.getSource() == registerJButton) {
				userFrameControl.getFrameControlUserLogin().getFrame()
						.setVisible(false);
				userFrameControl.getFrameControlUserRegister().run();
			}
		} catch (HeadlessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public JFrame getFrame() {
		return frame;
	}

	public userLogin() {
		super();
	}

	public void run() {
		frame = new JFrame("MeetingManage System");
		frame.setSize(new Dimension(300, 200));

		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width / 2; // 获取屏幕的宽
		int screenHeight = screenSize.height / 2; // 获取屏幕的高

		frame.setLocation(screenWidth / 2, screenHeight / 2);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setResizable(false);

		frame.getContentPane().setLayout(new GridBagLayout());

		Box nameBox, passwordBox, buttonBox;
		nameBox = Box.createHorizontalBox();
		passwordBox = Box.createHorizontalBox();
		buttonBox = Box.createHorizontalBox();

		nameField = new JTextField();
		nameField.setPreferredSize(new Dimension(100, 25));
		passwordField = new JTextField();
		passwordField.setPreferredSize(new Dimension(100, 25));

		nameLabel = new JLabel("用户名");
		passwordLabel = new JLabel("密码      ");

		nameBox.add(nameLabel);
		nameBox.add(Box.createHorizontalStrut(10));
		nameBox.add(nameField);

		passwordBox.add(passwordLabel);
		passwordBox.add(Box.createHorizontalStrut(5));
		passwordBox.add(passwordField);

		loginJButton = new JButton("登录");
		loginJButton.addActionListener(this);

		registerJButton = new JButton("注册");
		registerJButton.addActionListener(this);

		buttonBox.add(loginJButton);
		buttonBox.add(Box.createHorizontalStrut(15));
		buttonBox.add(registerJButton);

		GridBagConstraints tempGrid = new GridBagConstraints();
		tempGrid.gridx = 0;
		tempGrid.gridy = 0;
		tempGrid.insets = new Insets(0, 0, 20, 0);
		frame.getContentPane().add(nameBox, tempGrid);

		tempGrid.gridx = 0;
		tempGrid.gridy = 1;

		frame.getContentPane().add(passwordBox, tempGrid);

		tempGrid.gridx = 0;
		tempGrid.gridy = 3;
		tempGrid.insets = new Insets(10, 0, 0, 0);
		frame.getContentPane().add(buttonBox, tempGrid);

		frame.setVisible(true);
		// frame.pack();
	}

}