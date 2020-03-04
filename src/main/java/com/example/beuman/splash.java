package com.example.beuman;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splash extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

            new Handler().postDelayed(new Runnable() {

// Using handler with postDelayed called runnable run method

                @Override

                public void run() {

                    if(user != null && user.isEmailVerified()) {
                        Intent i = new Intent(splash.this,MainActivity.class);
                        startActivity(i);
                        finish();

                    }else
                    {
                        Intent i = new Intent(splash.this,signIN.class);
                        startActivity(i);
                        finish();
                    }

                }

            }, 2*1000); // wait for 5 seconds

        }





}
