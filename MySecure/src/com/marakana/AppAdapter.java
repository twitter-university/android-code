package com.marakana;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AppAdapter extends ArrayAdapter<PackageInfo> {
  Context context;
  PackageInfo[] apps;
  String text;

  public AppAdapter(Context context, PackageInfo[] apps) {
    super(context, -1, apps);
    this.context = context;
    this.apps = apps;
  }

  // Called each time adapter is setting a single row with its data
  @Override
  public View getView(int position, View row, ViewGroup parent) {
    // Inflate new row if not already initialized
    if (row == null) {
      LayoutInflater inflater = ((Activity) context).getLayoutInflater();
      row = inflater.inflate(R.layout.row, null);
    }

    // Get current app
    PackageInfo app = apps[position];

    // Update the row
    TextView textFileName = (TextView) row.findViewById(R.id.textFileName);

    // Quick stats on the app (package)
    text = String.format("%s [%d/%s]",app.packageName, 
        app.versionCode, app.versionName);
    textFileName.setText(text);
    textFileName.setTextColor(Color.BLUE);

    Log.d("AppAdapter", app.toString());
    return row;
  }

}
