package com.example.testinterface_two;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.testinterface_two.databinding.ActivityMyShopItemBinding;
import com.example.testinterface_two.models.Shop;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyShopItemActivity extends AppCompatActivity {
    private ActivityMyShopItemBinding binding;
    private Dialog dialog;
    private FirebaseFirestore database;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyShopItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseFirestore.getInstance();
        intent = getIntent();
        dialog = new Dialog(this);
        getShop();
        set();
    }
    private void set() {
        String id = intent.getStringExtra("myShopID");
        binding.goBackMyShopItem.setOnClickListener(v -> onBackPressed());
        binding.editMyShopItemButton.setOnClickListener(v -> {
           dialog.setContentView(R.layout.alert_dialog_edit);
           dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
           Button edit = dialog.findViewById(R.id.editButton);
           Button cancel =  dialog.findViewById(R.id.cancelButtonAlert);
            EditText editPrice = dialog.findViewById(R.id.editTitle);
            EditText editCount = dialog.findViewById(R.id.editContent);
            EditText editMind = dialog.findViewById(R.id.editNotice);
            dialog.show();
            cancel.setOnClickListener(v12 -> dialog.dismiss());
            edit.setOnClickListener(v1 -> {
                String price = editPrice.getText().toString();
                String count = editCount.getText().toString();
                String mind = editMind.getText().toString();
                if (!price.equals("")) {
                    database.collection("Item").document(id).update("itemPrice", price).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            binding.myShopItemPrice.setText(price);
                            Toast.makeText(MyShopItemActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }
                if (!count.equals("")) {
                    database.collection("Item").document(id).update("itemCount", count).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            binding.myShopItemCount.setText(count);
                            Toast.makeText(MyShopItemActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }
                if (!mind.equals("")) {
                    database.collection("Item").document(id).update("itemMind", mind).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            binding.myShopItemNotice.setText(mind);
                            Toast.makeText(MyShopItemActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }
            });

        });
    }
    private void getShop() {
        String ID = intent.getStringExtra("myShopID");
        database.collection("Item").document(ID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String nm = task.getResult().getString("itemThing");
                String p = task.getResult().getString("itemPrice");
                String c = task.getResult().getString("itemCount");
                String n = task.getResult().getString("itemMind");
                String image  = task.getResult().getString("itemImage");
                binding.myShopItemName.setText(nm);
                binding.myShopItemPrice.setText(p);
                binding.myShopItemCount.setText(c);
                binding.myShopItemNotice.setText(n);
                Glide.with(this).load(image).into(binding.myShopItemImage);
            }
        });

    }
}