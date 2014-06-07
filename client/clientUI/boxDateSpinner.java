package clientUI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.datatransfer.StringSelection;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.Box;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

public class boxDateSpinner {
	private JSpinner dateSpinner;
	private Calendar cld;

	public boxDateSpinner() {
		Calendar tempCalendar = Calendar.getInstance();
		tempCalendar.set(Calendar.YEAR, 2010);
		Comparable<java.util.Date> sDate = tempCalendar.getTime();
		tempCalendar.set(Calendar.YEAR, 2050);
		Comparable<java.util.Date> eDate = tempCalendar.getTime();

		SpinnerDateModel spinnerDateModel = new SpinnerDateModel();
		spinnerDateModel.setStart(sDate);
		spinnerDateModel.setEnd(eDate);

		dateSpinner = new JSpinner(spinnerDateModel);
		JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner,
				"yyyy年  MM月  dd日  HH时  mm分");
		dateSpinner.setEditor(editor);

		cld = Calendar.getInstance();
		cld.setTime((java.util.Date) dateSpinner.getValue());
	}

	public JSpinner getJSpinner() {
		return dateSpinner;
	}

	public void setValue(String year, String month, String day, String hour,
			String minute, String second) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, Integer.parseInt(year));
		calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
		calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
		calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
		calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
		calendar.set(Calendar.SECOND, Integer.parseInt(minute));
		dateSpinner.getModel().setValue(calendar.getTime());
		cld.setTime((java.util.Date) dateSpinner.getValue());
	}

	public int getYear() {
		cld.setTime((java.util.Date) dateSpinner.getValue());
		return cld.get(Calendar.YEAR);
	}

	public int getMonth() {
		cld.setTime((java.util.Date) dateSpinner.getValue());
		return cld.get(Calendar.MONTH);
	}

	public int getDay() {
		cld.setTime((java.util.Date) dateSpinner.getValue());
		return cld.get(Calendar.DAY_OF_MONTH);
	}

	public int getHour() {
		cld.setTime((java.util.Date) dateSpinner.getValue());
		return cld.get(Calendar.HOUR_OF_DAY);
	}

	public int getMinute() {
		cld.setTime((java.util.Date) dateSpinner.getValue());
		return cld.get(Calendar.MINUTE);
	}

	public int getSecond() {
		cld.setTime((java.util.Date) dateSpinner.getValue());
		return cld.get(Calendar.SECOND);
	}

	public void setAllEditAble(boolean flag) {
		dateSpinner.setEnabled(flag);
	}
}