package com.af.silaa_grp;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.af.silaa_grp.CustomControl.CustomTextView;
import com.af.silaa_grp.CustomControl.CustomTextViewBold;
import com.af.silaa_grp.Retrofit.Api;
import com.af.silaa_grp.Retrofit.ItemsListUpload;
import com.af.silaa_grp.Retrofit.ServiceGenerator;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCheck_manager extends ActivityEnhanced {
    Intent intent;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    CustomTextView txtcat;
    CustomTextViewBold txtconfirm;
    LinearLayout linearedit, lineardelete, linearpublish;
    int itemid;
    int lastId;
    String title = "";
    String imglogo = "";
    String mobile = "";

    String address = "";
    String catname = "";
    int catid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_manager);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(G.Context);
        txtcat = findViewById(R.id.txtcat);
        txtconfirm = findViewById(R.id.txtconfirm);
        lineardelete = findViewById(R.id.lineardelete);
        linearpublish = findViewById(R.id.linearpublish);
        linearedit = findViewById(R.id.linearedit);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // if we get from insertactivity
            if (bundle.getInt("lastid") > 0 && bundle.getInt("catid") > 0) {

                Toast.makeText(this, "lats id", Toast.LENGTH_SHORT).show();

                itemid = bundle.getInt("lastid");
                catid = bundle.getInt("catid");
                catname = bundle.getString("catname");
            } else {
                //get data from myadadaptor and insertActivity

                Toast.makeText(this, "modiriat", Toast.LENGTH_SHORT).show();
                itemid = bundle.getInt("itemid");
                catname = bundle.getString("catname");
                catid = bundle.getInt("catid");
            }
        }


        txtcat.setText(catname);
        findViewById(R.id.txtpread).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(G.Context, ActivityDetail.class);
                intent.putExtra("id", itemid);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                G.Context.startActivity(intent);

            }
        });

        findViewById(R.id.linearedit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(G.Context, ActivityEdit.class);
                intent.putExtra("itemid", itemid);
                intent.putExtra("catid", catid);
                intent.putExtra("catname", catname);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                G.Context.startActivity(intent);
            }
        });

        linearpublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(itemid, 4);

            }
        });
        lineardelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ActivityCheck_manager.this);
                LayoutInflater inflater = ActivityCheck_manager.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);
                dialogBuilder.setView(dialogView);
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.setCancelable(false);
                alertDialog.setCanceledOnTouchOutside(false);
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
                        alertDialog.dismiss();
                        if (lastId > 0) {
                            deleteItem(lastId, 3);
                        }
                        if (itemid > 0) {
                            deleteItem(itemid, 3);
                        }
                    }
                });

            }
        });


    }

    public void deleteItem(int catid, int code) {
        G.show_progress_dialog(this, false, false);
        RequestBody R_catid = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(catid));
        RequestBody R_code = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(code));

        Api getResponse = ServiceGenerator.getClient().create(Api.class);
        Call call = getResponse.uploadfile(null, R_catid, null, R_code, null, null, null, null, null, null, null);
        call.enqueue(new Callback<ItemsListUpload>() {
            @Override
            public void onResponse(Call<ItemsListUpload> call, Response<ItemsListUpload> response) {
                G.dismiss_P_Dialog();
                try {
                    ItemsListUpload answer = response.body();
                    if (answer != null) {

                        if (answer.response.equals("item_deleted")) {
                            onBackPressed();
                            Toast.makeText(ActivityCheck_manager.this, "آگهی حذف گردید", Toast.LENGTH_SHORT).show();

                        } else if (answer.response.equals("published_ok")) {
                            Toast.makeText(ActivityCheck_manager.this, "آگهی منتشر گردید", Toast.LENGTH_SHORT).show();
                            onBackPressed();

                        } else {
                            Toast.makeText(ActivityCheck_manager.this, "مشکل در ارتباط با سرور", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (NullPointerException e) {
                    Log.i("log", "null");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ItemsListUpload> call, @NonNull Throwable t) {
                G.dismiss_P_Dialog();
                Toast.makeText(G.Context, "مشکل در ارتباط با سرور ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            Intent k = new Intent(getApplicationContext(), Activity_manager.class);
            startActivity(k);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
