package com.example.testinterface_two;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.testinterface_two.Adapter.PersonAdapter;
import com.example.testinterface_two.models.User;
import com.example.testinterface_two.models.UserPersonComment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyPastDealCommentFragment extends Fragment {
    private List<UserPersonComment> userPersonCommentList;
    private PersonAdapter personAdapter;


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_past_deal_comment, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        RecyclerView recyclerView = root.findViewById(R.id.myPastDealCommentRecyclerView);
        assert user != null;
        String ID = user.getUid();
        userPersonCommentList = new ArrayList<>();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        personAdapter = new PersonAdapter(userPersonCommentList ,(Activity) getContext());
        recyclerView.setAdapter(personAdapter);

        firebaseFirestore.collection("UserPersonComment").whereEqualTo("personID", ID).addSnapshotListener((value, error) -> {
            assert value != null;
            for (DocumentChange documentChange: value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    String id = documentChange.getDocument().getId();
                    UserPersonComment userPersonComment = documentChange.getDocument().toObject(UserPersonComment.class).withId(id);
                    userPersonCommentList.add(userPersonComment);
                    personAdapter.notifyDataSetChanged();
                }
            }
        });
        return root;
    }
}