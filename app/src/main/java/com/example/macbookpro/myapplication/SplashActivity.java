package com.example.macbookpro.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_LENGTH = 1000 ;
    private FirebaseAuth mAuth ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser() ;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentUser == null) {

                    Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class) ;
                    SplashActivity.this.startActivity(loginIntent);
                    finish();
                }
                else {

                    Intent loginIntent = new Intent(SplashActivity.this, HomeActivity.class) ;
                    SplashActivity.this.startActivity(loginIntent);
                    finish();
                }

            }
        }, SPLASH_LENGTH) ;
    }


}
