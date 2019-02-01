package com.af.silaa_grp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.af.silaa_grp.Retrofit.Api;
import com.af.silaa_grp.Retrofit.ItemsListUpload;
import com.af.silaa_grp.Retrofit.ServiceGenerator;
import com.bumptech.glide.Glide;
import com.af.silaa_grp.CustomControl.CustomTextView;
import com.af.silaa_grp.Retrofit.Constants;


import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAdAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    public Context context;
    private boolean loading;
    Intent intent;
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_ad_item_preview, parent, false);
            vh = new MyAdAdaptor.OriginalViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            vh = new MyAdAdaptor.ProgressViewHolder(v);
        }
        return vh;
    }

    @SuppressLint("ResourceAsColor")
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


            switch (heros.published) {
                case 0:
                    vItem.txtmode.setText("در انتظار تایید");
                    vItem.txtmode.setTextColor(context.getResources().getColor(R.color.color_red));

                    break;
                case 1:
                    vItem.txtmode.setText("تایید شده");
                    vItem.txtmode.setTextColor(context.getResources().getColor(R.color.colorToolbar));
                    break;

                case 2:
                    vItem.txtmode.setText("رد شده");
                    vItem.txtmode.setTextColor(context.getResources().getColor(R.color.color_red));
                    break;


            }

            vItem.txtprev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = new Intent(G.Context, ActivityDetail.class);
                    intent.putExtra("id", heros.id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    G.Context.startActivity(intent);
                }
            });
            vItem.txtedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = new Intent(G.Context, ActivityEdit.class);
                    intent.putExtra("itemid", heros.id);
                    intent.putExtra("catid", heros.parent_id);
                    intent.putExtra("catname", heros.categoryname);
                    Log.i("cat", heros.parent_id + heros.categoryname);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    G.Context.startActivity(intent);
                    ((Activity_MyAd) context).finish();
                }
            });

            vItem.txtdelet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AppCompatActivity activity = (AppCompatActivity) view.getContext();

                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = activity.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);
                    dialogBuilder.setView(dialogView);
                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.setCancelable(false);
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();


                    dialogView.findViewById(R.id.btn_negative).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });

                    dialogView.findViewById(R.id.btn_positive).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            alertDialog.dismiss();
                            deleteItem(heros.id, 3);

                        }
                    });
                }
            });

            //codes to go go check ad activity
//            vItem.linearitem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(G.Context, ActivityCheckAd.class);
//                    intent.putExtra("catname", heros.categoryname);
//                    intent.putExtra("catid", heros.parent_id);
//                    intent.putExtra("itemid", heros.id);
//
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
//                    //i passed Context from Activity_myad.class
//                    ((Activity) context).finish();
//                }
//
//            });


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
        CustomTextView txttitle, txtprev, txtdelet, txtedit, txtmode;
        CustomTextView txttime;
        ImageView image;
        ImageView imgfav;

        OriginalViewHolder(View itemView) {
            super(itemView);

            txttitle = itemView.findViewById(R.id.txttitle);
            txtmode = itemView.findViewById(R.id.txtmode);
            txttime = itemView.findViewById(R.id.txttime);
            image = itemView.findViewById(R.id.image);
            txtprev = itemView.findViewById(R.id.txtprev);
            txtdelet = itemView.findViewById(R.id.txtdelete);
            txtedit = itemView.findViewById(R.id.txtedit);
            linearitem = itemView.findViewById(R.id.linearitemcardview);

        }
    }

    public void deleteItem(int catid, int code) {

        G.show_progress_dialog(context, false, false);
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
                        if (answer.response.equals("item_deleted")) {
                            G.startActivity(Activity_MyAd.class, true);


                        } else {
                            Toast.makeText(context, "مشکل در حذف آگهی", Toast.LENGTH_LONG).show();
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
        });
    }
}