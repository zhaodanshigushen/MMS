package clientUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicArrowButton;

import clientCode.User;

public class MulitComboBox extends JComponent {
	private MulitPopup popup;
	private JTextField editor;
	private JButton arrowButton;

	private JButton createArrowButton() {
		JButton button = new BasicArrowButton(BasicArrowButton.SOUTH,
				UIManager.getColor("ComboBox.buttonBackground"),
				UIManager.getColor("ComboBox.buttonShadow"),
				UIManager.getColor("ComboBox.buttonDarkShadow"),
				UIManager.getColor("ComboBox.buttonHighlight"));
		button.setName("ComboBox.arrowButton");
		return button;
	}

	public MulitComboBox() {
		popup = new MulitPopup();
		init();
	}

	public void setPreferredSize(Dimension s) {
		editor.setPreferredSize(s);
	}

	public MulitPopup getPopup() {
		return popup;
	}

	public void setPopup(MulitPopup popup) {
		this.popup = popup;
	}

	public String getEditorText() {
		return editor.getText();
	}

	public void setEditorText(String s) {
		editor.setText(s);
		popup.setText(s);
	}

	public void init() {
		this.setLayout(new FlowLayout());
		popup.addActionListener(new PopupAction());
		editor = new JTextField();
		editor.setBackground(Color.WHITE);
		editor.setEditable(false);
		editor.setBorder(null);
		arrowButton = createArrowButton();
		arrowButton.addMouseListener(new EditorHandler());
		add(editor);
		add(arrowButton);
	}

	private class PopupAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("cancle")) {
				popup.textToSelectedVlaues();
			} else if (e.getActionCommand().equals("ensure")) {
				popup.setText(popup.getSelectedVlaues());
				editor.setText(popup.getText());
			}
			togglePopup();
		}
	}

	private void togglePopup() {
		if (popup.isVisible()) {
			popup.setVisible(false);
		} else {
			popup.show(this, 0, getHeight());
			popup.setVisible(true);
		}
	}

	private class EditorHandler implements MouseListener {
		public void mouseClicked(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
			togglePopup();
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}
	}
}