
package com.marakana.android.audioplayerdemo;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class AudioPlayerDemoService extends Service implements OnPreparedListener, OnErrorListener,
        OnAudioFocusChangeListener, OnCompletionListener {
    private static final String TAG = "AudioPlayerDemoService";

    private final IntentFilter AUDIO_BECOMING_NOISY_INTENT_FILTER = new IntentFilter(
            AudioManager.ACTION_AUDIO_BECOMING_NOISY);

    public static final String ACTION_PLAY_PAUSE = "com.marakana.android.audioplayerdemo.action.PLAY_PAUSE";

    public static final String ACTION_STOP = "com.marakana.android.audioplayerdemo.action.STOP";

    private Intent lastIntent;

    private MediaPlayer mediaPlayer;

    private AudioManager audioManager;

    private NoisyAudioReceiver noisyAudioReceiver;

    private ComponentName remoteControlReceiverName;

    @Override
    public void onCreate() {
        super.onCreate();
        this.audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        this.noisyAudioReceiver = new NoisyAudioReceiver();
        this.remoteControlReceiverName = new ComponentName(getApplicationContext(),
                RemoteControlReceiver.class);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(ACTION_PLAY_PAUSE)) {
            Log.d(TAG, "Received request to start/pause playback");
            if (this.mediaPlayer == null) {
                if (this.initMediaPlayer(intent)) {
                    this.lastIntent = intent;
                } else {
                    Log.e(TAG, "Failed to start playback. Stopping service.");
                    this.stopSelf();
                    return START_NOT_STICKY;
                }
            } else if (this.mediaPlayer.isPlaying()) {
                Log.e(TAG, "Pausing playback.");
                this.mediaPlayer.pause();
            } else {
                Log.e(TAG, "Starting playback.");
                this.mediaPlayer.start();
            }
        } else if (intent.getAction().equals(ACTION_STOP)) {
            Log.d(TAG, "Received request to stop playback");
            this.releaseMediaPlayer();
        }
        return START_REDELIVER_INTENT;
    }

    private boolean initMediaPlayer(Intent intent) {
        Log.d(TAG, "Initializing the media player");
        this.mediaPlayer = new MediaPlayer();
        this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            AssetFileDescriptor afd = super.getResources().openRawResourceFd(R.raw.test_cbr);
            try {
                this.mediaPlayer.setDataSource(afd.getFileDescriptor());
                Log.d(TAG, "Successfully set the data source");
            } finally {
                afd.close();
            }
        } catch (Exception e) {
            Log.wtf(TAG, "Failed to set data source", e);
            this.notifyUser("Failed to initialize audio stream");
            this.releaseMediaPlayer();
            return false;
        }
        this.mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        this.mediaPlayer.setOnErrorListener(this);
        this.mediaPlayer.setOnPreparedListener(this);
        this.mediaPlayer.prepareAsync(); // call onPrepared when prepared
        Log.d(TAG, "Waiting for prepare to finish");
        return true;
    }

    public void onPrepared(MediaPlayer mp) {
        Log.d(TAG, "Media player is ready (prepared). Requesting audio focus.");
        if (this.requestAudioFocus()) {
            Log.d(TAG, "Starting as foreground service");
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                    new Intent(getApplicationContext(), AudioPlayerDemoActivity.class),
                    PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification = new Notification(android.R.drawable.ic_media_play,
                    getText(R.string.foreground_service_notificaton_ticker_text),
                    System.currentTimeMillis());
            notification.setLatestEventInfo(getApplicationContext(),
                    getText(R.string.foreground_service_notification_title),
                    getText(R.string.foreground_service_notification_message), pendingIntent);
            notification.flags |= Notification.FLAG_ONGOING_EVENT;
            super.startForeground(1, notification);

            this.mediaPlayer.setOnCompletionListener(this);
            Log.d(TAG, "Starting playback");
            this.mediaPlayer.start();

            Log.d(TAG, "Registering for noisy audio events");
            super.registerReceiver(this.noisyAudioReceiver, AUDIO_BECOMING_NOISY_INTENT_FILTER);

            Log.d(TAG, "Registering for audio remote control");
            this.audioManager.registerMediaButtonEventReceiver(this.remoteControlReceiverName);
        } else {
            Log.w(TAG, "Failed to get audio focus");
            this.notifyUser("Failed to get audio focus");
            this.releaseMediaPlayer();
        }
    }

    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG, "Completed playback");
        this.notifyUser("Completed playback");
        this.releaseMediaPlayer();
    }

    // Called when MediaPlayer has encountered a problem from an async operation
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(TAG,
                String.format("Music player encountered an error: what=%d, extra=%d", what, extra));
        this.notifyUser("Music player encountered an error. Aborting.");
        this.releaseMediaPlayer();
        return true;
    }

    private boolean requestAudioFocus() {
        Log.d(TAG, "Requesting audio focus.");
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == this.audioManager.requestAudioFocus(this,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    private boolean abandonAudioFocus() {
        Log.d(TAG, "Abandoning audio focus.");
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == this.audioManager.abandonAudioFocus(this);
    }

    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                Log.d(TAG, "Re/gained focus.");
                this.startPlaybackAtFullVolume();
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                Log.d(TAG, "Lost focus for an unbounded amount of time.");
                this.releaseMediaPlayer();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                Log.d(TAG, "Lost focus for a short time.");
                this.pauseMediaPlayer();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                Log.d(TAG, "Lost focus for a short time. Can duck.");
                this.lowerVolume();
                break;
            default:
                Log.w(TAG, "Unexpected onAudioFocusChange(" + focusChange + ")");
        }
    }

    private void startPlaybackAtFullVolume() {
        if (this.mediaPlayer == null) {
            this.initMediaPlayer(this.lastIntent);
        } else if (!this.mediaPlayer.isPlaying()) {
            Log.d(TAG, "Resuming playback.");
            this.mediaPlayer.start();
        } else {
            Log.d(TAG, "Going back to full volume.");
            this.mediaPlayer.setVolume(1.0f, 1.0f);
        }
    }

    private boolean lowerVolume() {
        if (this.mediaPlayer != null && this.mediaPlayer.isPlaying()) {
            Log.d(TAG, "Lowering volume.");
            this.mediaPlayer.setVolume(0.1f, 0.1f);
            return true;
        } else {
            return false;
        }
    }

    private boolean pauseMediaPlayer() {
        if (this.mediaPlayer != null && this.mediaPlayer.isPlaying()) {
            Log.d(TAG, "Pausing playback.");
            this.mediaPlayer.pause();
            return true;
        } else {
            return false;
        }
    }

    private void releaseMediaPlayer() {
        if (this.mediaPlayer == null) {
            Log.d(TAG, "No media player. Nothing to release");
        } else {
            Log.d(TAG, "Releasing audio player.");
            if (this.mediaPlayer.isPlaying()) {
                Log.d(TAG, "Stopping playback.");
                this.mediaPlayer.stop();
            }
            this.mediaPlayer.release();
            this.mediaPlayer = null;
            this.abandonAudioFocus();
            Log.d(TAG, "Stopping foreground.");
            this.stopForeground(true);
            Log.d(TAG, "Unregistering noisy audio receiver.");
            super.unregisterReceiver(this.noisyAudioReceiver);
            Log.d(TAG, "Unregistering remote audio control receiver.");
            this.audioManager.unregisterMediaButtonEventReceiver(this.remoteControlReceiverName);
            Log.d(TAG, "Stopping service.");
            super.stopSelf();
        }
    }

    private void notifyUser(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        this.releaseMediaPlayer();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class NoisyAudioReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
                Log.d(TAG, "Audio becoming noisy.");
                if (AudioPlayerDemoService.this.pauseMediaPlayer()) {
                    AudioPlayerDemoService.this.notifyUser("Detected noise. Pausing.");
                }
            }
        }
    }
}
