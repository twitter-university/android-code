package org.example.webviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class WebViewDemo extends Activity {
	private WebView webView;
	private EditText urlField;
	private Button goButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Create reference to UI elements
		urlField = (EditText) findViewById(R.id.url);
		goButton = (Button) findViewById(R.id.go_button);
		webView = (WebView) findViewById(R.id.webview_compontent);
		// workaround so that the default browser doesn't take over
		// webView.setWebViewClient(new MyWebViewClient());

		// Create reference to UI elements
		urlField = (EditText) findViewById(R.id.url);
		goButton = (Button) findViewById(R.id.go_button);

		// Setup click listener
		goButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				openURL();
			}
		});

		// Setup key listener
		urlField.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View view, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					openURL();
					return true;
				} else {
					return false;
				}
			}
		});

	}

	/** Opens the URL in a browser */
	private void openURL() {
		webView.loadUrl(urlField.getText().toString());
		// webView.requestFocus();
	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
}
