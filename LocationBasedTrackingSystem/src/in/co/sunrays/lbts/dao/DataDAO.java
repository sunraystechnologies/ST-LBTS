package in.co.sunrays.lbts.dao;

import in.co.sunrays.lbts.database.DBAdapter;
import in.co.sunrays.lbts.dto.DataDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class DataDAO {

	public Context context;
	Date date1;
	Date date2;
	SimpleDateFormat dateFormat;
	String old;
	String current;

	public DataDAO(Context con) {
		this.context = con;
	}

	public void add(DataDTO dto) throws Exception {

		ContentValues values = new ContentValues();
		values.put(DataDTO.TASK_NAME, dto.getTaskName());
		values.put(DataDTO.TASK_DATE, dto.getTaskDate());
		values.put(DataDTO.TASK_LOCATION, dto.getTaskLocation());
		values.put(DataDTO.TASK_RADIUS, dto.getTaskRadius());
		values.put(DataDTO.TASK_NEARBY, dto.getTaskNearby());
		values.put(DataDTO.TASK_TIME, dto.getTaskTime());

		Log.i("Date time in add ", dto.getTaskDate() + " " + dto.getTaskTime());
		DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
		adapter.createDataBase();
		adapter.openDataBase();
		adapter.addContact(DataDTO.TABLE_NAME, null, values);
		adapter.close();

	}

	public void addLocation(DataDTO dto) throws Exception {
		Log.i("Daaaaaaaaaaataaaaaaaaaaaaaaaaaaaaaaaa", dto.getTaskLocation());
		ContentValues values = new ContentValues();
		values.put(DataDTO.TASK_LOCATION, dto.getTaskLocation());
		values.put(DataDTO.Latitude, dto.getLatitude());
		values.put(DataDTO.Longitude, dto.getLongitude());
		System.out.println("DataDao add Location method"
				+ dto.getTaskLocation());
		DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
		adapter.createDataBase();
		adapter.openDataBase();
		adapter.addContact(DataDTO.TABLE_NAME1, null, values);
		adapter.close();
	}

	public void updateLocation(DataDTO dto) throws Exception {
		ContentValues values = new ContentValues();
		values.put(DataDTO.TASK_LOCATION, dto.getTaskLocation());
		values.put(DataDTO.Latitude, dto.getLatitude());
		values.put(DataDTO.Longitude, dto.getLongitude());

		System.out.println("Hellllllllllllllllllllloooooooooooooooooooo"
				+ dto.getID());
		DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
		adapter.createDataBase();
		adapter.openDataBase();
		adapter.updateRecordsInDB(DataDTO.TABLE_NAME1, values, DataDTO.TASK_ID
				+ "=" + dto.getID(), null);
		adapter.close();

	}

	public void update(DataDTO dto) throws Exception {
		/*
		 * String query = DataDTO.TABLE_NAME + " set "+ DataDTO.TASK_NAME +
		 * " = '"+ dto.getTaskName().trim() +"' , "+ DataDTO.TASK_DATE + " = '"
		 * + dto.getTaskDate().trim()+"' , " + DataDTO.TASK_LOCATION + " = '" +
		 * dto.getTaskLocation().trim() + "' , "+ DataDTO.TASK_RADIUS + " = " +
		 * dto.getTaskRadius() + " , " + DataDTO.TASK_NEARBY + " = '" +
		 * dto.getTaskNearby().trim()+ "' , "+ DataDTO.TASK_TIME + " = '" +
		 * dto.getTaskTime().trim() +"' where " + DataDTO.TASK_ID + " = " +
		 * dto.getID();
		 */
		// Log.i("query ", query);

		ContentValues values = new ContentValues();
		values.put(DataDTO.TASK_NAME, dto.getTaskName());
		values.put(DataDTO.TASK_DATE, dto.getTaskDate());
		values.put(DataDTO.TASK_LOCATION, dto.getTaskLocation());
		values.put(DataDTO.TASK_RADIUS, dto.getTaskRadius());
		values.put(DataDTO.TASK_NEARBY, dto.getTaskNearby());
		values.put(DataDTO.TASK_TIME, dto.getTaskTime());

		System.out.println("Hellllllllllllllllllllloooooooooooooooooooo"
				+ dto.getID());

		DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
		adapter.createDataBase();
		adapter.openDataBase();
		adapter.updateRecordsInDB(DataDTO.TABLE_NAME, values, DataDTO.TASK_ID
				+ "=" + dto.getID(), null);
		adapter.close();

	}

	public void delete(DataDTO dto) throws Exception {

		String query = DataDTO.TABLE_NAME + " where " + DataDTO.TASK_ID + " ='"
				+ dto.getID() + "' ";
		// String query = "delete from doctor where  id = 1";
		Log.i("Query " + query, "Query");

		DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
		adapter.createDataBase();
		adapter.openDataBase();
		adapter.deleteRecordInDB(query, null, null);
		adapter.close();

	}

	public void deleteLocation(DataDTO dto) throws Exception {
		String query = DataDTO.TABLE_NAME1 + " where " + DataDTO.TASK_ID
				+ " ='" + dto.getID() + "' ";
		// String query = "delete from doctor where  id = 1";
		Log.i("Query " + query, "Query");

		DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
		adapter.createDataBase();
		adapter.openDataBase();
		adapter.deleteRecordInDB(query, null, null);
		adapter.close();
	}

	public ArrayList getLocation() {

		ArrayList<DataDTO> list = new ArrayList<DataDTO>();
		try {
			String query = "select * from " + DataDTO.TABLE_NAME1 + " ";
			Log.i("Query " + query, "Select Query");
			DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
			adapter.createDataBase();
			adapter.openDataBase();
			Cursor cursor = adapter.selectRecordsFromDB(query, null);

			while (cursor.moveToNext()) {
				DataDTO dto = new DataDTO();
				dto.setID(cursor.getInt(0));
				dto.setTaskLocation(cursor.getString(1));
				dto.setLatitude(cursor.getDouble(2));
				dto.setLongitude(cursor.getDouble(3));
				list.add(dto);
				System.out
						.println("Hellllllllllllllllllllloooooooooooooooooooo"
								+ cursor.getString(0));
			}

			cursor.close();
			adapter.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return list;

	}

	public ArrayList getPassedTask() {

		ArrayList<DataDTO> list = new ArrayList<DataDTO>();
		try {
			String query = "select ID,taskName,taskDate,taskTime from "
					+ DataDTO.TABLE_NAME + " ";
			Log.i("Query " + query, "Select Query");
			DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
			adapter.createDataBase();
			adapter.openDataBase();
			Cursor cursor = adapter.selectRecordsFromDB(query, null);

			while (cursor.moveToNext()) {

				DataDTO dto = new DataDTO();
				String date = cursor.getString(2);
				String time = cursor.getString(3);
				String timeDate = date + " " + time;

				dateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm a");
				// -------------------Current Date---------------------//
				Calendar calendar = Calendar.getInstance();
				current = dateFormat.format(calendar.getTime());
				date2 = dateFormat.parse(current);
				// current= dateFormat.format(date2);

				// System.out.println("Current date is"+current);

				// ---------------Database Date----------------------//
				dateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm a");
				try {
					date1 = dateFormat.parse(timeDate);
					old = dateFormat.format(date1);
					System.out.println("Complete Convertion:" + old);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("date1 " + date1 + "  " + "date2" + date2);
				if (date2.after(date1)) {

					dto.setID(cursor.getInt(0));
					dto.setTaskName(cursor.getString(1));
					dto.setTaskDate(date);
					dto.setTaskTime(time);
					list.add(dto);
					System.out
							.println("Hellllllllllllllllllllloooooooooooooooooooo"
									+ dto.getTaskName()
									+ dto.getTaskDate()
									+ dto.getTaskTime());
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

		ArrayList<DataDTO> list = new ArrayList<DataDTO>();
		try {
			String query = "select * from " + DataDTO.TABLE_NAME + " ";
			Log.i("Query " + query, "Select Query");
			DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
			adapter.createDataBase();
			adapter.openDataBase();
			Cursor cursor = adapter.selectRecordsFromDB(query, null);
			while (cursor.moveToNext()) {
				DataDTO dto = new DataDTO();
				String date = cursor.getString(2);
				String time = cursor.getString(6);
				Log.i("Timedate in Upcoming ... " + cursor.getInt(0) + " "
						+ cursor.getString(1) + " " + cursor.getString(2), " Data ");

				String timeDate = date + " " + time;

				dateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm");
				// -------------------Current Date---------------------//
				Calendar calendar = Calendar.getInstance();
				current = dateFormat.format(calendar.getTime());
				date2 = dateFormat.parse(current);
				// current= dateFormat.format(date2);

				// System.out.println("Current date is"+current);

				// ---------------Database Date----------------------//
				dateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm");
				try {
					date1 = dateFormat.parse(timeDate);
					old = dateFormat.format(date1);
					System.out.println("Complete Convertion:" + old);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("date1 " + date1 + "  " + "date2" + date2);
				if (date2.before(date1)) {
					dto.setID(cursor.getInt(0));
					dto.setTaskName(cursor.getString(1));
					dto.setTaskDate(date);
					dto.setTaskLocation(cursor.getString(3));
					dto.setTaskRadius(cursor.getInt(4));
					dto.setTaskNearby(cursor.getString(5));
					dto.setTaskTime(time);
					list.add(dto);
					System.out
							.println("Hellllllllllllllllllllloooooooooooooooooooo"
									+ dto.getTaskRadius());
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