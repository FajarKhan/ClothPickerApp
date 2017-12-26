package com.example.fajar.clothpickerapp.Initial;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fajar.clothpickerapp.MainActivity;
import com.example.fajar.clothpickerapp.R;
import com.example.fajar.clothpickerapp.Utils.Preferences;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class Login extends AppCompatActivity {

    //Facebook Login
    private LoginButton mFacebookSignInButton;
    private CallbackManager mFacebookCallbackManager;

    //Google Login
    private static final int RC_SIGN_IN = 9001;
    private SignInButton mGoogleSignInButton;
    private GoogleApiClient mGoogleApiClient;

    //Email Login
    private Button EmailLogin;
    private RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        if (!Preferences.getValue_String(getApplicationContext(), Preferences.USER_ID).isEmpty()) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            intent.putExtra("From", "SkipLogin");
            startActivity(intent);
            finish();
        }

        //initializing Logo Animation
        StartAnimation();

        //Email Login
        Login();

        //facebook Login
        Facebook();

        //Google Login
        Google();

    }


    private void Login() {

        EmailLogin = (Button) findViewById(R.id.EmailLogin);
        EmailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, NativeLogin.class);
                startActivity(intent);
            }
        });
    }

    private void Google() {

        mGoogleSignInButton = (SignInButton) findViewById(R.id.google_sign_in_button);
        setGooglePlusButtonText(mGoogleSignInButton, "Login with Google");
        mGoogleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });
    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }

    private void StartAnimation() {

        relativeLayout = (RelativeLayout) findViewById(R.id.RelativeLayout);
        final RelativeLayout LoginBox = (RelativeLayout) findViewById(R.id.LoginBox);
        LoginBox.setVisibility(View.GONE);

        //setting Animation
        Animation animTranslate = AnimationUtils.loadAnimation(Login.this, R.anim.translate);
        animTranslate.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {

                //EditText are visible when animation Ends
                LoginBox.setVisibility(View.VISIBLE);
                Animation animFade = AnimationUtils.loadAnimation(Login.this, R.anim.fade);
                LoginBox.startAnimation(animFade);
            }
        });
        ImageView imgLogo = (ImageView) findViewById(R.id.imageView1);
        imgLogo.startAnimation(animTranslate);
    }


    private void signInWithGoogle() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        final Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void Facebook() {
        mFacebookCallbackManager = CallbackManager.Factory.create();
        mFacebookSignInButton = (LoginButton) findViewById(R.id.login_button);

        mFacebookSignInButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));
        mFacebookSignInButton.registerCallback(mFacebookCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        //TODO: Use the Profile class to get information about the current user.

                        AccessToken accessToken = loginResult.getAccessToken();
                        Profile profile = Profile.getCurrentProfile();

                        // Facebook Email address and Name
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        Log.v("LoginActivity Response ", response.toString());

                                        try {
                                            //getting info
                                            String Fb_Name, Fb_Email;
                                            Fb_Name = object.getString("name");
                                            Fb_Email = object.getString("email");

                                            //storing user data
                                            Preferences.setValue(getApplicationContext(), Preferences.USER_ID, loginResult.getAccessToken().getUserId());
                                            Preferences.setValue(getApplicationContext(), Preferences.USER_NAME, Fb_Name);
                                            Preferences.setValue(getApplicationContext(), Preferences.USER_EMAIL, Fb_Email);
                                            Preferences.setValue(getApplicationContext(), Preferences.USER_PROFILE_PICTURE, "https://graph.facebook.com/" + loginResult.getAccessToken().getUserId() + "/picture?type=large");


                                            //starting Main screen
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            intent.putExtra("From", "Login");
                                            startActivity(intent);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Snackbar snackbar = Snackbar.make(relativeLayout, "Successfully Canceled!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(MainActivity.class.getCanonicalName(), error.getMessage());
                        Snackbar snackbar = Snackbar.make(relativeLayout, "There was an Error Please try later!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }
        );
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {
                final GoogleApiClient client = mGoogleApiClient;
                result.getSignInAccount();

                //storing user data
                Preferences.setValue(getApplicationContext(), Preferences.USER_ID, result.getSignInAccount().getId());
                Preferences.setValue(getApplicationContext(), Preferences.USER_NAME, result.getSignInAccount().getDisplayName());
                Preferences.setValue(getApplicationContext(), Preferences.USER_EMAIL, result.getSignInAccount().getEmail());
                if (result.getSignInAccount().getPhotoUrl() != null) {
                    Preferences.setValue(getApplicationContext(), Preferences.USER_PROFILE_PICTURE, result.getSignInAccount().getPhotoUrl().toString());
                }
                //starting Main screen
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("From", "Login");
                startActivity(intent);
            } else {

                Snackbar snackbar = Snackbar.make(relativeLayout, "There was an Error Please try later!", Snackbar.LENGTH_LONG);
                snackbar.show();
                //handleSignInResult(...);
            }
        }

        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
