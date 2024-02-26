package com.example.imagereco.Authentication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.imagereco.R;
import com.example.imagereco.ReusableClass;
import com.example.imagereco.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SignUpActivity extends AppCompatActivity {
    // Constants
    private static final String USERS_COLLECTION = "users";
    private static final String TOAST_ACCOUNT_CREATION_FAILED = "Account creation failed: ";

    // UI elements
    private TextView loginTextView;
    private Button signUpButton;
    private Uri selectedImageUri;
    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private ReusableClass reusableClass = new ReusableClass();

    // EditText fields
    private EditText firstNameEditText, lastNameEditText, passwordEditText, confirmPasswordEditText,
            emailEditText;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize UI elements
        loginTextView = findViewById(R.id.sign_in_textview);
        signUpButton = findViewById(R.id.sign_up_button);
        emailEditText = findViewById(R.id.email_editText);
        passwordEditText = findViewById(R.id.password_edittext);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edittext);
        firstNameEditText = findViewById(R.id.firstname_editText);
        lastNameEditText = findViewById(R.id.lastname_editText);
        progressBar = findViewById(R.id.progress_circular);

        // Initialize Firebase instances
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();


        loginTextView.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, Login.class));
            finish();
        });

        signUpButton.setOnClickListener(v -> {
            createAccount();
        });
    }

    void createAccount() {
        String firstname = firstNameEditText.getText().toString();
        String lastname = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        boolean isValid = validateInput(email, password, confirmPassword, firstname, lastname);

        if (isValid) {
            progressIndicator(true);
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressIndicator(false);
                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                if (user != null) {
                                    User newUser = new User(firstname, lastname, email);

                                    // Save user to Firestore first
                                    saveUserToFirestore(newUser, user.getUid());
                                    reusableClass.showToast(SignUpActivity.this, "Account Created, Login");
                                }
                            } else {
                                reusableClass.showToast(SignUpActivity.this, TOAST_ACCOUNT_CREATION_FAILED + task.getException().getMessage());
                            }
                        }
                    });
        }
    }

    void saveUserToFirestore(User user, String userId) {
        firestore.collection(USERS_COLLECTION)
                .document(userId)
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            String message = task.getException().getLocalizedMessage();
                            Log.d("SignUpError", message);
                            reusableClass.showToast(SignUpActivity.this, message);
                        }
                    }
                });
    }


    void progressIndicator(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            signUpButton.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            signUpButton.setEnabled(true);
        }
    }


    boolean validateInput(String email, String password, String confirmPassword,
                          String firstname, String lastname) {

        if (firstname.isEmpty() && email.isEmpty() && lastname.isEmpty()
                && password.isEmpty() && confirmPassword.isEmpty()) {
            firstNameEditText.setError("Please enter your firstname");
            lastNameEditText.setError("Please enter your lastname");
            emailEditText.setError("please enter email address");
            passwordEditText.setError("Please enter password");
            confirmPasswordEditText.setError("Please confirm your password");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            reusableClass.showToast(SignUpActivity.this, "Invalid email address");
            return false;
        }

        if (password.length() < 8) {
            reusableClass.showToast(SignUpActivity.this, "Password must be at least 8 characters");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            reusableClass.showToast(SignUpActivity.this, "Passwords do not match");
            return false;
        }

        return true;
    }

}
