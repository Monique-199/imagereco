package com.example.imagereco.home;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.imagereco.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeViewModel extends ViewModel {

    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final MutableLiveData<User> userData = new MutableLiveData<>();

    public LiveData<User> getUserData() {
        // Fetch data from Firestore when the ViewModel is created
        fetchUserData();
        return userData;
    }

    private void fetchUserData() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            DocumentReference userRef = firestore.collection("users").document(user.getUid());
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        User _userData = document.toObject(User.class);
                        userData.setValue(_userData);
                    }
                }
            });
        }
    }

    public void signOut() {
        auth.signOut();
    }
}

