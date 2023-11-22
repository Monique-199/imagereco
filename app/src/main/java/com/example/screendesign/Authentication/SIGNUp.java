package com.example.screendesign.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.screendesign.R;

public class SIGNUp extends AppCompatActivity {
    TextView LoginTextview;
    Button SignUpButton;
    EditText EmailEditText, PasswordEditText, ConfirmPasswordEditText,
            UsernameEditText,PhoneEditText,GenderEditText, shortBioEditText,skillsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        LoginTextview = findViewById(R.id.login_textView);
        SignUpButton = findViewById(R.id.SignUp_button);
        EmailEditText = findViewById(R.id.Email_editText);
        PasswordEditText = findViewById(R.id.Password_editText);
        ConfirmPasswordEditText = findViewById(R.id.ConfirmPassword_editText);
        UsernameEditText = findViewById(R.id.UserName_editText);
        PhoneEditText = findViewById(R.id.Phone_number_editText);
        shortBioEditText = findViewById(R.id.shortBio_editText);
        skillsEditText = findViewById(R.id.skills_editText);
        GenderEditText = findViewById(R.id.Gender_editText);
        LoginTextview.setOnClickListener(v -> {
            startActivity(new Intent(SIGNUp.this, Login.class));
        });
    }
}