package com.af.silaa_grp;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.af.silaa_grp.CustomControl.CustomTextView;
import com.af.silaa_grp.Retrofit.Post;
import com.af.silaa_grp.Retrofit.RegisteruserMobile;
import com.af.silaa_grp.Retrofit.registerMobileModel;

public class ActivityConfirmCode extends ActivityEnhanced {
    SharedPreferences setting;

    EditText edt_code;
    Button btn_submit;
    String code;
    String mobile;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_code);

        setting = PreferenceManager.getDefaultSharedPreferences(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        G.showSnackbar(findViewById(R.id.linearconfirm_view), getString(R.string.code_sending_to_you_txt));

        CustomTextView txttitle = findViewById(R.id.txttitle);
        ImageView imgback = findViewById(R.id.imgback);
        imgback.setImageResource(R.drawable.tick);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                confirmsms();
            }
        });
        txttitle.setText("تایید شماره");
        edt_code = (EditText) findViewById(R.id.edt_code);
        btn_submit = (AppCompatButton) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                confirmsms();
            }
        });


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mobile = getIntent().getStringExtra("mobile");

        }
        {
            ProgressDialog pd = new ProgressDialog(ActivityConfirmCode.this);


        }

        findViewById(R.id.txtwrongnumber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                G.startActivity(Activity_register.class, true);
            }
        });
    }

    private void confirmsms() {

        code = edt_code.getText().toString().trim();
        if (code.length() == 0) {
            G.showSnackbar(findViewById(R.id.linearconfirm_view), getString(R.string.insert_confirm_code_txt));

        } else if (code.length() < 5) {
            G.showSnackbar(findViewById(R.id.linearconfirm_view), getString(R.string.inset_code_carefuly_txt));

        } else if (code.length() == 5) {//code is valid
            G.show_progress_dialog(ActivityConfirmCode.this, false, false);
            new Post().registerSms("verify_code", mobile, "", code, new RegisteruserMobile() {
                @Override
                public void AnswerBase(registerMobileModel answer) {
                    G.dismiss_P_Dialog();
                    if (answer.response.equals("result_ok")) {


                        int user_id = answer.userId;
                        String mobile = answer.mobile;
                        String name = answer.name;
                        int user_group = answer.user_group;

                        editor.putInt("userId", user_id);
                        editor.putString(ActivityInsert.spmobile, mobile);
                        editor.putInt(ActivityInsert.spusergroup, user_group);
                        editor.putString(ActivityInsert.spName, name);
                        editor.apply();
                        Toast.makeText(ActivityConfirmCode.this, user_group+"", Toast.LENGTH_LONG).show();

                        G.startActivity(MainActivity.class, true);

                    } else {

                        G.showSnackbar(findViewById(R.id.linearconfirm_view), getString(R.string.code_not_valid_txt));

//            Snackbar.make(findViewById(android.R.id.content), R.string.code_not_valid_txt, Snackbar.LENGTH_LONG)
//        .setActionTextColor(Color.RED)
//        .show();
                    }
                }

                @Override
                public void SendError(Throwable t) {
                    G.dismiss_P_Dialog();

                    Toast.makeText(G.Context, "خطا در ارتباط با سرور", Toast.LENGTH_SHORT).show();

                }
            });

        } else {
            Snackbar.make(findViewById(android.R.id.content), "کد وارد شده صحیح نمی باشد!", Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.RED)
                    .show();
        }

    }

    @Override
    public void onBackPressed() {

    }
}