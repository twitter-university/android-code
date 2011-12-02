package com.marakana;

import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ListView;

public class AppList extends Activity {
  ListView listApps;
  PackageManager pm;
  PackageInfo[] apps;
  AppAdapter adapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.applist);

    // Find views
    listApps = (ListView) findViewById(R.id.listApps); // View

    // Get package manager
    pm = this.getPackageManager();
    List<PackageInfo> appsList = pm.getInstalledPackages(pm.GET_META_DATA);
    apps = new PackageInfo[appsList.size()];
    apps = appsList.toArray(apps);// Model

    // Setup adapter
    adapter = new AppAdapter(this, apps); // Controller
    listApps.setAdapter(adapter);
  }
}
