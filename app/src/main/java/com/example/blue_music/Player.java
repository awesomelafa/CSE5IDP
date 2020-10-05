package com.example.blue_music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Button;
import android.os.CountDownTimer;

import java.util.ArrayList;
import java.util.Locale;

public class Player extends AppCompatActivity {

    ImageView play,prev,next,imageView;
    TextView song;
    SeekBar mSeekbarTime,mSeekbarVolume;
    static MediaPlayer mediaPlayer;
    private Runnable runnable;
    private AudioManager mAudioManager;
    int currentIndex = 0;

    //Countdown
    private static final long START_TIME_IN_MILLIS = 600000;
    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewCountDown = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);

        mButtonStartPause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                resetTimer();
            }
        });

        updateCountDownText();

        play = findViewById(R.id.play);
        prev = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        imageView = findViewById(R.id.image_view);

        song = findViewById(R.id.song_title);

        mSeekbarTime = findViewById(R.id.seek_bar_time);
        mSeekbarVolume = findViewById(R.id.seek_volume);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Integer> songs = new ArrayList<>();
        songs.add(0,R.raw.calm1);
        songs.add(1,R.raw.calm2);

        mediaPlayer = MediaPlayer.create(getApplicationContext(),songs.get(currentIndex));

        int maxV = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curv = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mSeekbarVolume.setMax(maxV);
        mSeekbarVolume.setProgress(curv);

        mSeekbarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekbarTime.setMax(mediaPlayer.getDuration());
                if(mediaPlayer != null && mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    play.setImageResource(R.drawable.play_arrow_24);
                }else{
                    mediaPlayer.start();
                    play.setImageResource(R.drawable.pause_24);
                }
                songDetails();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer != null){
                    play.setImageResource(R.drawable.image1);
                }
                if(currentIndex < songs.size()-1){
                    currentIndex ++;
                }else{
                    currentIndex = 0;
                }
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                mediaPlayer = MediaPlayer.create(getApplicationContext(),songs.get(currentIndex));
                mediaPlayer.start();
                songDetails();
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer != null){
                    play.setImageResource(R.drawable.pause_24);
                }
                if(currentIndex >0 ){
                    currentIndex --;
                }else{
                    currentIndex = songs.size() -1;
                }
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                mediaPlayer = mediaPlayer.create(getApplicationContext(),songs.get(currentIndex));
                mediaPlayer.start();
                songDetails();
            }
        });

        mSeekbarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                    mSeekbarTime.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();

            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mButtonStartPause.setText("Start");
                mButtonStartPause.setVisibility(View.INVISIBLE);
                mButtonReset.setVisibility(View.VISIBLE);

            }
        }.start();

        mTimerRunning=true;
        mButtonStartPause.setText("pause");
        mButtonReset.setVisibility(View.INVISIBLE);
    }

    private void pauseTimer(){
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonStartPause.setText("Start");
        mButtonReset.setVisibility(View.VISIBLE);
    }

    private void resetTimer(){
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);
    }

    private void updateCountDownText(){
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private void songDetails() {
        if(currentIndex == 0){
            song.setText("calm1");
            imageView.setImageResource(R.drawable.image1);
        }
        if(currentIndex == 1){
            song.setText("calm2");
            imageView.setImageResource(R.drawable.image2);
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mSeekbarTime.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null){
                    try{
                        if(mediaPlayer.isPlaying()){
                            Message message = new Message();
                            message.what = mediaPlayer.getCurrentPosition();
                            handler.sendMessage(message);
                            Thread.sleep(1000);
                        }
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            mSeekbarTime.setProgress(msg.what);
        }
    };

}