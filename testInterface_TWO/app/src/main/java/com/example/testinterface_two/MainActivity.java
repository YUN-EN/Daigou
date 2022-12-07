package com.example.testinterface_two;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;
import com.example.testinterface_two.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import java.util.Objects;

public class  MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseAuth Auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Auth = FirebaseAuth.getInstance();
        binding.SignUpTextView.setOnClickListener(v -> startActivity(new Intent(MainActivity.this ,SignupActivity.class)));
        binding.loginButton.setOnClickListener(v -> showBottomView());
        binding.forgetPasswordTextview.setOnClickListener(v -> toForgetActivity());
    }

    private void toForgetActivity() {
        startActivity(new Intent(MainActivity.this ,ForgetActivity.class));
    }

    private void showBottomView() {
        String userMail = binding.MailTextView.getText().toString().trim();
        String userPassword = binding.PasswordTextView.getText().toString().trim();
        if(userMail.isEmpty()) {
            binding.MailTextView.setError("請輸入信箱");
            binding.MailTextView.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(userMail).matches()) {
            binding.MailTextView.setError("請輸入有效的密碼");
            binding.MailTextView.requestFocus();
            return;
        }
        if(userPassword.isEmpty()) {
            binding.PasswordTextView.setError("請輸入密碼");
            binding.PasswordTextView.requestFocus();
            return;
        }
        Auth.signInWithEmailAndPassword(userMail ,userPassword).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                getToken();
                startActivity(new Intent(MainActivity.this , shopActivity.class));
            } else {
                Toast.makeText(MainActivity.this, "登入失敗", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken) ;
    }

    private void updateToken(String token) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =database.collection("Users").document(Objects.requireNonNull(Auth.getUid()));
        documentReference.update("Token" , token)
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this , "Unable to update Token" ,Toast.LENGTH_SHORT ).show());
    }
}