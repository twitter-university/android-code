package com.marakana.logservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class LogService extends Service { // <1>
  static final String TAG = "LOGGER";
  
  @Override
  public IBinder onBind(Intent intent) { // <2>
    final String version = intent.getExtras().getString("version");

    return new ILogService.Stub() { // <3>

      public void log_d(String tag, String message) throws RemoteException { // <4>
        Log.d(TAG+" "+ tag, message + " version: " + version);
      }

      public void log(Message msg) throws RemoteException { // <5>
        Log.d(TAG+" "+ msg.getTag(), msg.getText());
      }
    };
  }

}
