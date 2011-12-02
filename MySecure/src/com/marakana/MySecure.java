package com.marakana;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.provider.Contacts;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MySecure extends Activity implements OnClickListener {
  public static final String TAG = "MySecure";
  Button buttonStartService, buttonStopService;
  Button buttonPrefs, buttonBackup, buttonFileList, buttonAppList;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    // Find buttons
    buttonStartService = (Button) findViewById(R.id.buttonStartService);
    buttonStartService.setOnClickListener(this);

    buttonStopService = (Button) findViewById(R.id.buttonStopService);
    buttonStopService.setOnClickListener(this);

    buttonPrefs = (Button) findViewById(R.id.buttonPrefs);
    buttonPrefs.setOnClickListener(this);

    buttonBackup = (Button) findViewById(R.id.buttonBackup);
    buttonBackup.setOnClickListener(this);

    buttonFileList = (Button) findViewById(R.id.buttonFileList);
    buttonFileList.setOnClickListener(this);

    buttonAppList = (Button) findViewById(R.id.buttonAppList);
    buttonAppList.setOnClickListener(this);
  }

  @Override
  public void onStop() {
    super.onStop();
  }

  public void onClick(View v) {
    switch (v.getId()) {
    case R.id.buttonStartService:
      startService(new Intent(this, HeartbeatService.class));
      break;
    case R.id.buttonStopService:
      stopService(new Intent(this, HeartbeatService.class));
      break;
    case R.id.buttonPrefs:
      startActivity(new Intent(this, Prefs.class));
      break;
    case R.id.buttonBackup:
      Backup backup = new Backup(this);
      backup.runBackup(Contacts.People.CONTENT_URI);
      backup.runBackup(Settings.System.CONTENT_URI);
      backup.runBackup(Browser.BOOKMARKS_URI);
      backup.runDBBackup(HeartbeatDBHelper.TABLE);
      break;
    case R.id.buttonFileList:
      startActivity(new Intent(this, FileList.class));
      break;
    case R.id.buttonAppList:
      startActivity(new Intent(this, AppList.class));
      break;
    }
  }

  /** Initializes the menu. Called only once, first time user clicks on menu. **/
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case R.id.menuCallSupport:
      startActivity(new Intent(Intent.ACTION_CALL, Uri
          .parse("tel:1-800-555-1212")));
      break;
    }
    return true;
  }
}