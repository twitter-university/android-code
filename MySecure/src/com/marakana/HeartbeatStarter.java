package com.marakana;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class HeartbeatStarter extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    // Start the HeartbeatService
    context.startService( new Intent(context, HeartbeatService.class) );
  }

}
