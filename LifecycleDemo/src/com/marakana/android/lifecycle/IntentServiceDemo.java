package com.marakana.android.lifecycle;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class IntentServiceDemo extends IntentService {
  static final String TAG = "IntentServiceDemo";
  
  public IntentServiceDemo() {
    super(TAG);
  }

  @Override
  public void onCreate() {
    super.onCreate();
    Log.d(TAG, "onCreate");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    Log.d(TAG, "onHandleIntent for action: " + intent.getAction());
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "onDestroy");
  }

}
