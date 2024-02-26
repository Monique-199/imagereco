package com.example.imagereco.Authentication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.imagereco.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class ForgotPassword extends AppCompatActivity {
    EditText emailEditText;
    Button resetButton;
    ImageView imageViewBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText = findViewById(R.id.editTextEmail);
        resetButton = findViewById(R.id.buttonForgotPassword);
        imageViewBack = findViewById(R.id.imageViewBack);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        imageViewBack.setOnClickListener(onBackPressed -> {
            finish();
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = emailEditText.getText().toString().trim();
//                String userEmail = firebaseUser != null ? firebaseUser.getEmail() : emailEditText.getText().toString().trim();

                if (userEmail.isEmpty()) {
                    Toast.makeText(ForgotPassword.this, "Enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                firebaseAuth.sendPasswordResetEmail(userEmail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                task.addOnSuccessListener(onSuccess -> {
                                    showResetDialog();
                                });
                                task.addOnFailureListener(onFail -> {
                                    Toast.makeText(ForgotPassword.this, "Failed to send reset email", Toast.LENGTH_SHORT).show();
                                });
                            }
                        });
            }
        });
    }

    private void showResetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Password Reset Email Sent")
                .setMessage("An email with instructions to reset your password has been sent to your email address. Please check your inbox.")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Navigate back to login screen or perform any other action
                    finish();
                })
                .setCancelable(false)
                .show();
    }
}
