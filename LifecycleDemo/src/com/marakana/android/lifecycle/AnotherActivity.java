package com.marakana.android.lifecycle;

import android.app.Activity;
import android.os.Bundle;

public class AnotherActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.another_activity);
  }

}
