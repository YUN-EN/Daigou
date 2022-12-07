package com.example.testinterface_two;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.example.testinterface_two.Adapter.UserAdapter;
import com.example.testinterface_two.databinding.ActivityAllUserBinding;
import com.example.testinterface_two.models.User;
import com.example.testinterface_two.models.UserListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AllUserActivity extends AppCompatActivity implements UserListener {
    private ActivityAllUserBinding binding;
    private UserAdapter userAdapter;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore database;
    private String UserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.userRecycleView.setHasFixedSize(true);
        binding.userRecycleView.setLayoutManager(new LinearLayoutManager(this));
        init();
        showAllUser();
        setListener();
    }

    private void init() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        UserID =firebaseUser.getUid();
        database = FirebaseFirestore.getInstance();
    }
    private void setListener() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }
    private void showAllUser() {
        database.collection("Users").get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult() != null) {
                List<User> userList =new ArrayList<>();
                for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    if(UserID.equals(queryDocumentSnapshot.getId())) {
                        continue;
                    }
                    User user =new User();
                    user.userName = queryDocumentSnapshot.getString("userName");
                    user.userMail = queryDocumentSnapshot.getString("userMail");
                    user.userImage =queryDocumentSnapshot.getString("userImage");
                    user.id =queryDocumentSnapshot.getId();
                    userList.add(user);
                }
                if (userList.size()>0) {
                    userAdapter = new UserAdapter(AllUserActivity.this ,userList ,AllUserActivity.this);
                    binding.userRecycleView.setAdapter(userAdapter);
                } else {
                    Toast.makeText(AllUserActivity.this, "Error:" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AllUserActivity.this, "Error:" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext() ,ChatRoomActivity.class);
        intent.putExtra("user" ,  user);
        startActivity(intent);
        finish();
    }
}