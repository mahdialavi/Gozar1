package com.cilla_project.gozar;

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
import android.widget.ProgressBar;

import com.cilla_project.gozar.CustomControl.CustomTextView;
import com.cilla_project.gozar.Retrofit.Constants;

import java.util.ArrayList;

public class SearchAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private Context context;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public static ArrayList<JobItemsList> itemsArraylist = new ArrayList<>();


    private AdapterView.OnItemClickListener mOnItemClickListener;

    public SearchAdaptor(Context context, RecyclerView view, ArrayList<JobItemsList> itemsArraylist) {
        SearchAdaptor.itemsArraylist = itemsArraylist;
        context = context;
        lastItemViewDetector(view);
    }

    public SearchAdaptor(Context context, RecyclerView view) {
        this.context = context;
        lastItemViewDetector(view);

    }

    public SearchAdaptor(ArrayList<JobItemsList> searchArr, RecyclerView view) {
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
            vh = new OriginalViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OriginalViewHolder) {
            final JobItemsList heros = itemsArraylist.get(position);

            final OriginalViewHolder vItem = (OriginalViewHolder) holder;

//
            vItem.txtcatname.setText(heros.name);
            vItem.txttime.setText(heros.tarikh);
            Tools.displayImageOriginal(context, vItem.image, G.IMAGE_URL + heros.image);
            vItem.linearitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //we sent image to viewpager in adaptorImage in activityshow array
//                    if (heros.buisinesPics == null || heros.buisinesPics.equals("")) {
//                        ActivityShow.sliderArr.add(G.IMAGE_URL + heros.imglogo);
//                    } else {
//                        for (int i = 0; i < heros.buisinesPics.size(); i++) {
//                            ActivityShow.sliderArr.add(G.IMAGE_URL + heros.getBuisinesPics().get(i));
//                        }
//                    }
//                    String imglogo = G.IMAGE_URL + heros.imglogo;

                    Intent intent = new Intent(G.Context, ActivityDetail.class);
                    intent.putExtra("id", heros.id);
//                    intent.putExtra("imglogo", heros.imglogo);
//                    intent.putExtra("name", heros.itemname);
//                    intent.putExtra("email", heros.email);
//                    intent.putExtra("categoryname", heros.categoryname);
//                    intent.putExtra("website", heros.website);
//                    intent.putExtra("manager", heros.manager);
//
//                    intent.putExtra("estabyear", heros.stabyear);
//                    intent.putExtra("telephone", heros.telephone);
//                    intent.putExtra("address", heros.address);
//                    intent.putExtra("boss", heros.boss);
//                    intent.putExtra("conssetionair", heros.conssetionair);
//                    intent.putExtra("description", heros.description);

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    G.Context.startActivity(intent);
                }

            });


        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        ImageView image, imgfav;
        CardView linearitem;
        CustomTextView txtcatname,txttime;


        public OriginalViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            txttime = itemView.findViewById(R.id.txttime);
            txtcatname = itemView.findViewById(R.id.txttitle);
            linearitem = itemView.findViewById(R.id.linearitemcardview);
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

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
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

//        Toast.makeText(G.Context,"2", Toast.LENGTH_SHORT).show();
//        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

//            final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
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

}