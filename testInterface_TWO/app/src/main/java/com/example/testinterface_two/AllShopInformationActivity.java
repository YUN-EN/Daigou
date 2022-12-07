package com.example.testinterface_two;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.testinterface_two.databinding.ActivityAllShopInformationBinding;
import com.example.testinterface_two.models.Shop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Objects;

public class AllShopInformationActivity extends AppCompatActivity {
    private ActivityAllShopInformationBinding activityAllShopInformationBinding;
    private Shop ShopInfo;
    private int number;
    private FirebaseUser firebaseUser;
    private String Id;
    private FirebaseFirestore database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAllShopInformationBinding = ActivityAllShopInformationBinding.inflate(getLayoutInflater());
        setContentView(activityAllShopInformationBinding.getRoot());
        database  = FirebaseFirestore.getInstance();
        loadShopInformation();
        setListener();
    }
    private void showNumber() {
            activityAllShopInformationBinding.buyNumberText.setText(String.valueOf(number));
    }
    private void setListener() {
        activityAllShopInformationBinding.backShopMall.setOnClickListener(v -> startActivity(new Intent(this ,shopActivity.class)));
    }
    private void loadShopInformation() {
        ShopInfo = (Shop) getIntent().getSerializableExtra("shop");
        Intent intentID = getIntent();
        String itemID = intentID.getStringExtra("shopID");
        activityAllShopInformationBinding.shopItemName.setText(ShopInfo.itemThing);
        //activityAllShopInformationBinding.itemPriceNumber.setText(ShopInfo.itemPrice);
        activityAllShopInformationBinding.itemInformation.setText(ShopInfo.itemMind);
        Glide.with(this).load(ShopInfo.itemImage).into(activityAllShopInformationBinding.shopItemImage);
        String id = ShopInfo.userID;
        int pp = Integer.parseInt(ShopInfo.itemPrice);

        activityAllShopInformationBinding.removeImageView.setOnClickListener(v -> {
            if (number == 0) {
                activityAllShopInformationBinding.itemPriceNumber.setText(String.valueOf(0));
                Toast.makeText(AllShopInformationActivity.this, "can not lower zero", Toast.LENGTH_SHORT).show();
            } else {
                number --;
                activityAllShopInformationBinding.itemPriceNumber.setText(String.valueOf(number*pp));
                showNumber();
            }
        });
        activityAllShopInformationBinding.addImageView.setOnClickListener(v -> {
            number++;
            activityAllShopInformationBinding.itemPriceNumber.setText(String.valueOf(number*pp));
            showNumber();
        });

        activityAllShopInformationBinding.goItemAllDealComment.setOnClickListener(v -> {
            Intent intent = new Intent(this ,PastDealCommentActivity.class);
            intent.putExtra("dealCommentItemID", itemID);
            startActivity(intent);
            finish();
        });
        activityAllShopInformationBinding.checkUserShopButton.setOnClickListener(v -> {
            Intent intent = new Intent(new Intent(this ,SellerDetailActivity.class));
            intent.putExtra("SellerID" ,id);
            startActivity(intent);
            finish();
        });
        database.collection("Users").document(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String name = Objects.requireNonNull(task.getResult()).getString("userName");
                String image = task.getResult().getString("userImage");
                activityAllShopInformationBinding.userNameInformation.setText(name);
                Glide.with(AllShopInformationActivity.this).load(image).into(activityAllShopInformationBinding.userImageInformation);
            }
        });
        activityAllShopInformationBinding.AddToCartButton.setOnClickListener(v -> {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            assert firebaseUser != null;
            Id = firebaseUser.getUid();
            HashMap<String ,Object> myShopCart = new HashMap<>();
            myShopCart.put("UserId"  ,Id);
            myShopCart.put("SellerID", id);
            myShopCart.put("MyShopCartItemId" ,itemID);
            myShopCart.put("MyShopCartName" ,activityAllShopInformationBinding.shopItemName.getText().toString());
            myShopCart.put("MyShopCartImage" ,ShopInfo.itemImage);
            myShopCart.put("MyShopCartCount" ,activityAllShopInformationBinding.buyNumberText.getText().toString());
            myShopCart.put("MyShopCartPrice" ,activityAllShopInformationBinding.itemPriceNumber.getText().toString());
            database.collection("MyShopCart").add(myShopCart).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    startActivity(new Intent(this , shopActivity.class));
                    Toast.makeText(AllShopInformationActivity.this, "Add TO MyShopCart Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AllShopInformationActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

}