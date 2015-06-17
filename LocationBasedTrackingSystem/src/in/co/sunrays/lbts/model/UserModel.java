package in.co.sunrays.lbts.model;

import in.co.sunrays.lbts.adapter.DBAdapter;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class UserModel {

	public static final String TABLE_NAME = "user";
	public static final String ID = "ID";
	public static final String FIRST_NAME = "FISRT_NAME";
	public static final String LAST_NAME = "LAST_NAME";
	public static final String LOGIN = "LOGIN";
	public static final String PASSWORD = "PASSWORD";
	public static final String ADDRESS = "ADDRESS";
	public static final String MOBILE_NO = "MOBILE_NO";
	public static final String LATITUDE = "LAST_LATTITUDE";
	public static final String LONGITUDE = "LAST_LONGITUDE";

	private long id;
	private String firstName;
	private String lastName;
	private String login;
	private String password;
	private String address;
	private String mobileNo;
	private double lattitude;
	private double longitude;
	public Context context;

	public UserModel() {
	}

	public UserModel(Context con) {
		this.context = con;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public double getLattitude() {
		return lattitude;
	}

	public void setLattitude(double lattitude) {
		this.lattitude = lattitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public int nextPK() {
		int pk = 1;
		try {
			String query = "select max(ID) from " + TABLE_NAME;
			Log.i("Query " + query, "Next PK");
			DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
			adapter.createDataBase();
			adapter.openDataBase();
			Cursor cursor = adapter.selectRecordsFromDB(query, null);
			if (cursor.moveToNext()) {
				pk += cursor.getInt(0);
			}
			Log.i("Pk : " + pk, "Next Primary Key");
			cursor.close();
			adapter.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return pk;
	}

	public void add() throws Exception {
		ContentValues values = new ContentValues();
		values.put(ID, nextPK());
		values.put(FIRST_NAME, firstName);
		values.put(LAST_NAME, lastName);
		values.put(LOGIN, login);
		values.put(PASSWORD, password);
		values.put(ADDRESS, address);
		values.put(MOBILE_NO, mobileNo);
		values.put(LATITUDE, lattitude);
		values.put(LONGITUDE, longitude);

		DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
		adapter.createDataBase();
		adapter.openDataBase();
		adapter.addContact(TABLE_NAME, null, values);
		adapter.close();
	}

	public void update() throws Exception {
		ContentValues values = new ContentValues();
		values.put(FIRST_NAME, firstName);
		values.put(LAST_NAME, lastName);
		values.put(LOGIN, login);
		values.put(PASSWORD, password);
		values.put(ADDRESS, address);
		values.put(MOBILE_NO, mobileNo);
		values.put(LATITUDE, lattitude);
		values.put(LONGITUDE, longitude);
		DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
		adapter.createDataBase();
		adapter.openDataBase();
		adapter.updateRecordsInDB(TABLE_NAME, values, ID + "=" + id, null);
		adapter.close();
	}

	public void delete() throws Exception {
		String query = TABLE_NAME + " where " + ID + " =" + id;
		Log.i("Query " + query, "Query");
		DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
		adapter.createDataBase();
		adapter.openDataBase();
		adapter.deleteRecordInDB(query, null, null);
		adapter.close();
	}

	public UserModel findByLogin(String login) {
		UserModel model = null;
		try {
			String query = "select * from " + TABLE_NAME + " where " + LOGIN
					+ " = '" + login + "'";
			Log.i("Query " + query, "findByLogin Query");
			DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
			adapter.createDataBase();
			adapter.openDataBase();
			Cursor cursor = adapter.selectRecordsFromDB(query, null);
			if (cursor.moveToNext()) {
				model = new UserModel();
				model.setId(cursor.getLong(0));
				model.setFirstName(cursor.getString(1));
				model.setLastName(cursor.getString(2));
				model.setLogin(cursor.getString(3));
				model.setPassword(cursor.getString(4));
				model.setAddress(cursor.getString(5));
				model.setMobileNo(cursor.getString(6));
				model.setLattitude(cursor.getDouble(7));
				model.setLongitude(cursor.getDouble(8));
			}
			cursor.close();
			adapter.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return model;
	}

	public UserModel findByPK(int id) {
		UserModel dto = null;
		try {
			String query = "select * from " + TABLE_NAME + " where " + ID
					+ " = " + id;
			Log.i("Query " + query, "findByPK Query");
			DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
			adapter.createDataBase();
			adapter.openDataBase();
			Cursor cursor = adapter.selectRecordsFromDB(query, null);
			if (cursor.moveToNext()) {
				dto = new UserModel();
				dto.setId(cursor.getLong(0));
				dto.setFirstName(cursor.getString(1));
				dto.setLastName(cursor.getString(2));
				dto.setLogin(cursor.getString(3));
				dto.setPassword(cursor.getString(4));
				dto.setAddress(cursor.getString(5));
				dto.setMobileNo(cursor.getString(6));
				dto.setLattitude(cursor.getDouble(7));
				dto.setLongitude(cursor.getDouble(8));
			}
			cursor.close();
			adapter.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dto;
	}

	public UserModel authenticate(String login, String password) {
		UserModel model = null;
		try {
			String query = "select * from " + TABLE_NAME + " where " + LOGIN
					+ " = '" + login + "' and " + PASSWORD + " ='" + password
					+ "'";
			Log.i("Query " + query, "Authenticate Query");
			DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
			adapter.createDataBase();
			adapter.openDataBase();
			Cursor cursor = adapter.selectRecordsFromDB(query, null);
			if (cursor.moveToNext()) {
				model = new UserModel();
				model.setId(cursor.getLong(0));
				model.setFirstName(cursor.getString(1));
				model.setLastName(cursor.getString(2));
				model.setLogin(cursor.getString(3));
				model.setPassword(cursor.getString(4));
				model.setAddress(cursor.getString(5));
				model.setMobileNo(cursor.getString(6));
				model.setLattitude(cursor.getDouble(7));
				model.setLongitude(cursor.getDouble(8));
			}
			cursor.close();
			adapter.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return model;
	}

	public List<UserModel> list() {
		List<UserModel> list = new ArrayList<UserModel>();
		try {
			String query = "select * from " + TABLE_NAME;
			Log.i("Query " + query, "List Query");
			DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
			adapter.createDataBase();
			adapter.openDataBase();
			Cursor cursor = adapter.selectRecordsFromDB(query, null);
			while (cursor.moveToNext()) {
				UserModel dto = new UserModel();
				dto.setId(cursor.getLong(0));
				dto.setFirstName(cursor.getString(1));
				dto.setLastName(cursor.getString(2));
				dto.setLogin(cursor.getString(3));
				dto.setPassword(cursor.getString(4));
				dto.setAddress(cursor.getString(5));
				dto.setMobileNo(cursor.getString(6));
				dto.setLattitude(cursor.getDouble(7));
				dto.setLongitude(cursor.getDouble(8));
				list.add(dto);
			}
			cursor.close();
			adapter.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
}