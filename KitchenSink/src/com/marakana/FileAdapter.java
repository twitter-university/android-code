package com.marakana;

import java.io.File;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FileAdapter extends ArrayAdapter<File> {
  File[] files;
  LayoutInflater inflater;

  public FileAdapter(Context context, File[] files) {
    super(context, -1, files);
    this.files = files;
    inflater = ((Activity) context).getLayoutInflater();
  }

  // Called for mapping each file to its row in the list
  @Override
  public View getView(int position, View row, ViewGroup parent) {
    File file = files[position];

    // Initialize the row if doesn't exist already
    if (row == null) {
      row = inflater.inflate(R.layout.row, null);
    }
    TextView textValue = (TextView) row.findViewById(R.id.textValue);

    // Specify the text for this row
    String text = "";
    if (file.isDirectory()) {
      text = file.getName();
    } else {
      text = String.format("%s [%d] (%s)", file.getName(), file.length(),
          DateUtils.getRelativeTimeSpanString(file.lastModified()));
    }

    if (file.canRead()) {
      textValue.setTextColor(Color.GREEN);
    } else {
      textValue.setTextColor(Color.RED);
    }

    // Update the view
    textValue.setText(text);

    return row;
  }

}
