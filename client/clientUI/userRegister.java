package clientUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.naming.InitialContext;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class userRegister extends javax.swing.JFrame implements ActionListener {
	private JFrame frame;
	private JButton ensureButton, resetButton, returnButton;
	private JLabel nameLabel, passwordLabel, telephonelLabel, mailLabel;
	private JTextField nameField, passwordField, telephoneField, mailField;

	public void setTextFieldEmpty() {
		nameField.setText("");
		passwordField.setText("");
		telephoneField.setText("");
		mailField.setText("");
	}

	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static boolean isMail(String str) {
		if (str.length() < 7)
			return false;
		if (!str.endsWith(".com"))
			return false;
		if (!str.contains("@"))
			return false;
		return true;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ensureButton) {
			String name, password, telephone, mail;
			name = nameField.getText();
			password = passwordField.getText();
			telephone = telephoneField.getText();
			mail = mailField.getText();
			setTextFieldEmpty();
			String registerString = new String();
			if (name.isEmpty()) {
				JOptionPane.showMessageDialog(null, "用户名不能为空！", "注册错误",
						JOptionPane.WARNING_MESSAGE);
			} else if (name.length() > 10) {
				JOptionPane.showMessageDialog(null, "用户名不能超过10位！", "注册错误",
						JOptionPane.WARNING_MESSAGE);
			} else if (password.isEmpty()) {
				JOptionPane.showMessageDialog(null, "密码不能为空！", "注册错误",
						JOptionPane.WARNING_MESSAGE);
			} else if (password.length() > 16) {
				JOptionPane.showMessageDialog(null, "密码不能超过16位！", "注册错误",
						JOptionPane.WARNING_MESSAGE);
			} else if (telephone.isEmpty()) {
				JOptionPane.showMessageDialog(null, "电话不能为空！", "注册错误",
						JOptionPane.WARNING_MESSAGE);
			} else if (telephone.length() != 11) {
				JOptionPane.showMessageDialog(null, "电话必须等于11位！", "注册错误",
						JOptionPane.WARNING_MESSAGE);
			} else if (!isNumeric(telephone)) {
				JOptionPane.showMessageDialog(null, "电话必须为全数字", "注册错误",
						JOptionPane.WARNING_MESSAGE);
			} else if (mail.isEmpty()) {
				JOptionPane.showMessageDialog(null, "邮箱不能为空！", "注册错误",
						JOptionPane.WARNING_MESSAGE);
			} else if (!isMail(mail)) {
				JOptionPane.showMessageDialog(null, "邮箱格式不正确", "注册错误",
						JOptionPane.WARNING_MESSAGE);
			} else {
				if (!userFrameControl.getClient().getConnective())
					try {
						userFrameControl.getClient().run();
					} catch (UnknownHostException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				try {
					registerString = userFrameControl.getClient().register(
							name, password, telephone, mail);
					if (registerString.equals("registerSuccess")) {
						userFrameControl.getFrameControlUserRegister()
								.getFrame().setVisible(false);
						userFrameControl.getFrameControlUserLogin().getFrame()
								.setVisible(true);
					} else if (registerString.equals("registerFail")) {
						JOptionPane.showMessageDialog(null, "用户名或邮箱已被占用",
								"Client", JOptionPane.WARNING_MESSAGE);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		else if (e.getSource() == resetButton) {
			nameField.setText("");
			passwordField.setText("");
			telephoneField.setText("");
			mailField.setText("");
		}

		else if (e.getSource() == returnButton) {
			setTextFieldEmpty();
			userFrameControl.getFrameControlUserRegister().getFrame()
					.setVisible(false);
			userFrameControl.getFrameControlUserLogin().getFrame()
					.setVisible(true);
		}

	}

	public JFrame getFrame() {
		return frame;
	}

	public userRegister() {
		super();
	}

	public void run() {
		init();
	}

	public void init() {
		frame = new JFrame("注册新用户");
		frame.setSize(new Dimension(400, 350));

		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width / 2; // 获取屏幕的宽
		int screenHeight = screenSize.height / 2; // 获取屏幕的高

		frame.setLocation(screenWidth / 2, screenHeight / 2);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setResizable(false);

		frame.getContentPane().setLayout(new GridBagLayout());

		ensureButton = new JButton("注册");
		resetButton = new JButton("重置");
		returnButton = new JButton("返回");
		ensureButton.addActionListener(this);
		resetButton.addActionListener(this);
		returnButton.addActionListener(this);

		nameLabel = new JLabel("姓名:");
		passwordLabel = new JLabel("密码:");
		telephonelLabel = new JLabel("电话:");
		mailLabel = new JLabel("邮箱:");

		nameField = new JTextField();
		passwordField = new JTextField();
		telephoneField = new JTextField();
		mailField = new JTextField();
		nameField.setPreferredSize(new Dimension(200, 30));
		passwordField.setPreferredSize(new Dimension(200, 30));
		telephoneField.setPreferredSize(new Dimension(200, 30));
		mailField.setPreferredSize(new Dimension(200, 30));

		Box nameBox, passwordBox, telephonebBox, mailBox, buttonBox;
		nameBox = Box.createHorizontalBox();
		passwordBox = Box.createHorizontalBox();
		telephonebBox = Box.createHorizontalBox();
		mailBox = Box.createHorizontalBox();
		buttonBox = Box.createHorizontalBox();

		nameBox.add(nameLabel);
		nameBox.add(Box.createHorizontalStrut(10));
		nameBox.add(nameField);

		passwordBox.add(passwordLabel);
		passwordBox.add(Box.createHorizontalStrut(10));
		passwordBox.add(passwordField);

		telephonebBox.add(telephonelLabel);
		telephonebBox.add(Box.createHorizontalStrut(10));
		telephonebBox.add(telephoneField);

		mailBox.add(mailLabel);
		mailBox.add(Box.createHorizontalStrut(10));
		mailBox.add(mailField);

		buttonBox.add(ensureButton);
		buttonBox.add(Box.createHorizontalStrut(10));
		buttonBox.add(resetButton);
		buttonBox.add(Box.createHorizontalStrut(10));
		buttonBox.add(returnButton);

		GridBagConstraints tempGrid = new GridBagConstraints();
		tempGrid.gridx = 0;
		tempGrid.gridy = 0;
		tempGrid.insets = new Insets(20, 0, 30, 0);
		frame.getContentPane().add(nameBox, tempGrid);

		tempGrid.gridx = 0;
		tempGrid.gridy = 1;
		tempGrid.insets = new Insets(0, 0, 30, 0);
		frame.getContentPane().add(passwordBox, tempGrid);

		tempGrid.gridx = 0;
		tempGrid.gridy = 2;
		frame.getContentPane().add(telephonebBox, tempGrid);

		tempGrid.gridx = 0;
		tempGrid.gridy = 3;
		frame.getContentPane().add(mailBox, tempGrid);

		tempGrid.gridx = 0;
		tempGrid.gridy = 4;
		frame.getContentPane().add(buttonBox, tempGrid);

		frame.setVisible(true);
	}

	// public static void main(String args[]) {
	// userRegister a = new userRegister();
	// a.run();
	// }
}