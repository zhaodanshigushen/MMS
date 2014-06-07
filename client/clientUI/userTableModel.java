package clientUI;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

public class userTableModel extends AbstractTableModel {
	private String columnNames[] = { "序号", "好友名称", "好友邮箱", "好友电话" };

	public userTableModel() {
		super();
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public String getColumnName(int column) {
		return columnNames[column];
	}

	public int getRowCount() {
		return userFrameControl.getClient().getUser().getFriend().size();
	}

	public Object getValueAt(int row, int column) {
		if (column == 0)
			return Integer.toString(row);
		else if (column == 1)
			return userFrameControl.getClient().getUser().getFriend().get(row)
					.getName();
		else if (column == 2)
			return userFrameControl.getClient().getUser().getFriend().get(row)
					.getMail();
		else
			return userFrameControl.getClient().getUser().getFriend().get(row)
					.getTelePhone();
	}

	public boolean isCellEditable(int row, int column) {
		return false;
	}// 表格不允许被编辑

}