package com.marakana.android.lifecycle;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SystemServicesDemo extends Activity implements LocationListener {
  static final String TAG = "SystemServicesDemo";
  static final String PROVIDER = LocationManager.GPS_PROVIDER;
  LocationManager locationManager;
  TextView locationText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.system_services_demo);
    locationText = (TextView) findViewById(R.id.text_location);

    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    Location location = locationManager.getLastKnownLocation(PROVIDER);
    if (location != null) {
      Log.d(TAG, "onCreated with location: " + printLocation(location));
    } else {
      Log.d(TAG, "onCreated without previously known location");
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    locationManager.requestLocationUpdates(PROVIDER, 10, 10, this);
    Log.d(TAG, "onResumed and starting to listen to LocationManager updates");
  }

  @Override
  protected void onPause() {
    super.onPause();
    locationManager.removeUpdates(this);
    Log.d(TAG, "onPaused and no longer listening to LocationManager updates");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "onDestroy");
  }

  // --- LocationListener callback methods
  public void onLocationChanged(Location location) {
    Log.d(TAG, "onLocationChanged: " + printLocation(location));
  }

  public void onProviderDisabled(String provider) {
  }

  public void onProviderEnabled(String provider) {
  }

  public void onStatusChanged(String provider, int status, Bundle extras) {
  }

  // --- Private helper method
  private String printLocation(Location location) {
    String locationString = String.format("[%3.6f,%3.6f]",
        location.getLongitude(), location.getLatitude());
    locationText.setText(locationString);
    return locationString;
  }
}
