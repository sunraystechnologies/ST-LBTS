package com.locationbasedtrackingsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class NotifyAlarmActivity extends BaseActivity {

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