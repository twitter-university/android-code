package com.marakana.android.loglibclient;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.marakana.android.lib.log.LibLog;

public class LogActivity extends Activity 
  implements View.OnClickListener, Runnable {

  private TextView output;
  private Handler handler;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    super.setContentView(R.layout.log);
    this.output = (TextView) super.findViewById(R.id.output);
    Button button = (Button) super.findViewById(R.id.button);
    button.setOnClickListener(this);
    this.handler = new Handler();
  }

  private void updateOutput() {
    this.output.setText(
      super.getString(R.string.log_utilization_message,      
      LibLog.getUsedLogSize(), LibLog.getTotalLogSize()));
  }

  @Override
  public void onResume() {
    super.onResume();
    this.handler.post(this);
  }
 
  @Override
  public void onPause() {
    super.onPause();
    this.handler.removeCallbacks(this);
  }

  public void onClick(View view) {
    LibLog.flushLog();
    this.updateOutput();
  }

  public void run() {
    this.updateOutput();
    this.handler.postDelayed(this, 1000);
  }
}
