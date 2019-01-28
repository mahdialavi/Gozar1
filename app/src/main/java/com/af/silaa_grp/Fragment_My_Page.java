package com.af.silaa_grp;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.af.silaa_grp.CustomControl.CustomTextView;
import com.af.silaa_grp.adapters.Adapter_manager;


public class Fragment_My_Page extends Fragment {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    CustomTextView txtmatn, txtcity,txttoolcatname;
    Button btnexit;
    static int userId = 0;
    LinearLayout linearcity;
    public static int selectedCatid = 0;

    public static Fragment_My_Page newInstance() {
        Fragment_My_Page fragment = new Fragment_My_Page();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(G.Context);
        editor = sharedPreferences.edit();
        btnexit = view.findViewById(R.id.btnexit);
        txttoolcatname = view.findViewById(R.id.txttoolcatname);
        txttoolcatname.setText("صفحه من");
        txtmatn = view.findViewById(R.id.txtmatn);
        txtcity = view.findViewById(R.id.txtcity);
        linearcity = view.findViewById(R.id.linearcity);
        view.findViewById(R.id.linearback).setVisibility(View.GONE);
        String catname = sharedPreferences.getString("sp_city_name", "");

        int catid = sharedPreferences.getInt("cat_city", 0);

        if (!catname.equals("")) {
            txtcity.setText(catname);
        }
        view.findViewById(R.id.linearsetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                G.startActivity(Activity_register.class);

            }
        });
        view.findViewById(R.id.linearersanapp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "http://cafebazaar.ir/app/?id=com.af.silaa_grp");
//                intent.putExtra(Intent.EXTRA_SUBJECT,txtitemname.getText().toString());
                startActivity(Intent.createChooser(intent, "ارسال نرم افزار"));
            }
        });
        view.findViewById(R.id.imgback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onBackPressed();
            }
        });

        view.findViewById(R.id.imgadmin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int user_group = sharedPreferences.getInt(ActivityInsert.spusergroup, 1);
                if (user_group > 1) {
                    Intent intent = new Intent(G.Context, Activity_manager.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        linearcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (OnlineCheck.isConnect(getActivity())) {
                    Dialog_City dialog_city = new Dialog_City(getActivity(), txtcity, selectedCatid);
                    dialog_city.setListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                        }
                    }).show();
                    dialog_city.setCancelable(false);
                    dialog_city.setCanceledOnTouchOutside(false);
                } else {
                    G.showSnackbar(view, getString(R.string.no_internet_message));
                }
            }
        });
        view.findViewById(R.id.txtmyad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int userId = sharedPreferences.getInt("userId", 0);

                if (userId != 0) {
                    G.startActivity(Activity_MyAd.class, true);
                    Activity_MyAd.activitydestination = "ActivityCheckAd";
                } else {
                    G.showSnackbar(view, getString(R.string.enter_account_txt));
                }
            }
        });
        view.findViewById(R.id.linearbookmark).setOnClickListener(new View.OnClickListener() {
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


        }
        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userId > 0) {
                    editor.remove("userId");
                    editor.apply();
                    txtmatn.setText("برای استفاده از برنامه سیلا وارد حساب کاربری خود شوید.");
                    btnexit.setText("ورود");
                    G.startActivity(Activity_register.class, true);

                } else {
                    G.startActivity(Activity_register.class, true);
                }
            }
        });

        return view;


    }

}
