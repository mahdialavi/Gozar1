package com.af.silaa_grp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.af.silaa_grp.CustomControl.CustomTextView;
import com.af.silaa_grp.Retrofit.AnswerPosts;
import com.af.silaa_grp.Retrofit.Constants;
import com.af.silaa_grp.Retrofit.Post;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Transformers.BaseTransformer;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;


public class Fragment_tablighat extends Fragment {
    RecyclerView rvItems;
    TablighAdapter itemsAdapter;
    private SwipeRefreshLayout swipe_refresh;
    private int post_total = 0;
    String catname = "بخش تبلیغات";
    public static int citycode = 0;
    public static int page = 1;
    public static SQLiteDatabase database;
    CustomTextView txtcatname;
    SliderLayout sliderShow;
    public static ArrayList<Tabligh> lists = new ArrayList<>();
    public static ArrayList<Tabligh_not_vizhe> simplead = new ArrayList<>();

    ArrayList<Tabligh> tabligharray = new ArrayList();


    RelativeLayout linearslider;
    TextSliderView textSliderView;
    NestedScrollView nestedscrollview;

    LinearLayout linearprogress;
    SharedPreferences sharedPreferences;


    public static Fragment_tablighat newInstance() {
        Fragment_tablighat fragment = new Fragment_tablighat();
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
        final View view = inflater.inflate(R.layout.fragment_tablighat, container, false);

//        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        swipe_refresh = view.findViewById(R.id.swipe_refresh_layout);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        citycode = sharedPreferences.getInt("cat_city", 0);

        linearslider = view.findViewById(R.id.linearslider);
        nestedscrollview = view.findViewById(R.id.nestedscrollview);
        linearprogress = view.findViewById(R.id.linearprogress);
        linearprogress.setVisibility(View.GONE);

        view.findViewById(R.id.imgsearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(G.Context, ActivitySearch.class);
                startActivity(intent);
//                clearItemadaptorArr();
//                finish();
            }
        });

        txtcatname = view.findViewById(R.id.txttoolcatename);
        rvItems = view.findViewById(R.id.rvItems);
        itemsAdapter = new TablighAdapter(G.Context, rvItems, nestedscrollview);
        rvItems.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvItems.setHasFixedSize(true);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setNestedScrollingEnabled(false);

        requestAction(page, citycode, view);
        txtcatname.setText(catname);
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                linearprogress.setVisibility(View.GONE);
                clearslider();

                itemsAdapter.resetListData();
                requestAction(1, citycode, view);
            }
        });

        sliderShow = view.findViewById(R.id.slider);
        sliderShow.setDuration(8000);
        sliderShow.setPresetTransformer(SliderLayout.Transformer.CubeIn);
        sliderShow.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Visible);
        sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Left_Bottom);
        sliderShow.setCustomAnimation(new DescriptionAnimation());

        itemsAdapter.setOnLoadMoreListener(new TablighAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int current_page) {

                if (post_total > itemsAdapter.getItemCount() && current_page != 0) {
                    int next_page = current_page + 1;
                    simplead.clear();
                    requestAction(next_page, citycode, view);
                    linearprogress.setVisibility(View.VISIBLE);

                } else {
                    itemsAdapter.setLoaded();
                    linearprogress.setVisibility(View.GONE);

                }
            }
        });
        return view;


    }

    private void clearslider() {
        sliderShow.setPagerTransformer(false, new BaseTransformer() {
            @Override
            protected void onTransform(View view, float v) {
            }
        });
        clearArray();
        textSliderView = new TextSliderView(getActivity());
        sliderShow.stopAutoCycle();
        textSliderView.description("").image("");
        sliderShow.removeAllSliders();
    }

    private void requestAction(final int page_no, final int citycode, final View view) {
        showFailedView(false, "", view);
        showNoItemView(false, view);
        if (page_no == 1) {
            swipeProgress(true);
        } else {
            itemsAdapter.setLoading();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestListProduct(page_no, citycode, view);
            }
        }, 300);
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

    private void displayApiResult(final ArrayList<Tabligh_not_vizhe> items, View view) {
        itemsAdapter.insertData(items);
        swipeProgress(false);
        if (items.size() == 0) showNoItemView(true, view);
    }

    private void onFailRequest(int page, View view) {
        itemsAdapter.setLoaded();
        swipeProgress(false);
        if (OnlineCheck.isConnect(getActivity())) {
            showFailedView(true, "اینترنت قطه", view);
        } else {
            showFailedView(true, getString(R.string.no_internet_text), view);
        }

    }

    private void showFailedView(boolean show, String message, View parentview) {

        View lyt_failed = parentview.findViewById(R.id.lyt_failed);
        CustomTextView txt= parentview.findViewById(R.id.failed_message);
        txt.setText(message);
        if (show) {
            rvItems.setVisibility(View.GONE);
            lyt_failed.setVisibility(View.VISIBLE);
        } else {
            rvItems.setVisibility(View.VISIBLE);
            lyt_failed.setVisibility(View.GONE);
        }
        parentview.findViewById(R.id.failed_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestAction(page, citycode, parentview);
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

    private void slider() {
        for (int i = 0; i < lists.size(); i++) {
            textSliderView = new TextSliderView(getActivity());
            String image = lists.get(i).image;
            String name = lists.get(i).name;

            textSliderView.description(name).image(G.IMAGE_URL + image);
            textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {

                    int position = sliderShow.getCurrentPosition();
                    int currentid = lists.get(position).id;
                    Intent intent = new Intent(getActivity(), Activity_Tabligh_Detail.class);
                    intent.putExtra("id", currentid);
                    startActivity(intent);
                }
            });

            sliderShow.addSlider(textSliderView);
        }
    }

    private void requestListProduct(final int page, final int citycode, View view) {
        new Post().getadslist(page, citycode, new AnswerPosts() {
            @Override
            public void AnswerBase(ArrayList<JobItemsList> answer) {
                try {
                    if (answer.get(0).name != null) {
                        swipeProgress(false);
                        showFailedView(false, "", view);
                        if (post_total > Constants.PRODUCT_PER_REQUEST) {
                            linearprogress.setVisibility(View.VISIBLE);
                        }
                        post_total = answer.get(0).totalposts;

                        for (int i = 0; i < answer.size(); i++) {
                            int vizhe = answer.get(i).vizhe;
                            if (vizhe == 1) {
                                String name = answer.get(i).name;
                                String image = answer.get(i).image;
                                int id = answer.get(i).id;
                                lists.add(new Tabligh(id, name, image));
                                Log.i("log", "adding list");
                            }
                            if (vizhe == 0) {
                                String name = answer.get(i).name;
                                int id = answer.get(i).id;
                                String image = answer.get(i).image;
                                int totalpost= answer.get(i).totalposts;

                                //first we add to simple ad and then to adapter array
                                simplead.add(new Tabligh_not_vizhe(id, name, image, totalpost));
                                }
                        }
                        displayApiResult(simplead, view);

                        //we add to sliser array once
                        if (page == 1) {
                            slider();
                        }

//                            simplead=answer;
//
//                            for (int t = 0; t < lists.size(); t++) {
//                                simplead.remove(t);
//                            }
//
//                            for (int c=0;c<simplead.size();c++) {
//                                String name=simplead.get(c).name;
//                                int id=simplead.get(c).id;
//                                String image=simplead.get(c).image;
//
//                                Log.i("log", String.valueOf(id));
//                                Log.i("log", image);
//                                Log.i("log", name);
//                            }

                    } else {
                        swipeProgress(false);
                        showFailedView(true, getString(R.string.no_item), view);
                    }
                } catch (Exception e) {
                    Log.i("log", String.valueOf(e));
                }
            }

            @Override
            public void SendError(Throwable t) {
                onFailRequest(page, view);
            }
        });

    }


    private void clearArray() {
        lists.clear();
        simplead.clear();
        TablighAdapter.itemsArraylist.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearArray();
        Log.i("log", "list clear");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("log", "onresume ha");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("log", "onstop ha");
        clearArray();

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("log", "onstart ha");
        }

}
