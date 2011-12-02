
package com.marakana.android.fibonaccinative;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class FibonacciActivity extends Activity implements OnClickListener {

    private EditText input; // our input n

    private RadioGroup type; // fibonacci implementation type

    private TextView output; // destination for fibonacci result

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.main);
        // connect to our UI elements
        this.input = (EditText)super.findViewById(R.id.input);
        this.type = (RadioGroup)super.findViewById(R.id.type);
        this.output = (TextView)super.findViewById(R.id.output);
        Button button = (Button)super.findViewById(R.id.button);
        // request button click call-backs via onClick(View) method
        button.setOnClickListener(this);
    }

    // handle button clicks
    public void onClick(View view) {
        // parse n from input (or report errors)
        final long n;
        String s = this.input.getText().toString();
        if (TextUtils.isEmpty(s)) {
            return;
        }
        try {
            n = Long.parseLong(s);
        } catch (NumberFormatException e) {
            this.input.setError(super.getText(R.string.input_error));
            return;
        }
        // showing the user that the calculation is in progress
        final ProgressDialog dialog = ProgressDialog.show(this, "", super
                .getText(R.string.progress_text), true);
        // since the calculation can take a long time, we do it in a separate
        // thread to avoid blocking the UI
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                // this method runs in a background thread
                long result = 0;
                long t = System.currentTimeMillis(); // measure the time
                // call into our library (based on the type selection)
                switch (FibonacciActivity.this.type.getCheckedRadioButtonId()) {
                    case R.id.type_fib_jr:
                        result = FibLib.fibJR(n);
                        break;
                    case R.id.type_fib_ji:
                        result = FibLib.fibJI(n);
                        break;
                    case R.id.type_fib_nr:
                        result = FibLib.fibNR(n);
                        break;
                    case R.id.type_fib_ni:
                        result = FibLib.fibNI(n);
                        break;
                }
                // measure the time difference
                t = System.currentTimeMillis() - t;
                // generate the result
                return String.format("fibonacci(%d)=%d\nin %d ms", n, result, t);
            }

            @Override
            protected void onPostExecute(String result) {
                // get rid of the dialog
                dialog.dismiss();
                // show the result to the user
                FibonacciActivity.this.output.setText(result);
            }
        }.execute(); // run our AsyncTask
    }
}
