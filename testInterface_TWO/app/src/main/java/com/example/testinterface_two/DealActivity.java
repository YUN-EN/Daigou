package com.example.testinterface_two;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.testinterface_two.databinding.ActivityDealBinding;
import com.example.testinterface_two.models.MyShopCart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class DealActivity extends AppCompatActivity {
    private ActivityDealBinding binding;
    private MyShopCart myCart;
    private FirebaseFirestore firebaseFirestore;
    private String buyerID;
    private final static String PRIVATE_KEY="5ddd08311993eb2f2c73c9b904a395c2f523873076bffe7c9a0c8cbe8d331f46";
    private final static String BLOCKCHAIN_NODE="https://goerli.infura.io/v3/60414d13a9aa4c5abe5348f3df0fb2a8";
    private final static BigInteger GAS_LIMIT=BigInteger.valueOf(210000);
    private final static BigInteger GAS_PRICE=BigInteger.valueOf(1000000000L);
    private String date;
    private String myShoId;
    private String contract_address;
    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDealBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        buyerID = firebaseUser.getUid();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My Notification" ,"My Notification" , NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        date = dateFormat.format(calendar.getTime());
        myCart = (MyShopCart) getIntent().getSerializableExtra("MyShopCart");
        Intent intent = getIntent();
        myShoId = intent.getStringExtra("Id");
        setListener();
        getDeal();
    }
    @SuppressLint("SimpleDateFormat")
    private void getDeal() {
        binding.payItemName.setText(myCart.MyShopCartName);
        binding.payItemCount.setText(myCart.MyShopCartCount);
        binding.payItemTotal.setText(myCart.MyShopCartPrice);
        binding.buyItemDate.setText(date);
        Glide.with(DealActivity.this).load(myCart.MyShopCartImage).into(binding.payItemImage);
        String sellerId = myCart.SellerID;
       firebaseFirestore.collection("Users").document(sellerId).get().addOnCompleteListener(task -> {
           String name = Objects.requireNonNull(task.getResult()).getString("userName");
           binding.sellerItemName.setText(name);
       });
        binding.payButton.setOnClickListener(v -> {
            Toast.makeText(DealActivity.this,"交易處理中",Toast.LENGTH_LONG).show();
            String itemID = myCart.MyShopCartItemId;
            int  c = Integer.parseInt(binding.payItemTotal.getText().toString());
            int p = Integer.parseInt(binding.payItemCount.getText().toString());
            String t = String.valueOf(c*p);
            String order_no= myShoId;
            String dateOrder="date:" +date;
            String buyer_id="buyer_id:" +buyerID;
            String seller_id="seller_id:" +myCart.SellerID;
            String com_id="commodity_id:" + itemID;
            String com_amount="amount:" +binding.payItemCount.getText().toString();
            String total="total_price:" + t;
            Web3j web3j=Web3j.build(new HttpService(BLOCKCHAIN_NODE));
            try {
                Credentials credentials=Credentials.create(PRIVATE_KEY);
                ContractGasProvider gasProvider =new StaticGasProvider(GAS_PRICE,GAS_LIMIT);
                Solidity_order_sol_myOrder solidity_order_sol_myOrder=Solidity_order_sol_myOrder.deploy(web3j,credentials,gasProvider,
                        order_no,dateOrder,buyer_id,seller_id,com_id,com_amount,total).sendAsync().get();
                contract_address = solidity_order_sol_myOrder.getContractAddress();
                NotificationCompat.Builder builder = new NotificationCompat.Builder(DealActivity.this ,"My Notification");
                builder.setContentTitle("上鏈成功");
                builder.setContentText(contract_address);
                builder.setAutoCancel(true);
                builder.setSmallIcon(R.drawable.ic_notifications);
                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(DealActivity.this);
                managerCompat.notify(1,builder.build());
                //Toast.makeText(DealActivity.this,"交易成功",Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(DealActivity.this,"失敗",Toast.LENGTH_LONG).show();
            }
            HashMap<String , Object> dealMap = new HashMap<>();
            dealMap.put("dealUserId" , buyerID);
            dealMap.put("dealItemName" , binding.payItemName.getText().toString());
            dealMap.put("dealItemImage" , myCart.MyShopCartImage);
            dealMap.put("dealItemCount" , binding.payItemCount.getText().toString());
            dealMap.put("dealItemPrice" , binding.payItemTotal.getText().toString());
            dealMap.put("dealItemID", itemID);
            dealMap.put("dealBlockAddress", contract_address);
            dealMap.put("dealCartID", myShoId);
            dealMap.put("sellerId" ,myCart.SellerID);
            dealMap.put("sellerName" , binding.sellerItemName.getText().toString());
            dealMap.put("dealDate" , binding.buyItemDate.getText().toString());
            dealMap.put("dealAddress" ,binding.editAddressText.getText().toString());
            dealMap.put("dealItemFormat", binding.remarkEditText.getText().toString());
            dealMap.put("isReceive" ,false);
            dealMap.put("isOut", "no");
            firebaseFirestore.collection("Deals").add(dealMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    startActivity(new Intent(DealActivity.this , shopActivity.class));
                } else {
                    Toast.makeText(DealActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });
    }
    private void setListener() {
        binding.cancelButton.setOnClickListener(v -> onBackPressed());
    }
}