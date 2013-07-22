package com.jun.pregnancy.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DialyDbAdapter {

	public static final String KEY_ROWID = "_id";

	public static final String KEY_WEIGHT = "weight";
	public static final String KEY_TEMP = "temp";
	public static final String KEY_LH = "lh";

	public static final String KEY_MOOD = "mood";
	public static final String KEY_DATE = "dateday";
	public static final String KEY_PERIOD = "period";

	public static final String KEY_LOVE = "love";
	public static final String KEY_TOOL = "tool";
	public static final String KEY_MEDICINE = "medicine";
	public static final String KEY_TEXT = "textnote";

	private static final String TAG = "DialyDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	/**
	 * Database creation sql statement
	 */
	private static final String DATABASE_CREATE = "CREATE TABLE notes (_id INTEGER PRIMARY KEY autoincrement, "
			+ "dateday TEXT NOT NULL, textnote TEXT DEFAULT NULL, weight REAL DEFAULT NULL, temp REAL DEFAULT NULL, lh REAL DEFAULT NULL, mood INTEGER DEFAULT NULL, period INTEGER DEFAULT NULL, love INTEGER DEFAULT NULL,tool INTEGER DEFAULT NULL,  medicine INTEGER DEFAULT NULL);";

	private static final String DATABASE_NAME = "data";
	private static final String DATABASE_TABLE = "notes";
	private static final int DATABASE_VERSION = 2;

	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}
	}

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param ctx
	 *            the Context within which to work
	 */
	public DialyDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	/**
	 * Open the notes database. If it cannot be opened, try to create a new
	 * instance of the database. If it cannot be created, throw an exception to
	 * signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public DialyDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	/**
	 * Create a new note using the title and body provided.
	 * @param weight
	 * @param temp
	 * @param lh
	 * @param text
	 * @param mood
	 * @param dates
	 * @param love
	 * @param periods
	 * @param medicine
	 * @param tools
	 * @return id
	 */
	public long createNote(float weight, float temp, float lh, String text,
			int mood, String dates, int love, int periods, int medicine,
			int tools) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_WEIGHT, weight);
		initialValues.put(KEY_TEMP, temp);
		initialValues.put(KEY_LH, lh);
		initialValues.put(KEY_TEXT, text);
		initialValues.put(KEY_MOOD, mood);

		initialValues.put(KEY_DATE, dates);
		initialValues.put(KEY_PERIOD, periods);
		initialValues.put(KEY_LOVE, love);
		initialValues.put(KEY_MEDICINE, medicine);
		initialValues.put(KEY_TOOL, tools);

		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	/**
	 * Delete the note with the given rowId
	 * 
	 * @param rowId
	 *            id of note to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteNote(long rowId) {

		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * Return a Cursor over the list of all notes in the database
	 * 
	 * @return Cursor over all notes
	 */
	public Cursor fetchAllNotes() {

		return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_WEIGHT,
				KEY_TEMP, KEY_LH, KEY_TEXT, KEY_MOOD, KEY_DATE, KEY_PERIOD, KEY_LOVE, KEY_MEDICINE, KEY_TOOL }, null, null, null, null, null);
	}

	/**
	 * Return a Cursor positioned at the note that matches the given rowId
	 * 
	 * @param rowId
	 *            id of note to retrieve
	 * @return Cursor positioned to matching note, if found
	 * @throws SQLException
	 *             if note could not be found/retrieved
	 */
	public Cursor fetchNote(long rowId) throws SQLException {

		Cursor mCursor =

		mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_WEIGHT,
				KEY_TEMP, KEY_LH, KEY_TEXT, KEY_MOOD, KEY_DATE, KEY_PERIOD, KEY_LOVE, KEY_MEDICINE, KEY_TOOL  }, KEY_ROWID + "=" + rowId, null, null, null, null,
				null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	/**
	 * Update the note using the details provided. The note to be updated is
	 * specified using the rowId, and it is altered to use the title and body
	 * values passed in
	 * 
	 * @param rowId
	 *            id of note to update
	 * @param temp
	 *            value to set note body to
	 * @return true if the note was successfully updated, false otherwise
	 */
	public boolean updateNoteOfTemp(long rowId, float temp) {
		ContentValues args = new ContentValues();
		args.put(KEY_TEMP, temp);
		
		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	
	/**
	 * 
	 * @param rowId
	 * @param weight
	 * @param temp
	 * @param lh
	 * @param text
	 * @param mood
	 * @param dates
	 * @param love
	 * @param periods
	 * @param medicine
	 * @param tools
	 * @return
	 */
	public boolean updateNote(long rowId, float weight, float temp, float lh, String text,
			int mood, String dates, int love, int periods, int medicine,
			int tools) {
		ContentValues args = new ContentValues();
		args.put(KEY_WEIGHT, weight);
		args.put(KEY_TEMP, temp);
		args.put(KEY_LH, lh);
		args.put(KEY_TEXT, text);
		args.put(KEY_MOOD, mood);

		args.put(KEY_DATE, dates);
		args.put(KEY_PERIOD, periods);
		args.put(KEY_LOVE, love);
		args.put(KEY_MEDICINE, medicine);
		args.put(KEY_TOOL, tools);
		
		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
}