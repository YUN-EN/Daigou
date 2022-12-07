package com.example.testinterface_two;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.testinterface_two.databinding.ActivityAddItemBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.HashMap;

public class AddItemActivity extends AppCompatActivity {
    private ActivityAddItemBinding binding;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore database;
    private StorageReference storageReference;
    private String userID;
    private ActivityResultLauncher<String> myGetContent;
    private Uri ItemImgUri = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        userID = firebaseUser.getUid();
        database = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        binding.itemImage.setOnClickListener(v -> selectItemImage());
        binding.cancelItemButton.setOnClickListener(v -> onBackPressed());
        myGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if(result != null) {
               ItemImgUri = result;
                binding.itemImage.setImageURI(ItemImgUri);
            }
        });
        saveItem();
    }
    private void saveItem() {
       binding.onlineButton.setOnClickListener(v -> {
           binding.progressBar.setVisibility(View.VISIBLE);
           String thing = binding.editThingText.getText().toString();
           String count = binding.editItemCount.getText().toString();
           String price = binding.editItemPrice.getText().toString();
           String mind = binding.editItemMind.getText().toString();
           if (thing.isEmpty()) {
               binding.editThingText.setError("Please enter something");
               binding.editThingText.requestFocus();
           }
           if (count.isEmpty()) {
               binding.editItemCount.setError("Please write the count of item");
               binding.editItemCount.requestFocus();
           }
           if (price.isEmpty()) {
               binding.editItemPrice.setError("Please enter the price of item");
               binding.editItemPrice.requestFocus();
           }
           if (ItemImgUri != null) {
               StorageReference itemImageRef = storageReference.child("Item_Image").child(FieldValue.serverTimestamp() + ".jpg");
               itemImageRef.putFile(ItemImgUri).addOnCompleteListener(task -> {
                   if (task.isSuccessful()) {
                       itemImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                           HashMap<String, Object> item = new HashMap<>();
                           if (binding.checkCloth.isChecked()) {
                               item.put("itemCategory", "clothes");
                           } else if(binding.checkBag.isChecked()) {
                               item.put("itemCategory" ,"bag");
                           } else if (binding.checkMedicine.isChecked()) {
                               item.put("itemCategory" ,"medicine");
                           } else if (binding.checkShoes.isChecked()) {
                               item.put("itemCategory", "shoes");
                           } else if (binding.checkSnack.isChecked()){
                               item.put("itemCategory", "snack");
                           } else if (binding.checkAccessories.isChecked()){
                               item.put("itemCategory", "accessories");
                           }
                           item.put("userID", userID);
                           item.put("itemImage", uri.toString());
                           item.put("itemThing", thing);
                           item.put("itemCount", count);
                           item.put("itemPrice", price);
                           item.put("itemMind", mind);
                           item.put("itemDate", FieldValue.serverTimestamp());
                           database.collection("Item").add(item).addOnCompleteListener(task1 -> {
                               if (task1.isSuccessful()) {
                                   Toast.makeText(AddItemActivity.this, "上架成功", Toast.LENGTH_SHORT).show();
                                   database.collection("Users").document(userID).update("userSeller", "seller");
                                   startActivity(new Intent(AddItemActivity.this ,shopActivity.class));
                                   finish();
                               } else {
                                   Toast.makeText(AddItemActivity.this, "online fail", Toast.LENGTH_SHORT).show();
                               }
                               binding.progressBar.setVisibility(View.INVISIBLE);
                           });
                       });
                   }
               });
           }
       });
    }
    private void selectItemImage() {
        myGetContent.launch("image/*");
    }

}