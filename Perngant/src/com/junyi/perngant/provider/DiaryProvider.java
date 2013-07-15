
package com.junyi.perngant.provider;

import java.util.HashMap;

import com.junyi.perngant.util.DatabaseUtil;
import com.junyi.perngant.util.EventDiary;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class DiaryProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher;

    private DatabaseUtil dbUtil;

    private static final String EVENT_DIARY_TABLE_NAME = "eventdiary";

    private static HashMap<String, String> sEventDiariesProjectionMap;

    private static final int EVENT_DIARIES = 1;

    private static final int EVENT_ITEM_ID = 2;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(EventDiary.AUTHORITY, "eventdiary", EVENT_DIARIES);
        sUriMatcher.addURI(EventDiary.AUTHORITY, "eventdiary/#", EVENT_ITEM_ID);

        sEventDiariesProjectionMap = new HashMap<String, String>();
        sEventDiariesProjectionMap.put(DiaryAdapter.KEY_ROWID, DiaryAdapter.KEY_ROWID);
        sEventDiariesProjectionMap.put(DiaryAdapter.KEY_DATE, DiaryAdapter.KEY_DATE);
        sEventDiariesProjectionMap
                .put(DiaryAdapter.KEY_PERIOD_STATE, DiaryAdapter.KEY_PERIOD_STATE);
        sEventDiariesProjectionMap.put(DiaryAdapter.KEY_LOVE, DiaryAdapter.KEY_LOVE);
        sEventDiariesProjectionMap.put(DiaryAdapter.KEY_TOOL, DiaryAdapter.KEY_TOOL);
        sEventDiariesProjectionMap.put(DiaryAdapter.KEY_MEDICINE, DiaryAdapter.KEY_MEDICINE);
        sEventDiariesProjectionMap.put(DiaryAdapter.KEY_TEMPERATURE, DiaryAdapter.KEY_TEMPERATURE);

    }

    @Override
    public boolean onCreate() {
        dbUtil = new DatabaseUtil(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)) {
            case EVENT_DIARIES:
                qb.setTables(EVENT_DIARY_TABLE_NAME);
                qb.setProjectionMap(sEventDiariesProjectionMap);
                break;
                
            case EVENT_ITEM_ID:
                qb.setTables(EVENT_DIARY_TABLE_NAME);
                qb.setProjectionMap(sEventDiariesProjectionMap);
                qb.appendWhere(DiaryAdapter.KEY_ROWID + "="
                        + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = dbUtil.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        
        return c;
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = dbUtil.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case EVENT_DIARIES:
                count = db.delete(EVENT_DIARY_TABLE_NAME, where, whereArgs);
                break;
            case EVENT_ITEM_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(EVENT_DIARY_TABLE_NAME,
                        DiaryAdapter.KEY_ROWID
                                + "="
                                + id
                                + (!TextUtils.isEmpty(where) ? " AND (" + where
                                        + ')' : ""), whereArgs);
                break;
                
                default :
                    throw new IllegalArgumentException("Unknown URI " + uri);
        }
                
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return count;
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        SQLiteDatabase db = dbUtil.getWritableDatabase();
        long rowId = 0;
        Uri contentUri;
        switch (sUriMatcher.match(uri)) {
            case EVENT_DIARIES:
                rowId = db.insert(EVENT_DIARY_TABLE_NAME, "null", initialValues);
                contentUri = EventDiary.DIARY_ITEM_CONTENT_URI;
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (rowId > 0) {
            Uri returnUri = ContentUris.withAppendedId(contentUri, rowId);
            getContext().getContentResolver().notifyChange(returnUri, null);
            db.close();
            return returnUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {

        SQLiteDatabase db = dbUtil.getWritableDatabase();
        String id;
        int count;
        switch (sUriMatcher.match(uri)) {
            case EVENT_DIARIES:
                count = db.update(EVENT_DIARY_TABLE_NAME, values, where, whereArgs);
                break;
            case EVENT_ITEM_ID:
                id = uri.getPathSegments().get(1);
                count = db.update(EVENT_DIARY_TABLE_NAME, values, DiaryAdapter.KEY_ROWID + "=" + id
                        + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return count;
    }

    private static final String EVENT_ITEM_CONTENT_TYPE = "vnd.yilian.cursor.item/event";
    private static final String EVENT_CONTENT_TYPE = "vnd.yilian.cursor.dir/event";

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {

            case EVENT_DIARIES:
                return EVENT_CONTENT_TYPE;
            case EVENT_ITEM_ID:
                return EVENT_ITEM_CONTENT_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

}
