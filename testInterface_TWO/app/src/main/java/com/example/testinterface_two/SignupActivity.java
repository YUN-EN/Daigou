package com.example.testinterface_two;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;
import com.example.testinterface_two.databinding.ActivitySignupBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.HashMap;
import java.util.Objects;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fireStore;
    private ActivityResultLauncher<String> mGetContent;
    private StorageReference storageReference;
    private Uri imageUri;
    private Uri downloadUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        fireStore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        binding.cancelButton.setOnClickListener(v -> onBackPressed());
        binding.registerButton.setOnClickListener(v -> registerUser());
        binding.userCircleImageView.setOnClickListener(v -> showImage());
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if(result != null) {
                imageUri = result;
                binding.userCircleImageView.setImageURI(imageUri);
            }
        });
    }

    private void showImage() {
        mGetContent.launch("image/*");
    }

    private void registerUser() {
        binding.progressBarSignUp.setVisibility(View.VISIBLE);
        String userMail = binding.mailTextViewSignUp.getText().toString().trim();
        String userName = binding.NameTextViewSignUp.getText().toString().trim();
        String userPassword = binding.PasswordTextViewSignUp.getText().toString().trim();

        if(userMail.isEmpty()) {
            binding.mailTextViewSignUp.setError("Please enter your mail !");
            binding.mailTextViewSignUp.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(userMail).matches()) {
            binding.mailTextViewSignUp.setError("Please enter valid mail");
            binding.mailTextViewSignUp.requestFocus();
            return;
        }
        if(userName.isEmpty()) {
            binding.NameTextViewSignUp.setError("Please enter your name");
            binding.NameTextViewSignUp.requestFocus();
            return;
        }
        if(userPassword.isEmpty()) {
            binding.PasswordTextViewSignUp.setError("Please enter your password");
            binding.PasswordTextViewSignUp.requestFocus();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(userMail ,userPassword).addOnCompleteListener((task) -> {
            if(task.isSuccessful()) {
                StorageReference imgRef = storageReference.child("UserImg").child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid() + ".jpg");
                imgRef.putFile(imageUri).addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()) {
                        saveAll(userName ,userMail ,userPassword ,imgRef);
                    } else {
                        Toast.makeText(SignupActivity.this, "Error" + Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                Toast.makeText(SignupActivity.this, "註冊成功", Toast.LENGTH_SHORT).show();
            }  else {
                Toast.makeText(SignupActivity.this, "Error" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void saveAll( String userName, String userMail, String userPassword,  StorageReference imgRef) {
        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
            downloadUri =uri;
            HashMap<String ,Object> user = new HashMap<>();
            user.put("userName" ,userName);
            user.put("userMail" ,userMail);
            user.put("userPassword" ,userPassword);
            user.put("userImage" ,downloadUri.toString());
            user.put("Token", "no");
            user.put("userWallet", "no");
            user.put("userCoin", "0");
            if (binding.radioButtonBuyer.isChecked()) {
                user.put("userSeller", "buyer");
            } else if (binding.radioButtonSeller.isChecked()) {
                user.put("userSeller", "seller");
            }
            fireStore.collection("Users").document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).set(user).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    startActivity(new Intent(SignupActivity.this ,MainActivity.class));
                } else {
                    Toast.makeText(SignupActivity.this, "Error" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
                binding.progressBarSignUp.setVisibility(View.INVISIBLE);
            });
        });
    }
}