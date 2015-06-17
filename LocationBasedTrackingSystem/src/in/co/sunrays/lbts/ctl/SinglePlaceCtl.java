package in.co.sunrays.lbts.ctl;

import in.co.sunrays.lbts.utility.GooglePlaces;
import in.co.sunrays.lbts.utility.PlaceDetails;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.locationbasedtrackingsystem.activity.R;

public class SinglePlaceCtl extends BaseCtl {

	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Google Places
	private GooglePlaces googlePlaces;

	// Place Details
	private PlaceDetails placeDetails;

	// Progress dialog
	private ProgressDialog pDialog;

	// KEY Strings
	public static String KEY_REFERENCE = "reference"; // id of the place

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_place);

		Intent i = getIntent();

		// Place referece id
		String reference = i.getStringExtra(KEY_REFERENCE);

		// Calling a Async Background thread
		new LoadSinglePlaceDetails().execute(reference);
	}

	/**
	 * Background Async Task to Load Google places
	 * */
	class LoadSinglePlaceDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SinglePlaceCtl.this);
			pDialog.setMessage("Loading profile ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Profile JSON
		 * */
		protected String doInBackground(String... args) {
			String reference = args[0];

			// creating Places class object
			googlePlaces = new GooglePlaces();

			// Check if used is connected to Internet
			try {
				placeDetails = googlePlaces.getPlaceDetails(reference);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					final AlertDialog.Builder alert = new AlertDialog.Builder(
							SinglePlaceCtl.this);
					/**
					 * Updating parsed Places into LISTVIEW
					 * */
					if (placeDetails != null) {
						String status = placeDetails.status;

						// check place deatils status
						// Check for all possible status
						if (status.equals("OK")) {
							if (placeDetails.result != null) {
								String name = placeDetails.result.name;
								String address = placeDetails.result.formatted_address;
								String phone = placeDetails.result.formatted_phone_number;
								String latitude = Double
										.toString(placeDetails.result.geometry.location.lat);
								String longitude = Double
										.toString(placeDetails.result.geometry.location.lng);

								Log.d("Place ", name + address + phone
										+ latitude + longitude);

								// Displaying all the details in the view
								// single_place.xml
								TextView lbl_name = (TextView) findViewById(R.id.name);
								TextView lbl_address = (TextView) findViewById(R.id.address);
								TextView lbl_phone = (TextView) findViewById(R.id.phone);
								TextView lbl_location = (TextView) findViewById(R.id.location);

								// Check for null data from google
								// Sometimes place details might missing
								name = name == null ? "Not present" : name; // if
																			// name
																			// is
																			// null
																			// display
																			// as
																			// "Not present"
								address = address == null ? "Not present"
										: address;
								phone = phone == null ? "Not present" : phone;
								latitude = latitude == null ? "Not present"
										: latitude;
								longitude = longitude == null ? "Not present"
										: longitude;

								lbl_name.setText(name);
								lbl_address.setText(address);
								lbl_phone.setText(Html
										.fromHtml("<b>Phone:</b> " + phone));
								lbl_location.setText(Html
										.fromHtml("<b>Latitude:</b> "
												+ latitude
												+ ", <b>Longitude:</b> "
												+ longitude));
							}
						} else if (status.equals("ZERO_RESULTS")) {
							alert.setMessage("Sorry no place found.");
						} else if (status.equals("UNKNOWN_ERROR")) {
							alert.setMessage("Sorry unknown error occured.");
						} else if (status.equals("OVER_QUERY_LIMIT")) {
							alert.setMessage("Sorry query limit to google places is reached");
						} else if (status.equals("REQUEST_DENIED")) {
							alert.setMessage("Sorry error occured. Request is denied");
						} else if (status.equals("INVALID_REQUEST")) {
							alert.setMessage("Sorry error occured. Invalid Request");
						} else {
							alert.setMessage("Sorry error occured.");
						}
					} else {
						alert.setMessage("Sorry error occured.");
					}

				}
			});

		}

	}

}
