package clientUI;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.SelectableChannel;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import clientCode.Meeting;

public class userMeetingManager extends javax.swing.JFrame implements
		ActionListener {
	private JButton meetingButton, addMeetingButton, persoalFileButton,
			friendButton, historyMeetingButton;

	private JButton meetingButtonPanelCheck, meetingButtonPanelDelete,
			meetingButtonPanelUpdate;
	private JButton histroyMeetingButtonPanelCheck,
			histroyMeetingButtonPanelDelete,
			histroyMeetingButtonPanelAllDelete;
	private JButton addMeetingButtonPanelEnsure, addMeetingButtonPanelReset;
	private JButton persoalFileButtonPanelEnsure, persoalFileButtonPanelReset;
	private JButton friendButtonPanelAdd, friendButtonPanelReset,
			friendButtonPanelDelete;
	private JTextField passwordTextField, nameField;
	private JTextField positionTextField, titleTextField, contentTextField;

	private JSpinner syearJSpinner, smonthJSpinner, sdayJSpinner,
			shourJSpinner, sminuteJSpinner;
	private JSpinner eyearJSpinner, emonthJSpinner, edayJSpinner,
			ehourJSpinner, eminuteJSpinner;
	private JLabel lable;
	private JFrame frame;
	private JPanel jpTop, jpMiddle, jpDown;
	private JPanel jpButton;
	private JScrollPane meetingJScrollPane;
	private JScrollPane historyMeetingJScrollPane;
	private JScrollPane userJScrollPane;
	private MeetingAction meetingAction;
	private AddMeetingAction addMeetingAction;
	private HistoryMeetingAction historyMeetingAction;
	private PersoalFileAction persoalFileAction;
	private FriendAction friendAction;
	private MulitComboBox mulit;
	private boxDateSpinner stimeSpinner, etimeSpinner;

	private JTable meetingTable, historyTable, friendTable;

	public class MeetingAction implements ActionListener {
		public MeetingAction() {
			super();
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == meetingButtonPanelCheck) {
				if (meetingTable.getSelectedRow() != -1) {
					meetingCheckDialog dialog = new meetingCheckDialog(frame,
							"会议信息", true);
					try {
						String strings = userFrameControl.getClient()
								.getNowMeetingInfo(
										userFrameControl
												.getClient()
												.getUser()
												.getNowMeeting()
												.get(meetingTable
														.getSelectedRow())
												.getId());
						if (strings.equals("getMeetingInfoSuccess")) {
							Meeting meeting = userFrameControl.getClient()
									.getUser().getNowMeeting()
									.get(meetingTable.getSelectedRow());
							dialog.run(meeting.getSponsor(),
									meeting.getStringParticipator(),
									meeting.getPosition(), meeting.getStime(),
									meeting.getEtime(), meeting.getTitle(),
									meeting.getContent());
						}
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			} else if (e.getSource() == meetingButtonPanelDelete) {
				if (meetingTable.getSelectedRow() != -1) {
					try {
						String deleteMeetingString = userFrameControl
								.getClient().deleteNowMeeting(
										userFrameControl
												.getClient()
												.getUser()
												.getNowMeeting()
												.get(meetingTable
														.getSelectedRow())
												.getId());
						if (deleteMeetingString.equals("deleteMeetingSuccess")) {
							JOptionPane.showMessageDialog(null, "删除成功", "成功信息",
									JOptionPane.WARNING_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(null, "删除失败", "失败信息",
									JOptionPane.WARNING_MESSAGE);
						}
						meetingButtonPanel(frame.getContentPane());
					} catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}

			else if (e.getSource() == meetingButtonPanelUpdate) {
				if (meetingTable.getSelectedRow() != -1) {
					if (!userFrameControl
							.getClient()
							.getUser()
							.getNowMeeting()
							.get(meetingTable.getSelectedRow())
							.getSponsor()
							.equals(userFrameControl.getClient().getUser()
									.getName()))
						JOptionPane.showMessageDialog(null, "不是您发起的会议没有权限修改",
								"失败信息", JOptionPane.WARNING_MESSAGE);
					else {
						meetingUpdateDialog dialog = new meetingUpdateDialog(
								frame, "会议信息修改", true);
						String strings = new String();
						try {
							strings = userFrameControl.getClient()
									.getNowMeetingInfo(
											userFrameControl
													.getClient()
													.getUser()
													.getNowMeeting()
													.get(meetingTable
															.getSelectedRow())
													.getId());
							if (strings.equals("getMeetingInfoSuccess")) {
								Meeting meeting = userFrameControl.getClient()
										.getUser().getNowMeeting()
										.get(meetingTable.getSelectedRow());
								dialog.run(meeting.getId(),
										meeting.getSponsor(),
										meeting.getStringParticipator(),
										meeting.getPosition(),
										meeting.getStime(), meeting.getEtime(),
										meeting.getTitle(),
										meeting.getContent());
								meetingButtonPanel(frame.getContentPane());
							}
						} catch (UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		}
	}

	public class AddMeetingAction implements ActionListener {
		public AddMeetingAction() {
			super();
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == addMeetingButtonPanelEnsure) {
				if (mulit.getEditorText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "参与者不能为空", "失败信息",
							JOptionPane.WARNING_MESSAGE);
				} else if (positionTextField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "会议地点不能为空", "失败信息",
							JOptionPane.WARNING_MESSAGE);
				} else if (titleTextField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "会议主题不能为空", "失败信息",
							JOptionPane.WARNING_MESSAGE);
				} else if (contentTextField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "会议内容不能为空", "失败信息",
							JOptionPane.WARNING_MESSAGE);
				} else {
					String s[] = mulit.getEditorText().split(" ");
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
						String title = titleTextField.getText();
						String position = positionTextField.getText();
						String content = contentTextField.getText();
						try {
							String string = userFrameControl.getClient()
									.addMeeting(
											userFrameControl.getClient()
													.getUser().getId(),
											participatorString, stime, etime,
											title, position, content);
							if (string.equals("addMeetingSuccess")) {
								JOptionPane.showMessageDialog(null, "添加成功",
										"成功信息", JOptionPane.WARNING_MESSAGE);
							} else {
								JOptionPane.showMessageDialog(null, "添加失败",
										"失败信息", JOptionPane.WARNING_MESSAGE);
							}
							addMeetingButtonPanel(frame.getContentPane());
						} catch (UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			} else if (e.getSource() == addMeetingButtonPanelReset) {
				addMeetingButtonPanel(frame.getContentPane());
			}
		}
	}

	public class PersoalFileAction implements ActionListener {
		public PersoalFileAction() {
			super();
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == persoalFileButtonPanelEnsure) {
				String updateUserString = new String();
				try {
					if (passwordTextField.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "密码不能为空！", "注册错误",
								JOptionPane.WARNING_MESSAGE);
					} else if (passwordTextField.getText().length() > 16) {
						JOptionPane.showMessageDialog(null, "密码不能超过16位！",
								"注册错误", JOptionPane.WARNING_MESSAGE);
					} else {
						updateUserString = userFrameControl.getClient()
								.updateUser(passwordTextField.getText());
						if (updateUserString.equals("updateUserSuccess")) {
							JOptionPane.showMessageDialog(null, "修改成功", "成功信息",
									JOptionPane.WARNING_MESSAGE);
							persoalFileButtonPanel(frame.getContentPane());
						} else {
							JOptionPane.showMessageDialog(null, "修改失败", "错误信息",
									JOptionPane.WARNING_MESSAGE);
						}
					}
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else if (e.getSource() == persoalFileButtonPanelReset) {
				persoalFileButtonPanel(frame.getContentPane());
			}
		}
	}

	public class HistoryMeetingAction implements ActionListener {
		public HistoryMeetingAction() {
			super();
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == histroyMeetingButtonPanelCheck) {
				if (historyTable.getSelectedRow() != -1) {
					meetingCheckDialog dialog = new meetingCheckDialog(frame,
							"会议信息", true);
					try {
						String strings = userFrameControl.getClient()
								.getHistoryMeetingInfo(
										userFrameControl
												.getClient()
												.getUser()
												.getHistoryMeeting()
												.get(historyTable
														.getSelectedRow())
												.getId());
						if (strings.equals("getMeetingInfoSuccess")) {
							Meeting meeting = userFrameControl.getClient()
									.getUser().getHistoryMeeting()
									.get(historyTable.getSelectedRow());
							dialog.run(meeting.getSponsor(),
									meeting.getStringParticipator(),
									meeting.getPosition(), meeting.getStime(),
									meeting.getEtime(), meeting.getTitle(),
									meeting.getContent());
						}
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			} else if (e.getSource() == histroyMeetingButtonPanelDelete) {
				if (historyTable.getSelectedRow() != -1) {
					try {
						String deleteMeetingString = userFrameControl
								.getClient().deleteHistoryMeeting(
										userFrameControl
												.getClient()
												.getUser()
												.getHistoryMeeting()
												.get(historyTable
														.getSelectedRow())
												.getId());
						if (deleteMeetingString.equals("deleteMeetingSuccess")) {
							JOptionPane.showMessageDialog(null, "删除成功", "成功信息",
									JOptionPane.WARNING_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(null, "删除失败", "失败信息",
									JOptionPane.WARNING_MESSAGE);
						}
						Collections.sort(userFrameControl.getClient().getUser()
								.getHistoryMeeting(),
								new Comparator<Meeting>() {
									public int compare(Meeting m1, Meeting m2) {
										return m1.getStime().compareTo(
												m2.getStime());
									}
								});
						historyButtonPanel(frame.getContentPane());
					} catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}

			else if (e.getSource() == histroyMeetingButtonPanelAllDelete) {
				int l = historyTable.getRowCount();
				for (int i = 0; i < l; i++) {
					try {
						String deleteMeetingString = userFrameControl
								.getClient().deleteHistoryMeeting(
										userFrameControl.getClient().getUser()
												.getHistoryMeeting().get(0)
												.getId());
						if (!deleteMeetingString.equals("deleteMeetingSuccess")) {
							JOptionPane.showMessageDialog(null, "删除失败", "失败信息",
									JOptionPane.WARNING_MESSAGE);
							break;
						} else if (i + 1 == l) {
							JOptionPane.showMessageDialog(null, "删除成功", "成功信息",
									JOptionPane.WARNING_MESSAGE);
						}
						Collections.sort(userFrameControl.getClient().getUser()
								.getHistoryMeeting(),
								new Comparator<Meeting>() {
									public int compare(Meeting m1, Meeting m2) {
										return m1.getStime().compareTo(
												m2.getStime());
									}
								});
						historyButtonPanel(frame.getContentPane());
					} catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

			}
		}
	}

	public class FriendAction implements ActionListener {
		public FriendAction() {
			super();
		}

		public void actionPerformed(ActionEvent e) {
			int i;
			if (e.getSource() == friendButtonPanelAdd) {
				if (nameField.getText() != "") {
					if (nameField.getText().equals(
							userFrameControl.getClient().getUser().getName()))
						JOptionPane.showMessageDialog(null, "不能添加自己为好友",
								"错误信息", JOptionPane.WARNING_MESSAGE);
					else {
						for (i = 0; i < userFrameControl.getClient().getUser()
								.getFriend().size(); i++) {
							if (nameField.getText().equals(
									userFrameControl.getClient().getUser()
											.getFriend().get(i).getName())) {
								JOptionPane.showMessageDialog(null, "该好友已存在",
										"错误信息", JOptionPane.WARNING_MESSAGE);
								break;
							}
						}
						if (i == userFrameControl.getClient().getUser()
								.getFriend().size()) {
							try {
								String addFriendString = userFrameControl
										.getClient().addFriends(
												nameField.getText());
								if (addFriendString.equals("addFriendSuccess")) {
									JOptionPane.showMessageDialog(null,
											"添加好友成功", "成功信息",
											JOptionPane.WARNING_MESSAGE);
									// userTableModel model = new
									// userTableModel();
									// friendTable = new JTable(model);
								} 
								else {
									JOptionPane.showMessageDialog(null,
											"该好友不存在", "错误信息",
											JOptionPane.WARNING_MESSAGE);
								}
								friendButtonPanel(frame.getContentPane());
							} catch (UnsupportedEncodingException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				} else
					JOptionPane.showMessageDialog(null, "请输入好友名字", "错误信息",
							JOptionPane.WARNING_MESSAGE);

			} else if (e.getSource() == friendButtonPanelReset) {
				nameField.setText("");
			}

			else if (e.getSource() == friendButtonPanelDelete) {
				if (friendTable.getSelectedRow() != -1) {
					try {
						String deleteFriendString = userFrameControl
								.getClient().deleteFriend(
										userFrameControl
												.getClient()
												.getUser()
												.getFriend()
												.get(friendTable
														.getSelectedRow())
												.getId());
						if (deleteFriendString.equals("deleteFriendSuccess")) {
							JOptionPane.showMessageDialog(null, "删除好友成功",
									"成功信息", JOptionPane.WARNING_MESSAGE);
							// userTableModel model = new userTableModel();
							// friendTable = new JTable(model);
						} else if (deleteFriendString
								.equals("deleteFriendFail")) {
							JOptionPane.showMessageDialog(null, "删除好友失败",
									"错误信息", JOptionPane.WARNING_MESSAGE);
						}
						friendButtonPanel(frame.getContentPane());
					} catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == meetingButton) {
			// try {
			// userFrameControl.getClient().getUser().getNowMeeting().clear();
			// String listMeetingsString =
			// userFrameControl.getClient().listMeetings();
			// if (listMeetingsString.equals("listMeetingSuccess"))
			// meetingButtonPanel(frame.getContentPane());
			// else if (listMeetingsString.equals("listMeetingSuccess"))
			// JOptionPane.showMessageDialog(null,
			// "获取会议信息失败", "错误信息", JOptionPane.WARNING_MESSAGE);
			// } catch (UnsupportedEncodingException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// } catch (IOException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			meetingButtonPanel(frame.getContentPane());
		}

		else if (e.getSource() == addMeetingButton) {
			addMeetingButtonPanel(frame.getContentPane());
		} else if (e.getSource() == persoalFileButton) {
			persoalFileButtonPanel(frame.getContentPane());
		} else if (e.getSource() == friendButton) {
			// try {
			// userFrameControl.getClient().getUser().getFriend().clear();
			// String listFriendString =
			// userFrameControl.getClient().listFriends();
			// if (listFriendString.equals("listFriendSuccess")) {
			// friendButtonPanel(frame.getContentPane());
			// }
			// else if (listFriendString.equals("listFriendFail"))
			// JOptionPane.showMessageDialog(null,
			// "获取好友信息失败","错误信息", JOptionPane.WARNING_MESSAGE);
			// } catch (UnsupportedEncodingException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// } catch (IOException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			//
			friendButtonPanel(frame.getContentPane());
		} else if (e.getSource() == historyMeetingButton) {
			// try {
			// userFrameControl.getClient().getUser().getHistoryMeeting().clear();
			// String listMeetingsString =
			// userFrameControl.getClient().listMeetings();
			// if (listMeetingsString.equals("listMeetingSuccess"))
			// meetingButtonPanel(frame.getContentPane());
			// else if (listMeetingsString.equals("listMeetingInfoFail"))
			// JOptionPane.showMessageDialog(null,
			// "获取会议信息失败","错误信息", JOptionPane.WARNING_MESSAGE);
			// } catch (UnsupportedEncodingException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// } catch (IOException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			historyButtonPanel(frame.getContentPane());
		}
	}

	public JFrame getFrame() {
		return frame;
	}

	public userMeetingManager() {

	}

	public void run() {
		/*
		 * 窗体部分
		 */
		frame = new JFrame("Meeting Manage System");
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width / 2; // 获取屏幕的宽
		int screenHeight = screenSize.height / 2; // 获取屏幕的高
		frame.setLocation(screenWidth / 2, screenHeight / 2);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.getContentPane().setLayout(new GridBagLayout());
		frame.setResizable(false);
		meetingAction = new MeetingAction();
		addMeetingAction = new AddMeetingAction();
		historyMeetingAction = new HistoryMeetingAction();
		friendAction = new FriendAction();

		/*
		 * 顶部
		 */
		jpTop = new JPanel();
		jpDown = new JPanel();
		jpMiddle = new JPanel();

		jpTop.setLayout(new BoxLayout(jpTop, BoxLayout.Y_AXIS));

		String temp = userFrameControl.getClient().getUser().getName()
				+ "'s MMS";
		lable = new JLabel(temp);
		lable.setAlignmentX(Component.CENTER_ALIGNMENT);
		lable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		lable.setFont(new Font("微软雅黑", Font.BOLD, 18));
		jpTop.add(lable);
		jpTop.add(Box.createVerticalStrut(5));

		jpButton = new JPanel();
		jpButton.setLayout(new BoxLayout(jpButton, BoxLayout.X_AXIS));
		meetingButton = new JButton(" 所有会议 ");
		addMeetingButton = new JButton(" 添加会议 ");
		persoalFileButton = new JButton(" 个人资料 ");
		friendButton = new JButton(" 查看好友 ");
		historyMeetingButton = new JButton(" 历史记录 ");

		jpButton.add(meetingButton);
		jpButton.add(Box.createHorizontalStrut(10));
		jpButton.add(addMeetingButton);
		jpButton.add(Box.createHorizontalStrut(10));
		jpButton.add(persoalFileButton);
		jpButton.add(Box.createHorizontalStrut(10));
		jpButton.add(friendButton);
		jpButton.add(Box.createHorizontalStrut(10));
		jpButton.add(historyMeetingButton);

		meetingButton.addActionListener(this);
		addMeetingButton.addActionListener(this);
		persoalFileButton.addActionListener(this);
		friendButton.addActionListener(this);
		historyMeetingButton.addActionListener(this);

		jpTop.add(jpButton);

		GridBagConstraints tempGrid = new GridBagConstraints();
		tempGrid.fill = GridBagConstraints.VERTICAL;
		tempGrid.gridx = 0;
		tempGrid.gridy = 0;
		tempGrid.weighty = 0.2;
		tempGrid.ipady = 5;
		tempGrid.gridwidth = 1;
		frame.getContentPane().add(jpTop, tempGrid);
		/*
		 * 所有会议
		 */

		/*
		 * 所有会议->中间表格
		 */
		jpMiddle.removeAll();

		meetingTableModel model = new meetingTableModel(userFrameControl
				.getClient().getUser().getNowMeeting());
		meetingTable = new JTable(model);
		meetingTable.getTableHeader().setReorderingAllowed(false);

		meetingTable.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer(); // 设置文字居中
		tcr.setHorizontalAlignment(JLabel.CENTER);
		meetingTable.setDefaultRenderer(Object.class, tcr);

		meetingTable.getColumnModel().getColumn(0).setPreferredWidth(40);
		meetingTable.getColumnModel().getColumn(0).setMaxWidth(40);
		meetingTable.getColumnModel().getColumn(0).setMinWidth(40);
		meetingTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		meetingTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		meetingTable.getColumnModel().getColumn(3).setPreferredWidth(40);
		meetingTable.getColumnModel().getColumn(4).setPreferredWidth(104);

		meetingTable.setRowHeight(30);
		meetingJScrollPane = new JScrollPane(meetingTable);
		meetingJScrollPane.setPreferredSize(new Dimension(500, 400));

		tempGrid = new GridBagConstraints();
		tempGrid.fill = GridBagConstraints.VERTICAL;
		tempGrid.gridx = 0;
		tempGrid.gridy = 1;
		tempGrid.ipady = 5;
		tempGrid.weighty = 0.5;
		tempGrid.gridwidth = 1;

		jpMiddle.add(meetingJScrollPane);
		frame.getContentPane().add(jpMiddle, tempGrid);

		/*
		 * 所有会议->底部按钮
		 */
		jpDown.removeAll();
		jpDown.setLayout(new BoxLayout(jpDown, BoxLayout.X_AXIS));
		meetingButtonPanelCheck = new JButton("查看");
		meetingButtonPanelDelete = new JButton("删除");
		meetingButtonPanelUpdate = new JButton("修改");
		meetingButtonPanelCheck.addActionListener(meetingAction);
		meetingButtonPanelDelete.addActionListener(meetingAction);
		meetingButtonPanelUpdate.addActionListener(meetingAction);

		jpDown.add(meetingButtonPanelCheck);
		jpDown.add(Box.createHorizontalStrut(15));
		jpDown.add(meetingButtonPanelDelete);
		jpDown.add(Box.createHorizontalStrut(15));
		jpDown.add(meetingButtonPanelUpdate);

		tempGrid.fill = GridBagConstraints.VERTICAL;
		tempGrid.gridx = 0;
		tempGrid.gridy = 2;
		tempGrid.ipady = 5;
		tempGrid.weighty = 1;

		frame.getContentPane().add(jpDown, tempGrid);
		frame.setVisible(true);
		frame.pack();

		String collisionString = userFrameControl.getClient().HasCollision(
				userFrameControl.getClient().getUser().getNowMeeting());
		if (!collisionString.isEmpty()) {
			JOptionPane.showMessageDialog(null, collisionString, "提示信息",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	public void historyButtonPanel(Container pane) {
		/*
		 * 查看历史记录
		 */
		/*
		 * 查看历史记录->中间表格
		 */
		jpMiddle.removeAll();

		meetingTableModel model = new meetingTableModel(userFrameControl
				.getClient().getUser().getHistoryMeeting());
		historyTable = new JTable(model);
		historyTable.getTableHeader().setReorderingAllowed(false);
		historyTable.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer(); // 设置文字居中
		tcr.setHorizontalAlignment(JLabel.CENTER);
		historyTable.setDefaultRenderer(Object.class, tcr);

		historyTable.getColumnModel().getColumn(0).setPreferredWidth(40);
		historyTable.getColumnModel().getColumn(0).setMaxWidth(40);
		historyTable.getColumnModel().getColumn(0).setMinWidth(40);
		historyTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		historyTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		historyTable.getColumnModel().getColumn(3).setPreferredWidth(40);
		historyTable.getColumnModel().getColumn(4).setPreferredWidth(104);

		historyTable.setRowHeight(30);
		historyMeetingJScrollPane = new JScrollPane(historyTable);
		historyMeetingJScrollPane.setPreferredSize(new Dimension(500, 400));

		GridBagConstraints tempGrid = new GridBagConstraints();
		tempGrid.fill = GridBagConstraints.VERTICAL;
		tempGrid.gridx = 0;
		tempGrid.gridy = 1;
		tempGrid.ipady = 5;
		tempGrid.weighty = 0.5;
		tempGrid.gridwidth = 1;

		jpMiddle.add(historyMeetingJScrollPane);
		jpMiddle.doLayout();

		/*
		 * 查看历史记录->底部按钮
		 */
		jpDown.removeAll();
		jpDown.setLayout(new BoxLayout(jpDown, BoxLayout.X_AXIS));
		histroyMeetingButtonPanelCheck = new JButton("查看");
		histroyMeetingButtonPanelDelete = new JButton("删除");
		histroyMeetingButtonPanelAllDelete = new JButton("全部删除");
		histroyMeetingButtonPanelCheck.addActionListener(historyMeetingAction);
		histroyMeetingButtonPanelDelete.addActionListener(historyMeetingAction);
		histroyMeetingButtonPanelAllDelete
				.addActionListener(historyMeetingAction);

		jpDown.add(histroyMeetingButtonPanelCheck);
		jpDown.add(Box.createHorizontalStrut(15));
		jpDown.add(histroyMeetingButtonPanelDelete);
		jpDown.add(Box.createHorizontalStrut(15));
		jpDown.add(histroyMeetingButtonPanelAllDelete);

		tempGrid.fill = GridBagConstraints.VERTICAL;
		tempGrid.gridx = 0;
		tempGrid.gridy = 2;
		tempGrid.ipady = 5;
		tempGrid.weighty = 1;

		jpDown.doLayout();
		frame.getContentPane().validate();
	}

	public void friendButtonPanel(Container pane) {
		/*
		 * 查看好友
		 */
		/*
		 * 查看好友->中间表格
		 */
		jpMiddle.removeAll();

		userTableModel model = new userTableModel();
		friendTable = new JTable(model);

		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer(); // 设置文字居中
		tcr.setHorizontalAlignment(JLabel.CENTER);
		friendTable.setDefaultRenderer(Object.class, tcr);
		friendTable.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		friendTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		friendTable.getColumnModel().getColumn(0).setPreferredWidth(52);
		friendTable.getColumnModel().getColumn(0).setMaxWidth(52);
		friendTable.getColumnModel().getColumn(0).setMinWidth(52);

		friendTable.getColumnModel().getColumn(1).setPreferredWidth(80);
		friendTable.getColumnModel().getColumn(2).setPreferredWidth(175);
		friendTable.getColumnModel().getColumn(3).setPreferredWidth(175);

		friendTable.setRowHeight(30);
		friendTable.getTableHeader().setReorderingAllowed(false);
		userJScrollPane = new JScrollPane(friendTable);
		userJScrollPane.setPreferredSize(new Dimension(500, 400));

		GridBagConstraints tempGrid = new GridBagConstraints();
		tempGrid.fill = GridBagConstraints.VERTICAL;
		tempGrid.gridx = 0;
		tempGrid.gridy = 1;
		tempGrid.ipady = 5;
		tempGrid.weighty = 0.5;
		tempGrid.gridwidth = 1;

		jpMiddle.add(userJScrollPane);
		jpMiddle.doLayout();

		/*
		 * 查看好友->底部按钮
		 */
		jpDown.removeAll();
		nameField = new JTextField();
		nameField.setPreferredSize(new Dimension(200, 10));
		nameField.setMaximumSize(new Dimension(100, 27));
		jpDown.setLayout(new BoxLayout(jpDown, BoxLayout.X_AXIS));
		friendButtonPanelAdd = new JButton("添加");
		friendButtonPanelReset = new JButton("重置");
		friendButtonPanelDelete = new JButton("删除");
		friendButtonPanelAdd.addActionListener(friendAction);
		friendButtonPanelReset.addActionListener(friendAction);
		friendButtonPanelDelete.addActionListener(friendAction);

		jpDown.add(nameField);
		jpDown.add(Box.createHorizontalStrut(15));
		jpDown.add(friendButtonPanelAdd);
		jpDown.add(Box.createHorizontalStrut(15));
		jpDown.add(friendButtonPanelReset);
		jpDown.add(Box.createHorizontalStrut(15));
		jpDown.add(friendButtonPanelDelete);

		tempGrid.fill = GridBagConstraints.VERTICAL;
		tempGrid.gridx = 0;
		tempGrid.gridy = 2;
		tempGrid.ipady = 5;
		tempGrid.weighty = 1;

		jpDown.doLayout();
		frame.getContentPane().validate();
	}

	public void meetingButtonPanel(Container pane) {
		/*
		 * 所有会议
		 */

		/*
		 * 所有会议->中间表格
		 */
		jpMiddle.removeAll();

		meetingTableModel model = new meetingTableModel(userFrameControl
				.getClient().getUser().getNowMeeting());
		meetingTable = new JTable(model);
		meetingTable.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer(); // 设置文字居中
		tcr.setHorizontalAlignment(JLabel.CENTER);
		meetingTable.setDefaultRenderer(Object.class, tcr);
		meetingTable.getTableHeader().setReorderingAllowed(false);
		meetingTable.getColumnModel().getColumn(0).setPreferredWidth(40);
		meetingTable.getColumnModel().getColumn(0).setMaxWidth(40);
		meetingTable.getColumnModel().getColumn(0).setMinWidth(40);
		meetingTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		meetingTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		meetingTable.getColumnModel().getColumn(3).setPreferredWidth(40);
		meetingTable.getColumnModel().getColumn(4).setPreferredWidth(104);

		meetingTable.setRowHeight(30);
		meetingJScrollPane = new JScrollPane(meetingTable);
		meetingJScrollPane.setPreferredSize(new Dimension(500, 400));

		GridBagConstraints tempGrid = new GridBagConstraints();
		tempGrid.fill = GridBagConstraints.VERTICAL;
		tempGrid.gridx = 0;
		tempGrid.gridy = 1;
		tempGrid.ipady = 5;
		tempGrid.weighty = 0.5;
		tempGrid.gridwidth = 1;

		jpMiddle.add(meetingJScrollPane);
		jpMiddle.doLayout();

		/*
		 * 所有会议->底部按钮
		 */
		jpDown.removeAll();
		jpDown.setLayout(new BoxLayout(jpDown, BoxLayout.X_AXIS));
		meetingButtonPanelCheck = new JButton("查看");
		meetingButtonPanelDelete = new JButton("删除");
		meetingButtonPanelUpdate = new JButton("修改");
		meetingButtonPanelCheck.addActionListener(meetingAction);
		meetingButtonPanelDelete.addActionListener(meetingAction);
		meetingButtonPanelUpdate.addActionListener(meetingAction);

		jpDown.add(meetingButtonPanelCheck);
		jpDown.add(Box.createHorizontalStrut(15));
		jpDown.add(meetingButtonPanelDelete);
		jpDown.add(Box.createHorizontalStrut(15));
		jpDown.add(meetingButtonPanelUpdate);

		tempGrid.fill = GridBagConstraints.VERTICAL;
		tempGrid.gridx = 0;
		tempGrid.gridy = 2;
		tempGrid.ipady = 5;
		tempGrid.weighty = 1;

		jpDown.doLayout();
		frame.getContentPane().validate();
	}

	public void addMeetingButtonPanel(Container pane) {
		/*
		 * 添加会议
		 */
		/*
		 * 添加会议->上层控件
		 */
		jpMiddle.removeAll();
		jpMiddle.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		Box sponsorBox, participateBox, positionBox, titleBox, contentBox;
		Box stimeBox, etimeBox;
		sponsorBox = Box.createHorizontalBox();
		participateBox = Box.createHorizontalBox();
		stimeBox = Box.createHorizontalBox();
		etimeBox = Box.createHorizontalBox();
		positionBox = Box.createHorizontalBox();
		titleBox = Box.createHorizontalBox();
		contentBox = Box.createHorizontalBox();

		JTextField sponsorTextField;

		sponsorTextField = new JTextField(userFrameControl.getClient()
				.getUser().getName());
		positionTextField = new JTextField();
		titleTextField = new JTextField();
		contentTextField = new JTextField();

		mulit = new MulitComboBox();
		mulit.setPreferredSize(new Dimension(260, 22));

		sponsorTextField.setPreferredSize(new Dimension(300, 30));
		positionTextField.setPreferredSize(new Dimension(300, 30));
		titleTextField.setPreferredSize(new Dimension(300, 30));
		contentTextField.setPreferredSize(new Dimension(300, 30));

		sponsorBox.add(new JLabel("发起人:"));
		sponsorBox.add(Box.createHorizontalStrut(28));
		sponsorBox.add(sponsorTextField);
		sponsorTextField.setEditable(false);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.0;
		c.insets = new Insets(20, 0, 0, 0);

		jpMiddle.add(sponsorBox, c);

		participateBox.add(new JLabel("参与者:"));
		participateBox.add(Box.createHorizontalStrut(28));
		participateBox.add(mulit);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0.0;
		jpMiddle.add(participateBox, c);

		stimeSpinner = new boxDateSpinner();
		stimeBox.add(new JLabel("开始时间:"));
		stimeBox.add(Box.createHorizontalStrut(15));
		stimeBox.add(stimeSpinner.getJSpinner());

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 0.0;
		jpMiddle.add(stimeBox, c);

		etimeSpinner = new boxDateSpinner();
		etimeBox.add(new JLabel("结束时间:"));
		etimeBox.add(Box.createHorizontalStrut(15));
		etimeBox.add(etimeSpinner.getJSpinner());

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 0.0;
		jpMiddle.add(etimeBox, c);

		positionBox.add(new JLabel("会议地点:"));
		positionBox.add(Box.createHorizontalStrut(16));
		positionBox.add(positionTextField);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		c.weightx = 0.0;
		jpMiddle.add(positionBox, c);

		titleBox.add(new JLabel("会议主题:"));
		titleBox.add(Box.createHorizontalStrut(16));
		titleBox.add(titleTextField);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 5;
		c.weightx = 0.0;
		jpMiddle.add(titleBox, c);

		contentBox.add(new JLabel("会议内容:"));
		contentBox.add(Box.createHorizontalStrut(16));
		contentBox.add(contentTextField);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 6;
		c.weightx = 0.0;
		jpMiddle.add(contentBox, c);

		jpMiddle.doLayout();

		/*
		 * 添加会议->底部按钮
		 */
		jpDown.removeAll();
		jpDown.setLayout(new BoxLayout(jpDown, BoxLayout.X_AXIS));
		addMeetingButtonPanelEnsure = new JButton("确定");
		addMeetingButtonPanelEnsure.addActionListener(addMeetingAction);
		addMeetingButtonPanelReset = new JButton("重置");
		addMeetingButtonPanelReset.addActionListener(addMeetingAction);

		jpDown.add(addMeetingButtonPanelEnsure);
		jpDown.add(Box.createHorizontalStrut(15));
		jpDown.add(addMeetingButtonPanelReset);
		jpDown.doLayout();
		frame.getContentPane().validate();
	}

	public void persoalFileButtonPanel(Container pane) {
		/*
		 * 修改资料
		 */
		/*
		 * 修改资料->上层控件
		 */
		jpMiddle.removeAll();
		jpMiddle.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		Box userNameBox, passwordBox, mailBox, telephoneBox;
		userNameBox = Box.createHorizontalBox();
		passwordBox = Box.createHorizontalBox();
		mailBox = Box.createHorizontalBox();
		telephoneBox = Box.createHorizontalBox();

		JTextField userNameTextField, mailTextField, telephoneTextField;

		userNameTextField = new JTextField();
		passwordTextField = new JTextField();

		mailTextField = new JTextField();
		telephoneTextField = new JTextField();

		userNameTextField.setPreferredSize(new Dimension(300, 30));
		passwordTextField.setPreferredSize(new Dimension(300, 30));
		mailTextField.setPreferredSize(new Dimension(300, 30));
		telephoneTextField.setPreferredSize(new Dimension(300, 30));

		userNameTextField.setText(userFrameControl.getClient().getUser()
				.getName());
		passwordTextField.setText(userFrameControl.getClient().getUser()
				.getPassword());
		mailTextField.setText(userFrameControl.getClient().getUser().getMail());
		telephoneTextField.setText(userFrameControl.getClient().getUser()
				.getTelePhone());
		userNameTextField.setEditable(false);
		mailTextField.setEditable(false);
		telephoneTextField.setEditable(false);

		userNameBox.add(new JLabel("用户名:"));
		userNameBox.add(Box.createHorizontalStrut(15));
		userNameBox.add(userNameTextField);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.0;
		c.insets = new Insets(53, 0, 0, 0);

		jpMiddle.add(userNameBox, c);

		passwordBox.add(new JLabel("密码    :"));
		passwordBox.add(Box.createHorizontalStrut(16));
		passwordBox.add(passwordTextField);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0.0;
		jpMiddle.add(passwordBox, c);

		mailBox.add(new JLabel("邮箱    :"));
		mailBox.add(Box.createHorizontalStrut(16));
		mailBox.add(mailTextField);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 0.0;
		jpMiddle.add(mailBox, c);

		telephoneBox.add(new JLabel("电话    :"));
		telephoneBox.add(Box.createHorizontalStrut(16));
		telephoneBox.add(telephoneTextField);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 0.0;
		jpMiddle.add(telephoneBox, c);

		jpMiddle.doLayout();

		/*
		 * 修改资料->底部按钮
		 */
		jpDown.removeAll();
		jpDown.setLayout(new BoxLayout(jpDown, BoxLayout.X_AXIS));
		persoalFileButtonPanelEnsure = new JButton("确定");
		persoalFileButtonPanelReset = new JButton("重置");
		jpDown.add(persoalFileButtonPanelEnsure);
		jpDown.add(Box.createHorizontalStrut(15));
		jpDown.add(persoalFileButtonPanelReset);
		persoalFileButtonPanelEnsure.addActionListener(new PersoalFileAction());
		persoalFileButtonPanelReset.addActionListener(new PersoalFileAction());
		jpDown.doLayout();
		frame.getContentPane().validate();
	}

	// public static void main(String args[]) {
	// userMeetingManager a = new userMeetingManager();
	// a.run();
	// }
}