package com.example.screendesign.Authentication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.screendesign.R;

public class SIGNUp extends AppCompatActivity {
    TextView LoginTextview;
    Button SignUpButton;
    Uri selectedImageUri;
    ImageView imageView;
    Button chooseProfilePictureButton;
    EditText EmailEditText, PasswordEditText, ConfirmPasswordEditText,
            UsernameEditText,PhoneEditText,GenderEditText, shortBioEditText,skillsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        imageView = findViewById(R.id.profileImageView);
        chooseProfilePictureButton = findViewById(R.id.chooseProfilePictureButton);
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
        chooseProfilePictureButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            chooseProfilePictureLauncher.launch(intent);
        });
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
