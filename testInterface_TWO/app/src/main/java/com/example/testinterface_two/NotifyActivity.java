package com.example.testinterface_two;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.annotation.SuppressLint;
import android.os.Bundle;
import com.example.testinterface_two.Adapter.NotifyAdapter;
import com.example.testinterface_two.databinding.ActivityNotifyBinding;
import com.example.testinterface_two.models.Notify;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class NotifyActivity extends AppCompatActivity {
    private ActivityNotifyBinding binding;
    private NotifyAdapter notifyAdapter;
    private List<Notify> list;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        user = FirebaseAuth.getInstance().getCurrentUser();
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(NotifyActivity.this));
        list = new ArrayList<>();
        notifyAdapter = new NotifyAdapter(this ,list);
        binding.recyclerView.setAdapter(notifyAdapter);
        binding.includeToolbarNotify.backPost.setOnClickListener(v -> onBackPressed());
        showNotify();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void showNotify() {
        String id = user.getUid();
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        fireStore.collection("Notify").whereEqualTo("posterID", user.getUid()).addSnapshotListener(this, (value, error) -> {
            assert value != null;
            for (DocumentChange documentChange : value.getDocumentChanges())  {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    String notifyUserId = documentChange.getDocument().getString("userId");
                    assert notifyUserId != null;
                    if (!notifyUserId.equals(id)) {
                        Notify notify = documentChange.getDocument().toObject(Notify.class);
                        list.add(notify);
                    }
                }
                notifyAdapter.notifyDataSetChanged();
            }
        });
    }
}