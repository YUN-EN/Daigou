package com.example.testinterface_two.Adapter;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testinterface_two.databinding.ItemReceiveMessageBinding;
import com.example.testinterface_two.databinding.ItemSendMessageBinding;
import com.example.testinterface_two.models.ChatMessage;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<ChatMessage> chatMessageList;
    private final String senderId;
    public static final int VIEW_TYPE_SEND = 1;
    public static final int VIEW_TYPE_RECEIVE = 2;
    public ChatAdapter(String senderId, List<ChatMessage> chatMessageList) {
        this.senderId = senderId;
        this.chatMessageList = chatMessageList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_SEND) {
            return new SentMessageViewHolder(
                    ItemSendMessageBinding.inflate(LayoutInflater.from(parent.getContext()) , parent ,false)
            );
        } else {
            return new ReceiverMessageViewHolder(ItemReceiveMessageBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == VIEW_TYPE_SEND) {
            ((SentMessageViewHolder) holder).setData(chatMessageList.get(position));
        } else {
            ((ReceiverMessageViewHolder) holder).setData(chatMessageList.get(position));
        }
    }
    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(chatMessageList.get(position).senderId.equals(senderId)) {
            return VIEW_TYPE_SEND;
        } else {
            return VIEW_TYPE_RECEIVE;
        }
    }
    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemSendMessageBinding binding ;
        SentMessageViewHolder(ItemSendMessageBinding itemSendMessageBinding) {
            super(itemSendMessageBinding.getRoot());
            binding = itemSendMessageBinding;
        }
        void setData(ChatMessage chatMessage) {
            binding.textMessage.setText(chatMessage.message);
            binding.textDate.setText(chatMessage.dateTime);
        }
    }
    static class ReceiverMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemReceiveMessageBinding binding;
        ReceiverMessageViewHolder(ItemReceiveMessageBinding itemReceiveMessageBinding) {
            super(itemReceiveMessageBinding.getRoot());
            binding = itemReceiveMessageBinding;
        }
        void  setData(ChatMessage chatMessage) {
            binding.textMessage.setText(chatMessage.message);
            binding.textDate.setText(chatMessage.dateTime);
        }
    }
}
