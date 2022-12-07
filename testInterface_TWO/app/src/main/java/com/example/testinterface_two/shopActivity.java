package com.example.testinterface_two;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import com.example.testinterface_two.databinding.ActivityShopBinding;
public class shopActivity extends AppCompatActivity {
    private ActivityShopBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replacementFragment(new HomeFragment());

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            switch(item.getItemId()) {
                case R.id.nav_home:
                    replacementFragment(new HomeFragment());
                    break;
                case R.id.nav_category:
                    replacementFragment(new CategoryFragment());
                    break;
                case R.id.nav_send:
                    replacementFragment(new FindFragment());
                    break;
                case R.id.nav_person:
                    replacementFragment(new PersonFragment());
                    break;
            }
            return true;
        });

    }
    private void replacementFragment(Fragment fragment) {
        FragmentManager fragmentManager =getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentBottom , fragment);
        fragmentTransaction.commit();
    }
}