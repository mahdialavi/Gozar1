package com.cilla_project.gozar;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cilla_project.gozar.CustomControl.CustomTextView;
import com.cilla_project.gozar.CustomControl.CustomTextViewBold;
import com.cilla_project.gozar.Retrofit.AnswerPosts;
import com.cilla_project.gozar.Retrofit.ItemsListUpload;
import com.cilla_project.gozar.Retrofit.Post;
import com.cilla_project.gozar.Retrofit.UploadPosts;

import java.util.ArrayList;

public class ActivityCheckAd extends  ActivityEnhanced {

    Intent intent;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    CustomTextView txtcat;
    CustomTextViewBold txtconfirm;
    LinearLayout linearedit, lineardelete;
    int itemid, lastId;
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
//                                    Toast.makeText(ActivityCheckAd.this, "item id clicked", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });


            }



//            private void requestitem(final int id) {
//
//                new Post().getOneItem(id, new AnswerPosts() {
//                    @Override
//                    public void AnswerBase(ArrayList<ItemsList> answer) {
//                        if (answer.size() > 0) {
//                            for (int i = 0; i < answer.size(); i++) {
////                        Toast.makeText(G.Context, answer.get(0).itemname,Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void SendError(Throwable t) {
//
//                    }
//                });
//
//            }

            private void deleteItem(int id, int code) {
//                G.show_P_Dialog(ActivityCheckAd.this,false,false);
                new Post().uploadToServer(id, code, 0, catid, "", "", "", "", "", new UploadPosts() {
                            @Override
                            public void AnswerBase(ItemsListUpload answer) {
//                                G.dismiss_P_Dialog();
                                if (answer.response.equals("Item_Deleted_Successfully")) {
                                    Toast.makeText(G.Context, "آگهی به موفقیت حذف گردید", Toast.LENGTH_SHORT).show();
                                    G.startActivity(Activity_MyAd.class, true);

                                } else if (answer.response.equals("Delete_Failed")) {
                                    Toast.makeText(G.Context, "حذف با مشکل مواجه گردید", Toast.LENGTH_SHORT).show();

                                }

                            }
                            @Override
                            public void SendError(Throwable t) {
//                                G.dismiss_P_Dialog();

                                if (OnlineCheck.isConnect(ActivityCheckAd.this)) {
                                    Toast.makeText(G.Context, "حذف با مشکل مواجه گردید", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ActivityCheckAd.this, "لطفا اتصال دستگاه به انترنت را چک کنید", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }

            @Override
            public void onBackPressed() {
                super.onBackPressed();
                G.startActivity(Activity_MyAd.class, true);


            }
        }
