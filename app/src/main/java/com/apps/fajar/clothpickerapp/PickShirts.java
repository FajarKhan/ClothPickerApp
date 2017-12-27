package com.apps.fajar.clothpickerapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.yazeed44.imagepicker.model.ImageEntry;
import net.yazeed44.imagepicker.util.Picker;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import me.shaohui.advancedluban.Luban;

public class PickShirts extends AppCompatActivity {

    //Images
    ArrayList<File> CompressedShirtsImage = new ArrayList<>();
    private String[] arrFilePaths = new String[10];

    //Button and View
    private Button BtnPick_Shirts;
    private RelativeLayout relativeLayout;
    private TextView EmojiText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_shirts);

        EmojiText = (TextView) findViewById(R.id.emogi_text);
        BtnPick_Shirts = (Button) findViewById(R.id.BtnAddShirts);
        relativeLayout = (RelativeLayout) findViewById(R.id.RelativeLayout);

        // Check if we're running on Android 6.0 or higher
        if (Build.VERSION.SDK_INT >= 23) {
            // Implement Text with emoji
            EmojiText.setText("Upload all your upper wear for the best result with ‘What to wear’ \uD83D\uDE0D");
        } else {
            // Implement this without emoji
            EmojiText.setText("Upload all your upper wear for the best result with ‘What to wear’");
        }

        //opening album user can either choose from gallery or camera (max 10 Images)
        BtnPick_Shirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Picker.Builder(PickShirts.this, new MyPickListener(), R.style.MIP_theme)
                        .setPickMode(Picker.PickMode.MULTIPLE_IMAGES)
                        .setAlbumImagesCountTextColor(Color.BLACK)
                        .setLimit(10)
                        .build()
                        .startActivity();
            }
        });
    }


    private class MyPickListener implements Picker.PickListener {
        @Override
        public void onPickedSuccessfully(final ArrayList<ImageEntry> images) {
            //After selected shirts
            //getting selected images
            for (int i = 0; i < images.size(); i++) {
                CompressedShirtsImage.add(new File(images.get(i).path));
            }
            Snackbar bar = Snackbar.make(relativeLayout, "Hold on! We are adding your awesome upper wears..", Snackbar.LENGTH_INDEFINITE);
            ViewGroup contentLay = (ViewGroup) bar.getView().findViewById(android.support.design.R.id.snackbar_text).getParent();
            ProgressBar item = new ProgressBar(PickShirts.this);
            item.setPadding(4, 50, 4, 50);
            contentLay.addView(item, 160, 160);
            bar.show();

            //compress selected image
            Luban.compress(PickShirts.this, CompressedShirtsImage)
                    .putGear(Luban.CUSTOM_GEAR)
                    .asListObservable()
                    .subscribe(new Consumer<List<File>>() {
                        @Override
                        public void accept(List<File> files) throws Exception {

                            for (int i = 0; i < files.size(); i++) {
                                arrFilePaths[i] = files.get(i).toString();
                            }
                            if (arrFilePaths[0] != null) {
                                for (int i = 0; i < arrFilePaths.length - 1; i++) {
                                    if (arrFilePaths[i] == null) break;
                                    //creating a new folder in SD cards called "Shirts" and store all selected images
                                    Bitmap BitmapShirt = BitmapFactory.decodeFile(arrFilePaths[i]);
                                    createDirectoryAndSavePickedShirts(BitmapShirt, "image" + i);
                                }
                                //if done, let user choose pants in next screen
                                Intent intent = new Intent(PickShirts.this, PickPants.class);
                                startActivity(intent);
                            } else {
                                Snackbar snackbar = Snackbar.make(relativeLayout, "Choose some cloths!", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            throwable.printStackTrace();
                        }
                    });
        }

        @Override
        public void onCancel() {
            //User canceled the pick activity
            Snackbar snackbar = Snackbar.make(relativeLayout, "There was an Error", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    //creating folder and storing images
    private void createDirectoryAndSavePickedShirts(Bitmap imageToSave, String fileName) {
        File direct = new File(Environment.getExternalStorageDirectory() + "/Shirts");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/Shirts/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File("/sdcard/Shirts/"), fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
