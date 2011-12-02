package com.marakana.logclient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.marakana.logservice.ILogService;
import com.marakana.logservice.Message;

public class LogActivity extends Activity implements OnClickListener {
  private static final String TAG = "LogActivity";
  ILogService logService;
  LogConnection conn;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    // Request bind to the service
    conn = new LogConnection(); // <1>
    Intent intent = new Intent("com.marakana.logservice.ILogService"); // <2>
    intent.putExtra("version", "1.0"); // <3>
    bindService(intent, conn, Context.BIND_AUTO_CREATE); // <4>

    // Attach listener to button
    ((Button) findViewById(R.id.buttonClick)).setOnClickListener(this);
  }

  class LogConnection implements ServiceConnection { // <5>

    public void onServiceConnected(ComponentName name, IBinder service) { // <6>
      logService = ILogService.Stub.asInterface(service); // <7>
      Log.i(TAG, "connected");
    }

    public void onServiceDisconnected(ComponentName name) { // <8>
      logService = null;
      Log.i(TAG, "disconnected");
    }

  }

  public void onClick(View button) {
    try {
      logService.log_d("LogClient", "Hello from onClick()"); // <9>
      Message msg = new Message(Parcel.obtain()); // <10>
      msg.setTag("LogClient");
      msg.setText("Hello from inClick() version 1.1");
      logService.log(msg); // <11>
    } catch (RemoteException e) { // <12>
      Log.e(TAG, "onClick failed", e);
    }

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "onDestroyed");

    unbindService(conn); // <13>

    logService = null;
  }
}