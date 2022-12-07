package com.example.testinterface_two;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.example.testinterface_two.databinding.ActivityForgetBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetActivity extends AppCompatActivity {
        private ActivityForgetBinding binding;
        private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        binding.resetButton.setOnClickListener(v -> forget());
        binding.backMain.setOnClickListener(v -> onBackPressed());
    }

    private void forget() {
        String mail = binding.resetMailText.getText().toString();
        firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ForgetActivity.this, "check your mail to reset password", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ForgetActivity.this ,MainActivity.class));
            }
        });

    }


}