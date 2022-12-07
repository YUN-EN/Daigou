package com.example.testinterface_two.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.testinterface_two.MyGetFragment;
import com.example.testinterface_two.MySpendFragment;

public class FragmentRecordAdapter extends FragmentStateAdapter {
    private final String[] titles = {"獲得紀錄" ,"花費紀錄"};

    public FragmentRecordAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0:
                return new MyGetFragment();
            case 1:
                return new MySpendFragment();
        }
        return new MyGetFragment();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
