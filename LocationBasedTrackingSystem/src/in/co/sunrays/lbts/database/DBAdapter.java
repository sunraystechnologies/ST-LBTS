package in.co.sunrays.lbts.database;

import in.co.sunrays.lbts.dto.DataDTO;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "lbts.sqlite";
	private static String DB_PATH = "";
	private static DBAdapter mDBConnection;
	private SQLiteDatabase myDataBase;
	private final Context myContext;

	DataDTO dto = null;

	public DBAdapter(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.myContext = context;
		DB_PATH = "/data/data/"
				+ context.getApplicationContext().getPackageName()
				+ "/databases/";
	}

	public static synchronized DBAdapter getDBAdapterInstance(Context context) {
		if (mDBConnection == null) {
			mDBConnection = new DBAdapter(context);
		}
		return mDBConnection;
	}

	public void createDataBase() throws IOException {
		boolean dbExist = checkDataBase();
		if (dbExist) {
			/**
			 * does nothing if - database already exist
			 */
		} else {
			/**
			 * By calling following method 1) an empty database will be created
			 * into the default system path of your application 2) than we
			 * overwrite that database with our database.
			 */
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	public boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + DATABASE_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {
			/**
			 * database doesn't exist yet.
			 */
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	private void copyDataBase() throws IOException {
		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DATABASE_NAME);
		// Path to the just created empty db
		String outFileName = DB_PATH + DATABASE_NAME;
		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);
		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public void openDataBase() throws SQLException {
		String myPath = DB_PATH + DATABASE_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();
		super.close();
	}

	public SQLiteDatabase getMyDataBase() {
		return myDataBase;
	}

	public void setMyDataBase(SQLiteDatabase myDataBase) {
		this.myDataBase = myDataBase;
	}

	public void onCreate(SQLiteDatabase db) {

	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public Cursor selectRecordsFromDB(String tableName, String[] tableColumns,
			String whereClase, String whereArgs[], String groupBy,
			String having, String orderBy) {
		return myDataBase.query(tableName, tableColumns, whereClase, whereArgs,
				groupBy, having, orderBy);
	}

	public ArrayList<ArrayList<String>> selectRecordsFromDBList(
			String tableName, String[] tableColumns, String whereClase,
			String whereArgs[], String groupBy, String having, String orderBy) {

		ArrayList<ArrayList<String>> retList = new ArrayList<ArrayList<String>>();
		ArrayList<String> list = new ArrayList<String>();
		Cursor cursor = myDataBase.query(tableName, tableColumns, whereClase,
				whereArgs, groupBy, having, orderBy);

		if (cursor.moveToFirst()) {
			do {
				list = new ArrayList<String>();
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					list.add(cursor.getString(i));
				}
				retList.add(list);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return retList;

	}

	public long addContact(String tableName, String nullColumnHack,
			ContentValues initialValues) {

		return myDataBase.insert(tableName, nullColumnHack,
				initialValues);
	}

	public long insertRecordsInDBConflict(String tableName,
			String nullColumnHack, ContentValues initialValues) {
		return myDataBase.insertWithOnConflict(tableName, nullColumnHack,
				initialValues, SQLiteDatabase.CONFLICT_REPLACE);
	}

	public boolean updateRecordInDB(String tableName,
			ContentValues initialValues, String whereClause, String whereArgs[]) {
		return myDataBase.update(tableName, initialValues, whereClause,
				whereArgs) > 0;
	}

	/**
	 * This function used to update the Record in DB.
	 * 
	 * @param tableName
	 * @param initialValues
	 * @param whereClause
	 * @param whereArgs
	 * @return 0 in case of failure otherwise return no of row(s) are updated
	 */
	public int updateRecordsInDB(String tableName, ContentValues initialValues,
			String whereClause, String whereArgs[]) {

		return myDataBase.update(tableName, initialValues, whereClause,
				whereArgs);
	}

	/**
	 * This function used to delete the Record in DB.
	 * 
	 * @param tableName
	 * @param whereClause
	 * @param whereArgs
	 * @return 0 in case of failure otherwise return no of row(s) are deleted.
	 */
	public int deleteRecordInDB(String tableName, String whereClause,
			String[] whereArgs) {
		return myDataBase.delete(tableName, whereClause, whereArgs);
	}

	// --------------------- Select Raw Query Functions ---------------------

	/**
	 * apply raw Query
	 * 
	 * @param query
	 * @param selectionArgs
	 * @return Cursor
	 */
	public Cursor selectRecordsFromDB(String query, String[] selectionArgs) {
		Cursor cursor= myDataBase.rawQuery(query, selectionArgs);
		
		return cursor;
	}
	
	/**
	 * apply raw query and return result in list
	 * 
	 * @param query
	 * @param selectionArgs
	 * @return ArrayList<ArrayList<String>>
	 */
	public ArrayList<ArrayList<String>> selectRecordsFromDBList(String query,
			String[] selectionArgs) {
		ArrayList<ArrayList<String>> retList = new ArrayList<ArrayList<String>>();
		ArrayList<String> list = new ArrayList<String>();
		Cursor cursor = myDataBase.rawQuery(query, selectionArgs);
		if (cursor.moveToFirst()) {
			do {
				list = new ArrayList<String>();
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					list.add(cursor.getString(i));
				}
				retList.add(list);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return retList;
	}

	public void createTable(String query) {
		myDataBase.execSQL(query);
	}

	/**
	 * Checks allReady existing row
	 * 
	 * @return boolean
	 */

	public Boolean isAllreayExist(String tableName, String whereClause,
			String whereArgs[]) {
		String query = "Select * From " + tableName + " WHERE " + whereClause;
		Boolean b = Boolean.valueOf(false);
		Cursor mCursor = myDataBase.rawQuery(query, whereArgs);
		if (mCursor != null) {
			if (mCursor.getCount() > 0) {
				b = Boolean.valueOf(true);
			}
		}
		if (mCursor != null && !mCursor.isClosed()) {
			mCursor.close();
		}
		return b;
	}

	/*public RegisterDTO getContact(int LoginID) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(TABLE_NAME, new String[] { USER_NAME,
				LOGIN_ID, PASSWORD }, LoginID + "=?",
				new String[] { String.valueOf(LoginID) }, null, null, null,
				null);
		if (cursor != null) {
			cursor.moveToFirst();

			dto = new RegisterDTO();
			dto.setUserName(cursor.getString(1));
			dto.setLoginId(cursor.getString(2));
			dto.setPassword(cursor.getString(3));
		}
		return dto;

	}*/

}
