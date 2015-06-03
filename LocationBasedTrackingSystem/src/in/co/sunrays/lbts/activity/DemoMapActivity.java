package in.co.sunrays.lbts.activity;

import in.co.sunrays.lbts.dao.DataDAO;
import in.co.sunrays.lbts.dto.DataDTO;
import in.co.sunrays.lbts.service.GeocodeJSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.locationbasedtrackingsystem.activity.BaseActivity;
import com.locationbasedtrackingsystem.activity.R;

public class DemoMapActivity extends BaseActivity {
	// Google Map
		private GoogleMap googleMap;
		// static final LatLng INDORE = new LatLng(22.7253, 75.8655);
		MarkerOptions markerOptions;
		LatLng latLng;
		Double latitude;
		Double longitude;
		String taskLocation;
		String location;
		int ID;
		Button find;
		Button save;
		EditText etLocation;
		DataDAO dao;
		DataDTO dto;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_demo_map);

			Intent intent = getIntent();
			taskLocation = intent.getStringExtra("TaskLocation");
			String loca = intent.getStringExtra("Task");
			String check = intent.getStringExtra("Check");
			ID = intent.getIntExtra("TaskID", 0);
			System.out.println("Demo map activity" + taskLocation);

			if (taskLocation == null && loca != null) {

				directMap();

			} else if (taskLocation != null && loca == null && check == null) {

				addFavoritePlace();
			} else {

				editFavoritePlace();

			}

		}

		// ----------------------From Favorite Place Edit--------------------//

		public void editFavoritePlace() {

			System.out.println("Edit Favourite Place method is call");

			try {
				// Loading map
				initilizeMap();
				// location();

			} catch (Exception e) {
				e.printStackTrace();
			}

			etLocation = (EditText) findViewById(R.id.addressbarGet_dma);
			etLocation.setText(taskLocation);

			find = (Button) findViewById(R.id.add_fpa);
			find.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					// Getting user input location

					location = etLocation.getText().toString();
					System.out.println("llllllllooooooooooooo" + location);

					if (location != null && !location.equals("")) {
						//new GeocoderTask().execute(location);
						Log.v("onClick", "onClick call");
						String url = "https://maps.googleapis.com/maps/api/geocode/json?";					
						
						try {
							// encoding special characters like space in the user input place
							location = URLEncoder.encode(location, "utf-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						
						String address = "address=" + location;
						
						String sensor = "sensor=false";
						
						
						// url , from where the geocoding data is fetched
						url = url + address + "&" + sensor;
						Log.v("onClick", "url is: "+url);
					//	String modifiedURL= url.toString().replace(" ", "%20");

						// Instantiating DownloadTask to get places from Google Geocoding service
						// in a non-ui thread
						DownloadTask downloadTask = new DownloadTask();
						
						// Start downloading the geocoding places
						downloadTask.execute(url);
					}
				}
			});

			// ----------------------Save Button
			// Function---------------------------//

			save = (Button) findViewById(R.id.location_dma);
			save.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						dao = new DataDAO(getApplicationContext());
						dto = new DataDTO();
						dto.setTaskLocation(location);
						dto.setLatitude(latitude);
						dto.setLongitude(longitude);
						dto.setID(ID);
						System.out.println("Save Button is Clicked" + location);
						dao.updateLocation(dto);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

		}
