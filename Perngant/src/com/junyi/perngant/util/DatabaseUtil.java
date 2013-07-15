package com.junyi.perngant.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseUtil  extends SQLiteOpenHelper {
	
	public static final String DATABASE_NAME = "eventdiaries";
	public static final int DATABASE_VERSION = 7;

	private static final String EVENT_DIARY_TABLE_CREATE = "create table eventdiary (_id integer primary key autoincrement, "
			+ "date long not null,period_state integer not null,love integer not null,medicine integer not null,"
			+ "tool integer not null,temperature string not null);";

	public DatabaseUtil(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(EVENT_DIARY_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
