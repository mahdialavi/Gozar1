package com.af.silaa_grp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.af.silaa_grp.CustomControl.CustomTextView;
import com.af.silaa_grp.Retrofit.AnswerPosts;
import com.af.silaa_grp.Retrofit.Post;
import com.af.silaa_grp.adapters.Adapter_dialog_cities;

import java.util.ArrayList;


public class Fragment_Categories extends Fragment {

    RecyclerView rvItems;
    LinearLayoutManager manager;
    Categories_Adapter itemsAdapter;
    private SwipeRefreshLayout swipe_refresh;
    private int post_total = 0;
    String catname = "";
    int citycode=1;
    public int catid=0;
    SharedPreferences sharedPreferences;


    ImageView imgadd;
    LinearLayout linearbookmark;

    private int page = 1;
    CustomTextView txtcatname,txtmyad;
    public static Fragment_Categories newInstance() {
        Fragment_Categories fragment = new Fragment_Categories();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_categories, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        swipe_refresh = view.findViewById(R.id.swipe_refresh_layout);
        txtmyad = view.findViewById(R.id.txtmyad);


        // if is firstlaunch dialog is showing
        if (sharedPreferences.getInt("firstlaunch", 1) == 1) {
            if (OnlineCheck.isConnect(getActivity())) {
                Dialog_City dialog_city= new Dialog_City(getActivity());
                dialog_city.setListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                    }
                }).show();
                dialog_city.setCancelable(false);
                dialog_city.setCanceledOnTouchOutside(false);
            }
        }
        view.findViewById(R.id.imgsearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ActivitySearch.class);
                startActivity(intent);
                clearItemadaptorArr();
                getActivity().finish();
            }
        });
        txtcatname = view.findViewById(R.id.txttoolcatename);
        rvItems = view.findViewById(R.id.rvCats);
        itemsAdapter = new Categories_Adapter(G.Context,rvItems);
        manager = new GridLayoutManager(getActivity(), G.getGridSpanCount(getActivity()));
        rvItems.setLayoutManager(manager);
        rvItems.setHasFixedSize(true);
        rvItems.setAdapter(itemsAdapter);
        txtcatname.setText("دسته بندی ها");

            requestAction("category",citycode,catid,view);


//        rvItems.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy > 0 ) {
//                    ((MainActivity)getActivity()).setNavigationVisibility(0);
//
//
//                } else if (dy < 0 ) {
//                    ((MainActivity)getActivity()).setNavigationVisibility(1);
//
//                }
//            }
//        });

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                if (callbackCall != null && callbackCall.isExecuted()) callbackCall.cancel();
                itemsAdapter.resetListData();
                requestAction("category",citycode,catid,view);
            }
        });
        itemsAdapter.setOnLoadMoreListener(new Categories_Adapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int current_page) {
                if (post_total > itemsAdapter.getItemCount() && current_page != 0) {
                    int next_page = current_page + 1;
                    requestAction("category",citycode,catid,view);
                } else {
                    itemsAdapter.setLoaded();
                }
            }
        });


       return view;
           }
    private void requestListProduct(String command,int page,int citycode,int catid,View view) {
        new Post().getProductList(command,page,citycode,catid, new AnswerPosts() {
            @Override
            public void AnswerBase(ArrayList<JobItemsList> answer) {
                if (answer != null) {
                    for (int i=0;i<answer.size();i++) {
                        post_total = answer.get(i).totalposts;
                        }
                    displayApiResult(answer);
                } else {
                    onFailRequest(page,view);
                }
            }
            @Override
            public void SendError(Throwable t) {
                onFailRequest(page,view);
            }
        });
    }
    private void onFailRequest(int page,View view) {
        itemsAdapter.setLoaded();
        swipeProgress(false);
        if (OnlineCheck.isConnect(getContext())) {
            showFailedView(true, getString(R.string.no_item),view);
        } else {
            showFailedView(true, getString(R.string.no_internet_text),view);
        }
    }
    private void requestAction(String command, final int citycode, final int catid,View view) {
        showFailedView(false, "",view);
        showNoItemView(false,view);
//        if (page_no == 1) {
        swipeProgress(true);
//        } else {
//        itemsAdapter.setLoading();
//        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestListProduct(command,page,citycode,catid,view);
            }
        }, 300);
    }

    private void displayApiResult(final ArrayList<JobItemsList> items) {
        itemsAdapter.insertData(items);
        swipeProgress(false);
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
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearItemadaptorArr();

        Adapter_dialog_cities.itemsArraylist.clear();

    }


    private void showFailedView(boolean show, String message, final View parent_view) {
        View lyt_failed = parent_view.findViewById(R.id.lyt_failed);
        CustomTextView textView = parent_view.findViewById(R.id.failed_message);
        textView.setText(message);

        if (show) {
            rvItems.setVisibility(View.GONE);
            lyt_failed.setVisibility(View.VISIBLE);
        } else {
            rvItems.setVisibility(View.VISIBLE);
            lyt_failed.setVisibility(View.GONE);
        }
        parent_view.findViewById(R.id.failed_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestAction("command", citycode, catid, parent_view);
            }
        });
    }

    private void showNoItemView(boolean show,View view) {
        View lyt_no_item = (View) view.findViewById(R.id.lyt_no_item);
        if (show) {
            rvItems.setVisibility(View.GONE);
            lyt_no_item.setVisibility(View.VISIBLE);
        } else {
            rvItems.setVisibility(View.VISIBLE);
            lyt_no_item.setVisibility(View.GONE);
        }
    }
    public void clearItemadaptorArr() {
        Categories_Adapter.itemsArraylist.clear();
        itemsAdapter.notifyDataSetChanged();
    }


    }






