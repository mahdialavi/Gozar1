package com.af.silaa_grp;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.af.silaa_grp.CustomControl.CustomTextView;
import com.af.silaa_grp.Retrofit.AnswerPosts;
import com.af.silaa_grp.Retrofit.Post;

import java.util.ArrayList;

public class Activity_MyAd extends ActivityEnhanced {
    RecyclerView rvItems;
    LinearLayoutManager manager;
    MyAdAdaptor myAdAdaptor;
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

    public static String activitydestination = "ActivityCheckAd";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__my_ad);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
//        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        swipe_refresh = findViewById(R.id.swipe_refresh_layout);

        userId = sharedPreferences.getInt("userId", 0);
        findViewById(R.id.imgback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txtcatname = findViewById(R.id.txttoolcatname);
        rvItems = findViewById(R.id.rvItems);
        myAdAdaptor = new MyAdAdaptor(Activity_MyAd.this, rvItems);
        manager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(manager);
        rvItems.setHasFixedSize(true);
        rvItems.setAdapter(myAdAdaptor);
        txtcatname.setText(R.string.txt_myad);
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                if (callbackCall != null && callbackCall.isExecuted()) callbackCall.cancel();
                myAdAdaptor.resetListData();
                requestAction(1, userId);
            }
        });


        myAdAdaptor.setOnLoadMoreListener(new MyAdAdaptor.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int current_page) {
                if (post_total > myAdAdaptor.getItemCount() && current_page != 0) {
                    int next_page = current_page + 1;
                    requestAction(next_page, userId);
                } else {
                    myAdAdaptor.setLoaded();
                }
            }
        });
    }

    private void requestListProduct(final int page, final int userId) {
        new Post().getMyAds(page, userId, new AnswerPosts() {
            @Override
            public void AnswerBase(ArrayList<JobItemsList> answer) {

                if (answer.get(0).name!=null) {
                    for (int i = 0; i < answer.size(); i++) {
                        post_total = answer.get(i).totalposts;

                        Log.i("id", String.valueOf(post_total));
                    }
                    displayApiResult(answer);
                } else {
                    swipeProgress(false);
                    showFailedView(true, getString(R.string.no_item));

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

    private void requestAction(final int page_no, final int userId) {
        showFailedView(false, "");
        showNoItemView(false);
        if (page_no == 1) {
            swipeProgress(true);
        } else {
            myAdAdaptor.setLoading();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestListProduct(page_no, userId);
            }
        }, 300);
    }

    private void displayApiResult(final ArrayList<JobItemsList> items) {
        myAdAdaptor.insertData(items);
        swipeProgress(false);
        if (items.size() == 0) showNoItemView(true);
    }

    private void showFailedView(boolean show, String message) {
        View lyt_failed = (View) findViewById(R.id.lyt_failed);
        ((TextView) findViewById(R.id.failed_message)).setText(message);
        if (show) {
            rvItems.setVisibility(View.GONE);
            lyt_failed.setVisibility(View.VISIBLE);
        } else {
            rvItems.setVisibility(View.VISIBLE);
            lyt_failed.setVisibility(View.GONE);
        }
        ((Button) findViewById(R.id.failed_retry)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestAction(failed_page, userId);
            }
        });
    }

    private void showNoItemView(boolean show) {
        View lyt_no_item = (View) findViewById(R.id.lyt_no_item);
        if (show) {
            rvItems.setVisibility(View.GONE);
            lyt_no_item.setVisibility(View.VISIBLE);
        } else {
            rvItems.setVisibility(View.VISIBLE);
            lyt_no_item.setVisibility(View.GONE);
        }
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
        MyAdAdaptor.itemsArraylist.clear();
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
        requestAction(1, userId);
        Log.i("onstop", "onstart my ad");




    }
}
