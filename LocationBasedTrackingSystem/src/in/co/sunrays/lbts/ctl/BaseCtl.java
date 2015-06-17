package in.co.sunrays.lbts.ctl;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.locationbasedtrackingsystem.activity.R;
import com.locationbasedtrackingsystem.activity.R.id;
import com.locationbasedtrackingsystem.activity.R.menu;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class BaseCtl extends Activity {

	public final static int ARESULT = 10;
	public final static int BRESULT = 20;
	public final static int CRESULT = 30;
	public final static int DRESULT = 40;
	public static int ID;
	public static String LATITUDE;
	public static String LONGITUDE;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Logout
		if (item.getItemId() == R.id.action_logout) {
			Intent intent = new Intent(BaseCtl.this, LoginCtl.class);
			startActivity(intent);
		}

		// Last Location
		if (item.getItemId() == R.id.action_location) {
			Log.i("BaseActivity Lat Long ", LATITUDE + " " + LONGITUDE);
			Geocoder geo = new Geocoder(getApplicationContext(),
					Locale.getDefault());

			if (Geocoder.isPresent()) {
				try {
					List<Address> addresses = geo.getFromLocation(
							Double.parseDouble(LATITUDE),
							Double.parseDouble(LONGITUDE), 1);
					if (addresses != null && addresses.size() > 0) {
						Address address = addresses.get(0);
						String addressText = String.format(
								"%s, %s, %s",
								// If there's a street address, add it
								address.getMaxAddressLineIndex() > 0 ? address
										.getAddressLine(0) : "",
								// Locality is usually a city
								address.getLocality(),
								// The country of the address
								address.getCountryName());
						Toast.makeText(getApplicationContext(),
								"Location : " + addressText, Toast.LENGTH_LONG)
								.show();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// Find Friend
		if (item.getItemId() == R.id.action_findfriend) {
			Intent intent = new Intent(BaseCtl.this,
					UserListCtl.class);
			startActivity(intent);
		}

		return super.onOptionsItemSelected(item);
	}

}