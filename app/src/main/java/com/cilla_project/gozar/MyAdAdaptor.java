package com.cilla_project.gozar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.cilla_project.gozar.CustomControl.CustomTextView;
import com.cilla_project.gozar.Retrofit.Constants;


import java.util.ArrayList;

public class MyAdAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    public Context context;
    private boolean loading;
    private MyAdAdaptor.OnLoadMoreListener onLoadMoreListener;

    public static ArrayList<JobItemsList> itemsArraylist = new ArrayList<>();


    private AdapterView.OnItemClickListener mOnItemClickListener;

    public MyAdAdaptor(Context context, RecyclerView view, ArrayList<JobItemsList> itemsArraylist) {
        MyAdAdaptor.itemsArraylist = itemsArraylist;
        this.context = context;
        lastItemViewDetector(view);
    }

    public MyAdAdaptor(Context context, RecyclerView view) {
        this.context = context;
//        sharedPref = new SharedPref(context);
        lastItemViewDetector(view);

    }

    public MyAdAdaptor(ArrayList<JobItemsList> searchArr, RecyclerView view) {
        itemsArraylist = searchArr;
        this.context = context;
//        sharedPref = new SharedPref(Context);
        lastItemViewDetector(view);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_ad_item, parent, false);
            vh = new MyAdAdaptor.OriginalViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            vh = new MyAdAdaptor.ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyAdAdaptor.OriginalViewHolder) {
            final JobItemsList heros = itemsArraylist.get(position);


            final MyAdAdaptor.OriginalViewHolder vItem = (MyAdAdaptor.OriginalViewHolder) holder;
            vItem.txttitle.setText(heros.name);
            vItem.txttime.setText(heros.tarikh);
            Glide
                    .with(G.Context)
                    .load(G.IMAGE_URL + heros.image)
                    .into(((OriginalViewHolder) holder).image);
            vItem.linearitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(G.Context, ActivityCheckAd.class);
                    intent.putExtra("catname", heros.categoryname);
                    intent.putExtra("catid", heros.parent_id);
                    intent.putExtra("itemid", heros.id);

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    //i passed Context from Activity_myad.class
                    ((Activity)context).finish();
                }

            });


        } else {
            ((MyAdAdaptor.ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return itemsArraylist.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public void insertData(ArrayList<JobItemsList> items) {
        setLoaded();
        int positionStart = getItemCount();
        int itemCount = items.size();
        itemsArraylist.addAll(items);
        notifyItemRangeInserted(positionStart, itemCount);
    }

    public void setLoaded() {
        loading = false;
        for (int i = 0; i < getItemCount(); i++) {
            if (itemsArraylist.get(i) == null) {
                itemsArraylist.remove(i);
                notifyItemRemoved(i);
            }
        }
    }

    public void setLoading() {
        if (getItemCount() != 0) {
            itemsArraylist.add(null);
            notifyItemInserted(getItemCount() - 1);
            loading = true;
        }
    }

    public void resetListData() {
        itemsArraylist = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setOnLoadMoreListener(MyAdAdaptor.OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    @Override
    public int getItemCount() {
        return itemsArraylist.size();
    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progress_loading);
        }
    }

    private void lastItemViewDetector(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastPos = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                if (!loading && lastPos == getItemCount() - 1 && onLoadMoreListener != null) {
                    if (onLoadMoreListener != null) {
                        int current_page = getItemCount() / Constants.PRODUCT_PER_REQUEST;
//                        Toast.makeText(G.Context, "current_page="+current_page , Toast.LENGTH_SHORT).show();
                        onLoadMoreListener.onLoadMore(current_page);
                    }
                    loading = true;
                }
            }
        });
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int current_page);
    }


    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        CardView linearitem;
        CustomTextView txttitle;
        CustomTextView txttime;
        ImageView image;
        ImageView imgfav;

        OriginalViewHolder(View itemView) {
            super(itemView);

            txttitle = itemView.findViewById(R.id.txttitle);
            txttime = itemView.findViewById(R.id.txttime);
            image = itemView.findViewById(R.id.image);
            linearitem = itemView.findViewById(R.id.linearitemcardview);

        }
    }

}
