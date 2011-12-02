package com.marakana;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FileAdapter extends ArrayAdapter<File> {
  Context context;
  File[] files;

  public FileAdapter(Context context, File[] files) {
    super(context, -1, files);
    this.context = context;
    this.files = files;
  }

  // Called each time adapter is setting a single row with its data
  @Override
  public View getView(int position, View row, ViewGroup parent) {
    // Inflate new row if not already initialized
    if (row == null) {
      LayoutInflater inflater = ((Activity) context).getLayoutInflater();
      row = inflater.inflate(R.layout.row, null);
    }

    // Get current file
    File file = files[position];

    // Update the row
    TextView textFileName = (TextView) row.findViewById(R.id.textFileName);
    if (file.isDirectory()) {
      textFileName.setText(file.getPath() + " [dir]");
      textFileName.setTextColor(Color.BLUE);
    } else {
      textFileName.setText(String
          .format("%s [%d bytes]", file.getPath(), file.length()));
      if (file.canRead())
        textFileName.setTextColor(Color.GREEN);
    }

    return row;
  }

}
