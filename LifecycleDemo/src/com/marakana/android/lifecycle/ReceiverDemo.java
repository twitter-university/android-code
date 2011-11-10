package com.marakana.android.lifecycle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReceiverDemo extends BroadcastReceiver {
  static final String TAG = "ReceiverDemo";

  @Override
  public void onReceive(Context context, Intent intent) {
    Log.d(TAG, "onReceive action: "+intent.getAction() );
  }
   
}
