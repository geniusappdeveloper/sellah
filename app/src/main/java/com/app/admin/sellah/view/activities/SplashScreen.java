package com.app.admin.sellah.view.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.ApisHelper;
import com.app.admin.sellah.controller.utils.MainActivityInterface;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.controller.utils.PermissionCheckUtil;
import com.app.admin.sellah.model.extra.Notification.NotificationModel;

import static com.app.admin.sellah.controller.utils.PermissionCheckUtil.PERMISSION_REQUEST_CODE;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;
import static com.app.admin.sellah.controller.utils.Global.makeTransperantStatusBar;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_ACCEPT_REJECT;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_CHAT;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_COMMENT_ADDED;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_DATA;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_FOLLOW;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_OFFER_MAKE;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_PAYMENT;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_PRODUCT_ADDED;

public class SplashScreen extends AppCompatActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    ImageView imageView;
    SharedPreferences sharedPreferences;
    private String user_id;
    //    final int PERMISSION_REQUEST_CODE = 123;
    Animation animation;
    MainActivityInterface mainActivityInterface;
    private String TAG= SplashScreen.class.getSimpleName();
    SharedPreferences mPrefs;
    final String welcomeScreenShownPref = "welcomeScreenShown";
    Boolean welcomeScreenShown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeTransperantStatusBar(this, true);
        setContentView(R.layout.splash_screen);
