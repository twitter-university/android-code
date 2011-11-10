package com.marakana;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FibActivity extends Activity implements OnClickListener {
  TextView textOut;
  Button buttonGo;
  EditText editN;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fib);

    // Find views
    ((TextView) findViewById(R.id.textTitle)).setText("Fib");
    textOut = (TextView) findViewById(R.id.textOut);
    buttonGo = (Button) findViewById(R.id.buttonGo);
    editN = (EditText) findViewById(R.id.editN);

    // Implement the button listener
    buttonGo.setOnClickListener(this);
  }

  public void onClick(View v) {
    // Figure out N
    int n = 0;
    try {
      n = Integer.parseInt(editN.getText().toString());  // <1>
    } catch (NumberFormatException e) {
      textOut.setText("Invalid N. \nPlease try again.");
      return;
    }

    // Do calculations for Java
    long start = System.currentTimeMillis();  // <2>
    int fibJ = FibLib.fibJ(n);  // <3>
    long timeJ = System.currentTimeMillis() - start; // <4>

    // Do calculations for Native
    start = System.currentTimeMillis();
    int fibN = FibLib.fibN(n);  // <5>
    long timeN = System.currentTimeMillis() - start;

    // Report results
    String result = String.format(
        "fibJ(%d)=%d (%dms)\n" + "fibN(%d)=%d (%dms)", n, fibJ, timeJ, n, fibN,
        timeN); // <6>
    textOut.setText(result);
  }
}
