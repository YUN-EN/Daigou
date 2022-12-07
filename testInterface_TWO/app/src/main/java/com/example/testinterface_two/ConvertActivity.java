package com.example.testinterface_two;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;
import com.example.testinterface_two.databinding.ActivityConvertBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class ConvertActivity extends AppCompatActivity {
    private ActivityConvertBinding binding;
    private FirebaseUser user;
    private  FirebaseFirestore firebaseFirestore;
    private String m;
    private final static String BLOCKCHAIN_NODE="https://goerli.infura.io/v3/60414d13a9aa4c5abe5348f3df0fb2a8";
    private final static BigInteger GAS_LIMIT=BigInteger.valueOf(100000);
    private final static BigInteger GAS_PRICE=BigInteger.valueOf(2500000000L);
    private final static String PRIVATE_KEY="5ddd08311993eb2f2c73c9b904a395c2f523873076bffe7c9a0c8cbe8d331f46";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConvertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        binding.coinBack.setOnClickListener(v -> onBackPressed());
        firebaseFirestore.collection("Users").document(Objects.requireNonNull(user).getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                m = task.getResult().getString("userCoin");
                binding.userCoin.setText(m);
            }
        });
        convert();
    }
    private void convert() {
        binding.button.setOnClickListener(v -> {
            try {
                if (Integer.parseInt(m) > 10) {
                    change(0.5);
                    goBase("10");
                }else {
                    Toast.makeText(this, "代幣不足", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        binding.twoButton.setOnClickListener(v -> {
            try {
                if (Integer.parseInt(m) > 20) {
                    change(1);
                    goBase("20");
                }else {
                    Toast.makeText(this, "代幣不足", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        binding.threeButton.setOnClickListener(v -> {
            try {
                if (Integer.parseInt(m) > 30) {
                    change(1.5);
                    goBase("30");
                }else {
                    Toast.makeText(this, "代幣不足", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        binding.fourButton.setOnClickListener(v -> {
            try {
                if (Integer.parseInt(m) > 40) {
                    change(2);
                    goBase("40");
                }else {
                    Toast.makeText(this, "代幣不足", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        binding.fiveButton.setOnClickListener(v -> {
            try {
                if (Integer.parseInt(m) > 50) {
                    change(2.5);
                    goBase("50");
                }else {
                    Toast.makeText(this, "代幣不足", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        binding.sixButton.setOnClickListener(v -> {
            try {
                if (Integer.parseInt(m) > 60) {
                    change(3);
                    goBase("60");
                }else {
                    Toast.makeText(this, "代幣不足", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        binding.sevenButton.setOnClickListener(v -> {
            try {
                if (Integer.parseInt(m) > 70) {
                    change(3.5);
                    goBase("70");
                }else {
                    Toast.makeText(this, "代幣不足", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        binding.eightButton.setOnClickListener(v -> {
            try {
                if (Integer.parseInt(m) > 80) {
                    change(4);
                    goBase("80");
                } else {
                    Toast.makeText(this, "代幣不足", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void change(double value) throws Exception{
        String toAddress="0x1aA33A3C20EF14B56CE28129F481122a18C4304A";
        Web3j web3j=Web3j.build(new HttpService(BLOCKCHAIN_NODE));
        Credentials credentials=Credentials.create(PRIVATE_KEY);
        TransactionManager manager=new RawTransactionManager(web3j,credentials);
        Transfer transfer=new Transfer(web3j,manager);
        TransactionReceipt receipt =transfer.sendFunds(
                toAddress, BigDecimal.valueOf(value), Convert.Unit.FINNEY,GAS_PRICE,GAS_LIMIT).sendAsync().get();
        Toast.makeText(this, "test" +receipt, Toast.LENGTH_SHORT).show();
    }
    private void goBase(String value) {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String date = dateFormat.format(calendar.getTime());
        HashMap<String ,Object> coin = new HashMap<>();
        coin.put("userCoinSpend" ,user.getUid());
        coin.put("dateCoinSpend", date);
        coin.put("numCoinSpend", value);
        coin.put("categoryCoinSpend", "convertCoin");
        firebaseFirestore.collection("CoinSpend").add(coin).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ConvertActivity.this, "兌換成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ConvertActivity.this, "兌換失敗", Toast.LENGTH_SHORT).show();
            }
        });
        firebaseFirestore.collection("Users").document(user.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int  money = Integer.parseInt(Objects.requireNonNull(task.getResult().getString("userCoin")));
                int n = Integer.parseInt(value);
                binding.userCoin.setText(String.valueOf(money-n));
                firebaseFirestore.collection("Users").document(user.getUid()).update("userCoin" ,String.valueOf(money-n));
            }
        });
    }
}