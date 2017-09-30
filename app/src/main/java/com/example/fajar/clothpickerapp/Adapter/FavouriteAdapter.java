package com.example.fajar.clothpickerapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.fajar.clothpickerapp.Model.model_cloths;
import com.example.fajar.clothpickerapp.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Fajar Khan on 28-Sep-17.
 */

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {

    private ArrayList<model_cloths> Cloths;
    private Context mContext;

    public static class FavouriteViewHolder extends RecyclerView.ViewHolder {

        ImageView Shirts;
        ImageView Pants;

        public FavouriteViewHolder(View itemView) {
            super(itemView);
            this.Shirts = (ImageView) itemView.findViewById(R.id._shirts);
            this.Pants = (ImageView) itemView.findViewById(R.id._pants);
        }
    }

    public FavouriteAdapter(ArrayList<model_cloths> cloth, Context context) {
        this.Cloths = cloth;
        this.mContext = context;
    }

    @Override
    public FavouriteViewHolder onCreateViewHolder(ViewGroup parent,
                                                  int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favourite_item, parent, false);

        FavouriteViewHolder myViewHolder = new FavouriteViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(FavouriteViewHolder holder, int position) {


        //setting 'Favourite' screen cloth combination
        Glide.with(mContext)
                .load(new File(Cloths.get(position).getShirts())) // Uri of the picture
                .into(holder.Shirts);

        Glide.with(mContext)
                .load(new File(Cloths.get(position).getPants())) // Uri of the picture
                .into(holder.Pants);
    }

    @Override
    public int getItemCount() {
        return Cloths.size();
    }
}
