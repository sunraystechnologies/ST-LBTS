package in.co.sunrays.lbts.dao;

import java.util.ArrayList;
import java.util.List;

import in.co.sunrays.lbts.database.DBAdapter;
import in.co.sunrays.lbts.dto.UserDTO;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class UserDAO {

	public Context context;

	public UserDAO(Context con) {
		this.context = con;
	}

	public int nextPK() {
		int pk = 1;
		try {
			String query = "select max(ID) from " + UserDTO.TABLE_NAME;
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

	public void add(UserDTO dto) throws Exception {
		ContentValues values = new ContentValues();
		values.put(UserDTO.ID, nextPK());
		values.put(UserDTO.FIRST_NAME, dto.getFirstName());
		values.put(UserDTO.LAST_NAME, dto.getLastName());
		values.put(UserDTO.LOGIN, dto.getLogin());
		values.put(UserDTO.PASSWORD, dto.getPassword());
		values.put(UserDTO.ADDRESS, dto.getAddress());
		values.put(UserDTO.MOBILE_NO, dto.getMobileNo());
		values.put(UserDTO.LATITUDE, dto.getLattitude());
		values.put(UserDTO.LONGITUDE, dto.getLongitude());

		DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
		adapter.createDataBase();
		adapter.openDataBase();
		adapter.addContact(UserDTO.TABLE_NAME, null, values);
		adapter.close();
	}

	public void update(UserDTO dto) throws Exception {
		ContentValues values = new ContentValues();
		values.put(UserDTO.FIRST_NAME, dto.getFirstName());
		values.put(UserDTO.LAST_NAME, dto.getLastName());
		values.put(UserDTO.LOGIN, dto.getLogin());
		values.put(UserDTO.PASSWORD, dto.getPassword());
		values.put(UserDTO.ADDRESS, dto.getAddress());
		values.put(UserDTO.MOBILE_NO, dto.getMobileNo());
		values.put(UserDTO.LATITUDE, dto.getLattitude());
		values.put(UserDTO.LONGITUDE, dto.getLongitude());
		DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
		adapter.createDataBase();
		adapter.openDataBase();
		adapter.updateRecordsInDB(UserDTO.TABLE_NAME, values, UserDTO.ID + "="
				+ dto.getId(), null);
		adapter.close();
	}

	public void delete(UserDTO dto) throws Exception {
		String query = UserDTO.TABLE_NAME + " where " + UserDTO.ID + " ="
				+ dto.getId();
		Log.i("Query " + query, "Query");
		DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
		adapter.createDataBase();
		adapter.openDataBase();
		adapter.deleteRecordInDB(query, null, null);
		adapter.close();

	}

	public UserDTO findByLogin(String login) {
		UserDTO dto = null;
		try {
			String query = "select * from " + UserDTO.TABLE_NAME + " where "
					+ UserDTO.LOGIN + " = '" + login + "'";
			Log.i("Query " + query, "findByLogin Query");
			DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
			adapter.createDataBase();
			adapter.openDataBase();
			Cursor cursor = adapter.selectRecordsFromDB(query, null);
			if (cursor.moveToNext()) {
				dto = new UserDTO();
				dto.setId(cursor.getInt(0));
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

	public UserDTO findByPK(int id) {
		UserDTO dto = null;
		try {
			String query = "select * from " + UserDTO.TABLE_NAME + " where "
					+ UserDTO.ID + " = " + id;
			Log.i("Query " + query, "findByPK Query");
			DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
			adapter.createDataBase();
			adapter.openDataBase();
			Cursor cursor = adapter.selectRecordsFromDB(query, null);
			if (cursor.moveToNext()) {
				dto = new UserDTO();
				dto.setId(cursor.getInt(0));
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

	public UserDTO authenticate(String login, String password) {
		UserDTO dto = null;
		try {
			String query = "select * from " + UserDTO.TABLE_NAME + " where "
					+ UserDTO.LOGIN + " = '" + login + "' and "
					+ UserDTO.PASSWORD + " ='" + password + "'";
			Log.i("Query " + query, "Authenticate Query");
			DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
			adapter.createDataBase();
			adapter.openDataBase();
			Cursor cursor = adapter.selectRecordsFromDB(query, null);
			if (cursor.moveToNext()) {
				dto = new UserDTO();
				dto.setId(cursor.getInt(0));
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

	public List<UserDTO> list() {
		List<UserDTO> list = new ArrayList<UserDTO>();
		try {
			String query = "select * from " + UserDTO.TABLE_NAME;
			Log.i("Query " + query, "List Query");
			DBAdapter adapter = DBAdapter.getDBAdapterInstance(context);
			adapter.createDataBase();
			adapter.openDataBase();
			Cursor cursor = adapter.selectRecordsFromDB(query, null);
			while (cursor.moveToNext()) {
				UserDTO dto = new UserDTO();
				dto.setId(cursor.getInt(0));
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