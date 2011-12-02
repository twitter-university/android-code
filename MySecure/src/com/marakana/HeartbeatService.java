package com.marakana;

import winterwell.jtwitter.Twitter;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class HeartbeatService extends Service implements LocationListener {
  public static final String TAG = "HeartbeatService";
  public static final long DELAY = 600000; // 10 min
  Twitter twitter;
  SQLiteDatabase db;
  SharedPreferences prefs;
  Handler handler = null;
  Heartbeat heartbeat;
  LocationManager locationManager;
  Location location;
  String bestProvider;

  @Override
  public void onCreate() {
    super.onCreate();

    prefs = PreferenceManager.getDefaultSharedPreferences(this);

    // Register to get notified when preferences change
    prefs
        .registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {
          public void onSharedPreferenceChanged(
              SharedPreferences sharedPreferences, String key) {
            twitter = null;
            Log.d(TAG, "Preferences Changed!");
          }
        });

    // Open the database
    HeartbeatDBHelper dbHelper = new HeartbeatDBHelper(this);
    db = dbHelper.getWritableDatabase();
    
    // Get location manager
    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

    Log.d(TAG, "onCreate'd");
  }

  // Called when service is starting
  @Override
  public void onStart(Intent intent, int startId) {
    super.onStart(intent, startId);

    // Register for location updates
    setupLocationUpdates();

    // Setup heartbeats to run on handler
    if (handler == null) {
      handler = new Handler();
      heartbeat = new Heartbeat();
      handler.post(heartbeat);
    }

    Toast.makeText(this, "Heartbeat Started", Toast.LENGTH_LONG).show();
  }

  // Called when service is about to be stopped
  @Override
  public void onDestroy() {
    super.onDestroy();
    handler.removeCallbacks(heartbeat);
    locationManager.removeUpdates(this);
    handler = null;
    twitter = null;
    Toast.makeText(this, "Heartbeat Stopped", Toast.LENGTH_LONG).show();
  }

  @Override
  public IBinder onBind(Intent i) {
    return null;
  }

  // Lazy initialization of Twitter, useful when Prefs change
  private Twitter getTwitter() {
    if (twitter == null) {
      String username = prefs.getString("username", "");
      String password = prefs.getString("password", "");
      twitter = new Twitter(username, password);
      Log.d(TAG, String.format("getTwitter reinitilized with %s/%s", username,
          password));
    }
    return twitter;
  }

  // The runnable that wakes up every so often and sends
  // a heart beat to Twitter
  class Heartbeat implements Runnable {
    String msg;
    ContentValues values = new ContentValues();

    // Runs over and over again
    public void run() {
      // Log to DB as well
      values.put(HeartbeatDBHelper.C_RECORDED_AT, System.currentTimeMillis());
      values.put(HeartbeatDBHelper.C_LAT, 
          (location!=null)?location.getLatitude():0.0);
      values.put(HeartbeatDBHelper.C_LONG, 
          (location!=null)?location.getLongitude():0.0);
      db.insert(HeartbeatDBHelper.TABLE, null, values);

      // Log to Twitter, if available
      if (getTwitter().isValidLogin()) {
        if(location!=null)
        msg = String.format("HB from (%.5f,%.5f)", location.getLatitude(),
            location.getLongitude());
        else msg = "Location unknown";
        getTwitter().updateStatus(msg);
        Log.d(TAG, "Heartbeat sent: " + msg);        
      }
      
      // Run this code again after DELAY
      handler.postDelayed(this, DELAY);
    }
  }

  public void onLocationChanged(Location location) {
    this.location = location;
    Log.d(TAG, "onLocationChanged with Location: " + location);
  }

  public void onProviderDisabled(String provider) {
    //setupLocationUpdates();
  }

  public void onProviderEnabled(String provider) {
    //setupLocationUpdates();
  }

  // Called to setup location updates
  private void setupLocationUpdates() {
    locationManager.removeUpdates(this);
    bestProvider = locationManager.getBestProvider(new Criteria(), true);
    location = locationManager.getLastKnownLocation(bestProvider);
    locationManager.requestLocationUpdates(bestProvider, DELAY, 100, this);
    Log.d(TAG, "setupLocationUpdates with Location: " + location);
  }

  public void onStatusChanged(String provider, int status, Bundle extras) {
  }
}
