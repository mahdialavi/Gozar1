package com.cilla_project.gozar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.cilla_project.gozar.CustomControl.CustomButton;
import com.cilla_project.gozar.CustomControl.CustomEditText;
import com.cilla_project.gozar.CustomControl.CustomTextView;
import com.cilla_project.gozar.Retrofit.Post;
import com.cilla_project.gozar.Retrofit.RegisteruserMobile;
import com.cilla_project.gozar.Retrofit.registerMobileModel;

public class Activity_register extends ActivityEnhanced {
    public static String all = "";
    SharedPreferences setting;

    int userId = 0;

    CustomButton btn_submit;
    CustomEditText edtmobile;
    CustomEditText edtname;
    String mobile = "";
    String name = "";

    CustomTextView txt_login_suggest,txtmatn;
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
        editor=sharedPreferences.edit();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getInt("fr_insert_ac") == 1) {

            }
        }
        txttitle=findViewById(R.id.txttitle);
        txttitle.setText("ورود و ثبت نام در سیلا");

        setting = PreferenceManager.getDefaultSharedPreferences(this);
        dialog = new ProgressDialog(Activity_register.this);

        edtmobile = findViewById(R.id.edt_mobile);
        edtname= findViewById(R.id.edtname);
        btn_submit = findViewById(R.id.btn_submit);
        btnexit= findViewById(R.id.btnexit);
        txtmatn= findViewById(R.id.txtmatn);


        int userId=(sharedPreferences.getInt("userId", 0));
        if (userId > 0) {

            String mobile = sharedPreferences.getString(ActivityInsert.spmobile, "");
            String name= sharedPreferences.getString(ActivityInsert.spName, "");
            btnexit.setVisibility(View.VISIBLE);

            txtmatn.setText("کاربر گرامی شما با شماره " + mobile + "وارد سیلا شده اید.");
            edtname.setText(name);
            edtmobile.setText(mobile);
            edtmobile.setEnabled(false);
            btn_submit.setText("ذخیره");
            Toast.makeText(this, "userid not null", Toast.LENGTH_SHORT).show();
        }else {
            btnexit.setVisibility(View.GONE);
            Toast.makeText(this, "userid null", Toast.LENGTH_SHORT).show();

        }
        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.remove("userId");
                editor.apply();

                G.startActivity(ActivityCategory.class,true);
                btnexit.setVisibility(View.GONE);
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (btn_submit.getText().toString().equals("ذخیره")) {
                    updateUser("updatename");


                }else {
                    registerUser("register_user");
                    }
                }
        });
        }

    private void updateUser(String command) {
        hideKeyboard(Activity_register.this);
        name=edtname.getText().toString();
        mobile = edtmobile.getText().toString().trim();
        if (name.length() > 2) {
            if (mobile.length() > 10&& mobile.length() < 12) {
                new Post().registerSms(command, mobile, name, "", new RegisteruserMobile() {
                    @Override
                    public void AnswerBase(registerMobileModel answer) {

                        if (answer.response.equals("user_updated_successfully")) {

                            editor.putString(ActivityInsert.spName, name);
                            editor.apply();

                            G.startActivity(ActivityCategory.class,true);
                            Toast.makeText(Activity_register.this, "تغییرات با موفقیت ذخیره گردید!", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(G.Context, "ثبت نام با مشکل مواجه گردیده است!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void SendError(Throwable t) {
                        onFailRequest();

                    }
                });
            } else {
                Snackbar.make(findViewById(android.R.id.content), "شماره وارد شده معتبر نمیباشد!", Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.RED)
                        .show();
            }
        } else {
            Snackbar.make(findViewById(android.R.id.content), "برای نام حداقل سه حرف وارد کنید", Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.RED)
                    .show();
        }

    }
    public void registerUser(final String command) {

                hideKeyboard(Activity_register.this);
                name=edtname.getText().toString();
                mobile = edtmobile.getText().toString().trim();

                if (name.length() > 2) {
                    if (mobile.length() > 10&& mobile.length() < 12) {
                        new Post().registerSms(command, mobile, name, "", new RegisteruserMobile() {
                            @Override
                            public void AnswerBase(registerMobileModel answer) {

                                if (answer.response.equals("result_ok")) {
                                    editor.putString("usermobile", answer.mobile);
                                    editor.putInt("userId", answer.userId);
                                    editor.apply();

                                    Intent intent = new Intent(G.Context, ActivityConfirmCode.class);
                                    intent.putExtra("mobile", answer.mobile);
                                    startActivity(intent);
                                    finish();



                                } else {
                                    Toast.makeText(G.Context, "ثبت نام با مشکل مواجه گردیده است!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void SendError(Throwable t) {
                                onFailRequest();

                            }
                        });
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "شماره وارد شده معتبر نمیباشد!", Snackbar.LENGTH_LONG)
                                .setActionTextColor(Color.RED)
                                .show();
                    }
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "برای نام حداقل سه حرف وارد کنید", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
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
            Toast.makeText(this, getString(R.string.failed_text), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.no_internet_text), Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        G.startActivity(ActivityCategory.class, true);
    }

}
