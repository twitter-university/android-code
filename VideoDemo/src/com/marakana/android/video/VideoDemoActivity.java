package com.marakana.android.video;

import android.app.Activity;
import android.os.Bundle;
import android.widget.VideoView;

public class VideoDemoActivity extends Activity {
	VideoView videoView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		videoView = (VideoView) findViewById(R.id.video_view);
		videoView
				.setVideoPath("http://people.xiph.org/~maikmerten/demos/BigBuckBunny.ogv");
		videoView.start();
	}
}