/**
 * 
 * ------------------------Save Data in Favourite List---------------
 */
		public void addFavoritePlace() {

			System.out.println("addFavouritePlace Location method is call");
			try {
				// Loading map
				initilizeMap();
				// location();

			} catch (Exception e) {
				e.printStackTrace();
			}

			// --------------------find Button Function----------------------//
			find = (Button) findViewById(R.id.add_fpa);
			find.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					etLocation = (EditText) findViewById(R.id.addressbarGet_dma);
					// Getting user input location
					location = etLocation.getText().toString();

					if (location != null && !location.equals("")) {
						//new GeocoderTask().execute(location);
						Log.v("onClick", "onClick call");
						String url = "https://maps.googleapis.com/maps/api/geocode/json?";					
						
						try {
							// encoding special characters like space in the user input place
							location = URLEncoder.encode(location, "utf-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						
						String address = "address=" + location;
						
						String sensor = "sensor=false";
						
						
						// url , from where the geocoding data is fetched
						url = url + address + "&" + sensor;
						Log.v("onClick", "url is: "+url);
					//	String modifiedURL= url.toString().replace(" ", "%20");

						// Instantiating DownloadTask to get places from Google Geocoding service
						// in a non-ui thread
						DownloadTask downloadTask = new DownloadTask();
						
						// Start downloading the geocoding places
						downloadTask.execute(url);
					}
				}
			});

			// --------------------save button Function----------------//

			save = (Button) findViewById(R.id.location_dma);
			save.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						dao = new DataDAO(getApplicationContext());
						dto = new DataDTO();
						dto.setTaskLocation(location);
						dto.setLatitude(latitude);
						dto.setLongitude(longitude);
						System.out.println("Save Button is Clicked" + location+" Latitude is:"+latitude+" Longitude is"+longitude);
						dao.addLocation(dto);
						Intent intent = new Intent(getApplicationContext(),FavoritePlaceActivity.class);
						startActivity(intent);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			

		}

		// ------------------------map function-------------------------//
		/**
		 * Map function when come from Daily Rountine Activity
		 */

		public void directMap() {

			System.out.println("Direct Map method is call");
			try {
				// Loading map
				initilizeMap();
				// location();

			} catch (Exception e) {
				e.printStackTrace();
			}

			// --------------------find Button Function----------------------//
			find = (Button) findViewById(R.id.add_fpa);
			find.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					etLocation = (EditText) findViewById(R.id.addressbarGet_dma);
					// Getting user input location
					 location = etLocation.getText().toString();

					if (location != null && !"".equals(location)) {
					//	new GeocoderTask().execute(location);
						Log.v("onClick", "onClick call");
						String url = "https://maps.googleapis.com/maps/api/geocode/json?";					
						
						try {
							// encoding special characters like space in the user input place
							location = URLEncoder.encode(location, "utf-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						
						String address = "address=" + location;
						
						String sensor = "sensor=false";
						
						
						// url , from where the geocoding data is fetched
						url = url + address + "&" + sensor;
						Log.v("onClick", "url is: "+url);
					//	String modifiedURL= url.toString().replace(" ", "%20");

						// Instantiating DownloadTask to get places from Google Geocoding service
						// in a non-ui thread
						DownloadTask downloadTask = new DownloadTask();
						
						// Start downloading the geocoding places
						downloadTask.execute(url);
					}
				}
			});

			// ------------------Save Location Button
			// Function----------------------//

			save = (Button) findViewById(R.id.location_dma);
			save.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						dto = new DataDTO();
						dto.setTaskLocation(location);
						System.out.println("Save Button is Clicked" + location);
						Intent intent = new Intent();
						intent.putExtra("TaskLocation", dto.getTaskLocation());
						intent.putExtra("Latitude", latitude);
						intent.putExtra("Longitude", longitude);
						setResult(RESULT_OK, intent);
						finish();

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

		}

		/**
		 * function to load map. If map is not created it will create it for you
		 * */
		private void initilizeMap() {

			if (googleMap == null) {
				googleMap = ((MapFragment) getFragmentManager().findFragmentById(
						R.id.map)).getMap();

				// check if map is created successfully or not
				if (googleMap == null) {
					Toast.makeText(getApplicationContext(),
							"Sorry! unable to create maps", Toast.LENGTH_SHORT)
							.show();
				}
				// location();
			}
		}

		
		private String downloadUrl(String strUrl) throws IOException{
	        String data = "";
	        InputStream iStream = null;
	        HttpURLConnection urlConnection = null;
	        try{
	                URL url = new URL(strUrl);


	                // Creating an http connection to communicate with url 
	                urlConnection = (HttpURLConnection) url.openConnection();

	                // Connecting to url 
	                urlConnection.connect();

	                // Reading data from url 
	                iStream = urlConnection.getInputStream();

	                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

	                StringBuffer sb  = new StringBuffer();

	                String line = "";
	                while( ( line = br.readLine())  != null){
	                        sb.append(line);
	                }

	                data = sb.toString();

	                br.close();

	        }catch(Exception e){
	                Log.d("Exception while downloading url", e.toString());
	        }finally{
	                iStream.close();
	                urlConnection.disconnect();
	        }

	        return data;
	        
		}

		
		 /** A class, to download Places from Geocoding webservice */
	    private class DownloadTask extends AsyncTask<String, Integer, String>{

	            String data = null;

	            // Invoked by execute() method of this object
	            @Override
	            protected String doInBackground(String... url) {
	                    try{                    		
	                            data = downloadUrl(url[0]);
	                    }catch(Exception e){
	                             Log.d("Background Task",e.toString());
	                    }
	                    return data;
	            }

	            // Executed after the complete execution of doInBackground() method
	            @Override
	            protected void onPostExecute(String result){
	            		
	            		ParserTask parserTask = new ParserTask();
	                    parserTask.execute(result);
	            }

	    }

	  
		class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

			JSONObject jObject;
			
			
			@Override
			protected List<HashMap<String,String>> doInBackground(String... jsonData) {
			
				List<HashMap<String, String>> places = null;			
				GeocodeJSONParser parser = new GeocodeJSONParser();
	        
		        try{
		        	jObject = new JSONObject(jsonData[0]);
		        	
		            /** Getting the parsed data as a an ArrayList */
		            places = parser.parse(jObject);
		            
		        }catch(Exception e){
		                Log.d("Exception",e.toString());
		        }
		        return places;
			}
			
			// Executed after the complete execution of doInBackground() method
			@Override
			protected void onPostExecute(List<HashMap<String,String>> list){			
				
				// Clears all the existing markers			
				googleMap.clear();
				
				for(int i=0;i<list.size();i++){
				
					// Creating a marker
		            MarkerOptions markerOptions = new MarkerOptions();
		            HashMap<String, String> hmPlace = list.get(i);
		
		             latitude = Double.parseDouble(hmPlace.get("lat"));	            
		             longitude = Double.parseDouble(hmPlace.get("lng"));
		           
		            String name = hmPlace.get("formatted_address");
		            LatLng latLng = new LatLng(latitude, longitude);
		            markerOptions.position(latLng);
		            markerOptions.title(name);

		            googleMap.addMarker(markerOptions);    
		            
		            // Locate the first location
	                if(i==0)
	                	googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
	            }            
			}
		}
		
	
		
		
		/*
		 * public void location() { Marker marker = googleMap.addMarker(new
		 * MarkerOptions() .position(INDORE).title("Indore")
		 * .icon(BitmapDescriptorFactory.defaultMarker()));
		 * 
		 * googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); //
		 * googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); //
		 * googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE); //
		 * googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN); //
		 * googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
		 * googleMap.setMyLocationEnabled(true);
		 * //googleMap.setOnMyLocationButtonClickListener(this);
		 * 
		 * // googleMap.getUiSettings().setCompassEnabled(true); //
		 * googleMap.getUiSettings().setZoomControlsEnabled(false); // latitude and
		 * longitude
		 * 
		 * double latitude = 37.422006; double longitude = -122.084095;
		 * 
		 * // create marker MarkerOptions marker = new MarkerOptions().position( new
		 * LatLng(latitude, longitude)).title("Hello Maps "); // adding marker
		 * googleMap.addMarker(marker);
		 * 
		 * }
		 * 
		 * @Override protected void onResume() { super.onResume(); initilizeMap(); }
		 */
}
