package com.cilla_project.gozar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cilla_project.gozar.CustomControl.CustomTextView;
import com.cilla_project.gozar.Retrofit.AnswerPosts;
import com.cilla_project.gozar.Retrofit.Post;

import java.util.ArrayList;

public class MainActivity extends ActivityEnhanced {
    RecyclerView rvItems;
    LinearLayoutManager manager;
    MainActivity_Adapter itemsAdapter;
    private SwipeRefreshLayout swipe_refresh;
    private int post_total = 0;
    String catname = "";
    int citycode = 1;
    public int catid = 0;
    public int page = 1;
    public static SQLiteDatabase database;
    private View parent_view;
    private String command = "getAll";
    CustomTextView txtcatname;
    BottomNavigationView navigation;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        parent_view = findViewById(android.R.id.content);
        swipe_refresh = findViewById(R.id.swipe_refresh_layout);
        navigation = (BottomNavigationView) findViewById(R.id.bottomnavigation);

        findViewById(R.id.imgsearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(G.Context, ActivitySearch.class);
                startActivity(intent);
                clearItemadaptorArr();
                finish();
            }
        });
        txtcatname = findViewById(R.id.txttoolcatename);
        rvItems = findViewById(R.id.rvItems);
        itemsAdapter = new MainActivity_Adapter(G.Context, rvItems);
        manager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(manager);
        rvItems.setHasFixedSize(true);
        rvItems.setAdapter(itemsAdapter);

        final String cityname = sharedPreferences.getString("sp_city_name", "");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
                catid = bundle.getInt("catid");
                catname = bundle.getString("catname");
                txtcatname.setText(catname);

        }else {

            if (!cityname.equals("")) {
                txtcatname.setText("آگهی های " +cityname);

            } else {
                txtcatname.setText("آگهی های قم");
            }

        }
            int CityCode = sharedPreferences.getInt("cat_city", 0);
            if (citycode != 0) {
                citycode = CityCode;

        }
        requestAction(command, 1, citycode, catid);

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                if (callbackCall != null && callbackCall.isExecuted()) callbackCall.cancel();
                itemsAdapter.resetListData();
                requestAction(command, 1, citycode, catid);
            }
        });

        itemsAdapter.setOnLoadMoreListener(new MainActivity_Adapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int current_page) {
                if (post_total > itemsAdapter.getItemCount() && current_page != 0) {
                    int next_page = current_page + 1;
                    requestAction(command, next_page, citycode, catid);
                } else {
                    itemsAdapter.setLoaded();
                }
            }
        });


        BottomNavigationViewHelper.disableShiftMode(navigation);
        final Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                item.setChecked(true);
                switch (item.getItemId()) {
                    case R.id.menukhane:
                        G.startActivity(MainActivity.class, true);
                        MainActivity_Adapter.itemsArraylist.clear();
                        return true;
                    case R.id.menuCategory:
                        G.startActivity(ActivityCategory.class, true);

                        return true;

                    case R.id.menuinsert:
                        G.startActivity(ActivityInsert.class, true);
                        return true;

                    case R.id.menumypage:
                        G.startActivity(Activity_my_silla.class, true);
                        return true;
                }
                return false;
            }
        });
        rvItems.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0 && navigation.getVisibility() == View.VISIBLE) {

                    navigation.setVisibility(View.GONE);
                } else if (dy < 0 && navigation.getVisibility() != View.VISIBLE) {

                    slideDown(navigation);


                }
            }
        });
    }
    public void slideDown(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(400);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    private void requestListProduct(String command, final int page, int citycode, int catid) {
        new Post().getProductList(command, page, citycode, catid, new AnswerPosts() {
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

    private void requestAction(final String command, final int page, final int citycode, final int catid) {

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
                requestListProduct(command, page, citycode, catid);
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
                requestAction(command, page, citycode, catid);
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
        MainActivity_Adapter.itemsArraylist.clear();
        itemsAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("onstop", "onrestart heppend main");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("onstop", "onstop heppend main");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("onstop", "onpause heppend main");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("onstop", "ondestroy happend");
        clearItemadaptorArr();
        swipeProgress(false);

    }
}
