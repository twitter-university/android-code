package com.marakana.android.lifecycle;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ServiceDemo extends Service {
  static final String TAG = "ServiceDemo";

  @Override
  public IBinder onBind(Intent arg0) {
    return null;
  }

  @Override
  public void onCreate() {
    Log.d(TAG, "onCreate");
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.d(TAG, "onStartCommand");
    return  START_STICKY;
  }

  @Override
  public void onDestroy() {
    Log.d(TAG, "onDestroy");
  }
  
}
