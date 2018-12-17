package com.cilla_project.gozar;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.cilla_project.gozar.CustomControl.CustomEditText;
import com.cilla_project.gozar.CustomControl.CustomTextView;
import com.cilla_project.gozar.Retrofit.AnswerPosts;
import com.cilla_project.gozar.Retrofit.Post;

import java.util.ArrayList;

public class ActivitySearch extends ActivityEnhanced {
    RecyclerView rvItems;
    LinearLayoutManager manager;
    SearchAdaptor itemsAdapter;
    private SwipeRefreshLayout swipe_refresh;
    private int post_total = 0;
    String catname = "";
    int citycode = 1;
    public static int catid = 217;
    private int failed_page = 0;
    public static SQLiteDatabase database;
    CustomEditText txtsearch;
    private View parent_view;
    CustomTextView txtcatname,txttoolcatename;
    public static String text = "";
    public static String command = "kabulemansearch";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor=sharedPreferences.edit();

        parent_view = findViewById(android.R.id.content);
//        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        swipe_refresh = findViewById(R.id.swipe_refresh);
        txtsearch = findViewById(R.id.txtsearch);
        txttoolcatename = findViewById(R.id.txttoolcatename);
        txttoolcatename.setText("جست وجو");

        int CityCode = sharedPreferences.getInt("cat_city", 0);

        if (CityCode != 0) {
            citycode=CityCode;
        }
        findViewById(R.id.imgsearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = txtsearch.getText().toString();
                if (text.length() >= 1) {
                    swipeProgress(true);
                    rvItems.removeAllViews();
                    clearItemadaptorArr();
                    requestListProduct(command,text, 1,citycode, catid);
                }
            }
        });
        findViewById(R.id.linearback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        txtcatname = findViewById(R.id.txttoolcatename);
        rvItems = findViewById(R.id.searchRecycle);
        itemsAdapter = new SearchAdaptor(G.Context, rvItems);
        manager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(manager);
        rvItems.setHasFixedSize(true);
        rvItems.setAdapter(itemsAdapter);


        requestAction(command,text,citycode ,1,catid);

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                if (callbackCall != null && callbackCall.isExecuted()) callbackCall.cancel();
                itemsAdapter.resetListData();
                requestAction(command,text, citycode,1, catid);
            }
        });
        itemsAdapter.setOnLoadMoreListener(new SearchAdaptor.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int current_page) {
                if (post_total > itemsAdapter.getItemCount() && current_page != 0) {
                    int next_page = current_page + 1;
                    requestAction(command, text, next_page,citycode, catid);
                } else {
                    itemsAdapter.setLoaded();
                }
            }
        });
    }

    private void requestListProduct(String command, final String text, final int page,int citycode, final int catid) {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        new Post().getPostsSearch(command, text,citycode ,page, new AnswerPosts() {
            @Override
            public void AnswerBase(ArrayList<JobItemsList> answer) {
                if (answer.get(0).name!=null) {

                    swipeProgress(false);
                    showFailedView(false, "");
                    for (int i = 0; i < answer.size(); i++) {
                        post_total = answer.get(i).totalposts;
                        Log.i("name", String.valueOf(post_total));
                    }
                    displayApiResult(answer);

                }else {
                    swipeProgress(false);
                    showFailedView(true, getString(R.string.no_item));

                    Toast.makeText(ActivitySearch.this, "zero", Toast.LENGTH_SHORT).show();

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
        itemsAdapter.setLoaded();
        swipeProgress(false);
        if (OnlineCheck.isConnect(G.Context)) {
            showFailedView(true, getString(R.string.failed_text));
        } else {
            showFailedView(true, getString(R.string.no_internet_text));
        }
    }

    private void requestAction(String commandfinal, final String text, final int citycode, final int page_no, final int catid) {
        showFailedView(false, "");
        showNoItemView(false);
        if (page_no == 1) {
            swipeProgress(true);
        } else {
            itemsAdapter.setLoading();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestListProduct(command, text, page_no,citycode, catid);
            }
        }, 300);
    }

    private void displayApiResult(final ArrayList<JobItemsList> items) {
        itemsAdapter.insertData(items);
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
        findViewById(R.id.failed_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestAction(command,text, 1,citycode, catid);
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
        text = "";

        G.startActivity(MainActivity2.class,true);
//        if (command.equals("karyabisearch")) {
//            G.startActivity(ActivityJobFSeeking.class,true);
//        }

    }

    public void clearItemadaptorArr() {
        SearchAdaptor.itemsArraylist.clear();
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
