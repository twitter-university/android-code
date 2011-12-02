package com.marakana.android.fibonacci;

import android.util.Log;

public class FibLib {
	private static final String TAG = "FibLib";

	private static long fibJRhelper(long n) {
		return n <= 0 ? 0 : n == 1 ? 1 : fibJRhelper(n - 1)
				+ fibJRhelper(n - 2);
	}

	public static long fibJR(long n) {
		Log.d(TAG, "Running fibJR(" + n + ")");
		return fibJRhelper(n);
	}

	public static long fibJI(long n) {
		Log.d(TAG, "Running fibJI(" + n + ")");
		long previous = -1;
		long result = 1;
		for (long i = 0; i <= n; i++) {
			long sum = result + previous;
			previous = result;
			result = sum;
		}
		return result;
	}

	public native static long fibNR(long n);

	public native static long fibNI(long n);

	static {
		System.loadLibrary("com_marakana_android_fibonacci_FibLib");
	}
}
