package com.cilla_project.gozar;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cilla_project.gozar.Retrofit.Post;
import com.cilla_project.gozar.Retrofit.RegisteruserMobile;
import com.cilla_project.gozar.Retrofit.registerMobileModel;

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

        Snackbar.make(findViewById(android.R.id.content), "تا لحظات دیگر کد برای شما ارسال میگردید!", Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.RED)
                .show();

        edt_code = (EditText) findViewById(R.id.edt_code);
        btn_submit = (AppCompatButton) findViewById(R.id.btn_submit);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mobile = getIntent().getStringExtra("mobile");

            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    code = edt_code.getText().toString().trim();
                    if (code.length() == 5) {//code is valid
                        new Post().registerSms("verify_code", mobile, "", code, new RegisteruserMobile() {
                            @Override
                            public void AnswerBase(registerMobileModel answer) {
                                if (answer.response.equals("result_ok")) {
                                    int user_id = answer.userId;
                                    String mobile = answer.mobile;
                                    String name = answer.name;

                                    onBackPressed();
                                    editor.putInt("userId", user_id);
                                    editor.putString(ActivityInsert.spmobile, mobile);
                                    editor.putString(ActivityInsert.spName, name);
                                    editor.apply();
                                    Toast.makeText(ActivityConfirmCode.this, "به سیلا خوش امدید", Toast.LENGTH_LONG).show();

                                } else {
                                    Snackbar.make(findViewById(android.R.id.content), "کد وارد شده صحیح نمی باشد!", Snackbar.LENGTH_LONG)
                                            .setActionTextColor(Color.RED)
                                            .show();
                                }
                            }

                            @Override
                            public void SendError(Throwable t) {
                                Toast.makeText(G.Context, "خطا در ارتباط با سرور", Toast.LENGTH_SHORT).show();

                            }
                        });

                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "کد وارد شده صحیح نمی باشد!", Snackbar.LENGTH_LONG)
                                .setActionTextColor(Color.RED)
                                .show();
                    }
                }
            });
        }
        {
            ProgressDialog pd = new ProgressDialog(ActivityConfirmCode.this);


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        G.startActivity(MainActivity.class, true);
    }
}