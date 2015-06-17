package in.co.sunrays.lbts.ctl;

import in.co.sunrays.lbts.model.DataModel;
import in.co.sunrays.lbts.utility.LocationReminder;
import in.co.sunrays.lbts.utility.ReminderAlarm;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.locationbasedtrackingsystem.activity.R;

public class DailyRoutineCtl extends BaseCtl {

	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int tHour;
	private int mMinute;
	private String AM_PM;
	static final int DATE_DIALOG_ID = 1;
	static final int TIME_DIALOG_ID = 0;
	private Calendar c;
	private EditText date_1;
	private EditText time_1;
	private Context mcontext = this;
	private String near;
	private static int radius;
	private EditText tName;
	private Spinner spinner1;
	private Spinner spinner2;
	private Spinner spinner3;
	private Button save;
	private Button cancel;
	public static int notificationCount;
	// -------------for get only---------------//
	private int taskID;
	private String taskName;
	private String taskDate;
	private String taskLocation;
	private String taskRadius;
	private String taskNearby;
	private String taskTime;
	// ---------------------adapter---------------------//
	private ArrayAdapter<String> adapter1;

	/**
	 * Parameter For GPS Tracking
	 */
	private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 1; // in
																		// Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATE = 5000; // in
																	// Milliseconds

	private static long POINT_RADIUS = 1000; // in Meters
	private static final long PROX_ALERT_EXPIRATION = -1;

	private static final String POINT_LATITUDE_KEY = "POINT_LATITUDE_KEY";
	private static final String POINT_LONGITUDE_KEY = "POINT_LONGITUDE_KEY";

