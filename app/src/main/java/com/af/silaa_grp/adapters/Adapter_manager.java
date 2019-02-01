package com.af.silaa_grp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.af.silaa_grp.ActivityCheck_manager;
import com.af.silaa_grp.CustomControl.CustomTextView;
import com.af.silaa_grp.G;
import com.af.silaa_grp.JobItemsList;
import com.af.silaa_grp.R;
import com.af.silaa_grp.Tools;

import java.util.ArrayList;

public class Adapter_manager extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    public Context context;
    private boolean loading;
    private Adapter_manager.OnLoadMoreListener onLoadMoreListener;

    public static ArrayList<JobItemsList> itemsArraylist = new ArrayList<>();
    private AdapterView.OnItemClickListener mOnItemClickListener;

    public Adapter_manager(Context context, RecyclerView view) {
        this.context = context;
        lastItemViewDetector(view);
        }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_ad_item, parent, false);
            vh = new Adapter_manager.OriginalViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            vh = new Adapter_manager.ProgressViewHolder(v);
        }
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Adapter_manager.OriginalViewHolder) {
            final JobItemsList heros = itemsArraylist.get(position);

            final OriginalViewHolder vItem = (OriginalViewHolder) holder;

            vItem.txttitle.setText(heros.name);
            vItem.txttime.setText(heros.tarikh);
            Tools.displayImageOriginal(context, vItem.image, G.IMAGE_URL + heros.image);

            vItem.linearitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(G.Context, ActivityCheck_manager.class);
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
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }
    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        ImageView image, imgfav;
        CardView linearitem;
        CustomTextView txttitle,txttime;
        OriginalViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            txttime= itemView.findViewById(R.id.txttime);
            txttitle = itemView.findViewById(R.id.txttitle);
            linearitem = itemView.findViewById(R.id.linearitemcardview);
        }
    }
    @Override
    public int getItemViewType(int position) {
        return itemsArraylist.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public void insertData(ArrayList<JobItemsList> items) {
        setLoaded();
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
        notifyDataSetChanged();
    }

    public void setOnLoadMoreListener(Adapter_manager.OnLoadMoreListener onLoadMoreListener) {
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
                            int current_page = getItemCount() / G.PRODUCT_PER_REQUEST;
//                            Toast.makeText(G.Context, current_page+"", Toast.LENGTH_SHORT).show();
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
}
