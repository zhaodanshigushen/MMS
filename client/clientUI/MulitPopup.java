package clientUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.naming.InitialContext;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.xml.soap.Text;

import clientCode.User;

public class MulitPopup extends JPopupMenu {
	private List<ActionListener> listeners = new ArrayList<ActionListener>();
	private LinkedList<User> friendsLinkedList;
	private List<JCheckBox> checkBoxList = new ArrayList<JCheckBox>();
	private JButton ensureJButton, cancelJButton;
	private JPanel checkPanel, buttonPanel;
	private String text;

	public MulitPopup() {
		super();
		friendsLinkedList = new LinkedList<User>();
		friendsLinkedList = userFrameControl.getClient().getUser().getFriend();
		init();
	}

	private void init() {
		checkPanel = new JPanel();
		buttonPanel = new JPanel();
		text = new String();

		this.setLayout(new BorderLayout());
		checkPanel.setLayout(new GridLayout(checkBoxList.size(), 1, 0, 0));
		for (int i = 0; i < friendsLinkedList.size(); i++) {
			JCheckBox temp = new JCheckBox(friendsLinkedList.get(i).getName()
					.toString(), false);
			checkBoxList.add(temp);
			checkPanel.add(temp);
		}

		Box buttonBox = Box.createHorizontalBox();
		ensureJButton = new JButton("确定");
		ensureJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commit();

			}
		});

		cancelJButton = new JButton("取消");
		cancelJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancel();
			}
		});

		buttonBox.add(ensureJButton);
		buttonBox.add(Box.createHorizontalStrut(5));
		buttonBox.add(cancelJButton);

		buttonPanel.add(buttonBox);
		this.add(checkPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}

	public Object[] getSelectedVlaues() {
		List<Object> selectedValues = new ArrayList<Object>();
		for (int i = 0; i < checkBoxList.size(); i++) {
			if (checkBoxList.get(i).isSelected())
				selectedValues.add(friendsLinkedList.get(i).getName());
		}
		return selectedValues.toArray(new Object[selectedValues.size()]);
	}

	public void textToSelectedVlaues() {
		if (text.isEmpty()) {
			for (int i = 0; i < checkBoxList.size(); i++) {
				checkBoxList.get(i).setSelected(false);
			}
		} else {
			String[] s = text.split(" ");
			int i, j;
			for (i = 0; i < checkBoxList.size(); i++)
				checkBoxList.get(i).setSelected(false);
			for (i = 0; i < s.length; i++) {
				for (j = 0; j < checkBoxList.size(); j++) {
					if (s[i].equals(checkBoxList.get(j).getText())) {
						checkBoxList.get(j).setSelected(true);
						break;
					}
				}
			}
		}
	}

	public void setText(Object[] value) {
		StringBuilder builder = new StringBuilder();
		for (Object dv : value) {
			builder.append(dv);
			builder.append(" ");
		}
		text = builder.substring(0,
				builder.length() > 0 ? builder.length() - 1 : 0).toString();
	}

	public void setText(String s) {
		text = s;
	}

	public String getText() {
		return text;
	}

	public void addActionListener(ActionListener listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	public void removeActionListener(ActionListener listener) {
		if (listeners.contains(listener))
			listeners.remove(listener);
	}

	private void fireActionPerformed(ActionEvent e) {
		for (ActionListener l : listeners) {
			l.actionPerformed(e);
		}
	}

	public void commit() {
		fireActionPerformed(new ActionEvent(this, 0, "ensure"));
	}

	public void cancel() {
		fireActionPerformed(new ActionEvent(this, 0, "cancle"));
	}
}