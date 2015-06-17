package in.co.sunrays.lbts.ctl;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.locationbasedtrackingsystem.activity.R;

public class SpinnerCtl extends BaseCtl {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spinner);
	}

	public void setLocationSpinner() {
		Spinner spinner = (Spinner) findViewById(R.id.location_dra);
		String[] state = new String[3];
		state[0] = "Select";
		state[1] = "MAP";
		state[2] = "FAVOURITE PLACE";
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(),
				android.R.layout.simple_dropdown_item_1line, state);
		spinner.setAdapter(adapter);

	}

	public void setRadiusSpinner() {
		Spinner spinner = (Spinner) findViewById(R.id.radius_dra);
		String[] state = new String[7];
		state[0] = "Range";
		state[1] = "50";
		state[2] = "250";
		state[3] = "500";
		state[4] = "1000";
		state[5] = "2000";
		state[6] = "3000";
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(),
				android.R.layout.simple_dropdown_item_1line, state);
		spinner.setAdapter(adapter);

	}

	public void setSearchSpinner() {
		Spinner spinner = (Spinner) findViewById(R.id.searchnearby_dra);
		String[] state = new String[10];
		state[0] = "List";
		state[1] = "Shop";
		state[2] = "Restaurant";
		state[3] = "ATM";
		state[4] = "Petrol Station";
		state[5] = "Hospital";
		state[6] = "Police Station";
		state[7] = "Railway Station";
		state[8] = "Bus Stop";
		state[9] = "Movies";

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(),
				android.R.layout.simple_dropdown_item_1line, state);
		spinner.setAdapter(adapter);

	}

}