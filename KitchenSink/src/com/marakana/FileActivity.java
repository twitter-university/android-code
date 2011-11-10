package com.marakana;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class FileActivity extends Activity implements OnItemClickListener {
  ListView list;
  FileAdapter adapter;
  File[] files;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.files);

    // Find view
    list = (ListView) findViewById(R.id.list);
    list.setOnItemClickListener(this);

    // Setup adapter
    update(new File("/"));
  }

  public void onItemClick(AdapterView<?> parent, View view, int position,
      long id) {
    File file = files[position];
    if (file.isDirectory())
      update(file);
  }

  private void update(File file) {
    files = file.listFiles();
    adapter = new FileAdapter(this, files);
    list.setAdapter(adapter);
  }

}
