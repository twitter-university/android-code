
package com.marakana.android.fibonacciservice;

import android.os.RemoteException;
import android.util.Log;

import com.marakana.android.fibonaccicommon.FibonacciRequest;
import com.marakana.android.fibonaccicommon.FibonacciResponse;
import com.marakana.android.fibonaccicommon.IFibonacciService;
import com.marakana.android.fibonaccicommon.IFibonacciServiceResponseListener;
import com.marakana.android.fibonaccinative.FibLib;

public class IFibonacciServiceImpl extends IFibonacciService.Stub {
    private static final String TAG = "IFibonacciServiceImpl";

    @Override
    public void fib(FibonacciRequest request, IFibonacciServiceResponseListener listener)
            throws RemoteException {
        long n = request.getN();
        Log.d(TAG, "fib(" + n + ")");
        long timeInMillis = System.currentTimeMillis();
        long result;
        switch (request.getType()) {
            case FibonacciRequest.ITERATIVE_JAVA_TYPE:
                result = FibLib.fibJI(n);
                break;
            case FibonacciRequest.RECURSIVE_JAVA_TYPE:
                result = FibLib.fibJR(n);
                break;
            case FibonacciRequest.ITERATIVE_NATIVE_TYPE:
                result = FibLib.fibNI(n);
                break;
            case FibonacciRequest.RECURSIVE_NATIVE_TYPE:
                result = FibLib.fibNR(n);
                break;
            default:
                result = 0;
        }
        timeInMillis = System.currentTimeMillis() - timeInMillis;
        Log.d(TAG, String.format("Got fib(%d) = %d in %d ms", n, result, timeInMillis));
        listener.onResponse(new FibonacciResponse(result, timeInMillis));
    }
}
