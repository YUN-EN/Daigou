package com.example.testinterface_two.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testinterface_two.R;
import com.example.testinterface_two.models.CoinSpend;

import java.util.List;

public class MySpendCoinAdapter extends RecyclerView.Adapter<MySpendCoinAdapter.ViewHolder>{
    private final Activity context;
    private final List<CoinSpend> coinSpendList;

    public MySpendCoinAdapter(Activity context, List<CoinSpend> coinSpendList) {
        this.context = context;
        this.coinSpendList = coinSpendList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.each_spend_coin, parent ,false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CoinSpend coinSpend = coinSpendList.get(position);
        holder.setDateSpendText(coinSpend.getDateCoinSpend());
        holder.setNumSpendText(coinSpend.getNumCoinSpend());
        
    }

    @Override
    public int getItemCount() {
        return coinSpendList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView numSpendText ,dateSpendText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        public void setNumSpendText(String n) {
            numSpendText = itemView.findViewById(R.id.spendCoinNumber);
            numSpendText.setText(n);
        }
        public void setDateSpendText(String d) {
            dateSpendText = itemView.findViewById(R.id.spendCoinDate);
            dateSpendText.setText(d);
        }
    }
}
