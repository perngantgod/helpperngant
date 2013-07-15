
package com.junyi.perngant.provider;

import com.junyi.perngant.util.DatabaseUtil;
import com.junyi.perngant.util.EventDiary;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

public class DiaryAdapter {
    public static final String KEY_ROWID = "_id";

    public static final String KEY_DATE = "date";

    public static final String KEY_PERIOD_STATE = "period_state";

    public static final String KEY_LOVE = "love";

    public static final String KEY_TOOL = "tool";

    public static final String KEY_MEDICINE = "medicine";

    public static final String KEY_TEMPERATURE = "temperature";

    private DatabaseUtil dbUtil;

    private SQLiteDatabase mDb;

    public static final String DATABASE_TABLE = "eventdiary";

    private final Context mCtx;

    public DiaryAdapter(Context mCtx) {
        this.mCtx = mCtx;
    }

    public SQLiteDatabase getMDb() {
        return mDb;
    }

    public DiaryAdapter open() throws SQLException {
        dbUtil = new DatabaseUtil(mCtx);
        mDb = dbUtil.getWritableDatabase();
        return this;
    }


    public void close() {
        dbUtil.close();
    }

    public long createDiary(long date, int period_state, int islove, int ismedicine, int istool,
            String temperature) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_PERIOD_STATE, period_state);
        initialValues.put(KEY_LOVE, islove);
        initialValues.put(KEY_MEDICINE, ismedicine);
        initialValues.put(KEY_TOOL, istool);
        initialValues.put(KEY_TEMPERATURE, temperature);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteDiary(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    
    public boolean dateExist(long datetime){
        Cursor curosr  = fetchAllDiaries();
        boolean ishave = false;
        while(curosr.moveToNext()){
            if(curosr.getLong(curosr.getColumnIndex(DiaryAdapter.KEY_DATE)) == datetime){
                // update value exist
                ishave = true;
                break;
            }
        }
        curosr.close();
        return ishave;
    }
    
    public int getIdByDatetime(long datetime){
        Cursor curosr  = fetchAllDiaries();
        int id = 0 ;
        while(curosr.moveToNext()){
            if(curosr.getLong(curosr.getColumnIndex(DiaryAdapter.KEY_DATE)) == datetime){
                // update value exist
                id = curosr.getInt(curosr.getColumnIndex(DiaryAdapter.KEY_ROWID));
                break;
            }
        }
        curosr.close();
        return id;
    }
    
    
    public Cursor fetchAllDiaries() {

        return mDb.query(DATABASE_TABLE, new String[] {
                KEY_ROWID, KEY_DATE, KEY_PERIOD_STATE, KEY_LOVE, KEY_MEDICINE, KEY_TOOL,
                KEY_TEMPERATURE
        }, null, null, null, null, KEY_DATE);
    }

    public Cursor fetchDiary(long rowId) throws SQLException {

        Cursor mCursor =

        mDb.query(true, DATABASE_TABLE, new String[] {
                KEY_ROWID, KEY_DATE, KEY_PERIOD_STATE, KEY_LOVE, KEY_MEDICINE, KEY_TOOL,
                KEY_TEMPERATURE
        }, KEY_ROWID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchDiaryByDate(long date) throws SQLException {

        Cursor mCursor =

        mDb.query(true, DATABASE_TABLE, new String[] {
                KEY_ROWID, KEY_DATE, KEY_PERIOD_STATE, KEY_LOVE, KEY_MEDICINE, KEY_TOOL,
                KEY_TEMPERATURE
        }, KEY_DATE + "=" + date, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updateEventType(long rowId, long date, int state, int islove, int istool,
            int ismedicine, String temp) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_PERIOD_STATE, state);
        initialValues.put(KEY_LOVE, islove);
        initialValues.put(KEY_MEDICINE, ismedicine);
        initialValues.put(KEY_TOOL, istool);
        initialValues.put(KEY_TEMPERATURE, temp);
        return mDb.update(DATABASE_TABLE, initialValues, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateEventDate(long rowId, long date) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DATE, date);
        return mDb.update(DATABASE_TABLE, initialValues, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateEventState(long rowId, int state) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_PERIOD_STATE, state);
        return mDb.update(DATABASE_TABLE, initialValues, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateEventLove(long rowId, int islove) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_LOVE, islove);
        return mDb.update(DATABASE_TABLE, initialValues, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateEventTemp(long rowId, String temp) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TEMPERATURE, temp);
        return mDb.update(DATABASE_TABLE, initialValues, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateEventTool(long rowId, int istool) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TOOL, istool);
        return mDb.update(DATABASE_TABLE, initialValues, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateEventMed(long rowId, int ismedicine) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_MEDICINE, ismedicine);
        return mDb.update(DATABASE_TABLE, initialValues, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean findByDate(long datetime) {

        return false;
    }

}
