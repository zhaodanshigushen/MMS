package clientUI;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import clientUI.userMeetingManager.MeetingAction;

public class meetingUpdateDialog extends JDialog {
	private JLabel sponsorJLabel, participatorJLabel, positionJLabel,
			stimeJLabel, etimeJLabel, titleJLabel, contentJLabel;
	private JTextField sponsorJTextField, positionJTextField, titleJTextField,
			contentJTextField;
	private JButton updateJButton, returnJButton;
	private boxDateSpinner stimeSpinner, etimeSpinner;
	private MulitComboBox mulitComboBox;
	private UpdateMeetingAction updateMeetingAction;
	private String midString;

	public class UpdateMeetingAction implements ActionListener {
		public UpdateMeetingAction() {
			super();
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == updateJButton) {
				if (mulitComboBox.getEditorText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "参与者不能为空", "失败信息",
							JOptionPane.WARNING_MESSAGE);
				} else if (positionJTextField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "会议地点不能为空", "失败信息",
							JOptionPane.WARNING_MESSAGE);
				} else if (titleJTextField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "会议主题不能为空", "失败信息",
							JOptionPane.WARNING_MESSAGE);
				} else if (contentJTextField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "会议内容不能为空", "失败信息",
							JOptionPane.WARNING_MESSAGE);
				} else {
					String s[] = mulitComboBox.getEditorText().split(" ");
					StringBuffer stringBuffer = new StringBuffer();
					String participatorString = new String();
					for (int i = 0; i < s.length; i++) {
						for (int j = 0; j < userFrameControl.getClient()
								.getUser().getFriend().size(); j++) {
							if (s[i].equals(userFrameControl.getClient()
									.getUser().getFriend().get(j).getName())) {
								stringBuffer.append(userFrameControl
										.getClient().getUser().getFriend()
										.get(j).getId());
								if (i != s.length - 1)
									stringBuffer.append("/");
							}
						}
					}
					participatorString = stringBuffer.toString();
					String monthString = new String();
					String dayString = new String();
					String hourString = new String();
					String minuteString = new String();

					if (stimeSpinner.getMonth() <= 9) {
						monthString = "0"
								+ String.valueOf(stimeSpinner.getMonth());
					} else
						monthString = String.valueOf(stimeSpinner.getMonth());

					if (stimeSpinner.getDay() <= 9) {
						dayString = "0"
								+ String.valueOf(stimeSpinner.getMonth());
					} else
						dayString = String.valueOf(stimeSpinner.getDay());

					if (stimeSpinner.getHour() <= 9) {
						hourString = "0"
								+ String.valueOf(stimeSpinner.getHour());
					} else
						hourString = String.valueOf(stimeSpinner.getHour());

					if (stimeSpinner.getMinute() <= 9) {
						minuteString = "0"
								+ String.valueOf(stimeSpinner.getMinute());
					} else
						minuteString = String.valueOf(stimeSpinner.getMinute());

					String stime = String.valueOf(stimeSpinner.getYear())
							+ monthString + dayString + hourString
							+ minuteString + "00";

					if (etimeSpinner.getMonth() <= 9) {
						monthString = "0"
								+ String.valueOf(etimeSpinner.getMonth());
					} else
						monthString = String.valueOf(etimeSpinner.getMonth());

					if (etimeSpinner.getDay() <= 9) {
						dayString = "0"
								+ String.valueOf(etimeSpinner.getMonth());
					} else
						dayString = String.valueOf(etimeSpinner.getDay());

					if (etimeSpinner.getHour() <= 9) {
						hourString = "0"
								+ String.valueOf(etimeSpinner.getHour());
					} else
						hourString = String.valueOf(etimeSpinner.getHour());

					if (etimeSpinner.getMinute() <= 9) {
						minuteString = "0"
								+ String.valueOf(etimeSpinner.getMinute());
					} else
						minuteString = String.valueOf(etimeSpinner.getMinute());

					String etime = String.valueOf(etimeSpinner.getYear())
							+ monthString + dayString + hourString
							+ minuteString + "00";

					if (stime.compareTo(etime) > 0)
						JOptionPane.showMessageDialog(null, "开始时间必须小于结束时间",
								"失败信息", JOptionPane.WARNING_MESSAGE);
					else {
						String title = titleJTextField.getText();
						String position = positionJTextField.getText();
						String content = contentJTextField.getText();
						try {
							String string = userFrameControl.getClient()
									.updateMeeting(midString,
											participatorString, stime, etime,
											title, position, content);
							if (string.equals("updateMeetingSuccess")) {
								JOptionPane.showMessageDialog(null, "修改成功",
										"成功信息", JOptionPane.WARNING_MESSAGE);
								setVisible(false);
							} else
								JOptionPane.showMessageDialog(null, "修改失败",
										"错误信息", JOptionPane.WARNING_MESSAGE);
						} catch (UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				// setVisible(false);
			} else if (e.getSource() == returnJButton) {
				setVisible(false);
			}
		}
	}

	public meetingUpdateDialog(JFrame frame, String title, boolean modal) {
		super(frame, title, modal);
	}

	public void run(String id, String sponsor, String participator,
			String position, String stime, String etime, String title,
			String content) {
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width / 2; // 获取屏幕的宽
		int screenHeight = screenSize.height / 2; // 获取屏幕的高
		setLocation(screenWidth / 2, screenHeight / 2);
		setSize(new Dimension(400, 400));
		setResizable(false);

		midString = new String();
		midString = id;
		sponsorJLabel = new JLabel("发起人:");
		participatorJLabel = new JLabel("参加者:");
		positionJLabel = new JLabel("会议地点:");
		stimeJLabel = new JLabel("开始时间:");
		etimeJLabel = new JLabel("结束时间:");
		titleJLabel = new JLabel("会议主题:");
		contentJLabel = new JLabel("会议内容:");

		updateJButton = new JButton("修改");
		returnJButton = new JButton("返回");
		updateMeetingAction = new UpdateMeetingAction();
		updateJButton.addActionListener(updateMeetingAction);
		returnJButton.addActionListener(updateMeetingAction);

		mulitComboBox = new MulitComboBox();
		mulitComboBox.setEditorText(participator);
		mulitComboBox.getPopup().textToSelectedVlaues();

		sponsorJTextField = new JTextField(sponsor);
		sponsorJTextField.setEditable(false);
		positionJTextField = new JTextField(position);
		titleJTextField = new JTextField(title);
		contentJTextField = new JTextField(content);

		sponsorJTextField.setPreferredSize(new Dimension(250, 25));
		sponsorJTextField.setMaximumSize(new Dimension(250, 25));
		positionJTextField.setPreferredSize(new Dimension(250, 25));
		titleJTextField.setPreferredSize(new Dimension(250, 25));
		contentJTextField.setPreferredSize(new Dimension(250, 25));
		mulitComboBox.setPreferredSize(new Dimension(219, 25));

		Box sponsorBox, participatorBox, positionBox, stimeBox, etimeBox, titleBox, contentBox, buttonBox;
		sponsorBox = Box.createHorizontalBox();
		participatorBox = Box.createHorizontalBox();
		positionBox = Box.createHorizontalBox();
		stimeBox = Box.createHorizontalBox();
		etimeBox = Box.createHorizontalBox();
		titleBox = Box.createHorizontalBox();
		contentBox = Box.createHorizontalBox();
		buttonBox = Box.createHorizontalBox();

		sponsorBox.add(sponsorJLabel);
		sponsorBox.add(Box.createHorizontalStrut(17));
		sponsorBox.add(sponsorJTextField);

		participatorBox.add(participatorJLabel);
		participatorBox.add(Box.createHorizontalStrut(17));
		participatorBox.add(mulitComboBox);

		positionBox.add(positionJLabel);
		positionBox.add(Box.createHorizontalStrut(5));
		positionBox.add(positionJTextField);

		stimeSpinner = new boxDateSpinner();
		String yearString = stime.substring(0, 4);
		String monthString = stime.substring(4, 6);
		String dayString = stime.substring(6, 8);
		String hourString = stime.substring(8, 10);
		String minuteString = stime.substring(10, 12);
		String secondString = stime.substring(12, 14);
		stimeSpinner.setValue(yearString, monthString, dayString, hourString,
				minuteString, secondString);

		etimeSpinner = new boxDateSpinner();
		yearString = etime.substring(0, 4);
		monthString = etime.substring(4, 6);
		dayString = etime.substring(6, 8);
		hourString = etime.substring(8, 10);
		minuteString = etime.substring(10, 12);
		secondString = etime.substring(12, 14);
		etimeSpinner.setValue(yearString, monthString, dayString, hourString,
				minuteString, secondString);

		stimeBox.add(stimeJLabel);
		stimeBox.add(Box.createHorizontalStrut(5));
		stimeSpinner.getJSpinner().setPreferredSize(new Dimension(250, 22));
		stimeBox.add(stimeSpinner.getJSpinner());

		etimeBox.add(etimeJLabel);
		etimeBox.add(Box.createHorizontalStrut(5));
		etimeSpinner.getJSpinner().setPreferredSize(new Dimension(250, 22));
		etimeBox.add(etimeSpinner.getJSpinner());

		titleBox.add(titleJLabel);
		titleBox.add(Box.createHorizontalStrut(5));
		titleBox.add(titleJTextField);

		contentBox.add(contentJLabel);
		contentBox.add(Box.createHorizontalStrut(5));
		contentBox.add(contentJTextField);

		buttonBox.add(updateJButton);
		buttonBox.add(Box.createHorizontalStrut(15));
		buttonBox.add(returnJButton);

		getContentPane().setLayout(new GridBagLayout());

		GridBagConstraints tempGrid = new GridBagConstraints();
		tempGrid.fill = GridBagConstraints.VERTICAL;
		tempGrid.gridx = 0;
		tempGrid.gridy = 0;
		tempGrid.insets = new Insets(15, 0, 0, 0);
		getContentPane().add(sponsorBox, tempGrid);

		tempGrid.gridy = 1;
		tempGrid.insets = new Insets(15, 0, 0, 0);
		getContentPane().add(participatorBox, tempGrid);

		tempGrid.gridy = 2;
		getContentPane().add(positionBox, tempGrid);

		tempGrid.gridy = 3;
		getContentPane().add(stimeBox, tempGrid);

		tempGrid.gridy = 4;
		getContentPane().add(etimeBox, tempGrid);

		tempGrid.gridy = 5;
		getContentPane().add(titleBox, tempGrid);

		tempGrid.gridy = 6;
		getContentPane().add(contentBox, tempGrid);

		tempGrid.gridy = 7;
		getContentPane().add(buttonBox, tempGrid);
		setVisible(true);
	}
}