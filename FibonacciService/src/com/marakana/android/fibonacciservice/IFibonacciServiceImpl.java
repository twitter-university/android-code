package com.marakana.android.fibonacciservice;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.RemoteException;
import android.util.Log;

import com.marakana.android.fibonacci.FibLib;
import com.marakana.android.fibonaccicommon.IFibonacciService;

public class IFibonacciServiceImpl extends IFibonacciService.Stub {
	private static final long SLOW_N = 10;

	private static final String TAG = "IFibonacciServiceImpl";

	private final Context context;

	public IFibonacciServiceImpl(Context context) {
		this.context = context;
	}

	private long checkSlow(long n) {
		if (n > SLOW_N
				&& this.context
						.checkCallingPermission(Manifest.permission.USE_SLOW_FIBONACCI_SERVICE) != PackageManager.PERMISSION_GRANTED) {
			throw new SecurityException("You need to use "
					+ Manifest.permission.USE_SLOW_FIBONACCI_SERVICE);
		} else {
			return n;
		}
	}

	public long fibJI(long n) throws RemoteException {
		Log.d(TAG, "Running fibJI(" + n + ")");
		return FibLib.fibJI(n);
	}

	public long fibJR(long n) throws RemoteException {
		Log.d(TAG, "Running fibJR(" + n + ")");
		return FibLib.fibJR(checkSlow(n));
	}

	public long fibNI(long n) throws RemoteException {
		Log.d(TAG, "Running fibNI(" + n + ")");
		return FibLib.fibNI(n);
	}

	public long fibNR(long n) throws RemoteException {
		Log.d(TAG, "Running fibNR(" + n + ")");
		return FibLib.fibNR(checkSlow(n));
	}
}
