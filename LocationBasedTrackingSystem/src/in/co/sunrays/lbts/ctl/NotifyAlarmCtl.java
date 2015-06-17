package in.co.sunrays.lbts.ctl;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.locationbasedtrackingsystem.activity.R;

public class NotifyAlarmCtl extends BaseCtl {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notify_alarm);

		Intent intent = getIntent();
		String TaskName = intent.getStringExtra("TaskName");
		EditText text = (EditText) findViewById(R.id.taskName_notify);
		text.setText("Your task Name is " + TaskName);
	}

}