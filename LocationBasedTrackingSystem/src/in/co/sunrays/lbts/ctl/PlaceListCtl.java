package in.co.sunrays.lbts.ctl;

import in.co.sunrays.lbts.utility.GooglePlaces;
import in.co.sunrays.lbts.utility.Place;
import in.co.sunrays.lbts.utility.PlacesList;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.locationbasedtrackingsystem.activity.R;

public class PlaceListCtl extends BaseCtl {

	private String type = null;
	private int radius;
	private double latitude;
	private double longitude;

	private ListView listView;
	private GooglePlaces googlePlaces;
	private PlacesList placesList;

	// ListItems data
	private ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String, String>>();

	// KEY Strings
	public static String KEY_REFERENCE = "reference"; // id of the place
	public static String KEY_NAME = "name"; // name of the place
	public static String KEY_VICINITY = "vicinity"; // Place area name

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_list);

		Intent intent = getIntent();
		type = intent.getStringExtra("TYPE");
		radius = intent.getIntExtra("RADIUS", 50);
		latitude = intent.getDoubleExtra("LATITUDE", 0.00);
		longitude = intent.getDoubleExtra("LONGITUDE", 0.00);

		listView = (ListView) findViewById(R.id.listView_pla);
		/**
		 * ListItem click event On selecting a listitem SinglePlaceActivity is
		 * launched
		 * */
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String reference = ((TextView) view
						.findViewById(R.id.reference)).getText().toString();

				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						SinglePlaceCtl.class);

				// Sending place refrence id to single place activity
				// place refrence id used to get "Place full details"
				in.putExtra(KEY_REFERENCE, reference);
				startActivity(in);
			}
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter1, View view1,
					int position, long id) {

				// String reference=
				// ((TextView)view1.findViewById(R.id.reference)).getText().toString();
				final String name = ((TextView) view1.findViewById(R.id.name))
						.getText().toString();
				// Log.i("reference is",reference);
				Log.i("Name is ", name);

				AlertDialog.Builder builder = new AlertDialog.Builder(
						PlaceListCtl.this);
				builder.setTitle("Confirm ADD..... ");
				builder.setMessage("Are you sure you want to add?");

				builder.setPositiveButton("ADD",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								Intent intent = new Intent();
								intent.putExtra("NEARBY", name);
								setResult(RESULT_OK, intent);
								finish();
							}
						});

				builder.setNegativeButton("NO",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								Toast.makeText(getApplicationContext(),
										"You clicked on NO", Toast.LENGTH_SHORT)
										.show();
								dialog.cancel();
							}
						});
				builder.show();
				// TODO Auto-generated method stub
				return false;
			}
		});

		// calling background Async task to load Google Places
		// After getting places from Google all the data is shown in listview
		new LoadPlaces().execute();
	}

	class LoadPlaces extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			googlePlaces = new GooglePlaces();

			try {
				// Separeate your place types by PIPE symbol "|"
				// If you want all types places make it as null
				// Check list of types supported by google
				//
				String types = type; // Listing places only cafes, restaurants

				// Radius in meters - increase this value if you don't find any
				// places
				// double radius = 1000; // 1000 meters

				// get nearest places

				System.out.println("TYPE " + type + " radius " + radius
						+ " latitude " + latitude + " longitude " + longitude);
				placesList = googlePlaces.search(latitude, longitude, radius,
						type);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					PlaceListCtl.this);

			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed Places into LISTVIEW
					 * */
					// Get json response status
					String status = placesList.status;

					// Check for all possible status
					if (status.equals("OK")) {
						// Successfully got places details
						System.out.println("TYPE " + type + " radius " + radius
								+ " latitude " + latitude + " longitude "
								+ longitude);
						if (placesList.results != null) {
							// loop through each place
							for (Place p : placesList.results) {
								HashMap<String, String> map = new HashMap<String, String>();

								// Place reference won't display in listview -
								// it will be hidden
								// Place reference is used to get
								// "place full details"
								map.put(KEY_REFERENCE, p.reference);

								// Place name
								map.put(KEY_NAME, p.name);

								Log.i("Name of place is", p.name);
								// adding HashMap to ArrayList
								placesListItems.add(map);

							}
							// list adapter
							ListAdapter adapter = new SimpleAdapter(
									PlaceListCtl.this, placesListItems,
									R.layout.list_item, new String[] {
											KEY_REFERENCE, KEY_NAME },
									new int[] { R.id.reference, R.id.name });

							// Adding data into listview
							listView.setAdapter(adapter);

						}
					} else if (status.equals("ZERO_RESULTS")) {
						// Zero results found
						alertDialog
								.setMessage("Sorry no places found. Try to change the types of places");
					} else if (status.equals("UNKNOWN_ERROR")) {
						alertDialog.setMessage("Sorry unknown error occured.");
					} else if (status.equals("OVER_QUERY_LIMIT")) {
						alertDialog
								.setMessage("Sorry query limit to google places is reached");
					} else if (status.equals("REQUEST_DENIED")) {
						alertDialog
								.setMessage("Sorry error occured. Request is denied");
					} else if (status.equals("INVALID_REQUEST")) {
						alertDialog
								.setMessage("Sorry error occured. Invalid Request");
					} else {
						alertDialog.setMessage("Sorry error occured.");
					}
				}
			});
		}

	}

}