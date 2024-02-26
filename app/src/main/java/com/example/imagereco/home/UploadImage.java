package com.example.imagereco.home;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadImage {
    private static final String PROFILE_IMAGES_FOLDER = "profile_images";
    private static final String USERS_COLLECTION = "users";


    private FirebaseFirestore firestore;
    private FirebaseStorage storage;

    // Set up click listeners
//        chooseProfilePictureButton.setOnClickListener(v -> {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        chooseProfilePictureLauncher.launch(intent);
//    });
//    // Upload the image to Firebase Storage
//                                    if (selectedImageUri != null) {
//        uploadImageToStorage(selectedImageUri, user.getUid());
//    }

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
                        }
                    }
                });


    }

}
