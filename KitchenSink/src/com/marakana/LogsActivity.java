package com.marakana;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class LogsActivity extends Activity {
  ListView list;
  DbHelper dbHelper;
  Cursor cursor;
  SimpleCursorAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.logs);

    // Find views
    list = (ListView) findViewById(R.id.list);

    // Get logs data
    dbHelper = new DbHelper(this);
    cursor = dbHelper.getLogs();

    // Setup the adapter
    String[] from = { DbHelper.C_TAG, DbHelper.C_MSG };
    int[] to = { R.id.textTag, R.id.textMsg };
    adapter = new SimpleCursorAdapter(this, R.layout.logrow, cursor, from, to);
    list.setAdapter(adapter);
  }

}
