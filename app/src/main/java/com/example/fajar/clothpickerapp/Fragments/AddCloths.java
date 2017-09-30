package com.example.fajar.clothpickerapp.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fajar.clothpickerapp.PickShirts;
import com.example.fajar.clothpickerapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCloths extends Fragment {

    private FloatingActionButton BtnAddCloths;
    private View view;


    public AddCloths() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_cloths, container, false);


        //Add Cloths
        BtnAddCloths = (FloatingActionButton) view.findViewById(R.id.Btn_add_cloths);
        BtnAddCloths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Starting album activity
                Intent i = new Intent(getActivity(), PickShirts.class);
                startActivity(i);
            }
        });


        return view;
    }

}
