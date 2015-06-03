package in.co.sunrays.lbts.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.locationbasedtrackingsystem.activity.BaseActivity;
import com.locationbasedtrackingsystem.activity.R;

public class HomePageActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);

		Button newTask = (Button) findViewById(R.id.newtask_hpa);
		Button upComing = (Button) findViewById(R.id.upcoming_hpa);
		Button passedTask = (Button) findViewById(R.id.passedtask_hpa);
		Button helping = (Button) findViewById(R.id.helping_hpa);

		// -----------New Task Button Event----------------//
		newTask.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(HomePageActivity.this,
						DailyRoutineActivity.class);
				String name = null;
				intent.putExtra("TaskName", name);
				startActivity(intent);
			}
		});

		// -----------Up-Coming Button Event--------------//

		upComing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(HomePageActivity.this,
						UpComingActivity.class);
				startActivity(intent);
			}
		});

		// ---------------Passed Task Button Event---------------//

		passedTask.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(HomePageActivity.this,
						PassedActivity.class);
				startActivity(intent);
			}
		});

		// --------------Helping Task Button Event----------------//

		helping.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(HomePageActivity.this,
						HelpingActivity.class);
				startActivity(intent);
			}
		});
	}

}
