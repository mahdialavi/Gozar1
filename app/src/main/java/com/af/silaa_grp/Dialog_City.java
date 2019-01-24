
package com.af.silaa_grp;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.af.silaa_grp.CustomControl.CustomButton;
import com.af.silaa_grp.CustomControl.CustomTextView;
import com.af.silaa_grp.Retrofit.AnswerPosts;
import com.af.silaa_grp.Retrofit.Post;
import com.af.silaa_grp.adapters.Adapter_Dialog_Category;
import com.af.silaa_grp.adapters.Adapter_dialog_cities;

import java.util.ArrayList;

import static com.af.silaa_grp.ActivityInsert.spcatid;
import static com.af.silaa_grp.ActivityInsert.spcatname;

public class Dialog_City extends Dialog {
    private OnDismissListener listener;
    private Context context;
    private CustomTextView txtcity;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int selectedCatid = 0;
    ImageView imgclose;
    RelativeLayout linearprogress;

    String command = "getCityCats";
    RecyclerView rvItems;
    Adapter_dialog_cities itemsAdapter;


    Dialog_City(Context context, CustomTextView txtcity, int selectedCatid) {
        super(context);
        this.selectedCatid = selectedCatid;
        this.txtcity = txtcity;
        this.context = context;
        init();
    }

    Dialog_City(Context context) {
        super(context);
        this.context = context;
        init();
    }
    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_city);


        rvItems = findViewById(R.id.rvCats);
        linearprogress= findViewById(R.id.linearprogress);
        itemsAdapter = new Adapter_dialog_cities(G.Context, rvItems, txtcity, selectedCatid, this);
        rvItems.setLayoutManager(new LinearLayoutManager(context));
        rvItems.setHasFixedSize(true);
        rvItems.setAdapter(itemsAdapter);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();

        getCities(command);
        imgclose = findViewById(R.id.imgclose);

        if (txtcity == null && selectedCatid == 0) {
            imgclose.setVisibility(View.GONE);
        }
        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public Dialog_City setListener(OnDismissListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (listener != null) {
            listener.onDismiss(this);
        }
        Adapter_dialog_cities.itemsArraylist.clear();
    }

    private void getCities(String command) {
        linearprogress.setVisibility(View.VISIBLE);
        new Post().getProductList(command, 0, 0, 0, new AnswerPosts() {
            @Override
            public void AnswerBase(ArrayList<JobItemsList> answer) {
                try {
                    displayApiResult(answer);
                    linearprogress.setVisibility(View.GONE);
                } catch (NullPointerException e) {
                    Log.i("null", String.valueOf(e));

                }}

            @Override
            public void SendError(Throwable t) {
                linearprogress.setVisibility(View.GONE);
                dismiss();

            }
        });
    }

    private void displayApiResult(final ArrayList<JobItemsList> items) {
        itemsAdapter.insertData(items);
    }



}
