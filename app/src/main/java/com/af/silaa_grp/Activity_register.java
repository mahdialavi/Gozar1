package com.af.silaa_grp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.af.silaa_grp.CustomControl.CustomButton;
import com.af.silaa_grp.CustomControl.CustomEditText;
import com.af.silaa_grp.CustomControl.CustomTextView;
import com.af.silaa_grp.Retrofit.Post;
import com.af.silaa_grp.Retrofit.RegisteruserMobile;
import com.af.silaa_grp.Retrofit.registerMobileModel;

public class Activity_register extends ActivityEnhanced {
    public static String all = "";
    SharedPreferences setting;

    int userId = 0;

    CustomButton btn_submit;
    CustomEditText edtmobile;
    CustomEditText edtname;
    String mobile = "";
    String name = "";

    CustomTextView txt_login_suggest, txtmatn;
    ProgressDialog dialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    CustomTextView txttitle;

    Button btnexit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(G.Context);
        editor = sharedPreferences.edit();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getInt("fr_insert_ac") == 1) {

            }
        }
        txttitle = findViewById(R.id.txttoolcatname);
        txttitle.setText("ورود و ثبت نام در سیلا");

        setting = PreferenceManager.getDefaultSharedPreferences(this);
        dialog = new ProgressDialog(Activity_register.this);

        edtmobile = findViewById(R.id.edt_mobile);
        edtname = findViewById(R.id.edtname);
        btn_submit = findViewById(R.id.btn_submit);
        btnexit = findViewById(R.id.btnexit);
        txtmatn = findViewById(R.id.txtmatn);

        findViewById(R.id.imgback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        int userId = (sharedPreferences.getInt("userId", 0));
        if (userId > 0) {

            String mobile = sharedPreferences.getString(ActivityInsert.spmobile, "");
            String name = sharedPreferences.getString(ActivityInsert.spName, "");
            btnexit.setVisibility(View.VISIBLE);

            txtmatn.setText("شما با شماره " + mobile + " وارد سیلا شده اید.");
            edtname.setText(name);
            edtmobile.setText(mobile);
            edtmobile.setEnabled(false);
            btn_submit.setText("ذخیره");
        } else {
            btnexit.setVisibility(View.GONE);

        }
        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Activity_register.this);

                LayoutInflater inflater = Activity_register.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);
                dialogBuilder.setView(dialogView);
                final AlertDialog alertDialog = dialogBuilder.create();
                CustomTextView text=dialogView.findViewById(R.id.content);
                text.setText(R.string.exit_message);

                alertDialog.setCancelable(false);
                alertDialog.show();

                dialogView.findViewById(R.id.btn_negative).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                dialogView.findViewById(R.id.btn_positive).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        editor.remove("userId");
                        editor.apply();
                        G.startActivity(Activity_register.class, true);
                        btnexit.setVisibility(View.GONE);
                    }
                });



            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (btn_submit.getText().toString().equals("ذخیره")) {
                    updateUser("updatename");


                } else {
                    registerUser("register_user");
                }
            }
        });
    }

    private void updateUser(String command) {
        G.show_progress_dialog(this,false,false);
        hideKeyboard(Activity_register.this);
        name = edtname.getText().toString();
        mobile = edtmobile.getText().toString().trim();
        if (name.length() > 2) {
            if (mobile.length() > 10 && mobile.length() < 12) {
                new Post().registerSms(command, mobile, name, "", new RegisteruserMobile() {
                    @Override
                    public void AnswerBase(registerMobileModel answer) {
                        G.dismiss_P_Dialog();
                        if (answer.response.equals("user_updated_successfully")) {
                            editor.putString(ActivityInsert.spName, name);
                            editor.apply();

                            G.startActivity(ActivityCategory.class, true);
                            Toast.makeText(Activity_register.this, "تغییرات با موفقیت ذخیره گردید!", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(G.Context, "ثبت نام با مشکل مواجه گردیده است!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void SendError(Throwable t) {
                        onFailRequest();
                        G.dismiss_P_Dialog();


                    }
                });
            } else {

                G.showSnackbar(findViewById(R.id.linearregister),getString(R.string.mobile_not_valid_text));
            }
        } else {
            G.showSnackbar(findViewById(R.id.linearregister),getString(R.string.write_3_words_text));

        }

    }

    public void registerUser(final String command) {

        hideKeyboard(Activity_register.this);
        name = edtname.getText().toString();
        mobile = edtmobile.getText().toString().trim();

        if (name.length() > 2) {
            if (mobile.length() == 0) {
                G.showSnackbar(findViewById(R.id.linearregister),getString(R.string.insert_mobile_num_txt));
//
//                Snackbar.make(findViewById(android.R.id.content), R.string.insert_mobile_num_txt, Snackbar.LENGTH_LONG)
//                        .setActionTextColor(Color.RED)
//                        .show();
            } else {
                if (!(mobile.length() == 11) || !mobile.startsWith("09")) {
                    G.showSnackbar(findViewById(R.id.linearregister),getString(R.string.mobile_not_valid_txt));
//                    Snackbar.make(findViewById(android.R.id.content), R.string.mobile_not_valid_txt, Snackbar.LENGTH_LONG)
//                            .setActionTextColor(Color.RED)
//                            .show();
                } else {
                    G.show_progress_dialog(this, false, false);
                    new Post().registerSms(command, mobile, name, "", new RegisteruserMobile() {
                        @Override
                        public void AnswerBase(registerMobileModel answer) {
                            G.dismiss_P_Dialog();
                            if (answer.response.equals("result_ok")) {
                                Intent intent = new Intent(G.Context, ActivityConfirmCode.class);
                                intent.putExtra("mobile", answer.mobile);
                                startActivity(intent);
                                finish();


                            } else {
                                G.showSnackbar(findViewById(R.id.linearregister),getString(R.string.register_failed_message));
                            }
                        }

                        @Override
                        public void SendError(Throwable t) {
                            G.dismiss_P_Dialog();
                            onFailRequest();

                        }
                    });
                }
            }
        } else {    G.showSnackbar(findViewById(R.id.linearregister),getString(R.string.write_3_words_text));

        }


    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void onFailRequest() {
        if (OnlineCheck.isConnect(this)) {

            G.showSnackbar(findViewById(R.id.linearregister),getString(R.string.failed_text));
//            Toast.makeText(this, getString(R.string.failed_text), Toast.LENGTH_SHORT).show();
        } else {
            G.showSnackbar(findViewById(R.id.linearregister), getString(R.string.no_internet_text));

//            Toast.makeText(this, getString(R.string.no_internet_text), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
