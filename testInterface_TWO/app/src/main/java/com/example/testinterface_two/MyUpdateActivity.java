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
import com.example.testinterface_two.databinding.ActivityMyUpdateBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.Objects;

public class MyUpdateActivity extends AppCompatActivity {
    private ActivityMyUpdateBinding binding;
    private FirebaseUser user;
    private FirebaseFirestore fireStore;
    private ActivityResultLauncher<String> mGetContent;
    private StorageReference storageReference;
    private Uri imageUri;
    private Uri downloadUri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        user  = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        fireStore = FirebaseFirestore.getInstance();
        set();
        binding.updateMy.setOnClickListener(v -> update());
        binding.cancelMy.setOnClickListener(v -> onBackPressed());
        binding.imageMy.setOnClickListener(v -> showImage());
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if(result != null) {
                imageUri = result;
                binding.imageMy.setImageURI(imageUri);
            }
        });
    }
    private void set() {
        fireStore.collection("Users").document(user.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String nm = task.getResult().getString("userName");
                String ps = task.getResult().getString("userPassword");
                String st = task.getResult().getString("userState");
                String im = task.getResult().getString("userImage");
                String wl = task.getResult().getString("userWallet");
                binding.editNameMy.setText(nm);
                binding.editPasswordMy.setText(ps);
                binding.editStateMy.setText(st);
                binding.editWalletMy.setText(wl);
                Glide.with(MyUpdateActivity.this).load(im).into(binding.imageMy);
            }
        });
    }
    private void showImage() {
        mGetContent.launch("image/*");
    }
    private void update() {
        binding.progressBarUpdate.setVisibility(View.VISIBLE);
        String name = binding.editNameMy.getText().toString();
        String password  = binding.editPasswordMy.getText().toString();
        String state = binding.editStateMy.getText().toString();
        String wallet = binding.editWalletMy.getText().toString();

        StorageReference imgRef = storageReference.child("UserImg").child(FieldValue.serverTimestamp() + ".jpg");
        if(imageUri != null) {
            imgRef.putFile(imageUri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    save(name, password ,state ,imgRef ,wallet);
                } else {
                    Toast.makeText(this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            saveDT(name,password,state,wallet);
        }
    }


    private void save(String name ,String password ,String state ,StorageReference imgRef ,String wallet) {
        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
            downloadUri =uri;
            fireStore.collection("Users").document(user.getUid()).update("userName" ,name ,"userState" ,state ,"userPassword" ,password ,"userWallet" ,wallet ,"userImage" ,downloadUri.toString()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    startActivity(new Intent(MyUpdateActivity.this ,shopActivity.class));
                    Toast.makeText(MyUpdateActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyUpdateActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
                binding.progressBarUpdate.setVisibility(View.INVISIBLE);
            });
        });
    }
    private void saveDT(String name ,String password ,String state ,String wallet) {
        fireStore.collection("Users").document(user.getUid()).update("userName" ,name ,"userState" ,state ,"userPassword" ,password ,"userWallet" ,wallet).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(MyUpdateActivity.this ,shopActivity.class));
                Toast.makeText(MyUpdateActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MyUpdateActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
            binding.progressBarUpdate.setVisibility(View.INVISIBLE);
        });
    }
}