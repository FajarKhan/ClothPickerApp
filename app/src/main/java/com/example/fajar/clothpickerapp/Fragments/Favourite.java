package com.example.fajar.clothpickerapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fajar.clothpickerapp.Adapter.FavouriteAdapter;
import com.example.fajar.clothpickerapp.Model.model_cloths;
import com.example.fajar.clothpickerapp.R;
import com.example.fajar.clothpickerapp.Utils.Preferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Favourite extends Fragment {


    private View view;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FavouriteAdapter favouriteAdapter;

    private ArrayList<model_cloths> Favourite_Cloths = new ArrayList<model_cloths>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_favourite, container, false);

        Gson gson = new Gson();
        String json = Preferences.getValue_String(getApplicationContext(), Preferences.USER_FAVORITE_CLOTHS);
        Type type = new TypeToken<ArrayList<model_cloths>>() {
        }.getType();
        Favourite_Cloths = gson.fromJson(json, type);


        recyclerView = (RecyclerView) view.findViewById(R.id.favourite);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        //Setting "Favourite" list
        if (Favourite_Cloths != null) {
            view.findViewById(R.id.nofav_data).setVisibility(View.GONE);
            favouriteAdapter = new FavouriteAdapter(Favourite_Cloths, getActivity());
            recyclerView.setAdapter(favouriteAdapter);
        } else {
            view.findViewById(R.id.nofav_data).setVisibility(View.VISIBLE);
        }
        return view;
    }
}
