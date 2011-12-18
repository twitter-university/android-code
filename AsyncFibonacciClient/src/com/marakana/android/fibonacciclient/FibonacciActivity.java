
package com.marakana.android.fibonacciclient;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.marakana.android.fibonaccicommon.FibonacciRequest;
import com.marakana.android.fibonaccicommon.FibonacciResponse;
import com.marakana.android.fibonaccicommon.IFibonacciService;
import com.marakana.android.fibonaccicommon.IFibonacciServiceResponseListener;

public class FibonacciActivity extends Activity implements OnClickListener, ServiceConnection {

    private static final String TAG = "FibonacciActivity";

    // the id of a message to our response handler
    private static final int RESPONSE_MESSAGE_ID = 1;

    // the id of a progress dialog that we'll be creating
    private static final int PROGRESS_DIALOG_ID = 1;

    private EditText input; // our input n

    private Button button; // trigger for fibonacci calcualtion

    private RadioGroup type; // fibonacci implementation type

    private TextView output; // destination for fibonacci result

    private IFibonacciService service; // reference to our service

    // the responsibility of the responseHandler is to take messages
    // from the responseListener (defined below) and display their content
    // in the UI thread
    private final Handler responseHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case RESPONSE_MESSAGE_ID:
                    Log.d(TAG, "Handling response");
                    FibonacciActivity.this.output.setText((String) message.obj);
                    FibonacciActivity.this.removeDialog(PROGRESS_DIALOG_ID);
                    break;
            }
        }
    };

    // the responsibility of the responseListener is to receive call-backs
    // from the service when our FibonacciResponse is available
    private final IFibonacciServiceResponseListener responseListener = new IFibonacciServiceResponseListener.Stub() {

        // this method is executed on one of the pooled binder threads
        @Override
        public void onResponse(FibonacciResponse response) throws RemoteException {
            String result = String.format("%d in %d ms", response.getResult(),
                    response.getTimeInMillis());
            Log.d(TAG, "Got response: " + result);
            // since we cannot update the UI from a non-UI thread,
            // we'll send the result to the responseHandler (defined above)
            Message message = FibonacciActivity.this.responseHandler.obtainMessage(
                    RESPONSE_MESSAGE_ID, result);
            FibonacciActivity.this.responseHandler.sendMessage(message);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.main);
        // connect to our UI elements
        this.input = (EditText) super.findViewById(R.id.input);
        this.button = (Button) super.findViewById(R.id.button);
        this.type = (RadioGroup) super.findViewById(R.id.type);
        this.output = (TextView) super.findViewById(R.id.output);
        // request button click call-backs via onClick(View) method
        this.button.setOnClickListener(this);
        // the button will be enabled once we connect to the service
        this.button.setEnabled(false);
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart()'ed");
        super.onStart();
        // Bind to our FibonacciService service, by looking it up by its name
        // and passing ourselves as the ServiceConnection object
        // We'll get the actual IFibonacciService via a callback to
        // onServiceConnected() below
        if (!super.bindService(new Intent(IFibonacciService.class.getName()), this,
                BIND_AUTO_CREATE)) {
            Log.w(TAG, "Failed to bind to service");
        }
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop()'ed");
        super.onStop();
        // No need to keep the service bound (and alive) any longer than
        // necessary
        super.unbindService(this);
    }

    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d(TAG, "onServiceConnected()'ed to " + name);
        // finally we can get to our IFibonacciService
        this.service = IFibonacciService.Stub.asInterface(service);
        // enable the button, because the IFibonacciService is initialized
        this.button.setEnabled(true);
    }

    public void onServiceDisconnected(ComponentName name) {
        Log.d(TAG, "onServiceDisconnected()'ed to " + name);
        // our IFibonacciService service is no longer connected
        this.service = null;
        // disabled the button, since we cannot use IFibonacciService
        this.button.setEnabled(false);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case PROGRESS_DIALOG_ID:
                // this dialog will be opened in onClick(...) and
                // dismissed/removed by responseHandler.handleMessage(...)
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage(super.getText(R.string.progress_text));
                dialog.setIndeterminate(true);
                return dialog;
            default:
                return super.onCreateDialog(id);
        }
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

        // build the request object
        int type;
        switch (FibonacciActivity.this.type.getCheckedRadioButtonId()) {
            case R.id.type_fib_jr:
                type = FibonacciRequest.RECURSIVE_JAVA_TYPE;
                break;
            case R.id.type_fib_ji:
                type = FibonacciRequest.ITERATIVE_JAVA_TYPE;
                break;
            case R.id.type_fib_nr:
                type = FibonacciRequest.RECURSIVE_NATIVE_TYPE;
                break;
            case R.id.type_fib_ni:
                type = FibonacciRequest.ITERATIVE_NATIVE_TYPE;
                break;
            default:
                return;
        }

        FibonacciRequest request = new FibonacciRequest(n, type);
        try {
            Log.d(TAG, "Submitting request...");
            // submit the request; the response will come to responseListener
            this.service.fib(request, this.responseListener);
            Log.d(TAG, "Submited request");
            // this dialog will be dismissed/removed by responseHandler
            super.showDialog(PROGRESS_DIALOG_ID);
        } catch (RemoteException e) {
            Log.wtf(TAG, "Failed to communicate with the service", e);
        }
    }
}
