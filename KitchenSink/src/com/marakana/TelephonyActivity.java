package com.marakana;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.TextView;

public class TelephonyActivity extends Activity {
  TextView textOut;
  TelephonyManager telephonyManager;
  MyPhoneListener myPhoneListener;
  DbHelper dbHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.standard);

    dbHelper = new DbHelper(this);

    // Find views
    ((TextView) findViewById(R.id.textTitle)).setText("Telephony");
    textOut = (TextView) findViewById(R.id.textOut);
    textOut.setText("");

    // Get telephony manager
    myPhoneListener = new MyPhoneListener(); // <1>
    telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE); // <2>
    telephonyManager.listen(myPhoneListener,
        PhoneStateListener.LISTEN_CALL_STATE); // <3>
    textOut.append("DeviceID: " + telephonyManager.getDeviceId()); // <4>
  }

  // The phone listener class
  class MyPhoneListener extends PhoneStateListener { // <5>

    @Override
    public void onCallStateChanged(int state, String incomingNumber) { // <6>
      switch (state) {
      case TelephonyManager.CALL_STATE_IDLE:
        textOut.append("\nPhone idle");
        dbHelper.log("MyPhoneListener", "Phone idle");
        break;
      case TelephonyManager.CALL_STATE_OFFHOOK:
        textOut.append("\nPhone off hook");
        dbHelper.log("MyPhoneListener", "Phone off hook");
        break;
      case TelephonyManager.CALL_STATE_RINGING:
        textOut.append("\nIncoming call from " + incomingNumber);
        dbHelper.log("MyPhoneListener", "Incoming call from " + incomingNumber);
        break;
      }
    }

  }

}
