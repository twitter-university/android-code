package com.marakana;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

// Helps open the heartbeat database
public class HeartbeatDBHelper extends SQLiteOpenHelper {
  public static final String TAG = "HeartbeatDBHelper";
  public static final String DB_NAME = "heartbeat.db";
  public static final int DB_VERSION = 5;
  public static final String TABLE = "heartbeats";
  public static final String C_RECORDED_AT = "recorded_at";
  public static final String C_LAT = "latitude";
  public static final String C_LONG = "longitude";
  Context context;

  public HeartbeatDBHelper(Context context) {
    super(context, DB_NAME, null, DB_VERSION);
    this.context = context;
  }

  // Called only once, when database gets created first time
  @Override
  public void onCreate(SQLiteDatabase db) {
    String sql = String.format(
        "create table %s (%s integer primary key autoincrement,"
            + "%s integer, %s float, %s float);", TABLE, BaseColumns._ID,
        C_RECORDED_AT, C_LAT, C_LONG);
    Log.d(TAG, sql);
    db.execSQL(sql);
  }

  // This is where you upgrade your schema
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    try {
      String sql = context.getResources().getString(R.string.sqlDropTable);
      db.execSQL(sql);
    } catch (SQLException e) {
    }
    onCreate(db);
  }

}
