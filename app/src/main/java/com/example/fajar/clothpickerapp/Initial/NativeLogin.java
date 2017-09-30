package com.example.fajar.clothpickerapp.Initial;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.fajar.clothpickerapp.MainActivity;
import com.example.fajar.clothpickerapp.R;
import com.example.fajar.clothpickerapp.Utils.Preferences;

public class NativeLogin extends AppCompatActivity {

    private Button Login;
    private EditText TextUser, TextPassword;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_login);

        //login
        Login();

    }

    private void Login() {
        //initializing
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        Login = (Button) findViewById(R.id.BtnLogin);
        TextUser = (EditText) findViewById(R.id.TextUser);
        TextPassword = (EditText) findViewById(R.id.TextPassword);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Validating Login info
                if (!validate()) {
                    return;
                }

                //showing loading
                Snackbar bar = Snackbar.make(linearLayout, R.string.loggin_in, Snackbar.LENGTH_INDEFINITE);
                ViewGroup contentLay = (ViewGroup) bar.getView().findViewById(android.support.design.R.id.snackbar_text).getParent();
                ProgressBar item = new ProgressBar(NativeLogin.this);
                item.setPadding(4,50,4,50);
                contentLay.addView(item,160,160);
                bar.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //storing user data
                        Preferences.setValue(getApplicationContext(), Preferences.USER_ID, TextUser.getText().toString().trim() + "_" + TextPassword.getText().toString().trim());
                        Preferences.setValue(getApplicationContext(), Preferences.USER_NAME, TextUser.getText().toString().trim());
                        Preferences.setValue(getApplicationContext(), Preferences.USER_EMAIL, "");

                        //starting Main screen
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("From", "Login");
                        startActivity(intent);
                    }
                }, 2000);


            }
        });
    }

    private boolean validate() {
        if (TextUser.getText().toString().trim().equals("")) {
            Snackbar snackbar = Snackbar.make(linearLayout, "User Name Required!", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        } else if (TextPassword.getText().toString().trim().equals("")) {
            Snackbar snackbar = Snackbar.make(linearLayout, "Password Required!", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        return true;
    }
}
