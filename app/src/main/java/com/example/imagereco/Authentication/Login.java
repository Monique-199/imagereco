package com.example.imagereco.Authentication;

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

import com.example.imagereco.R;
import com.example.imagereco.ReusableClass;
import com.example.imagereco.home.homeScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    TextView signUpTextView;
    TextView forgotPasswordTextView;
    Button loginButton;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    ReusableClass reusableClass = new ReusableClass();
    EditText emailEditText, passwordEditText;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signUpTextView = findViewById(R.id.SignUp_textView);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progress_circular);
        emailEditText = findViewById(R.id.Login_Email_editText);
        passwordEditText = findViewById(R.id.textPassword);
        forgotPasswordTextView = findViewById(R.id.forgot_password);
        firebaseAuth = FirebaseAuth.getInstance();
        signUpTextView.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, SignUpActivity.class));
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        forgotPasswordTextView.setOnClickListener(onclick -> {
            startActivity(new Intent(Login.this, ForgotPassword.class));
        });
    }

    void signIn() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean isValid = validateInput(email, password);
        if (!isValid) {
            return;
        } else {
            loginUserToFirebase(email, password);
        }
    }

    void loginUserToFirebase(String email, String password) {
        showProgressBar(true);

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                showProgressBar(false);

                if (task.isSuccessful()) {
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                    if (currentUser != null) {
                        reusableClass.showToast(Login.this, "Successfully Logged In");
                        startActivity(new Intent(Login.this, homeScreen.class));
                        finish();
                    }
                } else {
                    // Handle different failure cases explicitly
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        reusableClass.showToast(Login.this, "Invalid email or password");
                    } else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                        String errorCode = ((FirebaseAuthInvalidUserException) task.getException()).getErrorCode();
                        if (errorCode.equals("ERROR_USER_NOT_FOUND")) {
                            reusableClass.showToast(Login.this, "User not found");
                        } else if (errorCode.equals("ERROR_USER_DISABLED")) {
                            reusableClass.showToast(Login.this, "User account has been disabled");
                        } else {
                            reusableClass.showToast(Login.this, "Login failed: " + task.getException().getLocalizedMessage());
                        }
                    } else {
                        reusableClass.showToast(Login.this, "Login failed: " + task.getException().getLocalizedMessage());
                    }
                }
            }
        });
    }

    boolean validateInput(String email, String password) {
        if (email.isEmpty() && password.isEmpty()) {
            emailEditText.setError("Please enter email");
            passwordEditText.setError("Please enter password");
            return false;
        } else if (password.isEmpty()) {

            return false;
        }
        return true;
    }

    void showProgressBar(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        }
    }
}