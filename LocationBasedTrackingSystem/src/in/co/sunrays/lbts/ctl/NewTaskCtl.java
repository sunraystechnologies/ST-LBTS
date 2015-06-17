package in.co.sunrays.lbts.ctl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.locationbasedtrackingsystem.activity.R;

public class NewTaskCtl extends BaseCtl {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_task);

		Button dailyRoutine = (Button) findViewById(R.id.dailyroutine_nta);
		Button weaklyRoutine = (Button) findViewById(R.id.weaklyroutine_nta);

		// -----------------Daily Routine--------------------------//
		dailyRoutine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NewTaskCtl.this,
						DailyRoutineCtl.class);
				String name = null;
				intent.putExtra("TaskName", name);
				startActivity(intent);
			}
		});
	}

}
