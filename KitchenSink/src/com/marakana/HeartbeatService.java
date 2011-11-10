package com.marakana;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class HeartbeatService extends Service {
  public static final String TAG = "HeartbeatService";
  private static final int DELAY = 10000; // 10sec
  Handler handler; // handler to the message queue
  UpdateRunnable updateRunnable; // unit of work

  DbHelper dbHelper;

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onCreate() {
    super.onCreate();

    // Open up the database
    dbHelper = new DbHelper(this);

    // Initialize handler and job
    handler = new Handler();
    updateRunnable = new UpdateRunnable();

    // Post job to be done
    handler.post(updateRunnable);

    dbHelper.log(TAG, "onCreate'd");
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    // Remove work from the queue
    handler.removeCallbacks(updateRunnable);

    dbHelper.log(TAG, "onDestroy'd");
  }

  @Override
  public void onStart(Intent intent, int startId) {
    super.onStart(intent, startId);
    dbHelper.log(TAG, "onStart'd");
  }

  // A unit of work to be done
  class UpdateRunnable implements Runnable {
    public void run() {
      dbHelper.log(TAG, "Update ran");

      // Repost the job
      handler.postDelayed(this, DELAY);
    }

  }
}
