package com.af.silaa_grp;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.af.silaa_grp.CustomControl.CustomTextView;
import com.af.silaa_grp.CustomControl.CustomTextViewBold;
import com.af.silaa_grp.Retrofit.AnswerPosts;
import com.af.silaa_grp.Retrofit.Api;
import com.af.silaa_grp.Retrofit.ItemsListUpload;
import com.af.silaa_grp.Retrofit.Post;
import com.af.silaa_grp.Retrofit.ServiceGenerator;
import com.af.silaa_grp.Retrofit.UploadPosts;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCheckAd extends  ActivityEnhanced {

    Intent intent;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    CustomTextView txtcat;
    CustomTextViewBold txtconfirm;
    LinearLayout linearedit, lineardelete;
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
        setContentView(R.layout.activity_check_ad);

                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(G.Context);
                txtcat = findViewById(R.id.txtcat);
                txtconfirm = findViewById(R.id.txtconfirm);
                lineardelete = findViewById(R.id.lineardelete);
                linearedit = findViewById(R.id.linearedit);


//                findViewById(R.id.linearback).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        onBackPressed();
//                    }
//                });
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    // if we get from insertactivity
                    if (bundle.getInt("lastid") > 0 && bundle.getInt("catid") > 0) {

                        itemid = bundle.getInt("lastid");
                        catid = bundle.getInt("catid");
                        catname = bundle.getString("catname");
                        Toast.makeText(this, catid+"", Toast.LENGTH_SHORT).show();
//                setSharedPdata();
//                Toast.makeText(G.Context,catname, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //get data from myadadaptor and insertActivity
                        itemid = bundle.getInt("itemid");
                        catname = bundle.getString("catname");
                        catid = bundle.getInt("catid");
                        Toast.makeText(this, catid+"       "+catname, Toast.LENGTH_SHORT).show();
                    }
                }


                txtcat.setText(catname);
                findViewById(R.id.txtpread).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //we send image to adaptorImage array in activityshow
//                ActivityShow.sliderArr.add(G.IMAGE_URL + imglogo);

                        intent = new Intent(G.Context, ActivityDetail.class);
                        intent.putExtra("id", itemid);

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        G.Context.startActivity(intent);

                    }
                });


                if (lastId > 0) {
//                    requestitem(lastId);

                }


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
                lineardelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ActivityCheckAd.this);
                        LayoutInflater inflater = ActivityCheckAd.this.getLayoutInflater();
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

    public void deleteItem(int catid,int code) {

        G.show_progress_dialog(this,false,false);
      RequestBody R_catid = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(catid));
        RequestBody  R_code = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(3));

    Api getResponse = ServiceGenerator.getClient().create(Api.class);
    Call call = getResponse.uploadfile(null, R_catid, null, R_code,null,null, null, null,null, null, null);
        call.enqueue(new Callback<ItemsListUpload>() {
        @Override
        public void onResponse(Call<ItemsListUpload> call, Response<ItemsListUpload> response) {
            G.dismiss_P_Dialog();
            try {
                ItemsListUpload answer = response.body();
                if (answer != null) {
                    if (answer.response.equals("updated_ok")) {
                        onBackPressed();
                    }else {
                        Toast.makeText(ActivityCheckAd.this, "مشکل در حذف آگهی", Toast.LENGTH_LONG).show();
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
        G.startActivity(Activity_MyAd.class, true);


    }
}



//            private void deleteItem(int id, int code) {
//                G.show_P_Dialog(ActivityCheckAd.this,false,false);
//                new Post().uploadfile(0, code, 0,0, id, "", "", "", "", "", new UploadPosts() {
//                            @Override
//                            public void AnswerBase(ItemsListUpload answer) {
//                                G.dismiss_P_Dialog();
//                                try{
//                                    Toast.makeText(G.Context, answer.getResponse(), Toast.LENGTH_SHORT).show();
//                                    Log.i("answer", answer.getResponse());
//                                    G.startActivity(Activity_MyAd.class, true);
//                                }catch (Exception e) {
//                                    Log.i("log", String.valueOf(e));
//                                }
//                                if (answer.response.equals("item_deleted")) {
//                                    Toast.makeText(G.Context, "آگهی به موفقیت حذف گردید", Toast.LENGTH_SHORT).show();
//                                    G.startActivity(Activity_MyAd.class, true);
//
//                                } else if (answer.response.equals("Delete_Failed")) {
//                                    Toast.makeText(G.Context, "حذف با مشکل مواجه گردید", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                            @Override
//                            public void SendError(Throwable t) {
//                                G.dismiss_P_Dialog();
//
//                                if (OnlineCheck.isConnect(ActivityCheckAd.this)) {
//                                    Toast.makeText(G.Context, "حذف با مشکل مواجه گردید", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(ActivityCheckAd.this, "لطفا اتصال دستگاه به انترنت را چک کنید", Toast.LENGTH_SHORT).show();
//                                }

//                            }
//                        });
//            }


