package com.marakana;

import java.util.List;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SensorActivity extends Activity implements SensorEventListener {
  TextView textOut;
  SensorManager sensorManager;
  Sensor sensor;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.standard);

    // Find views
    ((TextView) findViewById(R.id.textTitle)).setText("Sensor");
    textOut = (TextView) findViewById(R.id.textOut);
    textOut.setText("");

    // Get sensor manager
    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

    // List sensors
    List<Sensor> list = sensorManager.getSensorList(Sensor.TYPE_ALL);
    Log.d("SensorActivity", "found sensors: " + list.size());
    for (Sensor currentSensor : list) {
      textOut.append("\n" + currentSensor.getName());
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
  }

  @Override
  protected void onPause() {
    super.onPause();
    sensorManager.unregisterListener(this);
  }

  public void onAccuracyChanged(Sensor sensor, int accuracy) {

  }

  // Called when sensor changes
  public void onSensorChanged(SensorEvent event) {
    String out = String.format("X: %.2f\nY: %.2f\nZ: %.2f", event.values[0],
        event.values[1], event.values[2]);
    textOut.setText( out );
  }

}
