package com.example.linkedInClone.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.linkedInClone.R;
import com.example.linkedInClone.ReusableClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    TextView SignUpTextview;
    Button LoginButton;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    ReusableClass reusableClass = new ReusableClass();
    EditText EmailEditText, PasswordEditText;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SignUpTextview = findViewById(R.id.SignUp_textView);
        LoginButton = findViewById(R.id.Login_button);
        progressBar = findViewById(R.id.progress_circular);
        EmailEditText = findViewById(R.id.Login_Email_editText);
        PasswordEditText = findViewById(R.id.Login_Password_editText);
        SignUpTextview.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, SIGNUp.class));
        });
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }
    void signIn() {
        String email = EmailEditText.getText().toString();
        String password = PasswordEditText.getText().toString();

        boolean isValid = validateInput(email, password);
        if (!isValid) {
            return;
        } else {
            loginUserToFirebase(email, password);
        }
    }
    void loginUserToFirebase(String email, String password){
        showProgressBar(true);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                showProgressBar(false);
                if(task.isSuccessful()){
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    if (currentUser != null && currentUser.isEmailVerified()) {
                        reusableClass.showToast(Login.this,"Successfully Logged In");
                    } else{
                        reusableClass.showToast(Login.this,"Please verify email");
                    }
                } else{
                    reusableClass.showToast(Login.this,task.getException().getLocalizedMessage());
                }
            }
        });
    }
    boolean validateInput(String email, String password) {
        if (email.isEmpty()&&password.isEmpty()) {
            EmailEditText.setError("Please enter email");
            PasswordEditText.setError("Please enter password");
            return false;
        }else if(password.isEmpty()) {

            return false;
        }
        return true;
    }
    void showProgressBar(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            LoginButton.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            LoginButton.setVisibility(View.VISIBLE);
        }
    }
}