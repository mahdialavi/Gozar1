package com.cilla_project.gozar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cilla_project.gozar.CustomControl.CustomTextView;
import com.cilla_project.gozar.DataBase.Persons;

import java.util.ArrayList;

public class ActivityBookmark extends ActivityEnhanced {
    private FavItemAdaptor adapter;
    private RecyclerView rvFavItem;
    CustomTextView txttoolcatename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        txttoolcatename= findViewById(R.id.txttoolcatename);
        txttoolcatename.setText("نشان شده ها");
        findViewById(R.id.linearback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });

        rvFavItem = (RecyclerView) findViewById(R.id.rvFavItem);

        adapter = new FavItemAdaptor(Persons.all());
        rvFavItem.setLayoutManager(new LinearLayoutManager(G.Context));
        rvFavItem.setHasFixedSize(true);
        rvFavItem.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //when we back from activity show this activity onrestarts
        Log.i("onstop", "onrestart");
        FavItemAdaptor.persons = Persons.all();
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("onstop", "onstart");

        if (Persons.all().size() == 0) {
            Toast.makeText(G.Context, getString(R.string.no_news_saved), Toast.LENGTH_LONG).show();
            showNoItemView(true);
        } else {
            showNoItemView(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("onstop","onpause" );

        FavItemAdaptor.persons.clear();
        adapter.notifyDataSetChanged();
    }
    private void showNoItemView(boolean show) {
        View lyt_no_item = (View) findViewById(R.id.lyt_no_item);
        if (show) {
            lyt_no_item.setVisibility(View.VISIBLE);
        } else {
            lyt_no_item.setVisibility(View.GONE);
        }
    }

    public static class FavItemAdaptor extends RecyclerView.Adapter<FavItemAdaptor.ViewHolder> {
        public static ArrayList<JobItemsList> persons;

        FavItemAdaptor(ArrayList<JobItemsList> persons) {
            this.persons = persons;
        }

        public FavItemAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = G.INFLATER.inflate(R.layout.my_ad_item, parent, false);
            return new FavItemAdaptor.ViewHolder(v);
        }
        @Override
        public void onBindViewHolder(final FavItemAdaptor.ViewHolder holder, int position) {
            final JobItemsList person = persons.get(position);

            holder.txttitle.setText(person.name);

            Glide
                    .with(G.Context)
                    .load(G.IMAGE_URL + person.image)
                    .into(holder.image);
            holder.catitemcardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = person.id;
                    Intent intent = new Intent(G.Context, ActivityDetail.class);
                    intent.putExtra("id", id);
                    intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                    G.Context.startActivity(intent);

                }
            });
        }


        public int getItemCount() {
            return persons.size();
        }

        public  class ViewHolder extends RecyclerView.ViewHolder {
            public CustomTextView txttitle;
            public CustomTextView txtfulltext;
            public CustomTextView btncreated;
            public CustomTextView txthits;
            public ImageView imgFeatured;
            public ImageView image;
            public CardView catitemcardview;

            public ViewHolder(View itemView) {
                super(itemView);
                txttitle = itemView.findViewById(R.id.txttitle);
                image = itemView.findViewById(R.id.image);
                catitemcardview = itemView.findViewById(R.id.linearitemcardview);

            }
        }


    }





}
