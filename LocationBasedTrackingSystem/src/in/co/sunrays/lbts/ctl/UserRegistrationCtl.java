package in.co.sunrays.lbts.ctl;

import org.json.JSONException;
import org.json.JSONObject;

import com.locationbasedtrackingsystem.activity.R;
import com.locationbasedtrackingsystem.activity.R.id;
import com.locationbasedtrackingsystem.activity.R.layout;
import com.locationbasedtrackingsystem.activity.R.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserRegistrationCtl extends Activity implements
		LocationListener {

	LocationManager locationManager = null;
	Location location = null;
	String provider = null;
	double longitude = 0, latitude = 0;
	String success = null, dbfn = null, dbln = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_registration);

		final EditText firstName = (EditText) findViewById(R.id.regid_fn);
		final EditText lastName = (EditText) findViewById(R.id.regid_ln);
		final EditText login = (EditText) findViewById(R.id.regid_login);
		final EditText password = (EditText) findViewById(R.id.regid_pwd);
		final EditText address = (EditText) findViewById(R.id.regid_address);
		final EditText mobileNo = (EditText) findViewById(R.id.regid_mn);
		Button register = (Button) findViewById(R.id.regid_register);

		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		Criteria c = new Criteria();
		provider = locationManager.getBestProvider(c, false);
		location = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean flag = true;
				String fnVal = firstName.getText().toString();
				String lnVal = lastName.getText().toString();
				String loginVal = login.getText().toString();
				String pwdVal = password.getText().toString();
				String adVal = address.getText().toString();
				String mVal = mobileNo.getText().toString();

				if (isNull(fnVal)) {
					flag = false;
					firstName.setError("First Name is required");
				} else if (isNull(lnVal)) {
					flag = false;
					lastName.setError("Last Name is required");
				} else if (isNull(loginVal)) {
					flag = false;
					login.setError("Login is required");
				} else if (isNull(pwdVal)) {
					flag = false;
					password.setError("Password is required");
				} else if (isNull(adVal)) {
					flag = false;
					address.setError("Address is required");
				} else if (isNull(mVal)) {
					flag = false;
					mobileNo.setError("Mobile No. is required");
				}

				if (flag) {
					if (location != null) {
						longitude = location.getLongitude();
						latitude = location.getLatitude();
					}

					Log.i("Lat Long ", longitude + " " + latitude);

					UserRegistrationTask task = new UserRegistrationTask();
					task.execute(new String[] { "http://www.nenosystems.co.in:8085/LBTSWeb/UserCtl?firstName="
							+ fnVal
							+ "&lastName="
							+ lnVal
							+ "&login="
							+ loginVal
							+ "&password="
							+ pwdVal
							+ "&address="
							+ adVal
							+ "&latitude="
							+ latitude
							+ "&longitude="
							+ longitude + "&operation=Save" });

				}
			}
		});

	}

	@Override
	public void onLocationChanged(Location location) {
		longitude = location.getLongitude();
		latitude = location.getLatitude();
	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	public boolean isNull(String val) {
		if (val == null || val.length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.user_registration, menu);
		return true;
	}

	private class UserRegistrationTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {
			success = "";
			JSONParser jParser = new JSONParser();
			// Getting JSON from URL
			JSONObject json = jParser.getJSONFromUrl(urls[0]);
			try {
				success = json.getString("success");
				dbfn = json.getString("firstName");
				dbln = json.getString("lastName");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return success;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.equals("true")) {
				Toast.makeText(
						getApplicationContext(),
						"Welcome " + dbfn
								+ ", You are registered successfully.",
						Toast.LENGTH_LONG).show();
				Intent intent = new Intent(UserRegistrationCtl.this,
						LoginCtl.class);
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(),
						"Login is already exist, Please choose different one.",
						Toast.LENGTH_LONG).show();
			}
		}
	}

}