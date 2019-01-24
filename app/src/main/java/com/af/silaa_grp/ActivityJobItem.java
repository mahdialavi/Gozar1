package com.af.silaa_grp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
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

public class ActivityJobItem extends ActivityEnhanced {
    RecyclerView rvItems;
    LinearLayoutManager manager;
    Home_Adapter itemsAdapter;
    private SwipeRefreshLayout swipe_refresh;
    private int post_total = 0;
    String catname = "";
    int citycode = 1;
    public int catid=1;
    public int page = 1;
    public static SQLiteDatabase database;
    private View parent_view;
    private String command = "jobcatitem";
    CustomTextView txtcatname;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_item);
        parent_view = findViewById(android.R.id.content);
        swipe_refresh = findViewById(R.id.swipe_refresh_layout);
        swipe_refresh.setColorSchemeColors(Color.BLUE, Color.YELLOW, Color.BLUE);

        findViewById(R.id.imgsearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(G.Context,ActivitySearch.class);
                startActivity(intent);
//                G.startActivity(ActivitySearch.class,true);
                clearItemadaptorArr();
                finish();
            }
        });
        txtcatname = findViewById(R.id.txttoolcatename);
        rvItems = findViewById(R.id.rvItems);
        itemsAdapter = new Home_Adapter(G.Context,rvItems);
        manager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(manager);
        rvItems.setHasFixedSize(true);
        rvItems.setAdapter(itemsAdapter);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            catid= bundle.getInt("catid");
        }

        requestAction("jobcatitem",1,catid);

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                if (callbackCall != null && callbackCall.isExecuted()) callbackCall.cancel();
                itemsAdapter.resetListData();
                requestAction("jobcatitem",1,catid);
            }
        });

        itemsAdapter.setOnLoadMoreListener(new Home_Adapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int current_page) {
                if (post_total > itemsAdapter.getItemCount() && current_page != 0) {
                    int next_page = current_page + 1;
                    requestAction("jobcatitem",next_page,catid);
                } else {
                    itemsAdapter.setLoaded();
                }
            }
        });
    }

    private void requestListProduct(String command, final int page, int catid) {
        new Post().getProductList(command,page,citycode,catid, new AnswerPosts() {
            @Override
            public void AnswerBase(ArrayList<JobItemsList> answer) {
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
            }
            @Override
            public void SendError(Throwable t) {
                onFailRequest(page);
            }
        });
    }

    private void onFailRequest(int page) {
        itemsAdapter.setLoaded();
        swipeProgress(false);
        if (OnlineCheck.isConnect(this)) {
            showFailedView(true, getString(R.string.failed_text));
        } else {
            Toast.makeText(G.Context, "no internet", Toast.LENGTH_SHORT).show();
            showFailedView(true, getString(R.string.no_internet_text));
        }
    }
    private void requestAction(final String command, final int page, final int catid) {

        showFailedView(false, "");
        showNoItemView(false);
        if (page == 1) {
            swipeProgress(true);
        } else {
            itemsAdapter.setLoading();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestListProduct(command,page,catid);
            }
        }, 300);
    }

    private void displayApiResult(final ArrayList<JobItemsList> items) {
        itemsAdapter.insertData(items);
        swipeProgress(false);
        if (items.size() == 0) showNoItemView(true);

    }
    private void showFailedView(boolean show, String message) {

        View lyt_failed = findViewById(R.id.lyt_failed);
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
                requestAction(command,page,catid);
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

    }

    public void clearItemadaptorArr() {
        Home_Adapter.itemsArraylist.clear();
        itemsAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("onstop", "onrestart heppend");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i("onstop", "onstop heppend");
    }
    @Override
    protected void onPause() {
        super.onPause();
//        itemsArray.clear();
        Log.i("onstop", "onpause heppend");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        swipeProgress(false);
    }
}
