package com.af.silaa_grp;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.af.silaa_grp.Retrofit.CallbackInfo;
import com.af.silaa_grp.Retrofit.CheckVersion;
import com.af.silaa_grp.Retrofit.Post;
import com.af.silaa_grp.data.DialogUtils;
import com.af.utils.CallbackDialog;



public class StartActivity extends ActivityEnhanced {
    int versioncode = Tools.getVersionCode(G.Context);
    public static final int requestcodePermission = 123;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startactivity);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        if (OnlineCheck.isConnect(this)) {
            new Post().requestInfo(versioncode, new CheckVersion() {
                @Override
                public void AnswerBase(CallbackInfo answer) {
                    try {
                        if (answer.lastversion == versioncode && answer.status.equals("success")) {
                            recieve();
                        } else if (answer.lastversion < versioncode && answer.status.equals("success")) {
                            recieve();
                        } else if (answer.lastversion > versioncode && answer.status.equals("success")) {
                            dialogOutDate();
                        } else if (answer.lastversion > versioncode && answer.status.equals("unsuccess")) {
                            dialogOutDate();
                        }
                    } catch (Exception e) {
                        Log.i("log", String.valueOf(e));
                    }
                }

                @Override
                public void SendError(Throwable t) {
                    Log.i("log", String.valueOf(t));
                    Toast.makeText(G.Context, R.string.not_connected_to_internet, Toast.LENGTH_SHORT).show();
                    recieve();

                }
            });


        } else {
            recieve();
        }
    }

    private void dialogOutDate() {
        Dialog dialog;
        dialog = new DialogUtils(this).buildDialogInfo(R.string.update_text, R.string.msg_app_out_date, R.string.yes, R.string.no, R.drawable.logo, new CallbackDialog() {
            @Override
            public void onPositiveClick(Dialog dialog) {
                dialog.dismiss();
                Tools.rateAction(StartActivity.this);
                finish();
            }

            @Override
            public void onNegativeClick(Dialog dialog) {
                    dialog.dismiss();
                    recieve();

            }
        });
        dialog.show();
    }

    private void recieve() {

        if (checkPermission()) {
//            if (OnlineCheck.isConnect(this)) {
        handler(0000);
            }
//        }
    }

    private void handler(int time) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity();
            }

        }, time);

    }
    public boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale
                        (this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                        ActivityCompat.shouldShowRequestPermissionRationale
                                (this, Manifest.permission.CAMERA)) {
                    showSnackbar();

                } else {
                    ActivityCompat.requestPermissions(StartActivity.this
                            , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, requestcodePermission);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
    private void showSnackbar() {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "برای استفاده از دوربین و گالری مجوز های لازم داده شود", Snackbar.LENGTH_INDEFINITE).setAction("تایید", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(StartActivity.this,
                        new String[]{Manifest.permission
                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                        requestcodePermission);
            }
        });
        snackbar.show();
        snackbar.setActionTextColor(Color.WHITE);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            sbView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case requestcodePermission:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity();
                } else {
                    showSnackbar();
                }
                break;
        }
    }
    private void startActivity() {

        int userId = (sharedPreferences.getInt("userId", 0));
        if (userId > 0) {
        G.startActivity(MainActivity.class,true);
    }else {

        G.startActivity(Activity_register.class,true);

        }

}
}
