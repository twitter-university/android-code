package com.marakana.android.logservice;

import android.app.Application;
import android.os.ServiceManager;
import android.util.Log;
import com.marakana.android.service.log.ILogService;

public class LogServiceApp extends Application {
  private static final String TAG = "LogServiceApp";
  private static final String REMOTE_SERVICE_NAME = ILogService.class.getName();
  private ILogServiceImpl serviceImpl;

  public void onCreate() {
    super.onCreate();
    this.serviceImpl = new ILogServiceImpl(this);
    ServiceManager.addService(REMOTE_SERVICE_NAME, this.serviceImpl);
    Log.d(TAG, "Registered [" + serviceImpl.getClass().getName() + "] as [" + REMOTE_SERVICE_NAME + "]");
  }

  public void onTerminate() {
    super.onTerminate();
    Log.d(TAG, "Terminated");
  }
}

