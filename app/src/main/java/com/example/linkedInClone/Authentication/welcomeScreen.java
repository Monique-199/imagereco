package com.example.linkedInClone.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.linkedInClone.R;

public class welcomeScreen extends AppCompatActivity {
    TextView linkedInTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        linkedInTextView = findViewById(R.id.LinkedN_textView);
        linkedInTextView.animate().translationXBy(-500f).setDuration(2000);
        //set the splash screen time out , make the linkedIn text view  move from right to left with sound effect and then after 2 seconds move to the login activity
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Intent loginIntent = new Intent(welcomeScreen.this, Login.class);
                        startActivity(loginIntent);
                    }
                }, 3000);
    }
}