	private static final String PROX_ALERT_INTENT = "in.co.sunrays.lbts.activity";
	private static final NumberFormat nf = new DecimalFormat("##.########");
	private static double latitude;
	private static double longitude;
	private LocationManager locationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daily_routine);

		mcontext = getApplicationContext();

		Intent intent = getIntent();
		taskID = intent.getIntExtra("TaskID", 0);
		taskName = intent.getStringExtra("TaskName");
		taskDate = intent.getStringExtra("TaskDate");
		taskLocation = intent.getStringExtra("TaskLocation");
		taskNearby = intent.getStringExtra("TaskNearby");
		taskTime = intent.getStringExtra("TaskTime");
		Integer i = intent.getIntExtra("TaskRadius", 0);
		taskRadius = i.toString();
		if (taskName == null) {
			addData();
		} else {
			editData();
		}
	}

	/**
	 * ---------method to edit data on database--------
	 */
	public void editData() {
		tName = (EditText) findViewById(R.id.tasknametxt_dra);
		date_1 = (EditText) findViewById(R.id.datetxt_dra);
		time_1 = (EditText) findViewById(R.id.time_dra);

		tName.setText(taskName);
		date_1.setText(taskDate);
		time_1.setText(taskTime);
		// --------------------spinner 1----------------//
		spinner1 = (Spinner) findViewById(R.id.spinner_location1);
		String[] state1 = new String[1];
		state1[0] = taskLocation;
		adapter1 = new ArrayAdapter<String>(getApplicationContext(),
				R.layout.activity_spinner, state1);
		spinner1.setAdapter(adapter1);
		spinner1.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				setLocationSpinner();
				return false;
			}
		});
		// ---------------spinner 2-----------------//
		spinner2 = (Spinner) findViewById(R.id.spinner_radius);
		String[] state2 = new String[1];
		state2[0] = taskRadius;
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.activity_spinner, state2);
		spinner2.setAdapter(adapter2);
		spinner2.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				setRadiusSpinner();
				return false;
			}
		});
		// -------------spinner 3------------------//
		spinner3 = (Spinner) findViewById(R.id.spinner_searchnearby);
		String[] state3 = new String[1];
		state3[0] = taskNearby;
		ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.activity_spinner, state3);
		spinner3.setAdapter(adapter3);
		spinner3.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				setSearchSpinner();
				return false;
			}
		});
		date_1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onDateClicked(v);
			}
		});
		// ------------------Time--------------------------------//
		time_1 = (EditText) findViewById(R.id.time_dra);
		time_1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onTimeClicked(v);
			}
		});
		/**
		 * save data into database after update
		 */
		save = (Button) findViewById(R.id.save_dra);
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tName = (EditText) findViewById(R.id.tasknametxt_dra);
				date_1 = (EditText) findViewById(R.id.datetxt_dra);
				time_1 = (EditText) findViewById(R.id.time_dra);

				String taskName = tName.getText().toString();
				String taskDate = date_1.getText().toString();
				String taskTime = time_1.getText().toString();

				DataModel model = new DataModel(getApplicationContext());
				model.setId(taskID);
				model.setTaskName(taskName);
				model.setTaskDate(taskDate);
				model.setTaskLocation("Indore");
				model.setTaskRadius(radius);
				model.setTaskNearby(near);
				model.setTaskTime(taskTime);

				try {
					model.update();
					Toast.makeText(getApplicationContext(),
							"Recored Successfuly Update", Toast.LENGTH_LONG)
							.show();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Any Exception ",
							Toast.LENGTH_LONG).show();
				}
			}
		});
		// -----------------------Cancel Edit Data-----------------//
		cancel = (Button) findViewById(R.id.cancel_dra);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						UpComingCtl.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * -----------method to add data in database---------
	 */
	public void addData() {
		setLocationSpinner();
		setRadiusSpinner();
		setSearchSpinner();
		// --------------Date & Time-------------------------------//
		c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		// ----------------Date---------------------------------//
		date_1 = (EditText) findViewById(R.id.datetxt_dra);
		date_1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onDateClicked(v);
			}
		});
		// ------------------Time--------------------------------//
		time_1 = (EditText) findViewById(R.id.time_dra);
		time_1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onTimeClicked(v);
			}
		});

		// -----------------------take values from user------------------//
		save = (Button) findViewById(R.id.save_dra);
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tName = (EditText) findViewById(R.id.tasknametxt_dra);
				date_1 = (EditText) findViewById(R.id.datetxt_dra);
				time_1 = (EditText) findViewById(R.id.time_dra);
				taskName = tName.getText().toString();
				taskDate = date_1.getText().toString();
				taskTime = time_1.getText().toString();

				DataModel model = new DataModel(getApplicationContext());
				model.setTaskName(taskName);
				model.setTaskDate(taskDate);
				model.setTaskLocation(taskLocation);
				model.setTaskRadius(radius);
				model.setTaskNearby(near);
				model.setTaskTime(taskTime);

				try {
					model.add();
					Toast.makeText(getApplicationContext(),
							"Recored Successfuly added", Toast.LENGTH_LONG)
							.show();
					tName.setText("");
					date_1.setText("");
					time_1.setText("");
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Any Exception ",
							Toast.LENGTH_LONG).show();
				}
				alarmService();
			}
		});

		// -----------------------Cancel Edit Data-----------------//
		cancel = (Button) findViewById(R.id.cancel_dra);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						UpComingCtl.class);
				startActivity(intent);
			}
		});
	}

	public void onTimeClicked(View view) {
		showDialog(TIME_DIALOG_ID);
	}

	public void onDateClicked(View view) {
		showDialog(DATE_DIALOG_ID);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
					false);
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return super.onCreateDialog(id);
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear + 1;
			mDay = dayOfMonth;
			// set selected date into Text View
			date_1.setText(new StringBuilder().append(mMonth).append("-")
					.append(mDay).append("-").append(mYear).append(" "));
		}
	};

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// mHour = hourOfDay;
			mMinute = minute;
			tHour = hourOfDay;
			if (hourOfDay > 12) {
				mHour = hourOfDay - 12;
				AM_PM = "PM";
			} else {
				mHour = hourOfDay;
				AM_PM = "AM";
			}
			// set selected date into Text View
			time_1.setText(new StringBuilder().append(mHour).append(":")
					.append(mMinute).append(" ").append(AM_PM).append(" "));
		}
	};

	public void setLocationSpinner() {
		spinner1 = (Spinner) findViewById(R.id.spinner_location1);
		String[] state = new String[3];
		state[0] = "Select";
		state[1] = "MAP";
		state[2] = "FAVOURITE PLACE";

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.activity_spinner, state);
		spinner1.setAdapter(adapter);

		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent,
					View itemSelected, int selectedItemPosition, long selected) {
				int spinner_value;
				String location = null;
				String loca = "notnull";
				int id = 0;
				spinner_value = selectedItemPosition;
				if (spinner_value == 1) {
					Intent intent = new Intent(DailyRoutineCtl.this,
							DemoMapCtl.class);
					intent.putExtra("TaskLocation", location);
					intent.putExtra("Task", loca);
					intent.putExtra("TaskID", id);
					startActivityForResult(intent, ARESULT);
				} else if (spinner_value == 2) {
					Intent intent = new Intent(DailyRoutineCtl.this,
							FavoritePlaceCtl.class);
					startActivityForResult(intent, BRESULT);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	public void setRadiusSpinner() {
		spinner2 = (Spinner) findViewById(R.id.spinner_radius);
		String[] state = new String[7];
		state[0] = "Range";
		state[1] = "50";
		state[2] = "250";
		state[3] = "500";
		state[4] = "1000";
		state[5] = "2000";
		state[6] = "3000";
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.activity_spinner, state);
		spinner2.setAdapter(adapter);
		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent,
					View itemSelected, int position, long selected) {
				Object item = parent.getItemAtPosition(position);
				if (position >= 1) {
					String rad = item.toString();
					radius = Integer.parseInt(rad);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	public void setSearchSpinner() {
		spinner3 = (Spinner) findViewById(R.id.spinner_searchnearby);
		String[] state = new String[10];
		state[0] = "List";
		state[1] = "hindu_temple";
		state[2] = "restaurant";
		state[3] = "atm";
		state[4] = "shopping_mall";
		state[5] = "hospital";
		state[6] = "cafe";
		state[7] = "train_station";
		state[8] = "police";
		state[9] = "movie_theater";

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.activity_spinner, state);
		spinner3.setAdapter(adapter);
		spinner3.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent,
					View itemSelected, int position, long selected) {
				Object item = parent.getItemAtPosition(position);
				if (position >= 1) {
					near = item.toString();
					Intent intent = new Intent(getApplicationContext(),
							PlaceListCtl.class);
					intent.putExtra("TYPE", near);
					intent.putExtra("RADIUS", radius);
					intent.putExtra("LATITUDE", latitude);
					intent.putExtra("LONGITUDE", longitude);
					startActivityForResult(intent, CRESULT);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	/**
	 * Check Result Run for activity
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {

		case ARESULT:

			if (resultCode == RESULT_OK) {
				Toast.makeText(getApplicationContext(),
						"You are Return in DailyRoutineActivity",
						Toast.LENGTH_LONG).show();

				taskLocation = data.getStringExtra("TaskLocation");
				latitude = data.getDoubleExtra("Latitude", 0.00);
				longitude = data.getDoubleExtra("Longitude", 0.00);
				// --------------------spinner 1----------------//
				spinner1 = (Spinner) findViewById(R.id.spinner_location1);
				String[] state1 = new String[1];
				state1[0] = taskLocation;

				adapter1 = new ArrayAdapter<String>(getApplicationContext(),
						R.layout.activity_spinner, state1);
				spinner1.setAdapter(adapter1);

				spinner1.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						setLocationSpinner();
						return false;
					}
				});
			}
			break;

		case BRESULT:
			if (resultCode == RESULT_OK) {
				Toast.makeText(getApplicationContext(),
						"You are Return in DailyRoutineActivity",
						Toast.LENGTH_LONG).show();
				taskLocation = data.getStringExtra("TaskLocation");
				latitude = data.getDoubleExtra("latitude", 0.00);
				longitude = data.getDoubleExtra("longitude", 0.00);
				// --------------------spinner 1----------------//
				spinner1 = (Spinner) findViewById(R.id.spinner_location1);
				String[] state1 = new String[1];
				state1[0] = taskLocation;
				adapter1 = new ArrayAdapter<String>(getApplicationContext(),
						R.layout.activity_spinner, state1);
				spinner1.setAdapter(adapter1);
				spinner1.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						setLocationSpinner();
						return false;
					}
				});
			}
			break;

		case CRESULT:
			if (resultCode == RESULT_OK) {
				taskNearby = data.getStringExtra("NEARBY");
				System.out.println("Near by Place is " + taskNearby);
				spinner3 = (Spinner) findViewById(R.id.spinner_searchnearby);
				String[] state = new String[1];
				state[0] = taskNearby;
				adapter1 = new ArrayAdapter<String>(getApplicationContext(),
						R.layout.activity_spinner, state);
				spinner3.setAdapter(adapter1);
				spinner3.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						setSearchSpinner();
						return false;
					}
				});
			}

		default:
			break;
		}
	}

	/**
	 * ----------------Alarm Service by time------------------
	 */
	public void alarmService() {
		notificationCount = notificationCount + 1;
		String dateTime = mYear + "-" + mMonth + "-" + mDay + " " + tHour + "-"
				+ mMinute;
		System.out.println("Helllllllooooooooo  Date" + dateTime);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh-mm");
		Date dt = null;
		try {
			dt = df.parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long when = dt.getTime();
		AlarmManager mgr = (AlarmManager) mcontext
				.getSystemService(Context.ALARM_SERVICE);
		Intent notificationIntent = new Intent(mcontext, ReminderAlarm.class);
		startService(notificationIntent);
		notificationIntent.putExtra("TaskName", taskName);
		notificationIntent.putExtra("NotifyCount", notificationCount);
		PendingIntent pi = PendingIntent.getBroadcast(mcontext,
				notificationCount, notificationIntent, 0);
		mgr.set(AlarmManager.RTC_WAKEUP, when, pi);
		// ---------------Calling reminder for location----------//
		alarmReminder();
	}

	/**
	 * -------Alarm Service by Date-------------
	 */
	public void alarmReminder() {
		String dateTime = mYear + "-" + mMonth + "-" + mDay;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dt = null;
		try {
			dt = df.parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long when = dt.getTime();
		AlarmManager mgr = (AlarmManager) mcontext
				.getSystemService(Context.ALARM_SERVICE);
		Intent notificationIntent = new Intent(mcontext, LocationReminder.class);
		startService(notificationIntent);
		notificationIntent.putExtra("TaskName", taskName);
		notificationIntent.putExtra("TaskLocation", taskLocation);
		notificationIntent.putExtra("NotifyCount", notificationCount);
		PendingIntent pi = PendingIntent.getBroadcast(mcontext,
				notificationCount, notificationIntent, 0);
		mgr.set(AlarmManager.RTC_WAKEUP, when, pi);
		Toast.makeText(mcontext, "Your Reminder Activated", Toast.LENGTH_LONG)
				.show();
		// ---------------call to GPS Service-------------//
		gpsTracking();
	}

	/**
	 * Code for Gps tracking
	 */
	public void gpsTracking() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE,
				new MyLocationListener());
		saveProximityAlertPoint();
	}

	private void saveProximityAlertPoint() {
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location == null) {
			Toast.makeText(this, "No last known location. Aborting...",
					Toast.LENGTH_LONG).show();
			return;
		}
		saveCoordinatesInPreferences((float) latitude, (float) longitude);
		addProximityAlert(latitude, longitude);
	}

	private void addProximityAlert(double latitude, double longitude) {
		Intent intent = new Intent(PROX_ALERT_INTENT);
		PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0,
				intent, 0);
		POINT_RADIUS = radius;
		locationManager.addProximityAlert(latitude, // the latitude of the
													// central point of the
													// alert region
				longitude, // the longitude of the central point of the alert
							// region
				POINT_RADIUS, // the radius of the central point of the alert
								// region, in meters
				PROX_ALERT_EXPIRATION, // time for this proximity alert, in
										// milliseconds, or -1 to indicate no
										// expiration
				proximityIntent // will be used to generate an Intent to fire
								// when entry to or exit from the alert region
								// is detected
				);

		IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
		registerReceiver(new LocationReminder(), filter);
	}

	private void populateCoordinatesFromLastKnownLocation() {
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		}
	}

	private void saveCoordinatesInPreferences(float latitude, float longitude) {
		SharedPreferences prefs = this.getSharedPreferences(getClass()
				.getSimpleName(), Context.MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor = prefs.edit();
		prefsEditor.putFloat(POINT_LATITUDE_KEY, latitude);
		prefsEditor.putFloat(POINT_LONGITUDE_KEY, longitude);
		prefsEditor.commit();
	}

	private Location retrievelocationFromPreferences() {
		SharedPreferences prefs = this.getSharedPreferences(getClass()
				.getSimpleName(), Context.MODE_PRIVATE);
		Location location = new Location("POINT_LOCATION");
		location.setLatitude(prefs.getFloat(POINT_LATITUDE_KEY, 0));
		location.setLongitude(prefs.getFloat(POINT_LONGITUDE_KEY, 0));
		return location;
	}

	public class MyLocationListener implements LocationListener {
		public void onLocationChanged(Location location) {
			Location pointLocation = retrievelocationFromPreferences();
			float distance = location.distanceTo(pointLocation);
			Toast.makeText(getApplicationContext(),
					"Distance from Point:" + distance, Toast.LENGTH_LONG)
					.show();
		}

		public void onStatusChanged(String s, int i, Bundle b) {
		}

		public void onProviderDisabled(String s) {
		}

		public void onProviderEnabled(String s) {
		}
	}

}