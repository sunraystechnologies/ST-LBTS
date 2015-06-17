package in.co.sunrays.lbts.ctl;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.locationbasedtrackingsystem.activity.R;

public class LoginCtl extends BaseCtl implements LocationListener {

	private LocationManager locationManager = null;
	private Location location = null;
	private String provider = null;
	private double longitude = 0, latitude = 0;
	private String outputData = "";
	private String id = null, lat = null, longi = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		final EditText login = (EditText) findViewById(R.id.logid_login);
		final EditText password = (EditText) findViewById(R.id.logid_password);
		Button signin = (Button) findViewById(R.id.logid_logbut);
		Button signup = (Button) findViewById(R.id.logid_register);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		Criteria c = new Criteria();
		provider = locationManager.getBestProvider(c, false);
		location = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		signin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean flag = true;

				String loginVal = login.getText().toString();
				String pwdVal = password.getText().toString();

				if (isNull(loginVal)) {
					flag = false;
					login.setError("Login is required");
				} else if (isNull(pwdVal)) {
					flag = false;
					password.setError("Password is required");
				}
				if (flag) {
					LoginTask loginTask = new LoginTask();
					loginTask
							.execute(new String[] { "http://nenosystems.co.in:8085/LBTSWeb/LoginCtl?login="
									+ loginVal
									+ "&password="
									+ pwdVal
									+ "&operation=Login" });
				}
			}
		});

		signup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginCtl.this,
						UserRegistrationCtl.class);
				startActivity(intent);
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
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	public boolean isNull(String val) {
		if (val == null || val.length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	private class LoginTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			outputData = "";
			JSONParser jParser = new JSONParser();
			// Getting JSON from URL
			Log.i("URL in Login " + urls[0], " Value");
			JSONObject json = jParser.getJSONFromUrl(urls[0]);
			try {
				outputData = json.getString("success");
				id = json.getString("id");
				lat = json.getString("latitude");
				longi = json.getString("longitude");
				Log.i("Base Lat Long " + lat + " " + longi, " BaseActivity");

				/************ Show Output on screen/activity **********/
				// output.setText(OutputData);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return outputData;
		}

		@Override
		protected void onPostExecute(String result) {

			if (result.equals("true")) {
				locationManager = (LocationManager) getApplicationContext()
						.getSystemService(Context.LOCATION_SERVICE);
				Criteria c = new Criteria();
				provider = locationManager.getBestProvider(c, false);
				location = locationManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

				Log.i("Login Post " + lat + " " + longi, " Value");

				ID = Integer.parseInt(id);
				LATITUDE = lat;
				LONGITUDE = longi;

				Log.i("Login Post " + LATITUDE + " " + LONGITUDE, " Value");

				if (location != null) {
					longitude = location.getLongitude();
					latitude = location.getLatitude();
				}

				Intent intent = new Intent(LoginCtl.this, HomePageCtl.class);
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(),
						"Invalid Login / Password", Toast.LENGTH_SHORT).show();
			}

		}

	}

}