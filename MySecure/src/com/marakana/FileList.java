package com.marakana;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FileList extends Activity implements OnItemClickListener, OnClickListener {
  ListView listFiles;
  FileAdapter adapter;
  File[] files; // full path to the file
  File file; // current file
  File SDCARD = new File("/sdcard/");
  File ROOT   = new File("/");
  File HOME;
 
  Button buttonUp, buttonSDCard, buttonHome, buttonRoot;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.filelist);

    HOME   = this.getFilesDir();
    
    // Find buttons
    buttonUp = (Button) findViewById(R.id.buttonUp);
    buttonUp.setOnClickListener(this);
    
    buttonSDCard = (Button) findViewById(R.id.buttonSDCard);
    buttonSDCard.setOnClickListener(this);
    
    buttonHome = (Button) findViewById(R.id.buttonHome);
    buttonHome.setOnClickListener(this);
    
    buttonRoot = (Button) findViewById(R.id.buttonRoot);
    buttonRoot.setOnClickListener(this);

    // Find list view and set it up
    listFiles = (ListView) findViewById(R.id.listFiles);
    listFiles.setOnItemClickListener(this);

    // Show / initially
    file = new File("/");
    updateList(file);
  }

  public void onItemClick(AdapterView<?> listView, View row, int position,
      long id) {
    // Get the item that was clicked on
    file = adapter.getItem(position);

    // Check if it's a directory
    if (file.isDirectory() && file.canRead()) {
      updateList(file);
    } else {
      Toast
          .makeText(this, "Not directory or no permissions", Toast.LENGTH_LONG)
          .show();
    }
  }

  private void updateList(File parent) {
    // Update the list of files for that directory
    files = parent.listFiles();

    // Update the adapter
    adapter = new FileAdapter(this, files);
    listFiles.setAdapter(adapter);
  }

  public void onClick(View button) {
    switch(button.getId()) {
    case R.id.buttonUp:
      file = (file.getParentFile()!=null)?file.getParentFile():file;
      updateList(file);
      break;
    case R.id.buttonSDCard:
      updateList(SDCARD);
      break;
    case R.id.buttonHome:
      updateList(HOME);
      break;
    case R.id.buttonRoot:
      updateList(ROOT);
    }
  }

}
