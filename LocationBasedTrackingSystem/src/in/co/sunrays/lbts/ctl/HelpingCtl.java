package in.co.sunrays.lbts.ctl;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.locationbasedtrackingsystem.activity.R;

public class HelpingCtl extends BaseCtl {

	private final String htmlText = "<body><h3>Location Based Task Reminder</h3><p align='justify'>This application reminds the user about tasks that are to be accomplished depending upon the user’s location. The user defines the task on a Smartphone through a map view. The application constantly monitors the location of the mobile user using GPS and alerts the user when he or she is near a specification."
			+ "<br><br>"
			+ "<strong>New Task: </strong>User Add the task daily or weekly Basis for Reminder. Reminder is work on the basis of location or time."
			+ "<br><br>"
			+ "<strong>Upcoming Task:</strong>User can see the upcoming task or remaining task. User can also update or delete the task. "
			+ "<br><br>"
			+ "<strong>Passed Task:</strong>User can see the passed task. User can also delete the passed task.</p>  </body>";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_helping);

		TextView htmlTextView = (TextView) findViewById(R.id.help_page_intro);
		htmlTextView.setText(Html.fromHtml(htmlText));
	}

}