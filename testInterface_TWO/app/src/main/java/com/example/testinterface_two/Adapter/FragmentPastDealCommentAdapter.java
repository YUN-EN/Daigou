package com.example.testinterface_two.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.testinterface_two.MyItemDealCommentFragment;
import com.example.testinterface_two.MyPastDealCommentFragment;

public class FragmentPastDealCommentAdapter extends FragmentStateAdapter {
    private final String[] titles = {"商品評論" ,"個人評論"};
    public FragmentPastDealCommentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0 :
                return new MyItemDealCommentFragment();
            case 1:
                return new MyPastDealCommentFragment();
        }
        return new MyItemDealCommentFragment();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
