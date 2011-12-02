package com.marakana.android.logservice;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.RemoteException;
import android.util.Log;
import com.marakana.android.service.log.ILogService;
import com.marakana.android.lib.log.LibLog;

class ILogServiceImpl extends ILogService.Stub {
  private static final String TAG = "ILogServiceImpl";
  private final Context context;

  ILogServiceImpl(Context context) {
    this.context = context;
  }

  public void flushLog() throws RemoteException {
    if (this.context.checkCallingOrSelfPermission(Manifest.permission.FLUSH_LOG) != 
        PackageManager.PERMISSION_GRANTED) {
      throw new SecurityException("Requires FLUSH_LOG permission");
    }
    Log.d(TAG, "Flushing logs. If it works, you won't see this message.");
    LibLog.flushLog();
  }

  public int getUsedLogSize() throws RemoteException {
    return LibLog.getUsedLogSize();
  }

  public int getTotalLogSize() throws RemoteException {
    return LibLog.getTotalLogSize();
  }
}
