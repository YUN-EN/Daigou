package com.example.testinterface_two;

import android.content.Intent;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FindFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_find, container, false);
        CardView clothView = view.findViewById(R.id.clothesCard);
        CardView shoesView = view.findViewById(R.id.shoesCard);
        CardView bagView = view.findViewById(R.id.bagCard);
        CardView medicineView = view.findViewById(R.id.medicineCard);
        CardView snackView = view.findViewById(R.id.snackCard);
        CardView accessoriesView = view.findViewById(R.id.accessoriesCard);

        clothView.setOnClickListener(v -> startActivity(new Intent(getActivity() , clothActivity.class)));
        shoesView.setOnClickListener(v -> startActivity(new Intent(getActivity() ,shoesActivity.class)));
        bagView.setOnClickListener(v -> startActivity(new Intent(getActivity() ,bagActivity.class)));
        medicineView.setOnClickListener(v -> startActivity(new Intent(getActivity() ,medicineActivity.class)));
        snackView.setOnClickListener(v -> startActivity(new Intent(getActivity() ,snackActivity.class)));
        accessoriesView.setOnClickListener(v -> startActivity(new Intent(getActivity() ,accessoriesActivity.class)));
        return view ;
    }
}