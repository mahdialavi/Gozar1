package com.cilla_project.gozar;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cilla_project.gozar.CustomControl.CustomTextView;
import com.cilla_project.gozar.Retrofit.AnswerPosts;
import com.cilla_project.gozar.Retrofit.Post;

import java.util.ArrayList;

public class ActivityCategory extends ActivityEnhanced {
    RecyclerView rvItems;
    LinearLayoutManager manager;
    JobAdapter itemsAdapter;
    private SwipeRefreshLayout swipe_refresh;
    private int post_total = 0;
    String catname = "";
    public int catid=0;
    DrawerLayout drawerLayout;
    BottomNavigationView navigation;

    ImageView imgadd;
    LinearLayout linearbookmark;

    private int page = 1;
    CustomTextView txtcatname,txtmyad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        swipe_refresh = findViewById(R.id.swipe_refresh_layout);
        txtmyad = findViewById(R.id.txtmyad);
        navigation = (BottomNavigationView) findViewById(R.id.bottomnavigation);

        findViewById(R.id.linearbookmark).setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  G.startActivity(ActivityBookmark.class);


              }
          });

//        drawerLayout =  findViewById(R.id.activity_category);

//        findViewById(R.id.imgdrawer).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.openDrawer(Gravity.RIGHT);
//            }
//        });
        findViewById(R.id.imgsearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(G.Context,ActivitySearch.class);
                startActivity(intent);
                clearItemadaptorArr();
                finish();
            }
        });
        findViewById(R.id.txtkharid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(G.Context,Activity_Sale.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.linearsetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                G.startActivity(Activity_register.class,true);

            }
        });
        findViewById(R.id.imgadd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(G.Context,ActivityInsert.class);
                startActivity(intent);
                clearItemadaptorArr();
                finish();
            }
        });
        findViewById(R.id.txtmyad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (!userEmail.equals("")) {
                    G.startActivity(Activity_MyAd.class, true);
//                    Activity_MyAd.activitydestination = "ActivityCheckAd";
//                } else {

//                    Toast.makeText(G.Context, "لطفا وارد حساب کاربری شوید!", Toast.LENGTH_LONG).show();

            }
        });



        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        findViewById(R.id.imgdrawer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
        txtcatname = findViewById(R.id.txttoolcatename);
        rvItems = findViewById(R.id.rvCats);
        itemsAdapter = new JobAdapter(G.Context,rvItems);
        manager = new GridLayoutManager(this, G.getGridSpanCount(this));
        rvItems.setLayoutManager(manager);
        rvItems.setHasFixedSize(true);
        rvItems.setAdapter(itemsAdapter);

//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//
//            catname = bundle.getString("catname");
//            txtcatname.setText(catname);
//        }

        requestAction("category",catid);

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                if (callbackCall != null && callbackCall.isExecuted()) callbackCall.cancel();
                itemsAdapter.resetListData();
                requestAction("category",catid);
            }
        });
        itemsAdapter.setOnLoadMoreListener(new JobAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int current_page) {
                if (post_total > itemsAdapter.getItemCount() && current_page != 0) {
                    int next_page = current_page + 1;
                    requestAction("category",catid);
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
                        G.startActivity(ActivityCategory.class,true);
                        JobAdapter.itemsArraylist.clear();
                        return true;

                    case R.id.menuinsert:
                        G.startActivity(ActivityInsert.class, true);
                        return true;

                    case R.id.menumypage:
                        G.startActivity(Activity_my_silla.class,true);
                        return true;
                }
                return false;
            }
        });


    }

    private void requestListProduct(String command,int page,int catid) {
        new Post().getProductList(command,page,catid, new AnswerPosts() {
            @Override
            public void AnswerBase(ArrayList<JobItemsList> answer) {
                if (answer != null) {
                    for (int i=0;i<answer.size();i++) {
                        post_total = answer.get(i).totalposts;

                        Log.i("id", String.valueOf(post_total));
                    }
                    displayApiResult(answer);
//                    Toast.makeText(G.Context, "YESSS", Toast.LENGTH_SHORT).show();
                } else {
//                    onFailRequest(page);
                }
            }
            @Override
            public void SendError(Throwable t) {
//                onFailRequest(page);
            }
        });
    }
    private void onFailRequest(int page) {
        itemsAdapter.setLoaded();
        swipeProgress(false);
        if (OnlineCheck.isConnect(this)) {
//            showFailedView(true, getString(R.string.no_item));
        } else {
            Toast.makeText(G.Context, "no internet", Toast.LENGTH_SHORT).show();
//            showFailedView(true, getString(R.string.no_internet_text));
        }
    }
    private void requestAction(final String command, final int catid) {
//        showFailedView(false, "");
//        showNoItemView(false);
//        if (page_no == 1) {
            swipeProgress(true);
//        } else {
            itemsAdapter.setLoading();
//        }
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
//        if (items.size() == 0) showNoItemView(true);
    }
//    private void showFailedView(boolean show, String message) {
////        View lyt_failed = (View) findViewById(R.id.lyt_failed);
//        ((TextView) findViewById(R.id.failed_message)).setText(message);
//        if (show) {
//            rvItems.setVisibility(View.GONE);
////            lyt_failed.setVisibility(View.VISIBLE);
//        } else {
//            rvItems.setVisibility(View.VISIBLE);
////            lyt_failed.setVisibility(View.GONE);
//        }
//        ((Button) findViewById(R.id.failed_retry)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                requestAction(failed_page,catid);
//            }
//        });
//    }

//    private void showNoItemView(boolean show) {
//        View lyt_no_item = (View) findViewById(R.id.lyt_no_item);
//        if (show) {
//            rvItems.setVisibility(View.GONE);
//            lyt_no_item.setVisibility(View.VISIBLE);
//        } else {
//            rvItems.setVisibility(View.VISIBLE);
//            lyt_no_item.setVisibility(View.GONE);
//        }
//    }

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
        JobAdapter.itemsArraylist.clear();
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
