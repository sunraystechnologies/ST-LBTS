package in.co.sunrays.lbts.model;

import in.co.sunrays.lbts.adapter.DBAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class DataModel {

	public Context context;
	public static final String TABLE_NAME = "dailyRoutineData";
	public static final String TABLE_NAME1 = "favouritePlace";

	public static final String TASK_ID = "ID";
	public static final String TASK_NAME = "taskName";
	public static final String TASK_DATE = "taskDate";
	public static final String TASK_LOCATION = "taskLocation";
	public static final String TASK_RADIUS = "taskRadius";
	public static final String TASK_NEARBY = "taskNearby";
	public static final String TASK_TIME = "taskTime";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";

	private long id;
	private String taskName;
	private String taskDate;
	private String taskLocation;
	private int taskRadius;
	private String taskNearby;
	private String taskTime;
	private double latitude;
	private double longitude;
	private Date date1;
	private Date date2;
	private SimpleDateFormat dateFormat;
	private String old;
	private String current;

	public DataModel() {
	}

	public DataModel(Context context) {
		this.context = context;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(String taskDate) {
		this.taskDate = taskDate;
	}

	public String getTaskLocation() {
		return taskLocation;
	}

	public void setTaskLocation(String taskLocation) {
		this.taskLocation = taskLocation;
	}

	public int getTaskRadius() {
		return taskRadius;
	}

	public void setTaskRadius(int taskRadius) {
		this.taskRadius = taskRadius;
	}

	public String getTaskNearby() {
		return taskNearby;
	}

	public void setTaskNearby(String taskNearby) {
		this.taskNearby = taskNearby;
	}

	public String getTaskTime() {
		return taskTime;
	}

	public void setTaskTime(String taskTime) {
		this.taskTime = taskTime;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void add() throws Exception {
		ContentValues values = new ContentValues();
		values.put(TASK_NAME, taskName);
		values.put(TASK_DATE, taskDate);
		values.put(TASK_LOCATION, taskLocation);
		values.put(TASK_RADIUS, taskRadius);
		values.put(TASK_NEARBY, taskNearby);
		values.put(TASK_TIME, taskTime);
		DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
		adapter.createDataBase();
		adapter.openDataBase();
		adapter.addContact(TABLE_NAME, null, values);
		adapter.close();
	}

	public void addLocation() throws Exception {
		ContentValues values = new ContentValues();
		values.put(TASK_LOCATION, taskLocation);
		values.put(LATITUDE, latitude);
		values.put(LONGITUDE, longitude);
		DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
		adapter.createDataBase();
		adapter.openDataBase();
		adapter.addContact(TABLE_NAME1, null, values);
		adapter.close();
	}

	public void updateLocation() throws Exception {
		ContentValues values = new ContentValues();
		values.put(TASK_LOCATION, taskLocation);
		values.put(LATITUDE, latitude);
		values.put(LONGITUDE, longitude);
		DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
		adapter.createDataBase();
		adapter.openDataBase();
		adapter.updateRecordsInDB(TABLE_NAME1, values, TASK_ID + "=" + id, null);
		adapter.close();
	}

	public void update() throws Exception {
		ContentValues values = new ContentValues();
		values.put(TASK_NAME, taskName);
		values.put(TASK_DATE, taskDate);
		values.put(TASK_LOCATION, taskLocation);
		values.put(TASK_RADIUS, taskRadius);
		values.put(TASK_NEARBY, taskNearby);
		values.put(TASK_TIME, taskTime);
		DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
		adapter.createDataBase();
		adapter.openDataBase();
		adapter.updateRecordsInDB(TABLE_NAME, values, TASK_ID + "=" + id, null);
		adapter.close();
	}

	public void delete() throws Exception {
		String query = TABLE_NAME + " where " + TASK_ID + " ='" + id + "' ";
		Log.i("Query " + query, "Query");
		DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
		adapter.createDataBase();
		adapter.openDataBase();
		adapter.deleteRecordInDB(query, null, null);
		adapter.close();
	}

	public void deleteLocation() throws Exception {
		String query = TABLE_NAME1 + " where " + TASK_ID + " ='" + id + "' ";
		Log.i("Query " + query, "Query");
		DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
		adapter.createDataBase();
		adapter.openDataBase();
		adapter.deleteRecordInDB(query, null, null);
		adapter.close();
	}

	public ArrayList getLocation() {
		ArrayList<DataModel> list = new ArrayList<DataModel>();
		try {
			String query = "select * from " + TABLE_NAME1 + " ";
			Log.i("Query " + query, "Select Query");
			DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
			adapter.createDataBase();
			adapter.openDataBase();
			Cursor cursor = adapter.selectRecordsFromDB(query, null);
			while (cursor.moveToNext()) {
				DataModel model = new DataModel();
				model.setId(cursor.getLong(0));
				model.setTaskLocation(cursor.getString(1));
				model.setLatitude(cursor.getDouble(2));
				model.setLongitude(cursor.getDouble(3));
				list.add(model);
			}
			cursor.close();
			adapter.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}

	public ArrayList getPassedTask() {
		ArrayList<DataModel> list = new ArrayList<DataModel>();
		try {
			String query = "select ID,taskName,taskDate,taskTime from "
					+ TABLE_NAME + " ";
			Log.i("Query " + query, "Select Query");
			DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
			adapter.createDataBase();
			adapter.openDataBase();
			Cursor cursor = adapter.selectRecordsFromDB(query, null);
			while (cursor.moveToNext()) {
				DataModel dto = new DataModel();
				String date = cursor.getString(2);
				String time = cursor.getString(3);
				String timeDate = date + " " + time;
				dateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm a");
				// -------------------Current Date---------------------//
				Calendar calendar = Calendar.getInstance();
				current = dateFormat.format(calendar.getTime());
				date2 = dateFormat.parse(current);
				// ---------------Database Date----------------------//
				dateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm a");
				date1 = dateFormat.parse(timeDate);
				old = dateFormat.format(date1);
				if (date2.after(date1)) {
					dto.setId(cursor.getLong(0));
					dto.setTaskName(cursor.getString(1));
					dto.setTaskDate(date);
					dto.setTaskTime(time);
					list.add(dto);
				}
			}
			cursor.close();
			adapter.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}

	public ArrayList getUpcomingTask() {
		ArrayList<DataModel> list = new ArrayList<DataModel>();
		try {
			String query = "select * from " + TABLE_NAME + " ";
			Log.i("Query " + query, "Select Query");
			DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
			adapter.createDataBase();
			adapter.openDataBase();
			Cursor cursor = adapter.selectRecordsFromDB(query, null);
			while (cursor.moveToNext()) {
				DataModel model = new DataModel();
				String date = cursor.getString(2);
				String time = cursor.getString(6);
				Log.i("Timedate in Upcoming ... " + cursor.getInt(0) + " "
						+ cursor.getString(1) + " " + cursor.getString(2),
						" Data ");
				String timeDate = date + " " + time;
				dateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm");
				// -------------------Current Date---------------------//
				Calendar calendar = Calendar.getInstance();
				current = dateFormat.format(calendar.getTime());
				date2 = dateFormat.parse(current);
				// ---------------Database Date----------------------//
				dateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm");
				date1 = dateFormat.parse(timeDate);
				old = dateFormat.format(date1);
				if (date2.before(date1)) {
					model.setId(cursor.getLong(0));
					model.setTaskName(cursor.getString(1));
					model.setTaskDate(date);
					model.setTaskLocation(cursor.getString(3));
					model.setTaskRadius(cursor.getInt(4));
					model.setTaskNearby(cursor.getString(5));
					model.setTaskTime(time);
					list.add(model);
				}
			}
			cursor.close();
			adapter.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
}