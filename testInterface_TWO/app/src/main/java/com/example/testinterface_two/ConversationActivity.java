package com.example.testinterface_two;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.bumptech.glide.Glide;
import com.example.testinterface_two.Adapter.ConversationAdapter;
import com.example.testinterface_two.databinding.ActivityConversationBinding;
import com.example.testinterface_two.models.ChatMessage;
import com.example.testinterface_two.models.ConversationsListener;
import com.example.testinterface_two.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConversationActivity extends AppCompatActivity implements ConversationsListener {
    private ActivityConversationBinding binding;
    private ConversationAdapter conversationAdapter;
    private List<ChatMessage> conversations;
    private FirebaseFirestore database;
    private String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConversationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        UserId = firebaseUser.getUid();
        init();
        setListener();
        listenerConversation();
    }


    private void init() {
        conversations = new ArrayList<>();
        conversationAdapter = new ConversationAdapter(conversations ,this ,this);
        binding.conversationRecyclerView.setAdapter(conversationAdapter);
        database = FirebaseFirestore.getInstance();
       database.collection("Users").document(UserId).addSnapshotListener((value, error) -> {
           assert value != null;
           Glide.with(getApplicationContext()).load(value.getString("userImage")).into(binding.imageProfile);
           binding.textName.setText(value.getString("userName"));
       });

    }




    private void setListener() {
        binding.fabNewChat.setOnClickListener(v -> startActivity(new Intent(ConversationActivity.this ,AllUserActivity.class)));
        binding.imageHome.setOnClickListener(v -> startActivity(new Intent(this ,shopActivity.class)));
    }

    private void listenerConversation() {
        database.collection("Conversion").whereEqualTo("senderId" ,UserId).addSnapshotListener(eventListener);
        database.collection("Conversion").whereEqualTo("receiverId" ,UserId).addSnapshotListener(eventListener);
    }

    @SuppressLint("NotifyDataSetChanged")
    private final EventListener<QuerySnapshot> eventListener =(value, error) -> {
        if (error!=null) {
            return;
        }
        if (value!=null) {
            for (DocumentChange documentChange: value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    String senderId = documentChange.getDocument().getString("senderId");
                    String receiverId = documentChange.getDocument().getString("receiverId");
                    ChatMessage chatMessage =new ChatMessage();
                    chatMessage.senderId =senderId;
                    chatMessage.receiverId = receiverId;
                    if (UserId.equals(senderId)) {
                        chatMessage.conversionName = documentChange.getDocument().getString("receiverName");
                        chatMessage.conversionId =documentChange.getDocument().getString("receiverId");
                      //  chatMessage.conversionImage = documentChange.getDocument().getString("receiverImage");
                    } else {
                        chatMessage.conversionName = documentChange.getDocument().getString("senderName");
                        chatMessage.conversionId = documentChange.getDocument().getString("senderId");
                       // chatMessage.conversionImage = documentChange.getDocument().getString("senderImage");
                    }
                    chatMessage.message =documentChange.getDocument().getString("lastMessage");
                    chatMessage.dateObject = documentChange.getDocument().getDate("timeStamp");
                    conversations.add(chatMessage);
                } else if (documentChange.getType() == DocumentChange.Type.MODIFIED) {
                    for (int i = 0;i<conversations.size();i++) {
                        String senderId = documentChange.getDocument().getString("senderId");
                        String receiverId = documentChange.getDocument().getString("receiverId");
                        if (conversations.get(i).senderId.equals(senderId) && conversations.get(i).receiverId.equals(receiverId)) {
                            conversations.get(i).message = documentChange.getDocument().getString("lastMessage");
                            conversations.get(i).dateObject = documentChange.getDocument().getDate("timeStamp");
                            break;
                        }
                    }
                }
            }
            Collections.sort(conversations ,(obj1 ,obj2) -> obj2.dateObject.compareTo(obj1.dateObject));
            conversationAdapter.notifyDataSetChanged();
            binding.conversationRecyclerView.smoothScrollToPosition(0);
            binding.conversationRecyclerView.setVisibility(View.VISIBLE);
        }
    };
    @Override
    public void onConversationClick(User user) {
        Intent intent =new Intent(getApplicationContext() ,ChatRoomActivity.class);
        intent.putExtra("user" ,user);
        startActivity(intent);
        finish();
    }

}