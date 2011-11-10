package com.marakana;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class LocationActivity extends Activity implements LocationListener {
  LocationManager locationManager;
  Geocoder geocoder;
  TextView textOut;
  DbHelper dbHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.standard);
    ((TextView) findViewById(R.id.textTitle)).setText("Location");
    textOut = (TextView) findViewById(R.id.textOut);

    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    geocoder = new Geocoder(this);

    // Initialize with the last known location
    Location lastLocation = locationManager
        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
    if (lastLocation != null)
      onLocationChanged(lastLocation);

    dbHelper = new DbHelper(this);
  }

  @Override
  protected void onResume() {
    super.onRestart();
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000,
        10, this);
  }

  @Override
  protected void onPause() {
    super.onPause();
    locationManager.removeUpdates(this);
  }

  public void onLocationChanged(Location location) {
    String text = String.format(
        "Lat:\t %f\nLong:\t %f\nAlt:\t %f\nBearing:\t %f", location
            .getLatitude(), location.getLongitude(), location.getAltitude(),
        location.getBearing());
    textOut.setText(text);
    try {
      List<Address> addresses = geocoder.getFromLocation(
          location.getLatitude(), location.getLongitude(), 10);
      for (Address address : addresses) {
        textOut.append("\n" + address.getAddressLine(0));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Log, for fun
    dbHelper.log("LocationFix", text);
  }

  public void onProviderDisabled(String provider) {
  }

  public void onProviderEnabled(String provider) {
  }

  public void onStatusChanged(String provider, int status, Bundle extras) {
  }

}
