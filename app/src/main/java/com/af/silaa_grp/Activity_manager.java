package com.af.silaa_grp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.af.silaa_grp.CustomControl.CustomButton;
import com.af.silaa_grp.CustomControl.CustomTextView;
import com.af.silaa_grp.Retrofit.AnswerPosts;
import com.af.silaa_grp.Retrofit.Post;
import com.af.silaa_grp.adapters.Adapter_manager;

import java.util.ArrayList;

public class Activity_manager extends ActivityEnhanced {
    public RecyclerView rvItems;
    LinearLayoutManager manager;
    Adapter_manager myAdAdaptor;
    private SwipeRefreshLayout swipe_refresh;
    private int post_total = 0;
    private static String userEmail = "";
    private static int userId;
    private int failed_page = 1;
    public static SQLiteDatabase database;
    private View parent_view;
    CustomTextView txtcatname;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static String command = "get_unpublished";

    String catname = "";
    public int citycode = 1;
    public static int catid = 0;
    public int page = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        swipe_refresh = findViewById(R.id.swipe_refresh_layout);

        final String cityname = sharedPreferences.getString("sp_city_name", "");

        int CityCode = sharedPreferences.getInt("cat_city", 1);
        if (citycode != 0) {
            citycode = CityCode;
        }

        requestAction(command,page, citycode, catid);

        txtcatname = findViewById(R.id.txttoolcatename);
        rvItems = findViewById(R.id.rvItems);
        myAdAdaptor = new Adapter_manager(Activity_manager.this, rvItems);
        manager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(manager);
        rvItems.setHasFixedSize(true);
        rvItems.setAdapter(myAdAdaptor);
        txtcatname.setText("مدیریت آگهی های "+cityname);

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myAdAdaptor.resetListData();
                requestAction(command,page, citycode, catid);
            }
        });


        myAdAdaptor.setOnLoadMoreListener(new Adapter_manager.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int current_page) {
                if (post_total > myAdAdaptor.getItemCount() && current_page != 0) {
                    int next_page = current_page + 1;
                    requestAction(command,next_page, citycode, catid);
                } else {
                    myAdAdaptor.setLoaded();
                }
            }
        });
    }

    private void requestListProduct(String command, final int page, int citycode, int catid) {
        new Post().management(command, page, citycode, catid, new AnswerPosts() {
            @Override
            public void AnswerBase(ArrayList<JobItemsList> answer) {
                try {
                    if (answer.get(0).name != null) {
                        for (int i = 0; i < answer.size(); i++) {
                            post_total = answer.get(i).totalposts;

                            Log.i("id", String.valueOf(post_total));
                        }
                        displayApiResult(answer);
                    } else {
                        swipeProgress(false);
                        showFailedView(true, getString(R.string.no_item));

                    }
                } catch (NullPointerException n) {

                }
            }

            @Override
            public void SendError(Throwable t) {
                onFailRequest(page);
            }
        });
    }

    private void onFailRequest(int page) {
        failed_page = page;
        myAdAdaptor.setLoaded();
        swipeProgress(false);
        if (OnlineCheck.isConnect(this)) {
            showFailedView(true, getString(R.string.failed_text));
        } else {
            showFailedView(true, getString(R.string.no_internet_text));
        }
    }

    private void requestAction(String command, final int page, int citycode, int catid) {
        showFailedView(false, "");
        showNoItemView(false);
        if (page == 1) {
            swipeProgress(true);
        } else {
            myAdAdaptor.setLoading();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestListProduct(command, page, citycode, catid);            }
        }, 300);
    }

    private void displayApiResult(final ArrayList<JobItemsList> items) {
        myAdAdaptor.insertData(items);
        swipeProgress(false);
        if (items.size() == 0) showNoItemView(true);
    }

    private void showFailedView(boolean show, String message) {
        View lyt_failed = findViewById(R.id.lyt_failed);
        CustomTextView txt= findViewById(R.id.failed_message);

        txt.setText(message);
        if (show) {
//            rvItems.setVisibility(View.GONE);
            lyt_failed.setVisibility(View.VISIBLE);
        } else {
//            rvItems.setVisibility(View.VISIBLE);
            lyt_failed.setVisibility(View.GONE);
        }
        CustomButton btn= findViewById(R.id.failed_retry);

       btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestAction(command, page, citycode, catid);
            }
        });
    }

    private void showNoItemView(boolean show) {
//        View lyt_no_item = (View) findViewById(R.id.lyt_no_item);
//        if (show) {
//            rvItems.setVisibility(View.GONE);
//            lyt_no_item.setVisibility(View.VISIBLE);
//        } else {
//            rvItems.setVisibility(View.VISIBLE);
//            lyt_no_item.setVisibility(View.GONE);
//        }
    }

    private void swipeProgress(final boolean show) {
        if (!show) {
            swipe_refresh.setRefreshing(show);
            return;
        }
        swipe_refresh.post(new Runnable() {
            @Override
            public void run() {
                swipe_refresh.setRefreshing(show);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        clearItemadaptorArr();
        G.startActivity(MainActivity.class,true);


//        if (activitydestination.equals("ActivityCheckAd")) {
//            G.startActivity(MainActivity.class,true);
//    }
    }
    public void clearItemadaptorArr() {
        Adapter_manager.itemsArraylist.clear();
        myAdAdaptor.notifyDataSetChanged();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("onstop", "onrestart heppend");
//        requestAction(1,userId);


    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("onstop", "onstop heppend my ad");
    }

    @Override
    protected void onPause() {
        super.onPause();
//        itemsArray.clear();
        clearItemadaptorArr();
        Log.i("onstop", "onpause heppend my ad");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        swipeProgress(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("onstop", "onstart my ad");
    }
}
