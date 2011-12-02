package com.example.fib;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fibcommon.IFibService;

public class FibActivity extends Activity implements ServiceConnection {
  static final String TAG = "FibActivity";
  IFibService service;
  EditText textN;
  TextView textOut;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    textN = (EditText)findViewById(R.id.text_n);
    textOut = (TextView)findViewById(R.id.text_out);
  }
  
  public void onClick(View v) {
    long n = Long.parseLong( textN.getText().toString() );
    textOut.setText("");
    
    long start = System.currentTimeMillis();
    long result=-1;
    try {
      result = service.fib(n);
    } catch (RemoteException e) {
      Log.e(TAG, "onClick died", e);
    }
    long time = System.currentTimeMillis() - start;
    textOut.append( String.format("\nfib(%d)=%d (%d ms)", n, result, time)); 
    

  }
  
  @Override
  protected void onResume() {
    super.onResume();
    Intent intent = new Intent("com.example.fibservice.IFibService");
    bindService(intent, this, BIND_AUTO_CREATE);
    Log.d(TAG, "onResume binding to service");
  }

  @Override
  protected void onPause() {
    super.onPause();
    unbindService(this);
  }

  @Override
  public void onServiceConnected(ComponentName name, IBinder binder) {
    service = IFibService.Stub.asInterface(binder);
    Log.d(TAG, "onServiceConnected");
  }

  @Override
  public void onServiceDisconnected(ComponentName name) {
    service = null;
    Log.d(TAG, "onServiceDisconnected");
  }

  
}