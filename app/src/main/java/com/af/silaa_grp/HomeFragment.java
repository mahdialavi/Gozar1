package com.af.silaa_grp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.af.silaa_grp.CustomControl.CustomTextView;
import com.af.silaa_grp.Retrofit.AnswerPosts;
import com.af.silaa_grp.Retrofit.Api;
import com.af.silaa_grp.Retrofit.ItemsListUpload;
import com.af.silaa_grp.Retrofit.Post;
import com.af.silaa_grp.Retrofit.ServiceGenerator;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    RecyclerView rvItems;
    LinearLayoutManager manager;
    LinearLayout linear_firsfragment_view;
    Home_Adapter itemsAdapter;
    private SwipeRefreshLayout swipe_refresh;
    private int post_total = 0;
    String catname = "";
    public int citycode = 1;
    public static int catid = 0;
    public int page = 1;
    public static SQLiteDatabase database;
    private View parent_view;
    private static String command = "getAll";
    CustomTextView txtcatname;
    SharedPreferences sharedPreferences;
    int index;
    int top;


    public static int code = 0;
    public static int loaded_from_server = 0;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
        final View view = inflater.inflate(R.layout.fragment_first, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());


        parent_view = view.findViewById(android.R.id.content);
        swipe_refresh = view.findViewById(R.id.swipe_refresh_layout);
        swipe_refresh.setColorSchemeColors(Color.BLUE, Color.YELLOW, Color.BLUE);


        linear_firsfragment_view = view.findViewById(R.id.linear_firsfragment_view);
        txtcatname = view.findViewById(R.id.txttoolcatename);
        rvItems = view.findViewById(R.id.rvItems);
        itemsAdapter = new Home_Adapter(getActivity(), rvItems);
        manager = new LinearLayoutManager(getActivity());
        rvItems.setLayoutManager(manager);
        rvItems.setHasFixedSize(true);
        rvItems.setAdapter(itemsAdapter);

        final String cityname = sharedPreferences.getString("sp_city_name", "");


        int CityCode = sharedPreferences.getInt("cat_city", 1);

        if (citycode != 0) {
            citycode = CityCode;
        }
        Bundle bundle = getActivity().getIntent().getExtras();

        //if we come from fragment category bundle is not null
        if (bundle != null) {
            catid = bundle.getInt("catid");
            catname = bundle.getString("catname");
            txtcatname.setText(catname);


        }
        switch (code) {
            //getall items of selected city
            case 0:
                // catid=0 means i want to gea all items of category from site
                catid = 0;
                requestdata("getAll", citycode, 0, view);
                if (!cityname.equals("")) {
                    txtcatname.setText("آگهی های " + cityname);
                    } else {
                    txtcatname.setText("آگهی های قم");
                }
                break;
            //get selected items of category
            case 1:
                requestdata(command, citycode, catid, view);
                break;
        }


        view.findViewById(R.id.imgsearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivitySearch.class);
                startActivity(intent);
                clearItemadaptorArr();
                getActivity().finish();
            }
        });
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                itemsAdapter.resetListData();
                requestAction(command, 1, citycode, catid, view);
            }
        });
//        rvItems.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy > 0) {
//                    ((MainActivity) getActivity()).setNavigationVisibility(0);
//                } else if (dy < 0) {
//                    ((MainActivity) getActivity()).setNavigationVisibility(1);
//                }
//            }
//        });
        itemsAdapter.setOnLoadMoreListener(new Home_Adapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int current_page) {
                if (post_total > itemsAdapter.getItemCount() && current_page != 0) {
                    int next_page = current_page + 1;
                    requestAction(command, next_page, citycode, catid, view);
                } else {
                    itemsAdapter.setLoaded();

                }
            }
        });

        itemsAdapter.setUnpublishLinstener(new Home_Adapter.OnpublishemoreListener() {
            @Override
            public void onDelete(int code, int itemid) {
                deleteItem(itemid,code);
            }
        });


        return view;
    }

    private void requestdata(String command, int citycode, int catid, View view) {
        requestAction(command, 1, citycode, catid, view);
    }

    private void requestListProduct(String command, final int page, int citycode, int catid, final View view) {
        new Post().getProductList(command, page, citycode, catid, new AnswerPosts() {
            @Override
            public void AnswerBase(ArrayList<JobItemsList> answer) {

                try {
                    if (answer.get(0).name != null) {
                        loaded_from_server = 1;

                        for (int i = 0; i < answer.size(); i++) {
                            post_total = answer.get(i).totalposts;
                        }
                        displayApiResult(answer, view);
                    } else {
                        swipeProgress(false);
                        if (isAdded()) {
                            showFailedView(true, getString(R.string.no_item), view);
                        }
                    }
                } catch (NullPointerException e) {

                }
            }

            @Override
            public void SendError(Throwable t) {
                onFailRequest(page, view);
            }
        });
    }

    private void onFailRequest(int page, View view) {
        itemsAdapter.setLoaded();
        swipeProgress(false);
        if (OnlineCheck.isConnect(getActivity())) {
            showFailedView(true, getString(R.string.failed_text), view);
        } else {
            showFailedView(true, getString(R.string.no_internet_text), view);
        }
    }

    public void requestAction(final String command, final int page, final int citycode, final int catid, final View view) {

        showFailedView(false, "", view);
        showNoItemView(false, view);
        if (page == 1) {
            swipeProgress(true);
        } else {
            itemsAdapter.setLoading();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestListProduct(command, page, citycode, catid, view);
            }
        }, 300);
    }

    private void displayApiResult(final ArrayList<JobItemsList> items, View view) {
        itemsAdapter.insertData(items);
        swipeProgress(false);
        if (items.size() == 0) showNoItemView(true, view);

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
                requestAction(command, page, citycode, catid, parent_view);
            }
        });
    }

    private void showNoItemView(boolean show, View view) {
        View lyt_no_item = (View) view.findViewById(R.id.lyt_no_item);
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


    // unpublish an advert
    public void deleteItem(int catid, int code) {

        Toast.makeText(getActivity(), catid+code+ "caled", Toast.LENGTH_SHORT).show();
        G.show_progress_dialog(getActivity(), false, false);
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
                        if (answer.response.equals("un_published")) {
                            G.showSnackbar(linear_firsfragment_view,"آگهی از انتشار خارج شد");
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
        });}
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//    }


    @Override
    public void onStart() {
        super.onStart();
        Log.i("onstop", "fragment _onstart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("onstop", "fragment _ onstop");


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("onstop", "onview created");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("onstop", "onatach");

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("onstop", "onactivitycreated");

    }


    public void clearItemadaptorArr() {
        Home_Adapter.itemsArraylist.clear();
        itemsAdapter.notifyDataSetChanged();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("onstop", "ondestroy happend");
        clearItemadaptorArr();
        }
    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        Log.i("onstop", "atach fragment");

    }


    @Override
    public void onPause() {
        super.onPause();
        Log.i("onstop", "fragment pause");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("onstop", "fragment resume");

    }
}






