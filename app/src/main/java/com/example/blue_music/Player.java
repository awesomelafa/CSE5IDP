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

import java.util.ArrayList;

public class Player extends AppCompatActivity {

    ImageView play,prev,next,imageView;
    TextView song;
    SeekBar mSeekbarTime,mSeekbarVolume;
    static MediaPlayer mediaPlayer;
    private Runnable runnable;
    private AudioManager mAudioManager;
    int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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