package com.example.fibservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class FibService extends Service {
  static final String TAG = "FibService";
  IFibServiceImpl service;
  
  @Override
  public void onCreate() {
    super.onCreate();
    service = new IFibServiceImpl();
    Log.d(TAG, "onCreate");
  }

  @Override
  public IBinder onBind(Intent intent) {
    Log.d(TAG, "onBind");
    return service;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    service = null;
    Log.d(TAG, "onDestroy");
  }

  
}
