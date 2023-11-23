package com.example.linkedInClone.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.linkedInClone.R;
import com.example.linkedInClone.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class homeScreen extends AppCompatActivity {
    private ImageView profilePictureImageView;
    private TextView usernameTextView, genderTextView, phoneNumberTextView, shortBioTextView, skillsTextView;
    private AllUsersAdapter allUsersAdapter;
    private List<UserProfile> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        // Initialize UI elements
        profilePictureImageView = findViewById(R.id.profile_picture);
        usernameTextView = findViewById(R.id.username);
        genderTextView = findViewById(R.id.gender);
        phoneNumberTextView = findViewById(R.id.phone_number);
        shortBioTextView = findViewById(R.id.short_bio);
        skillsTextView = findViewById(R.id.skills);


        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getUserData().observe(this, this::updateUi);
        fetchUsersData();


        RecyclerView allUsersRecyclerView = findViewById(R.id.all_users);
        // Create an instance of the adapter
        allUsersAdapter = new AllUsersAdapter(userList);
        allUsersRecyclerView.setAdapter(allUsersAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        allUsersRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(allUsersRecyclerView.getContext(), layoutManager.getOrientation());
        allUsersRecyclerView.addItemDecoration(dividerItemDecoration);

        // Set item click listener if needed
        allUsersAdapter.setOnItemClickListener(new AllUsersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(UserProfile user) {
                // Handle item click
            }

            @Override
            public void onCallButtonClick(UserProfile user) {
                String phoneNumber = user.getPhoneNumber();
                if (phoneNumber != null && !phoneNumber.isEmpty()) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                    Intent chooser = Intent.createChooser(callIntent, "Choose a calling app:");
                    if (callIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(chooser);
                    } else {
                        // Handle the case where no activity can handle the call intent
                        // You might want to show a message to the user or take alternative action
                    }
                }
            }

            @Override
            public void onEmailButtonClick(UserProfile user) {
                String email = user.getEmail();
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
                startActivity(emailIntent);
            }
        });
    }

    private void updateUi(User user) {
        if (user != null) {
            // Update UI elements with user data
            Picasso.get().load(user.getProfilePictureUrl()).into(profilePictureImageView);
            usernameTextView.setText(user.getUserName());
            genderTextView.setText(user.getGender());
            phoneNumberTextView.setText(user.getPhoneNumber());
            shortBioTextView.setText(user.getShortBio());
            skillsTextView.setText(user.getSkills());
        }
    }

    private void fetchUsersData() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser userCurrent = auth.getCurrentUser();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<UserProfile> userList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    UserProfile user = document.toObject(UserProfile.class);
                    if (!user.getEmail().equals(userCurrent.getEmail())) {
                        userList.add(user);
                    }
                }
                updateUsersList(userList);
            } else {
                // Handle the error
                String errorMessage = task.getException().getMessage();
                Log.d("FetchUsersError", errorMessage);
            }
        });
    }

    private void updateUsersList(List<UserProfile> userList) {
        // Assuming you have a member variable 'allUsersAdapter' in your activity
        if (allUsersAdapter != null) {
            allUsersAdapter.setUserList(userList);
            allUsersAdapter.notifyDataSetChanged();
        }
    }

}