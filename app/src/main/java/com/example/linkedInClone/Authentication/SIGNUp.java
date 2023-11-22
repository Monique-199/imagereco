package com.example.linkedInClone.Authentication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.linkedInClone.R;
import com.example.linkedInClone.ReusableClass;
import com.example.linkedInClone.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SIGNUp extends AppCompatActivity {
    TextView LoginTextview;
    Button SignUpButton;
    Spinner spinner;
    ProgressBar progressBar;
    Uri selectedImageUri;
    ImageView imageView;
    Button chooseProfilePictureButton;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    ReusableClass reusableClass = new ReusableClass();
    EditText EmailEditText, PasswordEditText, ConfirmPasswordEditText,
            UsernameEditText,PhoneEditText,GenderEditText, shortBioEditText,skillsEditText;

    @SuppressLint("MissingInflatedId")
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
        progressBar = findViewById(R.id.progress_circular);
        imageView = findViewById(R.id.profileImageView);
        chooseProfilePictureButton = findViewById(R.id.chooseProfilePictureButton);
        spinner =findViewById(R.id.gender_spinner);
        chooseProfilePictureButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            chooseProfilePictureLauncher.launch(intent);
        });
        String[] options = {"Male", "Female","Non-Binary","Other"};
        ArrayAdapter<String> adapter = new
                ArrayAdapter<>(SIGNUp.this, android.R.layout.
                simple_spinner_dropdown_item, options);
        adapter.setDropDownViewResource(android.R.layout.
                simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        LoginTextview.setOnClickListener(v -> {
            startActivity(new Intent(SIGNUp.this, Login.class));
        });
        SignUpButton.setOnClickListener(pnclick ->{
            createAccount();
        });
    }
    void createAccount() {
        firebaseAuth = FirebaseAuth.getInstance();
        String email = EmailEditText.getText().toString();
        String password = PasswordEditText.getText().toString();
        String confirmPassword = ConfirmPasswordEditText.getText().toString();
        String userName = UsernameEditText.getText().toString();
        String phoneNumber = PhoneEditText.getText().toString();
        String shortBio = shortBioEditText.getText().toString();
        String skills = skillsEditText.getText().toString();
        String gender = spinner.getSelectedItem().toString();

        boolean isValid = validateInput(email, password, confirmPassword, userName, skills, shortBio, phoneNumber);

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
                                    User newUser = new User(userName, email, password, confirmPassword, gender, skills, shortBio);

                                    // Save user to Firestore first
                                    saveUserToFirestore(newUser);

                                    // Send email verification
                                    user.sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> emailTask) {
                                                    if (emailTask.isSuccessful()) {
                                                        reusableClass.showToast(SIGNUp.this, "Account created successfully. Check your email to verify.");
                                                        finish();
                                                    } else {
                                                        reusableClass.showToast(SIGNUp.this, "Failed to send verification email.");
                                                    }
                                                }
                                            });
                                }
                            } else {
                                reusableClass.showToast(SIGNUp.this, "Account creation failed: " + task.getException().getMessage());
                            }
                        }
                    });
        }
    }

    void saveUserToFirestore(User user) {
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("users")
                .add(user)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            // User data saved to Firestore
                        } else {
                            String errorMessage =  task.getException().getLocalizedMessage();
                            Log.d("SignUpError", errorMessage);
                            reusableClass.showToast(SIGNUp.this,errorMessage);
                        }
                    }
                });
    }
    boolean validateInput(String email, String password, String confirmPassword,
                          String userName, String phoneNumber, String shortBio, String skills) {

        if (userName.isEmpty()&&email.isEmpty()&&phoneNumber.isEmpty()
                &&password.isEmpty()&&confirmPassword.isEmpty()&&shortBio.isEmpty()&&skills.isEmpty()) {
            UsernameEditText.setError("Please enter your Name");
            EmailEditText.setError("please enter email address");
            PhoneEditText.setError("please enter phone number");
            PasswordEditText.setError("Please enter password");
            ConfirmPasswordEditText.setError("Please confirm your password");
            shortBioEditText.setError("Please write a short Bio");
            skillsEditText.setError("Please write your skills");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            reusableClass.showToast(SIGNUp.this,"Invalid email address");
            return false;
        }

        if (password.length() < 8) {
            reusableClass.showToast(SIGNUp.this,"Password must be at least 8 characters");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            reusableClass.showToast(SIGNUp.this,"Passwords do not match");
            return false;
        }

        return true;
    }
    void progressIndicator(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            SignUpButton.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            SignUpButton.setEnabled(true);
        }
    }
    final ActivityResultLauncher<Intent> chooseProfilePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // Handle the result, e.g., get the selected image URI
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        selectedImageUri = data.getData();
                        imageView.setImageURI(selectedImageUri);
                    }
                }
            }
    );
}