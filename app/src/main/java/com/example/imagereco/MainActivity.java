package com.example.imagereco;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.imagereco.Authentication.Login;
import com.example.imagereco.home.homeScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Intent loginIntent = new Intent(MainActivity.this, Login.class);
        Intent homeIntent = new Intent(MainActivity.this, homeScreen.class);
        if (user != null){
            startActivity(homeIntent);
            finish();
        }else {
            startActivity(loginIntent);
            finish();
        }
    }
}