
package com.marakana.android.audioplayerdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AudioPlayerDemoActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void startPausePlaying(View v) {
        Intent i = new Intent(this, AudioPlayerDemoService.class);
        i.setAction(AudioPlayerDemoService.ACTION_PLAY_PAUSE);
        super.startService(i);
    }

    public void stopPlaying(View v) {
        Intent i = new Intent(this, AudioPlayerDemoService.class);
        i.setAction(AudioPlayerDemoService.ACTION_STOP);
        super.startService(i);
    }
}
