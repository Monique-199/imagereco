package com.example.screendesign.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.screendesign.R;

public class Login extends AppCompatActivity {
    TextView SignUpTextview;
    Button LoginButton;
    EditText EmailEditText, PasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SignUpTextview = findViewById(R.id.SignUp_textView);
        LoginButton = findViewById(R.id.Login_button);
        EmailEditText = findViewById(R.id.Login_Email_editText);
        PasswordEditText = findViewById(R.id.Login_Password_editText);
        SignUpTextview.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, SIGNUp.class));
        });
    }
}