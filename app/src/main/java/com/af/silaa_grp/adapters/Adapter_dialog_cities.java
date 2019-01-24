package com.af.silaa_grp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.af.silaa_grp.ActivityCheck_manager;
import com.af.silaa_grp.ActivityInsert;
import com.af.silaa_grp.CustomControl.CustomButton;
import com.af.silaa_grp.CustomControl.CustomTextView;
import com.af.silaa_grp.Dialog_Category;
import com.af.silaa_grp.Dialog_City;
import com.af.silaa_grp.G;
import com.af.silaa_grp.HomeFragment;
import com.af.silaa_grp.Home_Adapter;
import com.af.silaa_grp.JobItemsList;
import com.af.silaa_grp.MainActivity;
import com.af.silaa_grp.R;

import java.util.ArrayList;

public class Adapter_dialog_cities extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private Context context;
    private boolean loading;
    CustomTextView txtcity = null;
    int selectedCat = 0;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Dialog_City dialog_city;
    public static ArrayList<JobItemsList> itemsArraylist = new ArrayList<>();

    public Adapter_dialog_cities(Context context, RecyclerView view, CustomTextView txtcity, int selectedcat, Dialog_City dialog) {
        this.context = context;
        this.txtcity = txtcity;
        this.txtcity = txtcity;
        this.dialog_city = dialog;
        this.selectedCat = selectedcat;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_cat_xml, parent, false);
            vh = new Adapter_dialog_cities.OriginalViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            vh = new Adapter_dialog_cities.ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Adapter_dialog_cities.OriginalViewHolder) {
            final JobItemsList heros = itemsArraylist.get(position);
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(G.Context);
            editor = sharedPreferences.edit();

            final Adapter_dialog_cities.OriginalViewHolder vItem = (Adapter_dialog_cities.OriginalViewHolder) holder;
            vItem.txtcatname.setText(heros.name);
            vItem.linearitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    //if we came from home fragment
//                    if (txtcity != null) {
//                        txtcity.setText(heros.name);
//                    } else {
//                        G.startActivity(MainActivity.class,true);
//                        Home_Adapter.itemsArraylist.clear();
//                        }



                    selectedCat = heros.id;
                    editor.putString("sp_city_name", heros.name);
                    editor.putInt("cat_city", heros.id);

                    editor.putInt("firstlaunch", 0);
                    editor.apply();
                    txtcity.setText(heros.name);

                    dialog_city.dismiss();





                }

            });


        } else {
            ((Adapter_dialog_cities.ProgressViewHolder) holder).progressBar.setIndeterminate(true);
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
