package com.marakana.android.lifecycle;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

public class ApplicationDemo extends Application {
  static final String TAG = "ApplicationDemo";

  @Override
  public void onCreate() {
    super.onCreate();
    Log.d(TAG, "onCreate");
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    Log.d(TAG, "onConfigurationChanged");
  }
  
  @Override
  public void onTerminate() {
    super.onTerminate();
    Log.d(TAG, "onTerminate");
  }

}
