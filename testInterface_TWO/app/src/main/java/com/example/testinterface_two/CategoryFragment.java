package com.example.testinterface_two;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.testinterface_two.Adapter.PostAdapter;
import com.example.testinterface_two.models.Post;
import com.example.testinterface_two.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CategoryFragment extends Fragment {
    private RecyclerView recyclerViewPost;
    private List<Post> list ;
    private PostAdapter postAdapter;
    private FirebaseFirestore fireStore;
    private ListenerRegistration listenerRegistration;
    private List<User> userList;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View view =  inflater.inflate(R.layout.fragment_category, container, false);
        ImageView notifyImage = view.findViewById(R.id.goNotify);
      notifyImage.setOnClickListener(v -> startActivity(new Intent(getActivity() ,NotifyActivity.class)));
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
      fireStore = FirebaseFirestore.getInstance();
        FloatingActionButton addButton = view.findViewById(R.id.floatingButtonPost);
        SearchView searchViewPost = view.findViewById(R.id.searchViewPost);
      addButton.setOnClickListener(v -> startActivity(new Intent(getContext() ,addPostActivity.class)));
      recyclerViewPost = view.findViewById(R.id.recyclerViewPost);
      recyclerViewPost.setLayoutManager(new LinearLayoutManager(getContext()));
      list = new ArrayList<>();
      userList = new ArrayList<>();
      postAdapter = new PostAdapter((Activity) getContext(),list ,userList);
      recyclerViewPost.setAdapter(postAdapter);
        searchViewPost.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
        if(firebaseUser != null ) {
            recyclerViewPost.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    boolean isBottom = !recyclerViewPost.canScrollVertically(1);
                    if(isBottom) {
                        Toast.makeText(getContext() ,"Reach bottom" ,Toast.LENGTH_SHORT).show();
                    }
                }
            });
            Query query = fireStore.collection("Post").orderBy("Time", Query.Direction.DESCENDING);
            listenerRegistration = query.addSnapshotListener((value, error) -> {
                assert value != null;
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        String postID = documentChange.getDocument().getId();
                        Post post = documentChange.getDocument().toObject(Post.class).withId(postID);
                        String postId = documentChange.getDocument().getString("User");
                        assert postId != null;
                        fireStore.collection("Users").document(postId).get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                User user = Objects.requireNonNull(task.getResult()).toObject(User.class);
                                userList.add(user);
                                list.add(post);
                                postAdapter.notifyDataSetChanged();
                            }else {
                                Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    postAdapter.notifyDataSetChanged();
                }
                listenerRegistration.remove();
            });
        }
      return view;
    }
    private void filter(String Text) {
        List<Post> listPost  = new ArrayList<>();
        for (Post post:list) {
            if (post.getCaption().toLowerCase().contains(Text.toLowerCase())) {
                listPost.add(post);
            }
        }
        postAdapter.filterList(listPost);
    }
}