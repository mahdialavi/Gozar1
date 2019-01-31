package com.af.silaa_grp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.af.silaa_grp.CustomControl.CustomTextView;
import com.af.silaa_grp.Retrofit.Constants;

import java.util.ArrayList;

public class TablighAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private Context context;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    public static ArrayList<Tabligh_not_vizhe> itemsArraylist = new ArrayList<>();

    public TablighAdapter(Context context, RecyclerView view, NestedScrollView nestedScrollView) {
        this.context = context;
        lastItemViewDetector(view, nestedScrollView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ads_item, parent, false);
            vh = new OriginalViewHolder(v);
        } else {
            //here i created empty view to use in stead of progress bar
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view, parent, false);
            vh = new TablighAdapter.ProgressViewHolder(v);
        }
        return vh;
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        ProgressViewHolder(View v) {
            super(v);
//            progressBar = (ProgressBar) v.findViewById(R.id.progress_loading);
        }
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        CustomTextView txtcatname;

        CardView cardView;

        OriginalViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            txtcatname = itemView.findViewById(R.id.txtcatname);
            cardView = itemView.findViewById(R.id.catitemcardview);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OriginalViewHolder) {
            final Tabligh_not_vizhe heros = itemsArraylist.get(position);
            final OriginalViewHolder vItem = (OriginalViewHolder) holder;

            vItem.txtcatname.setText(heros.name);

            Tools.displayImageOriginal(context, vItem.image, G.IMAGE_URL + heros.image);
            vItem.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ActivityDetail.class);
                    intent.putExtra("id", heros.id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

        }
    }

    @Override
    public int getItemViewType(int position) {
        return itemsArraylist.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public void insertData(ArrayList<Tabligh_not_vizhe> items) {
        setLoaded();
        int positionStart = getItemCount();
        int itemCount = items.size();
        itemsArraylist.addAll(items);
        notifyDataSetChanged();
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
        TablighAdapter.itemsArraylist = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    @Override
    public int getItemCount() {
        return itemsArraylist.size();
    }

    private void lastItemViewDetector(RecyclerView recyclerView, NestedScrollView nestedScrollView) {

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    //code to fetch more data for endless scrolling
                    if (!loading && onLoadMoreListener != null) {
                        int current_page = getItemCount() / Constants.PRODUCT_PER_REQUEST;

                        onLoadMoreListener.onLoadMore(current_page);
                        loading = true;
                    }
                }
            }
        });
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int current_page);
    }

}
