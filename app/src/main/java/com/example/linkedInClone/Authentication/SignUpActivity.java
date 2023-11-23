package com.example.linkedInClone.Authentication;

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

import com.example.linkedInClone.R;
import com.example.linkedInClone.ReusableClass;
import com.example.linkedInClone.User;
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
    private static final String PROFILE_IMAGES_FOLDER = "profile_images";
    private static final String TOAST_ACCOUNT_CREATED = "Account created successfully. Check your email to verify.";
    private static final String TOAST_FAILED_VERIFICATION = "Failed to send verification email.";
    private static final String TOAST_ACCOUNT_CREATION_FAILED = "Account creation failed: ";

    // UI elements
    private TextView loginTextView;
    private Button signUpButton;
    private Spinner genderSpinner;
    private ProgressBar progressBar;
    private Uri selectedImageUri;
    private ImageView imageView;
    private Button chooseProfilePictureButton;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private ReusableClass reusableClass = new ReusableClass();

    // EditText fields
    private EditText emailEditText, passwordEditText, confirmPasswordEditText,
            usernameEditText, phoneEditText, shortBioEditText, skillsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize UI elements
        loginTextView = findViewById(R.id.login_textView);
        signUpButton = findViewById(R.id.SignUp_button);
        emailEditText = findViewById(R.id.Email_editText);
        passwordEditText = findViewById(R.id.Password_editText);
        confirmPasswordEditText = findViewById(R.id.ConfirmPassword_editText);
        usernameEditText = findViewById(R.id.UserName_editText);
        phoneEditText = findViewById(R.id.Phone_number_editText);
        shortBioEditText = findViewById(R.id.shortBio_editText);
        skillsEditText = findViewById(R.id.skills_editText);
        genderSpinner = findViewById(R.id.gender_spinner);
        progressBar = findViewById(R.id.signup_progress_circular);
        imageView = findViewById(R.id.profileImageView);
        chooseProfilePictureButton = findViewById(R.id.chooseProfilePictureButton);

        // Set up gender spinner
        String[] options = {"Male", "Female", "Non-Binary", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(SignUpActivity.this,
                android.R.layout.simple_spinner_dropdown_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        // Initialize Firebase instances
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Set up click listeners
        chooseProfilePictureButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            chooseProfilePictureLauncher.launch(intent);
        });

        loginTextView.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, Login.class));
        });

        signUpButton.setOnClickListener(v -> {
            createAccount();
        });
    }

    void createAccount() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String userName = usernameEditText.getText().toString();
        String phoneNumber = phoneEditText.getText().toString();
        String shortBio = shortBioEditText.getText().toString();
        String skills = skillsEditText.getText().toString();
        String gender = genderSpinner.getSelectedItem().toString();

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
                                    User newUser = new User(userName, email, gender, shortBio,skills, "", phoneNumber);

                                    // Save user to Firestore first
                                    saveUserToFirestore(newUser, user.getUid());

                                    // Upload the image to Firebase Storage
                                    if (selectedImageUri != null) {
                                        uploadImageToStorage(selectedImageUri, user.getUid());
                                    }

                                    // Send email verification
                                    sendEmailVerification(user);
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
                            String errorMessage = task.getException().getLocalizedMessage();
                            Log.d("SignUpError", errorMessage);
                            reusableClass.showToast(SignUpActivity.this, errorMessage);
                        }
                    }
                });
    }

    void uploadImageToStorage(Uri imageUri, String userId) {
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child(PROFILE_IMAGES_FOLDER).child(userId);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Image uploaded successfully, get the download URL
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUrl) {
                                // Store the download URL in Firestore
                                updateProfilePictureUrl(downloadUrl.toString(), userId);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the error
                        String errorMessage = e.getMessage();
                        reusableClass.showToast(SignUpActivity.this, "Failed to upload image: " + errorMessage);
                    }
                });
    }

    private void updateProfilePictureUrl(String downloadUrl, String userId) {
        firestore.collection(USERS_COLLECTION)
                .document(userId)
                .update("profilePictureUrl", downloadUrl)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            // Handle the error
                            String errorMessage = task.getException().getMessage();
                            reusableClass.showToast(SignUpActivity.this, "Failed to update profile picture URL: " + errorMessage);
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

    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> emailTask) {
                        if (emailTask.isSuccessful()) {
                            reusableClass.showToast(SignUpActivity.this, TOAST_ACCOUNT_CREATED);
                            startActivity(new Intent(SignUpActivity.this, Login.class));
                            finish();
                        } else {
                            reusableClass.showToast(SignUpActivity.this, TOAST_FAILED_VERIFICATION);
                        }
                    }
                });
    }

    boolean validateInput(String email, String password, String confirmPassword,
                          String userName, String phoneNumber, String shortBio, String skills) {

        if (userName.isEmpty() && email.isEmpty() && phoneNumber.isEmpty()
                && password.isEmpty() && confirmPassword.isEmpty() && shortBio.isEmpty() && skills.isEmpty()) {
            usernameEditText.setError("Please enter your Name");
            emailEditText.setError("please enter email address");
            phoneEditText.setError("please enter phone number");
            passwordEditText.setError("Please enter password");
            confirmPasswordEditText.setError("Please confirm your password");
            shortBioEditText.setError("Please write a short Bio");
            skillsEditText.setError("Please write your skills");
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

    private final ActivityResultLauncher<Intent> chooseProfilePictureLauncher = registerForActivityResult(
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
