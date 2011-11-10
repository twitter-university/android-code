package com.marakana;

import java.util.List;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.TextView;

public class WifiActivity extends Activity {
  TextView textOut;
  WifiManager wifiManager;
  ScanReceiver scanReceiver;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.standard);

    // Find views and setup title
    ((TextView) findViewById(R.id.textTitle)).setText("Wifi");
    textOut = (TextView) findViewById(R.id.textOut);

    // Register scan receiver
    scanReceiver = new ScanReceiver();  // <1>
    registerReceiver(scanReceiver, new IntentFilter(
        WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)); // <2>

    // Get wifi manager
    wifiManager = (WifiManager) getSystemService(WIFI_SERVICE); // <3>
    wifiManager.startScan(); // non-blocking call <4>

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unregisterReceiver(scanReceiver); // <5>
  }

  // Scan receiver
  class ScanReceiver extends BroadcastReceiver { // <6>

    @Override
    public void onReceive(Context context, Intent intent) { // <7>
      textOut.setText("");

      // List configured networks
      List<WifiConfiguration> confs = wifiManager.getConfiguredNetworks(); // <8>
      for (WifiConfiguration conf : confs) { // <9>
        textOut.append("\n" + conf.SSID);
      }

      textOut.append("\n\nAvailable networks:");
      List<ScanResult> list = wifiManager.getScanResults(); // <10>
      for (ScanResult result : list) {
        textOut.append("\n" + result.SSID);
      }
    }

  }
}
