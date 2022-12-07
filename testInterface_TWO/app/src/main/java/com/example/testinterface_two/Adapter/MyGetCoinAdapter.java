package com.example.testinterface_two.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testinterface_two.R;
import com.example.testinterface_two.models.CoinRecord;

import java.util.List;

public class MyGetCoinAdapter extends RecyclerView.Adapter<MyGetCoinAdapter.ViewHolder>{
    private final Activity content;
    private final List<CoinRecord> coinRecordList;

    public MyGetCoinAdapter(Activity content, List<CoinRecord> coinRecordList) {
        this.content = content;
        this.coinRecordList = coinRecordList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(content).inflate(R.layout.each_get_coin, parent ,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CoinRecord coinRecord = coinRecordList.get(position);
        holder.setDateText(coinRecord.getDateCoinRecord());
        holder.setCategoryText(coinRecord.getCategoryCoinRecord());
        holder.setNumText(coinRecord.getNumCoinRecord());
        holder.setIdText(coinRecord.getDealIdCoinRecord());
        boolean visible = coinRecordList.get(position).isVisible();
        holder.expandCoinLayout.setVisibility(visible ? View.VISIBLE :View.GONE);
        if (visible) {
            holder.downImg.setImageResource(R.drawable.ic_keyboard_control_key);
        } else {
            holder.downImg.setImageResource(R.drawable.ic_keyboard_arrow_down);
        }
    }

    @Override
    public int getItemCount() {
        return coinRecordList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        TextView dateText ,categoryText ,numText ,idText;
        ConstraintLayout expandCoinLayout;
        ImageView downImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            expandCoinLayout = itemView.findViewById(R.id.expandableCoinLayout);
            downImg = itemView.findViewById(R.id.dealCoinDownImage);
            downImg.setOnClickListener(v -> {
                CoinRecord coin  = coinRecordList.get(getAdapterPosition());
                coin.setVisible(!coin.isVisible());
                notifyItemChanged(getAdapterPosition());
            });
        }

        public void setDateText(String date) {
            dateText = itemView.findViewById(R.id.getCoinDate);
            dateText.setText(date);
        }
        public void setCategoryText(String category) {
            categoryText = itemView.findViewById(R.id.dealCoinItemName);
            categoryText.setText(category);
        }
        public void setNumText(String num) {
            numText = itemView.findViewById(R.id.coinNumber);
            numText.setText(num);
        }
        public void setIdText (String id) {
            idText = itemView.findViewById(R.id.dealCoinID);
            idText.setText(id);
        }
    }
}
