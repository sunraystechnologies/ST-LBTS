package in.co.sunrays.lbts.ctl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.locationbasedtrackingsystem.activity.R;
import com.locationbasedtrackingsystem.activity.R.id;
import com.locationbasedtrackingsystem.activity.R.layout;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class UserListCtl extends BaseCtl {

	ListView listView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);
		listView = (ListView) findViewById(R.id.user_list_view);
		UserListTask loginTask = new UserListTask();
		loginTask.execute(new ArrayList());
	}

	private class UserListTask extends AsyncTask<List, Void, List> {

		@Override
		protected List doInBackground(List... params) {
			List<String> list = new ArrayList<String>();
			JSONParser jParser = new JSONParser();

			JSONObject json = jParser
					.getJSONFromUrl("http://www.nenosystems.co.in:8085/LBTSWeb/UserListCtl");
			try {
				JSONArray array = json.getJSONArray("UserList");

				Geocoder geo = new Geocoder(getApplicationContext(),
						Locale.getDefault());

				for (int i = 0; i < array.length(); i++) {
					JSONObject data = array.getJSONObject(i);
					if (Geocoder.isPresent()) {
						try {
							List<Address> addresses = geo
									.getFromLocation(Double.parseDouble(data
											.getString("latitude")), Double
											.parseDouble(data
													.getString("longitude")), 1);
							if (addresses != null && addresses.size() > 0) {
								Address address = addresses.get(0);
								String addressText = String
										.format("%s, %s, %s",
										// If there's a street address,
										// add it
												address.getMaxAddressLineIndex() > 0 ? address
														.getAddressLine(0) : "",
												// Locality is usually a city
												address.getLocality(),
												// The country of the address
												address.getCountryName());

								list.add(data.getString("firstName") + " "
										+ data.getString("lastName") + " "
										+ addressText);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return list;
		}

		@Override
		protected void onPostExecute(List result) {
			ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
					getApplicationContext(), R.layout.list_black_text,
					R.id.list_content, result);
			listView.setAdapter(arrayAdapter);
		}

	}
}