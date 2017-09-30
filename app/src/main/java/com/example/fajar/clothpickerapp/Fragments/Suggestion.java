package com.example.fajar.clothpickerapp.Fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import com.example.fajar.clothpickerapp.Adapter.AdapterSuggestion;
import com.example.fajar.clothpickerapp.Model.model_cloths;
import com.example.fajar.clothpickerapp.R;
import com.example.fajar.clothpickerapp.Utils.Preferences;
import com.google.gson.Gson;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import tourguide.tourguide.ChainTourGuide;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Sequence;
import tourguide.tourguide.ToolTip;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class Suggestion extends Fragment {

    //View
    private View view;
    private LinearLayout linearLayout;

    //Cloths
    ArrayList<String> _saved_Shirts = new ArrayList<String>();// list of image paths
    ArrayList<String> _saved_Pants = new ArrayList<String>();// list of image paths
    private ArrayList<model_cloths> Imagearray;
    File imagePath;
    private AdapterSuggestion adapterSuggestion;
    private ArrayList<model_cloths> Favarite_images = new ArrayList<>();

    //swipe card
    private SwipeFlingAdapterView flingContainer;

    //Buttons
    private FloatingActionButton BtnShare, BtnFavourite, BtnDislike, BtnNewcomp;

    //Showcase
    private Animation mEnterAnimation, mExitAnimation;


    public Suggestion() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_suggestion, container, false);


          /* setup enter and exit animation */
        mEnterAnimation = new AlphaAnimation(0f, 1f);
        mEnterAnimation.setDuration(600);
        mEnterAnimation.setFillAfter(true);

        mExitAnimation = new AlphaAnimation(1f, 0f);
        mExitAnimation.setDuration(600);
        mExitAnimation.setFillAfter(true);

        //initialize
        Init();

        //Showcase
        if (Preferences.getValue_String(getApplicationContext(), Preferences.FIRST_TIME).isEmpty()) {
            Tour();
        }

        //get cloth combination
        GetCloths();


        //Button Actions
        BtnDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flingContainer.getTopCardListener().selectLeft();
            }
        });

        BtnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flingContainer.getTopCardListener().selectRight();
            }
        });

        BtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = takeScreenshot();
                saveBitmap(bitmap);
                shareIt();
            }
        });

        BtnNewcomp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(linearLayout, "Stay tuned! Coming in feature.", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

        return view;
    }


    private void Init() {
        BtnNewcomp = (FloatingActionButton) view.findViewById(R.id.NewComp);
        BtnDislike = (FloatingActionButton) view.findViewById(R.id.BtnDislike);
        BtnShare = (FloatingActionButton) view.findViewById(R.id.BtnShare);
        BtnFavourite = (FloatingActionButton) view.findViewById(R.id.BtnFav);
        linearLayout = (LinearLayout) view.findViewById(R.id.LinearLayout);
        flingContainer = (SwipeFlingAdapterView) view.findViewById(R.id.frame);

    }

    private void Tour() {
        Preferences.setValue(getApplicationContext(), Preferences.FIRST_TIME, "false");

        ChainTourGuide tourGuide1 = ChainTourGuide.init(getActivity())
                .setToolTip(new ToolTip()
                        .setTitle("Add in your favourite!")
                        .setDescription("Swipe Right or click this button to add this compo to your favourite list!")
                        .setGravity(Gravity.TOP)
                )
                .setOverlay(new Overlay()
                        .setBackgroundColor(Color.parseColor("#EE2c3e50"))
                        .setEnterAnimation(mEnterAnimation)
                        .setExitAnimation(mExitAnimation)
                )
                .playLater(BtnFavourite);

        ChainTourGuide tourGuide2 = ChainTourGuide.init(getActivity())
                .setToolTip(new ToolTip()
                        .setTitle("Dislike")
                        .setDescription("Don't like this compo? don't worry just swipe left or click here!")
                        .setGravity(Gravity.TOP | Gravity.LEFT)
                        .setBackgroundColor(Color.parseColor("#c0392b"))
                )
                .setOverlay(new Overlay()
                        .setBackgroundColor(Color.parseColor("#EE2c3e50"))
                        .setEnterAnimation(mEnterAnimation)
                        .setExitAnimation(mExitAnimation)
                )
                .playLater(BtnDislike);


        ChainTourGuide tourGuide3 = ChainTourGuide.init(getActivity())
                .setToolTip(new ToolTip()
                        .setTitle("Share")
                        .setDescription("Feel like sharing this compo with friends? just tap here!")
                        .setGravity(Gravity.TOP)
                        .setBackgroundColor(Color.parseColor("#ff99cc00"))
                )
                .setOverlay(new Overlay()
                        .setBackgroundColor(Color.parseColor("#EE2c3e50"))
                        .setEnterAnimation(mEnterAnimation)
                        .setExitAnimation(mExitAnimation)
                )
                .playLater(BtnShare);

        Sequence sequence = new Sequence.SequenceBuilder()
                .add(tourGuide1, tourGuide2, tourGuide3)
                .setDefaultOverlay(new Overlay()
                        .setEnterAnimation(mEnterAnimation)
                        .setExitAnimation(mExitAnimation)
                )
                .setDefaultPointer(null)
                .setContinueMethod(Sequence.ContinueMethod.Overlay)
                .build();


        ChainTourGuide.init(getActivity()).playInSequence(sequence);
    }

    private void GetCloths() {

        //saved image folder
        File _saved_shirts_folder = new File(Environment.getExternalStorageDirectory() + "/Shirts");
        File _saved_pants_folder = new File(Environment.getExternalStorageDirectory() + "/Pants");


        //getting all images that is saved after user successfully selected
        getImageFromSdcard(_saved_shirts_folder, _saved_Shirts);
        getImageFromSdcard(_saved_pants_folder, _saved_Pants);

        //no. of combination to show what to wear today
        int total_no_list_of_wear_today = _saved_Shirts.size() + _saved_Pants.size();

        if (_saved_Shirts.size() > 0 && _saved_Pants.size() > 0) {
            Imagearray = new ArrayList<>();
            for (int i = 0; i < total_no_list_of_wear_today; i++) {
                //getting two random cloths from saved images
                String randomShirt = _saved_Shirts.get(new Random().nextInt(_saved_Shirts.size()));
                String randomPant = _saved_Pants.get(new Random().nextInt(_saved_Pants.size()));

                //storing for suggestion cards
                Imagearray.add(new model_cloths(randomShirt, randomPant));
            }

            adapterSuggestion = new AdapterSuggestion(Imagearray, getActivity());
            flingContainer.setAdapter(adapterSuggestion);
            flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
                @Override
                public void removeFirstObjectInAdapter() {

                }

                @Override
                public void onLeftCardExit(Object dataObject) {
                    //dislike on swipe life
                    //removing swiped combination from list
                    Imagearray.remove(0);
                    adapterSuggestion.notifyDataSetChanged();

                }

                @Override
                public void onRightCardExit(Object dataObject) {

                    //favourite on swipe right
                    Favarite_images.add(Imagearray.get(0));
                    Gson gson = new Gson();
                    String json = gson.toJson(Favarite_images);

                    //storing favourite list in shared preference
                    Preferences.setValue(getApplicationContext(), Preferences.USER_FAVORITE_CLOTHS, json);

                    //removing swiped combination from list
                    Imagearray.remove(0);
                    adapterSuggestion.notifyDataSetChanged();
                }


                @Override
                public void onAdapterAboutToEmpty(int itemsInAdapter) {

                    if (itemsInAdapter == 0) {
                        //If the no combination left, show message
                        Snackbar snackbar = Snackbar.make(linearLayout, "That's all We've Got! Try add more cloths!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }

                @Override
                public void onScroll(float scrollProgressPercent) {

                    View view = flingContainer.getSelectedView();
                    view.findViewById(R.id.background).setAlpha(0);
                    view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                    view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
                }
            });
        } else {
            //if there no cloths are added
//            Snackbar snackbar = Snackbar.make(linearLayout, "Please add more cloths to see what to wear today", Snackbar.LENGTH_LONG);
//            snackbar.show();
        }

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {

                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);

                adapterSuggestion.notifyDataSetChanged();
            }
        });
    }

    public Bitmap takeScreenshot() {
        View rootView = getActivity().findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    public void saveBitmap(Bitmap bitmap) {
        imagePath = new File(Environment.getExternalStorageDirectory() + "/screenshot.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }

    private void shareIt() {
        Uri uri = Uri.fromFile(imagePath);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        String shareBody = "Yo, What do you think about this? Cool Right?";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "What to wear");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void getImageFromSdcard(File file, ArrayList<String> f) {
        File[] listFile;
        if (file.isDirectory()) {
            listFile = file.listFiles();
            for (int i = 0; i < listFile.length; i++) {
                f.add(listFile[i].getAbsolutePath());

            }
        }
    }

}
