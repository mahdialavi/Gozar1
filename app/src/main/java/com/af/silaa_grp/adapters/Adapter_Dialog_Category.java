package com.af.silaa_grp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.af.silaa_grp.ActivityInsert;
import com.af.silaa_grp.CustomControl.CustomButton;
import com.af.silaa_grp.CustomControl.CustomTextView;
import com.af.silaa_grp.Dialog_Category;
import com.af.silaa_grp.G;
import com.af.silaa_grp.JobItemsList;
import com.af.silaa_grp.R;


import java.util.ArrayList;

public class Adapter_Dialog_Category extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private Context context;
    private boolean loading;
    CustomButton btncategory;
    int selectedCat=0;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Dialog_Category dialog_category;
    public static ArrayList<JobItemsList> itemsArraylist = new ArrayList<>();



    public Adapter_Dialog_Category(Context context, RecyclerView view, CustomButton btncategory, int selectedcat, Dialog_Category dialog) {
        this.context = context;
        this.btncategory=btncategory;
        this.dialog_category=dialog;
        this.selectedCat=selectedcat;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_cat_xml, parent, false);
            vh = new Adapter_Dialog_Category.OriginalViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            vh = new Adapter_Dialog_Category.ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Adapter_Dialog_Category.OriginalViewHolder) {
            final JobItemsList heros = itemsArraylist.get(position);
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(G.Context);
            editor = sharedPreferences.edit();

            final Adapter_Dialog_Category.OriginalViewHolder vItem = (Adapter_Dialog_Category.OriginalViewHolder) holder;

            vItem.txtcatname.setText(heros.name);
            vItem.linearitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    btncategory.setText(heros.name);
                    selectedCat=heros.id;
                    editor.putString(ActivityInsert.spcatname,heros.name );
                    editor.putInt(ActivityInsert.spcatid, heros.id);
                    editor.apply();
                    dialog_category.dismiss();
                   }

            });


        } else {
            ((Adapter_Dialog_Category.ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        CardView linearitem;
        CustomTextView txtcatname;


         OriginalViewHolder(View itemView) {
            super(itemView);
            txtcatname = itemView.findViewById(R.id.txtName);
            linearitem = itemView.findViewById(R.id.catitemcardview);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return itemsArraylist.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public void resetListData() {

        itemsArraylist = new ArrayList<>();
        notifyDataSetChanged();
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
 }
