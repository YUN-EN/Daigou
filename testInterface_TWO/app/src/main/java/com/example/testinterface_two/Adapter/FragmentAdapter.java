package com.example.testinterface_two.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.testinterface_two.MyItemFragment;
import com.example.testinterface_two.MyPostFragment;

public class FragmentAdapter extends FragmentStateAdapter {


    private final String[] titles = {"買家交易紀錄" ,"賣家交易紀錄"};
    public FragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MyItemFragment();
            case 1:
                return  new MyPostFragment();
        }
        return new MyItemFragment();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
