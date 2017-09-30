package com.example.fajar.clothpickerapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.fajar.clothpickerapp.Model.model_cloths;
import com.example.fajar.clothpickerapp.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Fajar Khan on 28-Sep-17.
 */

public class AdapterSuggestion extends BaseAdapter {


    public ArrayList<model_cloths> SuggestionsList;
    public Context context;

    public AdapterSuggestion(ArrayList<model_cloths> Suggestions, Context context) {
        this.SuggestionsList = Suggestions;
        this.context = context;
    }

    @Override
    public int getCount() {
        return SuggestionsList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        ViewHolder viewHolder;


        if (rowView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.suggestion_item, parent, false);

            // configure view holder
            viewHolder = new ViewHolder();
            viewHolder.Shirts = (ImageView) rowView.findViewById(R.id._shirts);
            viewHolder.background = (FrameLayout) rowView.findViewById(R.id.background);
            viewHolder.Pants = (ImageView) rowView.findViewById(R.id._pants);
            rowView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        //setting 'What to wear' screen cloth combination
        Glide.with(context)
                .load(new File(SuggestionsList.get(position).getShirts())) // Uri of the picture
                .into(viewHolder.Shirts);

        Glide.with(context)
                .load(new File(SuggestionsList.get(position).getPants())) // Uri of the picture
                .into(viewHolder.Pants);

        return rowView;
    }

    public static class ViewHolder {
        public static FrameLayout background;
        public ImageView Pants;
        public ImageView Shirts;

    }
}