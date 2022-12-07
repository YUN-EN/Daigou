package com.example.testinterface_two;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testinterface_two.databinding.ActivityEditPostBinding;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class editPostActivity extends AppCompatActivity {
    private ActivityEditPostBinding binding;
    private FirebaseFirestore fireStore;
    private ActivityResultLauncher<String> mGetContent;
    private StorageReference storageReference;
    private Uri imageUri;
    private Uri downloadUri = null;
    private String postID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        postID = intent.getStringExtra("postId");
        storageReference = FirebaseStorage.getInstance().getReference();
        fireStore = FirebaseFirestore.getInstance();
        setPost();
        binding.imageMyPost.setOnClickListener(v -> showImage());
        binding.updateMyPost.setOnClickListener(v -> update());
        binding.cancelMyPost.setOnClickListener(v -> onBackPressed());
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if(result != null) {
                imageUri = result;
                binding.imageMyPost.setImageURI(imageUri);
            }
        });
    }

    private void showImage() {
        mGetContent.launch("image/*");
    }

    private void setPost() {
        fireStore.collection("Post").document(postID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String image = task.getResult().getString("postImage");
                String caption  = task.getResult().getString("Caption");
                String content = task.getResult().getString("Content");
                binding.editMyContent.setText(content);
                binding.editMyTitle.setText(caption);
                Glide.with(editPostActivity.this).load(image).into(binding.imageMyPost);
            }
        });
    }
    private void update() {
        binding.progressBarPost.setVisibility(View.VISIBLE);
        String cp = binding.editMyTitle.getText().toString();
        String cn = binding.editMyContent.getText().toString();
        StorageReference imageRef = storageReference.child("Post_Image").child(FieldValue.serverTimestamp() + ".jpg");
        if (imageUri != null) {
            imageRef.putFile(imageUri).addOnCompleteListener(task -> {
                if (task.isSuccessful())     {
                    saveData(cp ,cn ,imageRef);
                }else {
                    Toast.makeText(editPostActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            save(cp ,cn);
        }
    }
    private void saveData(String title ,String content ,StorageReference imgRef) {
        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
            downloadUri = uri;
            fireStore.collection("Post").document(postID).update("postImage" ,downloadUri.toString() ,"Caption" ,title ,"Content" ,content).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    startActivity(new Intent(editPostActivity.this ,shopActivity.class));
                    Toast.makeText(editPostActivity.this, "編輯成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(editPostActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
                binding.progressBarPost.setVisibility(View.INVISIBLE);
            });
        });
    }
    private void save(String title ,String content) {
        fireStore.collection("Post").document(postID).update("Caption" ,title ,"Content" ,content).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(editPostActivity.this ,shopActivity.class));
                Toast.makeText(editPostActivity.this, "編輯成功", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(editPostActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
            binding.progressBarPost.setVisibility(View.INVISIBLE);
        });
    }
}