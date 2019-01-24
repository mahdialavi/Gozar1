package com.af.silaa_grp;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.af.silaa_grp.CustomControl.CustomTextView;

public class Activity_my_silla extends ActivityEnhanced {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    CustomTextView txtmatn, txtcity;
    Button btnexit;
    static int userId = 0;
    public static int selectedCatid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_silla);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(G.Context);
        editor = sharedPreferences.edit();
        btnexit = findViewById(R.id.btnexit);
        txtmatn = findViewById(R.id.txtmatn);
        txtcity = findViewById(R.id.txtcity);

        String catname = sharedPreferences.getString("sp_city_name", "");

        int catid = sharedPreferences.getInt("cat_city", 0);

        if (!catname.equals("")) {
            txtcity.setText(catname);
        }
        findViewById(R.id.linearsetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                G.startActivity(Activity_register.class, true);

            }
        });
        findViewById(R.id.imgback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txtcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(Activity_my_silla.this, "clicked city", Toast.LENGTH_SHORT).show();
                if (OnlineCheck.isConnect(Activity_my_silla.this)) {
                    new Dialog_City(Activity_my_silla.this, txtcity, selectedCatid).setListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {

                        }
                    }).show();

                } else {
                    G.showSnackbar(view,getString(R.string.no_internet_message));

                }
            }
        });
        findViewById(R.id.imgfragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                G.startActivity(MainActivity.class);
            }
        });
        findViewById(R.id.txtmyad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int userId = sharedPreferences.getInt("userId", 0);

                if (userId != 0) {
                    G.startActivity(Activity_MyAd.class, true);
                    Activity_MyAd.activitydestination = "ActivityCheckAd";
                } else {
                    Toast.makeText(G.Context, "لطفا وارد حساب کاربری شوید!", Toast.LENGTH_LONG).show();
                }
            }
        });
        findViewById(R.id.linearbookmark).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                G.startActivity(ActivityBookmark.class);


            }
        });

        userId = (sharedPreferences.getInt("userId", 0));
        if (userId > 0) {

            String mobile = sharedPreferences.getString(ActivityInsert.spmobile, "");
            btnexit.setText("خروج");
            btnexit.setVisibility(View.GONE);
            txtmatn.setText("کاربر گرامی شما با شماره " + mobile + " وارد سیلا شده اید.");
            Toast.makeText(this, "userid not null", Toast.LENGTH_SHORT).show();


        } else {
            Toast.makeText(this, "userid null", Toast.LENGTH_SHORT).show();

        }
        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userId > 0) {
                    editor.remove("userId");
                    editor.apply();
                    txtmatn.setText("برای استفاده از برنامه سیلا وارد حساب کاربری خود شوید.");
                    btnexit.setText("ورود");
                    G.startActivity(MainActivity.class,true);

                } else {
                    G.startActivity(Activity_register.class, true);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        G.startActivity(MainActivity.class, true);
    }
}
