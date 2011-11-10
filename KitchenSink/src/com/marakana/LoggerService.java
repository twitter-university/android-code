package com.marakana;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class LoggerService extends Service {
  DbHelper dbHelper;

  @Override
  public IBinder onBind(Intent intent) {
    if (dbHelper == null) {
      dbHelper = new DbHelper(this);
    }

    // Returns implementation of ILogger service
    return new ILogger.Stub() {
      public long log(String tag, String message) throws RemoteException {
        return dbHelper.log(tag, message);
      }
    };
  }

}
