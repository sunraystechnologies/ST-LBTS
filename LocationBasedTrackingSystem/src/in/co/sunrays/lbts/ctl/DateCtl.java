package in.co.sunrays.lbts.ctl;

import java.util.Calendar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.locationbasedtrackingsystem.activity.R;

public class DateCtl extends BaseCtl {
	private TextView text_date;
	private DatePicker date_picker;
	private Button set;
	private Button cancel;
	private int year;
	private int month;
	private int day;

	static final int DATE_DIALOG_ID = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_date);
		setCurrentDate();

	}

	public void setCurrentDate() {

		text_date = (TextView) findViewById(R.id.currentDate_da);
		date_picker = (DatePicker) findViewById(R.id.datePicker_da);

		final Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);

		// set current date into textview
		text_date.setText(new StringBuilder()
				// Month is 0 based, so you have to add 1
				.append(month + 1).append("-").append(day).append("-")
				.append(year).append(" "));

		// set current date into Date Picker
		date_picker.init(year, month, day, null);
	}

	/*
	 * protected Dialog onCreateDialog(int id) {
	 * 
	 * switch (id) { case DATE_DIALOG_ID: // set date picker as current date
	 * return new DatePickerDialog(this, datePickerListener, year, month,day); }
	 * return null; }
	 * 
	 * private DatePickerDialog.OnDateSetListener datePickerListener = new
	 * DatePickerDialog.OnDateSetListener() {
	 * 
	 * // when dialog box is closed, below method will be called. public void
	 * onDateSet(DatePicker view, int selectedYear, int selectedMonth, int
	 * selectedDay) { year = selectedYear; month = selectedMonth; day =
	 * selectedDay;
	 * 
	 * // set selected date into Text View text_date.setText(new
	 * StringBuilder().append(month + 1).append("-")
	 * .append(day).append("-").append(year).append(" "));
	 * 
	 * // set selected date into Date Picker date_picker.init(year, month, day,
	 * null);
	 * 
	 * } };
	 */

}
