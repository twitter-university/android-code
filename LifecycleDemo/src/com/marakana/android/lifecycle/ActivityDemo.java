package com.marakana.android.lifecycle;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ActivityDemo extends Activity {
  static final String TAG = "ActivityDemo";

  // --- Lifecycle methods

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    Log.d(TAG, "onCreate");
  }

  @Override
  protected void onStart() {
    super.onStart();
    Log.d(TAG, "onStart");
  }

  @Override
  protected void onResume() {
    super.onResume();
    Log.d(TAG, "onResume");
  }

  @Override
  protected void onPause() {
    super.onPause();
    Log.d(TAG, "onPause");
  }

  @Override
  protected void onStop() {
    super.onStop();
    Log.d(TAG, "onStop");
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    Log.d(TAG, "onRestart");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "onDestroy");
  }

  // --- Options menu methods

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case R.id.item_start_service:
      startService(new Intent(this, ServiceDemo.class));
      return true;
    case R.id.item_stop_service:
      stopService(new Intent(this, ServiceDemo.class));
      return true;
    case R.id.item_refresh:
      startService(new Intent("marakana.intent.action.IntentServiceDemo"));
      return true;
    case R.id.item_send_broadcast:
      sendBroadcast(new Intent("marakana.intent.action.ReceiverDemo"));
      return true;
    case R.id.item_location:
      startActivity(new Intent(this, SystemServicesDemo.class));
      return true;
    case R.id.item_insert:
      getContentResolver()
          .insert(ProviderDemo.CONTENT_URI, new ContentValues());
      return true;
    case R.id.item_query:
      getContentResolver().query(ProviderDemo.CONTENT_URI, null, null, null,
          null);
      return true;
    }

    return false;
  }

  // --- Button click event handler

  public void onClickAnotherActivity(View v) {
    startActivity(new Intent(this, AnotherActivity.class));
    Log.d(TAG, "onClickAnotherActivity");
  }

}
