
package com.marakana.android.logserviceclient;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.marakana.android.service.log.LogManager;

public class LogActivity extends Activity implements Runnable, OnClickListener {

  private TextView output;

  private Handler handler;

  private LogManager logManager;

  public void onCreate(Bundle savedInstanceState) {
    this.logManager = LogManager.getInstance();
    super.onCreate(savedInstanceState);
    super.setContentView(R.layout.log);
    this.output = (TextView)super.findViewById(R.id.output);
    Button button = (Button)super.findViewById(R.id.button);
    button.setOnClickListener(this);
    this.handler = new Handler();
  }

  private void updateOutput() {
    this.output.setText(super.getString(R.string.log_utilization_message, 
      this.logManager.getUsedLogSize(), this.logManager.getTotalLogSize()));
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
    this.logManager.flushLog();
    this.updateOutput();
  }

  public void run() {
    this.updateOutput();
    this.handler.postDelayed(this, 1000);
  }
}
