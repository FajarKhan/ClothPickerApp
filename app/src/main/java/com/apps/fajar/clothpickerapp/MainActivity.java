package com.apps.fajar.clothpickerapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.apps.fajar.clothpickerapp.Fragments.AddCloths;
import com.apps.fajar.clothpickerapp.Fragments.Closet;
import com.apps.fajar.clothpickerapp.Fragments.Favourite;
import com.apps.fajar.clothpickerapp.Fragments.Suggestion;
import com.apps.fajar.clothpickerapp.Initial.Login;
import com.apps.fajar.clothpickerapp.Utils.Preferences;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;


public class MainActivity extends AppCompatActivity {


    private String _user_name, _user_email, _user_image_url;
    private Toolbar toolbar;

    boolean doubleBackToExitPressedOnce = false;

    //drawer
    private Drawer result;

    private LinearLayout linearLayout;

    private String is_from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //getting User data from login Activity
        _user_name = Preferences.getValue_String(getApplicationContext(), Preferences.USER_NAME);
        _user_email = Preferences.getValue_String(getApplicationContext(), Preferences.USER_EMAIL);
        _user_image_url = Preferences.getValue_String(getApplicationContext(), Preferences.USER_PROFILE_PICTURE);
        Intent intent = getIntent();
        is_from = intent.getExtras().getString("From", "no_value");

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);


        //Navigation Drawer
        NavigationDrawer();

        //If the first time user login
        switch (is_from) {
            case "Login":
                result.setSelection(2, true);
                Snackbar snackbar = Snackbar.make(linearLayout, "Welcome " + _user_name, Snackbar.LENGTH_LONG);
                snackbar.show();
                //Starting album activity
                Intent i = new Intent(MainActivity.this, PickShirts.class);
                startActivity(i);
                break;
            case "Cloths":
                result.setSelection(1, true);
                break;
            default:
                result.setSelection(1, true);
                break;
        }
    }


    public void NavigationDrawer() {

        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Glide.with(MainActivity.this)
                        .load(_user_image_url) // Url of the picture
                        .placeholder(R.drawable.login_logo)
                        .into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Glide.with(MainActivity.this)
                        .load(_user_image_url) // Url of the picture
                        .placeholder(R.drawable.login_logo)
                        .into(imageView);
            }
        });

        //Drawer Header
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withSelectionListEnabledForSingleProfile(false)
                .withHeaderBackground(R.color.accent)
                .addProfiles(
                        new ProfileDrawerItem().withName(_user_name).withEmail(_user_email)
                )
                .build();

        //Drawer Items
        PrimaryDrawerItem Suggsions = new PrimaryDrawerItem().withIcon(R.drawable.home).withIdentifier(1).withName("What to wear");
        SecondaryDrawerItem Addcloth = new SecondaryDrawerItem().withIcon(R.drawable.cloth).withIdentifier(2).withName("Add cloths");
        SecondaryDrawerItem Bookmark = new SecondaryDrawerItem().withIcon(R.drawable.fav).withIdentifier(3).withName("Favourite collection");
        SecondaryDrawerItem Closet = new SecondaryDrawerItem().withIcon(R.drawable.closet).withIdentifier(4).withName("Closet");
        PrimaryDrawerItem Logout = new PrimaryDrawerItem().withIcon(R.drawable.logout).withIdentifier(5).withName("Logout");


        //Building Drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        Suggsions,
                        new DividerDrawerItem(),
                        Addcloth,
                        Bookmark,
                        Closet
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        //Drawer Clicked items
                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {

                                //"What to Wear" screen
                                setTitle("What to wear");
                                Suggestion suggestion = new Suggestion();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.frame, suggestion, "frag")
                                        .addToBackStack(null)
                                        .commit();

                            } else if (drawerItem.getIdentifier() == 2) {
                                //"Add cloths" screen
                                setTitle("Add cloths");
                                AddCloths addCloths = new AddCloths();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.frame, addCloths, "frag")
                                        .addToBackStack(null)
                                        .commit();

                            } else if (drawerItem.getIdentifier() == 3) {

                                //Favourite screen
                                setTitle("Favourite collection");
                                Favourite favourite = new Favourite();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.frame, favourite, "frag")
                                        .addToBackStack(null)
                                        .commit();

                            } else if (drawerItem.getIdentifier() == 4) {

                                //Closet screen
                                setTitle("Closet");
                                Closet closet = new Closet();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.frame, closet, "frag")
                                        .addToBackStack(null)
                                        .commit();

                            } else if (drawerItem.getIdentifier() == 5) {
                                //Logout
                                LoginManager.getInstance().logOut();
                                Preferences.setValue(getApplicationContext(), Preferences.USER_ID, "");
                                Preferences.setValue(getApplicationContext(), Preferences.USER_NAME, "");
                                Preferences.setValue(getApplicationContext(), Preferences.USER_EMAIL, "");
                                Preferences.setValue(getApplicationContext(), Preferences.USER_PROFILE_PICTURE, "");
                                Preferences.setValue(getApplicationContext(), Preferences.USER_FAVORITE_CLOTHS, "");
                                Intent i = new Intent(MainActivity.this, Login.class);
                                startActivity(i);
                                MainActivity.this.finish();
                            }
                            if (intent != null) {
                                MainActivity.this.startActivity(intent);
                            }
                        }

                        return false;
                    }
                })
                .build();
        result.addStickyFooterItem(Logout);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        //Tapping back button twice close app
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Snackbar snackbar = Snackbar.make(linearLayout, "Please click back again to exit", Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
