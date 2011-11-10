package com.marakana;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

public class LoggerActivity extends Activity {
  static final String TAG = "LoggerActivity";
  TextView textOut;
  ServiceConnection connection;
  ILogger logger;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.standard);

    Log.d(TAG, "onCreated started: " + System.currentTimeMillis());

    // Find views
    ((TextView) findViewById(R.id.textTitle)).setText("Logger");
    textOut = (TextView) findViewById(R.id.textOut);

    // Define the connection handler for this remote service
    connection = new ServiceConnection() {
      // Called when we get remote service connection
      public void onServiceConnected(ComponentName name, IBinder binder) {
        logger = ILogger.Stub.asInterface(binder);
        // Use remote service
        try {
          long id = logger.log("LoggerActivity", "Connected to LoggerService");
          textOut.setText("Logger connected and logged id: " + id);
          Log.d(TAG, "onServiceConnected: " + System.currentTimeMillis());
        } catch (RemoteException e) {
          e.printStackTrace();
        }
      }

      // Called when remote service drops the connection
      public void onServiceDisconnected(ComponentName name) {
        logger = null;
        Log.d(TAG, "onServiceDisconnected: " + System.currentTimeMillis());
      }
    };

    // Bind to the remote service
    bindService(new Intent("SandiaRemoteService"), connection, BIND_AUTO_CREATE);

    Log.d(TAG, "onCreated done: " + System.currentTimeMillis());
  }

  @Override
  public void onResume() {
    super.onResume();

    Log.d(TAG, "onResume: " + System.currentTimeMillis());
    if (logger == null)
      return;

    long id = -1;
    try {
      id = logger.log("LoggerActivity", "Logged in onResume");

    } catch (RemoteException e) {
      e.printStackTrace();
    }
    textOut.setText("Logger in onResume id: " + id);
  }

}
