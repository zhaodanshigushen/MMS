package clientUI;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.text.ParseException;
import java.util.LinkedList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class meetingCheckDialog extends JDialog {
	private JLabel sponsorJLabel, participatorJLabel, positionJLabel,
			stimeJLabel, etimeJLabel, titleJLabel, contentJLabel;
	private JTextField sponsorJTextField, participatorJTextField,
			positionJTextField, titleJTextField, contentJTextField;
	private boxDateSpinner stimeSpinner, etimeSpinner;

	public meetingCheckDialog(JFrame frame, String title, boolean modal) {
		super(frame, title, modal);
	}

	public void run(String sponsor, String participator, String position,
			String stime, String etime, String title, String content) {
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width / 2; // 获取屏幕的宽
		int screenHeight = screenSize.height / 2; // 获取屏幕的高
		setLocation(screenWidth / 2, screenHeight / 2);

		setSize(new Dimension(350, 400));
		setResizable(false);

		sponsorJLabel = new JLabel("发起人:");
		participatorJLabel = new JLabel("参加者:");
		positionJLabel = new JLabel("会议地点:");
		stimeJLabel = new JLabel("开始时间:");
		etimeJLabel = new JLabel("结束时间:");
		titleJLabel = new JLabel("会议主题:");
		contentJLabel = new JLabel("会议内容:");

		stimeSpinner = new boxDateSpinner();
		stimeSpinner.setAllEditAble(false);
		etimeSpinner = new boxDateSpinner();
		etimeSpinner.setAllEditAble(false);
		String yearString = stime.substring(0, 4);
		String monthString = stime.substring(4, 6);
		String dayString = stime.substring(6, 8);
		String hourString = stime.substring(8, 10);
		String minuteString = stime.substring(10, 12);
		String secondString = stime.substring(12, 14);

		stimeSpinner.setValue(yearString, monthString, dayString, hourString,
				minuteString, secondString);
		yearString = etime.substring(0, 4);
		monthString = etime.substring(4, 6);
		dayString = etime.substring(6, 8);
		hourString = etime.substring(8, 10);
		minuteString = etime.substring(10, 12);
		secondString = etime.substring(12, 14);
		etimeSpinner.setValue(yearString, monthString, dayString, hourString,
				minuteString, secondString);

		sponsorJTextField = new JTextField(sponsor);
		participatorJTextField = new JTextField(participator);
		positionJTextField = new JTextField(position);
		titleJTextField = new JTextField(title);
		contentJTextField = new JTextField(content);

		sponsorJTextField.setEditable(false);
		participatorJTextField.setEditable(false);
		positionJTextField.setEditable(false);
		titleJTextField.setEditable(false);
		contentJTextField.setEditable(false);

		sponsorJTextField.setPreferredSize(new Dimension(250, 25));
		participatorJTextField.setPreferredSize(new Dimension(250, 25));
		positionJTextField.setPreferredSize(new Dimension(250, 25));
		titleJTextField.setPreferredSize(new Dimension(250, 25));
		contentJTextField.setPreferredSize(new Dimension(250, 25));

		Box sponsorBox, participatorBox, positionBox, stimeBox, etimeBox, titleBox, contentBox;
		sponsorBox = Box.createHorizontalBox();
		participatorBox = Box.createHorizontalBox();
		positionBox = Box.createHorizontalBox();
		stimeBox = Box.createHorizontalBox();
		etimeBox = Box.createHorizontalBox();
		titleBox = Box.createHorizontalBox();
		contentBox = Box.createHorizontalBox();

		sponsorBox.add(sponsorJLabel);
		sponsorBox.add(Box.createHorizontalStrut(18));
		sponsorBox.add(sponsorJTextField);

		participatorBox.add(participatorJLabel);
		participatorBox.add(Box.createHorizontalStrut(18));
		participatorBox.add(participatorJTextField);

		positionBox.add(positionJLabel);
		positionBox.add(Box.createHorizontalStrut(5));
		positionBox.add(positionJTextField);

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

		getContentPane().setLayout(new GridBagLayout());

		GridBagConstraints tempGrid = new GridBagConstraints();
		tempGrid.fill = GridBagConstraints.VERTICAL;
		tempGrid.gridx = 0;
		tempGrid.gridy = 0;
		tempGrid.insets = new Insets(15, 0, 0, 0);
		getContentPane().add(sponsorBox, tempGrid);

		tempGrid.gridy = 1;
		tempGrid.insets = new Insets(20, 0, 0, 0);
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

		setVisible(true);
	}
}