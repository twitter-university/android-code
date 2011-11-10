package com.marakana;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class KitchenSink extends Activity implements OnClickListener {

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    // Find buttons <1>
    ((Button) findViewById(R.id.buttonStartService)).setOnClickListener(this);
    ((Button) findViewById(R.id.buttonStopService)).setOnClickListener(this);
    ((Button) findViewById(R.id.buttonLogs)).setOnClickListener(this);
    ((Button) findViewById(R.id.buttonSensor)).setOnClickListener(this);
    ((Button) findViewById(R.id.buttonTelephony)).setOnClickListener(this);
    ((Button) findViewById(R.id.buttonWifi)).setOnClickListener(this);
    ((Button) findViewById(R.id.buttonFiles)).setOnClickListener(this);
    ((Button) findViewById(R.id.buttonLocation)).setOnClickListener(this);
    ((Button) findViewById(R.id.buttonFib)).setOnClickListener(this);
    ((Button) findViewById(R.id.buttonLogger)).setOnClickListener(this);
  }

  // Called when button is clicked <2>
  public void onClick(View v) {
    Log.d("KitchenSink", "onClick'd");

    switch (v.getId()) {
    case R.id.buttonStartService:
      Toast.makeText(this, "Starting service...", Toast.LENGTH_LONG).show();
      startService(new Intent(this, HeartbeatService.class));
      break;
    case R.id.buttonStopService:
      Toast.makeText(this, "Stopping service...", Toast.LENGTH_LONG).show();
      stopService(new Intent(this, HeartbeatService.class));
      break;
    case R.id.buttonLogs:
      startActivity(new Intent(this, LogsActivity.class));
      break;
    case R.id.buttonSensor:
      startActivity(new Intent(this, SensorActivity.class));
      break;
    case R.id.buttonTelephony:
      startActivity(new Intent(this, TelephonyActivity.class));
      break;
    case R.id.buttonWifi:
      startActivity(new Intent(this, WifiActivity.class));
      break;
    case R.id.buttonFiles:
      startActivity(new Intent(this, FileActivity.class));
      break;
    case R.id.buttonLocation:
      startActivity(new Intent(this, LocationActivity.class));
      break;
    case R.id.buttonFib:
      startActivity(new Intent(this, FibActivity.class));
      break;
    case R.id.buttonLogger:
      startActivity(new Intent(this, LoggerActivity.class));
      break;
    }
  }

}