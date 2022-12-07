package com.example.testinterface_two;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.testinterface_two.databinding.ActivityAddPostBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class addPostActivity extends AppCompatActivity {
    private ActivityAddPostBinding binding;
    private ActivityResultLauncher<String> mGetContent;
    private Uri postImgUri = null ;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private String UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        UID = firebaseUser.getUid();
        binding.addPostImage.setOnClickListener(v -> selectImage());
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if(result != null) {
              postImgUri = result;
              binding.addPostImage.setImageURI(postImgUri);
            }
        });
        binding.addPostButton.setOnClickListener(v -> savePost());
        binding.cancelPostButton.setOnClickListener(v -> onBackPressed());
    }
    private void savePost() {
        binding.progressBarPost.setVisibility(View.VISIBLE);
        String caption = binding.postTitle.getText().toString();
        String content = binding.postContent.getText().toString();
        if(caption.isEmpty() || content.isEmpty()) {
            binding.postTitle.setError("Please enter your title");
            binding.postTitle.requestFocus();
            binding.postContent.setError("Please write your content");
            binding.postContent.requestFocus();
        }
        if(postImgUri != null) {
            StorageReference imageRef = storageReference.child("Post_Image").child(FieldValue.serverTimestamp() + ".jpg");
            imageRef.putFile(postImgUri).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        HashMap<String , Object> post = new HashMap<>();
                        post.put("User" ,UID);
                        post.put("Caption" ,caption);
                        post.put("Content" ,content);
                        post.put("postImage" ,uri.toString());
                        post.put("Time" , FieldValue.serverTimestamp());
                        firebaseFirestore.collection("Post").add(post).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(addPostActivity.this, "成功發文", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(addPostActivity.this ,shopActivity.class));
                                finish();
                            } else {
                                Toast.makeText(addPostActivity.this, "Add fail", Toast.LENGTH_SHORT).show();
                            }
                            binding.progressBarPost.setVisibility(View.INVISIBLE);

                        });
                    });
                }
            });
        }
    }
    private void selectImage() {
        mGetContent.launch("image/*");
    }
}