//        permissions();
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
         welcomeScreenShown = mPrefs.getBoolean(welcomeScreenShownPref, false);
        Log.e("onCreate: ",""+welcomeScreenShown );

        PermissionCheckUtil.create(this, () -> {



                launchActivityHandler();




        });
        new ApisHelper().getCategories("");

        imageView = (ImageView) findViewById(R.id.splashLogo);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeon);

        user_id = HelperPreferences.get(this).getString(UID);

        imageView.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                finish();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


       /* new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                if (getStatus != null && !getStatus.equals("")) {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                }

                finish();
            }

        }, SPLASH_TIME_OUT);*/
    }

    /*public void permissions() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                            == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                            == PackageManager.PERMISSION_GRANTED) {
                launchActivityHandler();

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.RECEIVE_SMS,
                                Manifest.permission.READ_SMS}, PERMISSION_REQUEST_CODE);

            }

        }else{
            launchActivityHandler();
        }
    }*/

    @SuppressLint("LongLogTag")
    private void handleNotificationData() {
        try {
            Intent notificationIntent = getIntent();
            NotificationModel message;

                Bundle bundle = notificationIntent.getExtras();
            if (bundle != null) {
                message = bundle.getParcelable(NT_DATA);
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
                if (message != null) {
                    Intent resultIntent;
                    switch (message.getNotiType()) {
                        case NT_ACCEPT_REJECT:
                            resultIntent = new Intent(getApplicationContext(), ChatActivity.class);
                            resultIntent.putExtra("otherUserId", message.getOtherUserId());
                            resultIntent.putExtra("otherUserImage", message.getUserimage());
                            resultIntent.putExtra("otherUserName", message.getUsername());
                            break;
                        case NT_FOLLOW:
                            resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                            resultIntent.putExtras(bundle);
                            break;
                        case NT_COMMENT_ADDED:
                            resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                            resultIntent.putExtras(bundle);
                            break;
                        case NT_PRODUCT_ADDED:
                            resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                            resultIntent.putExtras(bundle);
                            break;
                        case NT_CHAT:
                            resultIntent = new Intent(getApplicationContext(), ChatActivity.class);
                            resultIntent.putExtra("otherUserId", message.getOtherUserId());
                            resultIntent.putExtra("otherUserImage", message.getUserimage());
                            resultIntent.putExtra("otherUserName", message.getUsername());
                            break;
                        case NT_PAYMENT:
                            resultIntent = new Intent(getApplicationContext(), ChatActivity.class);
                            resultIntent.putExtra("otherUserId", message.getOtherUserId());
                            resultIntent.putExtra("otherUserImage", message.getUserimage());
                            resultIntent.putExtra("otherUserName", message.getUsername());
                            break;
                        case NT_OFFER_MAKE:
                            resultIntent = new Intent(getApplicationContext(), ChatActivity.class);
                            resultIntent.putExtra("otherUserId", message.getOtherUserId());
                            resultIntent.putExtra("otherUserImage", message.getUserimage());
                            resultIntent.putExtra("otherUserName", message.getUsername());
                            break;
                        default:
                            resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                            resultIntent.putExtras(bundle);
                            break;
                    }
                    Log.e("NotificationIntentData", ": Splash" + message.getMessage());
                    startActivity(resultIntent);
                }else{
                    Log.e("NotificationIntentData", ": Splash No data found");
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    /*if (!isLogined(SplashScreen.this)) {
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Global.ProfileStatusCheck.checkProfileStatus(SplashScreen.this, new Global.ProfileStatusCheck.ProfileStatusCallback() {
                            @Override
                            public void onIfProfileUpdated() {
                                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onIfProfileNotUpdated() {
                                Log.e(TAG, "onIfProfileNotUpdated:Profile not updated");
                                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                                intent.putExtra(PROFILESTATUS,"abc");
                                startActivity(intent);
                                finish();
                           *//* new ApisHelper().getProfileData(SplashScreen.this, new ApisHelper.GetProfileCallback() {
                                @Override
                                public void onGetProfileSuccess(ProfileModel data) {
                                    Log.e(TAG, "onGetProfileSuccess: "+data.getMessage());
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable(SAConstants.Keys.PROFILE_DATA, data);
                                    Intent in = new Intent(SplashScreen.this,MainActivity.class);
                                    in.putExtras(bundle);
                                    startActivity(in);
                                    finish();
                                }
                                @Override
                                public void onGetProfileFailure() {

                                }
                            });*//*

                            }
                        });
                    }*/
                }

            } else {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
                /*if (!isLogined(SplashScreen.this)) {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Global.ProfileStatusCheck.checkProfileStatus(SplashScreen.this, new Global.ProfileStatusCheck.ProfileStatusCallback() {
                        @Override
                        public void onIfProfileUpdated() {
                            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onIfProfileNotUpdated() {
                            Log.e(TAG, "onIfProfileNotUpdated:Profile not updated");
                            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                            intent.putExtra(PROFILESTATUS,"abc");
                            startActivity(intent);
                            finish();
                           *//* new ApisHelper().getProfileData(SplashScreen.this, new ApisHelper.GetProfileCallback() {
                                @Override
                                public void onGetProfileSuccess(ProfileModel data) {
                                    Log.e(TAG, "onGetProfileSuccess: "+data.getMessage());
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable(SAConstants.Keys.PROFILE_DATA, data);
                                    Intent in = new Intent(SplashScreen.this,MainActivity.class);
                                    in.putExtras(bundle);
                                    startActivity(in);
                                    finish();
                                }
                                @Override
                                public void onGetProfileFailure() {

                                }
                            });*//*

                        }
                    });
                }*/

                Log.e("NotificationIntentData1", ": Splash No data found");
            }

        } catch (Exception e) {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
            /*if (!isLogined(SplashScreen.this)) {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Global.ProfileStatusCheck.checkProfileStatus(SplashScreen.this, new Global.ProfileStatusCheck.ProfileStatusCallback() {
                    @Override
                    public void onIfProfileUpdated() {
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onIfProfileNotUpdated() {
                        Log.e(TAG, "onIfProfileNotUpdated:Profile not updated");
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        intent.putExtra(PROFILESTATUS,"abc");
                        startActivity(intent);
                        finish();
                      *//*  new ApisHelper().getProfileData(SplashScreen.this, new ApisHelper.GetProfileCallback() {
                            @Override
                            public void onGetProfileSuccess(ProfileModel data) {
                                Bundle bundle = new Bundle();
                                bundle.putParcelable(SAConstants.Keys.PROFILE_DATA, data);
                                Intent in = new Intent(SplashScreen.this,MainActivity.class);
                                in.putExtras(bundle);
                                startActivity(in);
                                finishAffinity();
                            }
                            @Override
                            public void onGetProfileFailure() {

                            }
                        });*//*

                    }
                });
            }
*/
            Log.e("NotificationIntentFailure", ": Splash" + e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        switch (permsRequestCode) {

            case PERMISSION_REQUEST_CODE:

              /*boolean readExternalStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean writeExternalStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean camera = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                if (readExternalStorage && camera) {

                }*/


                    launchActivityHandler();


                break;

        }

    }

    private void launchActivityHandler() {
        /*Intent in = getIntent();
        Uri data = in.getData();
        if(data!=null){
            Toast.makeText(this, "DeepLinkAvailable", Toast.LENGTH_SHORT).show();
        }*/
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
/*
                if (user_id!=0) {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                }*/
                if (!welcomeScreenShown) {


                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putBoolean(welcomeScreenShownPref, true);
                    editor.commit(); // Very important to save the preference
                    Intent intent = new Intent(SplashScreen.this, WelcomeActivity.class);
                    startActivity(intent);
                    finish();

                }
                else
                {
                    handleNotificationData();
                }


            }

        }, SPLASH_TIME_OUT);
    }
}
