package clientUI;

import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import clientCode.Meeting;

public class meetingTableModel extends AbstractTableModel {
	private String columnNames[] = { "序号", "开始时间", "结束时间", "发起人", "会议标题" };
	private LinkedList<Meeting> meeting;

	public meetingTableModel() {
		super();
		meeting = new LinkedList<Meeting>();
	}

	public meetingTableModel(LinkedList<Meeting> meetings) {
		meeting = new LinkedList<Meeting>();
		meeting = meetings;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public String getColumnName(int column) {
		return columnNames[column];
	}

	public int getRowCount() {
		return meeting.size();
	}

	public Object getValueAt(int row, int column) {
		if (column == 0)
			return Integer.toString(row);
		else if (column == 1) {
			String stime = meeting.get(row).getStime();
			// String yearString = stime.substring(2,4);
			String monthString = stime.substring(4, 6);
			String dayString = stime.substring(6, 8);
			String hourString = stime.substring(8, 10);
			String minuteString = stime.substring(10, 12);
			StringBuffer stringBuffer = new StringBuffer();
			// stringBuffer.append(yearString);
			// stringBuffer.append("年");
			stringBuffer.append(monthString);
			stringBuffer.append("月");
			stringBuffer.append(dayString);
			stringBuffer.append("日 ");
			stringBuffer.append(hourString);
			stringBuffer.append("点");
			stringBuffer.append(minuteString);
			stringBuffer.append("分");
			return stringBuffer.toString();
		} else if (column == 2) {
			String etime = meeting.get(row).getEtime();
			// String yearString = etime.substring(2,4);
			String monthString = etime.substring(4, 6);
			String dayString = etime.substring(6, 8);
			String hourString = etime.substring(8, 10);
			String minuteString = etime.substring(10, 12);
			StringBuffer stringBuffer = new StringBuffer();
			// stringBuffer.append(yearString);
			// stringBuffer.append("年");
			stringBuffer.append(monthString);
			stringBuffer.append("月");
			stringBuffer.append(dayString);
			stringBuffer.append("日 ");
			stringBuffer.append(hourString);
			stringBuffer.append("点");
			stringBuffer.append(minuteString);
			stringBuffer.append("分");
			return stringBuffer.toString();
		} else if (column == 3)
			return meeting.get(row).getSponsor();
		else
			return meeting.get(row).getTitle();
	}

	public boolean isCellEditable(int row, int column) {
		return false;
	}// 表格不允许被编辑

}