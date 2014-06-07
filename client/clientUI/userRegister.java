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
				JOptionPane.showMessageDialog(null, "�û�������Ϊ�գ�", "ע�����",
						JOptionPane.WARNING_MESSAGE);
			} else if (name.length() > 10) {
				JOptionPane.showMessageDialog(null, "�û������ܳ���10λ��", "ע�����",
						JOptionPane.WARNING_MESSAGE);
			} else if (password.isEmpty()) {
				JOptionPane.showMessageDialog(null, "���벻��Ϊ�գ�", "ע�����",
						JOptionPane.WARNING_MESSAGE);
			} else if (password.length() > 16) {
				JOptionPane.showMessageDialog(null, "���벻�ܳ���16λ��", "ע�����",
						JOptionPane.WARNING_MESSAGE);
			} else if (telephone.isEmpty()) {
				JOptionPane.showMessageDialog(null, "�绰����Ϊ�գ�", "ע�����",
						JOptionPane.WARNING_MESSAGE);
			} else if (telephone.length() != 11) {
				JOptionPane.showMessageDialog(null, "�绰�������11λ��", "ע�����",
						JOptionPane.WARNING_MESSAGE);
			} else if (!isNumeric(telephone)) {
				JOptionPane.showMessageDialog(null, "�绰����Ϊȫ����", "ע�����",
						JOptionPane.WARNING_MESSAGE);
			} else if (mail.isEmpty()) {
				JOptionPane.showMessageDialog(null, "���䲻��Ϊ�գ�", "ע�����",
						JOptionPane.WARNING_MESSAGE);
			} else if (!isMail(mail)) {
				JOptionPane.showMessageDialog(null, "�����ʽ����ȷ", "ע�����",
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
						JOptionPane.showMessageDialog(null, "�û����������ѱ�ռ��",
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
		frame = new JFrame("ע�����û�");
		frame.setSize(new Dimension(400, 350));

		Toolkit kit = Toolkit.getDefaultToolkit(); // ���幤�߰�
		Dimension screenSize = kit.getScreenSize(); // ��ȡ��Ļ�ĳߴ�
		int screenWidth = screenSize.width / 2; // ��ȡ��Ļ�Ŀ�
		int screenHeight = screenSize.height / 2; // ��ȡ��Ļ�ĸ�

		frame.setLocation(screenWidth / 2, screenHeight / 2);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setResizable(false);

		frame.getContentPane().setLayout(new GridBagLayout());

		ensureButton = new JButton("ע��");
		resetButton = new JButton("����");
		returnButton = new JButton("����");
		ensureButton.addActionListener(this);
		resetButton.addActionListener(this);
		returnButton.addActionListener(this);

		nameLabel = new JLabel("����:");
		passwordLabel = new JLabel("����:");
		telephonelLabel = new JLabel("�绰:");
		mailLabel = new JLabel("����:");

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