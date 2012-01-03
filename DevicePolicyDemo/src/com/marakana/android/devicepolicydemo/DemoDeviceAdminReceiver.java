package com.marakana.android.devicepolicydemo;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


public class DemoDeviceAdminReceiver extends DeviceAdminReceiver {
	static final String TAG = "DemoDeviceAdminReceiver";
	
	@Override
	public void onEnabled(Context context, Intent intent) {
		super.onEnabled(context, intent);
		Toast.makeText(context, R.string.device_admin_enabled, Toast.LENGTH_LONG).show();
		Log.d(TAG, "onEnabled");
	}

	@Override
	public void onDisabled(Context context, Intent intent) {
		super.onDisabled(context, intent);
		Toast.makeText(context, R.string.device_admin_disabled, Toast.LENGTH_LONG).show();
		Log.d(TAG, "onDisabled");
	}
	
	
	
}
