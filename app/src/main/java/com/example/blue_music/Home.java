package com.example.blue_music;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {
    private Button playMusicBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        playMusicBtn = (Button) findViewById(R.id.relaxBtn);
        playMusicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            openMusic();
            }
        });
    }
    public void openMusic(){
        Intent intent = new Intent(this,Player.class);
        startActivity(intent);
    }
}