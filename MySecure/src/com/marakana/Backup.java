package com.marakana;

import java.io.FileOutputStream;
import java.io.IOException;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class Backup {
  Context context;
  ContentResolver cr;
  HeartbeatDBHelper dbHelper;
  SQLiteDatabase db;

  public Backup(Context context) {
    this.context = context;
    
    // Get content resolver
    cr = context.getContentResolver();
    
    // Open the database
    dbHelper = new HeartbeatDBHelper(context);
    db = dbHelper.getReadableDatabase();
  }

  // Runs the backup of a content provider into a text file
  public int runBackup(Uri uri) {
    int count = 0;
    String file = uri.getHost() +"-"+ System.currentTimeMillis();
    
    Cursor cursor = cr.query(uri, null, null, null, null);
    count = cursorToCSV(cursor, file);
    cursor.close();

    String msg = String.format("Backed up %d records to %s file", count, file);
    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

    return count;
  }

  // Backs up a database table
  public int runDBBackup(String table) {
    int count = 0;
    String file = table +"-"+ System.currentTimeMillis();

    // Query the database table
    Cursor cursor = db.query(table, null, null, null, null, null, null);
    count = cursorToCSV(cursor, file);
    cursor.close();
    
    String msg = String.format("Backed up %d records to %s file", count, file);
    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    
    return count;
  }

  // Takes a cursor, iterates over it and, and saves CSV to file
  private int cursorToCSV(Cursor cursor, String file) {
    StringBuffer out = new StringBuffer();
    int count=0;
    
    // loop thru all the records
    while (cursor.moveToNext()) {
      // loop thru all the columns
      for (int i = 0; i < cursor.getColumnCount(); i++) {
        String column = cursor.getString(i);
        if (i != 0)
          out.append(",");
        out.append(column); // adds a column
      }
      out.append("\n");
      count++;
    }
    
    // save to a file
    try {
      FileOutputStream outStream = context.openFileOutput(file,
          Context.MODE_PRIVATE);
      outStream.write(out.toString().getBytes());
      outStream.close();
      Log.d("Backup", out.toString());
    } catch (IOException e) {
      Log.d("Backup", "Error writing CSV to file: "+file);
      e.printStackTrace();
    }
    
    return count;
  }
}
