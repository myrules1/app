package com.example.atry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class StepA extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_a);

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSound();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        Glide
                .with(this)
                .load(SplashScreen.ico)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into((ImageView) findViewById(R.id.ico));

        Config.admobNative(findViewById(R.id.ad_frame_native), this);
        Config.banner(findViewById(R.id.ad_frame), this);
    }
    private void clickSound(){
        final MediaPlayer clickSound = MediaPlayer.create(this,R.raw.start);
        clickSound.start();
    }
}