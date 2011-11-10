package com.marakana;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
  public static final String DB_NAME = "logs.db";
  public static final int DB_VERSION = 1;

  public static final String TABLE = "logs";
  public static final String C_ID = "_id";
  public static final String C_TAG = "tag";
  public static final String C_MSG = "message";
  public static final String C_TS = "tstamp";

  public DbHelper(Context context) {
    super(context, DB_NAME, null, DB_VERSION);
  }

  // Called first time when the database doesn't exist at all
  @Override
  public void onCreate(SQLiteDatabase db) {
    String sql = String.format("create table %s ("
        + "%s integer primary key autoincrement, "
        + "%s text, %s text, %s integer)", TABLE, C_ID, C_TAG, C_MSG, C_TS);
    db.execSQL(sql);
  }

  // Called whenever existing database version is different than new one
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }

  // Logs into the database
  public long log(String tag, String message) {
    SQLiteDatabase db = getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(C_TAG, tag);
    values.put(C_MSG, message);
    values.put(C_TS, System.currentTimeMillis());
    return db.insert(TABLE, null, values);
  }

  // Returns cursor representing all log items
  public Cursor getLogs() {
    SQLiteDatabase db = getReadableDatabase();
    return db.query(TABLE, null, null, null, null, null, C_TS + " DESC");
  }
}
