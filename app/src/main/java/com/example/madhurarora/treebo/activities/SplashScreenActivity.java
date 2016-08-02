package com.example.madhurarora.treebo.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.madhurarora.treebo.R;

/**
 * Created by madhur.arora on 02/08/16.
 */
public class SplashScreenActivity extends AppCompatActivity {

    public static final int SPLASH_TIME_OUT = 2000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        TextView txt = (TextView) findViewById(R.id.title_text);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Mika Melvas - RionaSans-RegularItalic.ttf");
        txt.setTypeface(font);
        init();
    }

    private void init() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, NotesActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
