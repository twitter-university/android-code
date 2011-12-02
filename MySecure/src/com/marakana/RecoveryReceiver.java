package com.marakana;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class RecoveryReceiver extends BroadcastReceiver {

  // Triggered by an incoming SMS
  @Override
  public void onReceive(Context context, Intent intent) {
    Bundle bundle = intent.getExtras();
    SmsMessage[] msgs = null;
    String from, body;
    Log.d("RecoveryReceiver", "onReceive");
    if (bundle != null) {
      // ---retrieve the SMS message received---
      Object[] pdus = (Object[]) bundle.get("pdus");
      msgs = new SmsMessage[pdus.length];
      for (int i = 0; i < msgs.length; i++) {
        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
        from    = msgs[i].getOriginatingAddress();
        body    = msgs[i].getMessageBody().toString();
        
        // Is it the recovery message
        if("It rains in Spain".equals(body.trim())) {
          // Delete the phone data
          Toast.makeText(context, "PHONE STOLEN!", Toast.LENGTH_LONG).show();
          Log.d("RecoveryReceiver", "PHONE STOLEN!");
        } else {
          Log.d("RecoveryReceiver", "Ignored");
        }
      }
    }
  }

}
