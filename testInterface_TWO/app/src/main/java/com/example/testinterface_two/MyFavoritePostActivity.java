package com.example.testinterface_two;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import android.annotation.SuppressLint;
import android.os.Bundle;
import com.example.testinterface_two.Adapter.SavePostAdapter;
import com.example.testinterface_two.databinding.ActivityMyFavoritePostBinding;
import com.example.testinterface_two.models.SavePost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class MyFavoritePostActivity extends AppCompatActivity {
    private List<SavePost> List;
    private SavePostAdapter adapter;
    private FirebaseFirestore firebaseFirestore;
    private String id;
    private ActivityMyFavoritePostBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyFavoritePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        id= user.getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        binding.myFavoritePostRecyclerView.setHasFixedSize(true);
        binding.myFavoritePostRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext() ,3));
        List = new ArrayList<>();
        adapter = new SavePostAdapter(List ,this);
        binding.myFavoritePostRecyclerView.setAdapter(adapter);
        action();
        showMyFavorite();
    }


    private void action() {
        binding.goBackToPerson.setOnClickListener(v -> onBackPressed());
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showMyFavorite(){
        firebaseFirestore.collection("SavePost").whereEqualTo("saveUserId" ,id).addSnapshotListener(this, (value, error) -> {
            assert value != null;
            for (DocumentChange documentChange:value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    SavePost savePost = documentChange.getDocument().toObject(SavePost.class);
                    List.add(savePost);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}