package com.marakana.android.fibonacciservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class FibonacciService extends Service {
	private static final String TAG = "FibonacciService";
	private IFibonacciServiceImpl service;

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate()'d");
		super.onCreate();
		this.service = new IFibonacciServiceImpl(this);
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "onBind()'d");
		return this.service;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.d(TAG, "onUnbind()'d");
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestory()'d");
		super.onDestroy();
	}

}
