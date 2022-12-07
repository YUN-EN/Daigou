package com.example.testinterface_two;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.testinterface_two.databinding.ActivityCommentDealBinding;
import com.example.testinterface_two.models.Deal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class CommentDealActivity extends AppCompatActivity {
    private ActivityCommentDealBinding binding;
    private FirebaseFirestore database;
    private String id;
    private StorageReference storageReference;
    private ActivityResultLauncher<String> myGetContent;
    private Uri ImgUri = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentDealBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        assert firebaseUser != null;
        id = firebaseUser.getUid();
        database = FirebaseFirestore.getInstance();
        binding.commentImage.setOnClickListener(v -> selectItemImage());
        myGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if(result != null) {
                ImgUri = result;
                binding.commentImage.setImageURI(ImgUri);
            }
        });
        back();
        binding.submitButton.setOnClickListener(v -> submitComment());
    }


    private void back() {
        binding.cancelCommentButton.setOnClickListener(v -> startActivity(new Intent(this ,shopActivity.class)));
    }

    private void submitComment() {
        binding.progressBarCommentDeal.setVisibility(View.VISIBLE);
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String date = dateFormat.format(calendar.getTime());
        String commentText = binding.editCommentText.getText().toString();
        String star = String.valueOf(binding.ratingBar.getRating());
            if (ImgUri != null) {
                StorageReference commentImageRef = storageReference.child("Comment_Image").child(FieldValue.serverTimestamp() + ".jpg");
                commentImageRef.putFile(ImgUri).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        commentImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            Intent intent = getIntent();
                            Intent intent1 = getIntent();
                            String name = intent1.getStringExtra("dealItemName");
                            String dealItemID = intent.getStringExtra("dealItemID");
                            HashMap<String, Object> comment = new HashMap<>();
                            comment.put("user" ,id);
                            comment.put("commentItemID", dealItemID);
                            comment.put("commentText" ,commentText);
                            comment.put("commentItemName", name);
                            comment.put("star" ,star);
                            comment.put("commentImage" ,uri.toString());
                            comment.put("commentDate" ,date);
                            database.collection("ItemComment").add(comment).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Toast.makeText(CommentDealActivity.this, "評論成功", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(CommentDealActivity.this , shopActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(CommentDealActivity.this, Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                binding.progressBarCommentDeal.setVisibility(View.INVISIBLE);
                            });
                        });
                        createBlock();
                    }
                });
            }
    }
    private void selectItemImage() {
        myGetContent.launch("image/*");
    }
    private void createBlock() {
        Deal dealBlock = (Deal) getIntent().getSerializableExtra("deal");
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String date = dateFormat.format(calendar.getTime());
        Intent idIntent  = getIntent();
        String Id = idIntent.getStringExtra("dealItemID");
        HashMap<String ,Object> block = new HashMap<>();
        block.put("userCoinRecord", dealBlock.dealUserId);
        block.put("dateCoinRecord" ,date);
        block.put("numCoinRecord", "1");
        block.put("dealIdCoinRecord", Id);
        block.put("categoryCoinRecord", "商品評價");
        database.collection("CoinRecord").add(block);
        database.collection("Users").document(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int my  = Integer.parseInt(Objects.requireNonNull(task.getResult().getString("userCoin")));
                database.collection("Users").document(id).update("userCoin" ,String.valueOf(my+1));
            }
        });
    }
}