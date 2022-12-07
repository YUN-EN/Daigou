package com.example.testinterface_two;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.bumptech.glide.Glide;
import com.example.testinterface_two.Adapter.ChatAdapter;
import com.example.testinterface_two.databinding.ActivityChatRoomBinding;
import com.example.testinterface_two.models.ChatMessage;
import com.example.testinterface_two.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ChatRoomActivity extends AppCompatActivity {
    private ActivityChatRoomBinding binding;
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore firebaseFirestore;
    private User receiverUser;
    private String userId;
    private String conversionId =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        userId = firebaseUser.getUid();
        preferenceManager = new PreferenceManager(getApplicationContext());
        init();
        loadReceiver();
        setListener();
        listenMessage();
    }
    private void init() {
        firebaseFirestore =FirebaseFirestore.getInstance();
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(userId,chatMessages);
        binding.roomRecycleView.setAdapter(chatAdapter);
    }

    private void loadReceiver() {
        receiverUser = (User) getIntent().getSerializableExtra("user");
        binding.textName.setText(receiverUser.userName);
        String id = receiverUser.id;
        if (id != null) {
            firebaseFirestore.collection("Users").document(id).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    String imageView = Objects.requireNonNull(task.getResult()).getString("userImage");
                    Glide.with(ChatRoomActivity.this).load(imageView).into(binding.chatUserImage);
                }
            });
        }
        Glide.with(this).load(receiverUser.userImage).into(binding.chatUserImage);
    }
    private void setListener() {
        binding.imageBack.setOnClickListener(v -> startActivity(new Intent(this ,ConversationActivity.class)));
        binding.layoutSend.setOnClickListener(v -> sendMessage());
    }


    private void sendMessage() {
        HashMap<String ,Object> message = new HashMap<>();
        message.put("senderId" ,userId);
        message.put("receiverId" ,receiverUser.id);
        message.put("message" ,binding.inputMessage.getText().toString());
        message.put("timeStamp" ,new Date());
        firebaseFirestore.collection("Chat").add(message);
        if (conversionId!=null) {
            updateConversion(binding.inputMessage.getText().toString());
        } else {
            HashMap<String ,Object> conversion =new HashMap<>();
            conversion.put("senderId" ,userId);
            conversion.put("senderName" ,preferenceManager.getSrtring("userName"));
                   // conversion.put("senderImage" ,documentSnapshot.getString("userImage"));
            conversion.put("receiverId" ,receiverUser.id);
            conversion.put("receiverName" ,receiverUser.userName);
                   // conversion.put("receiverImage" , receiverUser.userImage);
            conversion.put("lastMessage" ,binding.inputMessage.getText().toString());
            conversion.put("timeStamp" ,new Date());
            addConversion(conversion);
        }
        binding.inputMessage.setText(null);
    }
    private String getReadableDateTime(Date date) {
        return new SimpleDateFormat("MMMM dd,yyyy hh:mm a" , Locale.getDefault()).format(date);
    }
    @SuppressLint("NotifyDataSetChanged")
    private final EventListener<QuerySnapshot> eventListener =(value, error) -> {
      if (error!=null) {
          return;
      }
      if (value!=null) {
          int count = chatMessages.size();
          for (DocumentChange documentChange: value.getDocumentChanges()) {
              if (documentChange.getType() == DocumentChange.Type.ADDED) {
                  ChatMessage chatMessage = new ChatMessage();
                  chatMessage.senderId = documentChange.getDocument().getString("senderId");
                  chatMessage.receiverId = documentChange.getDocument().getString("receiverId");
                  chatMessage.message = documentChange.getDocument().getString("message");
                  chatMessage.dateTime = getReadableDateTime(documentChange.getDocument().getDate("timeStamp"));
                  chatMessage.dateObject = documentChange.getDocument().getDate("timeStamp");
                  chatMessages.add(chatMessage);
              }
          }
          Collections.sort(chatMessages , Comparator.comparing(obj -> obj.dateObject));
          if (count == 0) {
              chatAdapter.notifyDataSetChanged();
          } else {
              chatAdapter.notifyItemRangeInserted(chatMessages.size() , chatMessages.size());
              binding.roomRecycleView.smoothScrollToPosition(chatMessages.size() -1);
          }
          binding.roomRecycleView.setVisibility(View.VISIBLE);
      }
      if (conversionId ==null) {
          checkConversion();
      }
    };
    private void listenMessage() {
        firebaseFirestore.collection("Chat")
                .whereEqualTo("senderId" ,userId)
                .whereEqualTo("receiverId" ,receiverUser.id)
                .addSnapshotListener(eventListener);
        firebaseFirestore.collection("Chat")
                .whereEqualTo("senderId" ,receiverUser.id)
                .whereEqualTo("receiverId" ,userId)
                .addSnapshotListener(eventListener);
    }

    private final OnCompleteListener<QuerySnapshot> conversationOnCompleteListener = task -> {
        if (task.isSuccessful() && task.getResult()!= null && task.getResult().getDocuments().size()>0) {
            DocumentSnapshot documentSnapshot =task.getResult().getDocuments().get(0);
                conversionId =documentSnapshot.getId();
        }
    };
    private void checkConversionRemotely(String senderId ,String receiverId) {
        firebaseFirestore.collection("Conversion").whereEqualTo("senderId" ,senderId).whereEqualTo("receiverId" ,receiverId).get().addOnCompleteListener(conversationOnCompleteListener);
    }
    private void checkConversion() {
        if (chatMessages.size()!=0) {
            checkConversionRemotely(userId ,receiverUser.id);
            checkConversionRemotely(receiverUser.id ,userId);
        }
    }
    private void addConversion(HashMap<String ,Object> conversion) {
      firebaseFirestore.collection("Conversion").add(conversion).addOnSuccessListener(documentReference -> conversionId =documentReference.getId());
    }
    private void updateConversion(String message) {
        DocumentReference documentReference =firebaseFirestore.collection("Conversion").document(conversionId);
        documentReference.update("lastMessage" ,message ,"timeStamp" ,new Date());
    }
}