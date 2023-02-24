package com.example.atry;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {


    String dataPath = "https://raw.githubusercontent.com/myrules1/resources/main/new.json";
    public static String network, admob_ban, admob_int, admob_native, admob_rewarded, game_link, ico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splachscreen);

        data();
        animationLogo();
        animationLoading();
        introSound();

    }

    private void data() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, dataPath, null,
                response -> {
                    try{
                        JSONArray array = response.getJSONArray("config");
                        JSONObject jsonObject = array.getJSONObject(0);

                        network = jsonObject.getString("network");

                        admob_ban = jsonObject.getString("admob_ban");
                        admob_native = jsonObject.getString("admob_native");
                        admob_int = jsonObject.getString("admob_int");
                        admob_rewarded = jsonObject.getString("admob_rewarded");

                        ico = jsonObject.getString("ico");
                        game_link = jsonObject.getString("game_link");

                        ad_i();
                    }
                    catch (JSONException e)
                    {
                        ad_i();
                        e.printStackTrace();
                        Toast.makeText(this, "e " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error ->
                {
                    ad_i();
                    Toast.makeText(this, "error " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void ad_i() {
        if (network.equals("admob")){
            MobileAds.initialize(this, initializationStatus -> {
                Config.interstitial(this);
                next();
            });
        }else{
            Toast.makeText(this, "other network", Toast.LENGTH_SHORT).show();
            next();
        }
    }

    private void next() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), StepA.class);
                startActivity(intent);
                finish();
            }
        }, 5000);
    }

    private void introSound(){
        final MediaPlayer intro = MediaPlayer.create(this,R.raw.intro);
        intro.start();
    }

    private void animationLoading(){
        ImageView imageView = (ImageView) findViewById(R.id.imageView2);
        YoYo.with(Techniques.FadeIn).duration(700).repeat(10).playOn(imageView);
    }

    private void animationLogo(){
        ImageView imageView = (ImageView) findViewById(R.id.logo);
        YoYo.with(Techniques.Shake).duration(700).repeat(3).playOn(imageView);
    }

}