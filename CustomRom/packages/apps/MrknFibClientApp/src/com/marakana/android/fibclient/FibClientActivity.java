package com.marakana.android.fibclient;

import android.app.Activity;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ServiceManager;
import android.util.Log;
import android.widget.TextView;

import com.marakana.android.fibservice.IFibService;

public class FibClientActivity extends Activity {
    private TextView output;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(this.output = new TextView(this));
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
           IFibService fibService = IFibService.Stub.asInterface(
               (IBinder)ServiceManager.getService("FibService"));
           this.output.setText("fib(10)=" + fibService.fib(10));
        } catch (Exception e) {
            Log.wtf("FibClientActivity", "Failed to get fib", e);
        }
    }
}
