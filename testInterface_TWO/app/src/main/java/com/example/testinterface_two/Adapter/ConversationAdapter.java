package com.example.testinterface_two.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.testinterface_two.databinding.EachConversationBinding;
import com.example.testinterface_two.models.ChatMessage;
import com.example.testinterface_two.models.ConversationsListener;
import com.example.testinterface_two.models.User;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;
import java.util.Objects;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversionViewHolder>{
    private final List<ChatMessage> chatMessageList;
    private final ConversationsListener conversationsListener;
    private final Activity content;
    private FirebaseFirestore database;
    public ConversationAdapter(List<ChatMessage> chatMessageList ,ConversationsListener conversationsListener ,Activity content) {
        this.chatMessageList = chatMessageList;
        this.conversationsListener = conversationsListener;
        this.content = content;
    }

    @NonNull
    @Override
    public ConversionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        database = FirebaseFirestore.getInstance();
        return new ConversionViewHolder(EachConversationBinding.inflate(LayoutInflater.from(parent.getContext()) , parent ,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ConversionViewHolder holder, int position) {
        holder.setData(chatMessageList.get(position));
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }


    class ConversionViewHolder extends RecyclerView.ViewHolder {
        EachConversationBinding binding;
        ConversionViewHolder(EachConversationBinding eachConversationBinding) {
            super(eachConversationBinding.getRoot());
            binding = eachConversationBinding;
        }
        void setData(ChatMessage chatMessage) {
            binding.userConversationTextView.setText(chatMessage.conversionName);
            binding.textRecentMessage.setText(chatMessage.message);
            User user = new User();
            user.id =chatMessage.conversionId;
            user.userName =chatMessage.conversionName;
            database.collection("Users").document(user.id).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    String image  = Objects.requireNonNull(task.getResult()).getString("userImage");
                    Glide.with(content).load(image).into(binding.image);
                }
            });
            binding.getRoot().setOnClickListener(v -> conversationsListener.onConversationClick(user));
        }
    }

}
