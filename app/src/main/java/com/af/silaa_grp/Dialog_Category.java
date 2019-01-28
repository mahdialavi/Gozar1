
package com.af.silaa_grp;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.af.silaa_grp.CustomControl.CustomButton;
import com.af.silaa_grp.Retrofit.AnswerPosts;
import com.af.silaa_grp.Retrofit.Post;
import com.af.silaa_grp.adapters.Adapter_Dialog_Category;

import java.util.ArrayList;

import static com.af.silaa_grp.ActivityInsert.spcatid;
import static com.af.silaa_grp.ActivityInsert.spcatname;

public class Dialog_Category extends Dialog {
    private OnDismissListener listener;
    private Context context;
    public CustomButton btncategoty;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int selectedCatid = 0;
    String command = "category";
    RelativeLayout linearprogress;
    RecyclerView rvItems;
    Adapter_Dialog_Category itemsAdapter;

    public Dialog_Category(Context context, int selectedCatid, CustomButton btncategoty) {
        super(context);

        this.btncategoty = btncategoty;
        this.selectedCatid = selectedCatid;
        this.context = context;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_category);

        rvItems = findViewById(R.id.rvCats);
        linearprogress = findViewById(R.id.linearprogress);
        itemsAdapter = new Adapter_Dialog_Category(G.Context, rvItems, btncategoty, selectedCatid, this);
        rvItems.setLayoutManager(new LinearLayoutManager(context));
        rvItems.setHasFixedSize(true);
        rvItems.setAdapter(itemsAdapter);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();


        getCategories(command);
        findViewById(R.id.imgclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public Dialog_Category setListener(OnDismissListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (listener != null) {
            listener.onDismiss(this);
        }
        Adapter_Dialog_Category.itemsArraylist.clear();

    }

    private void getCategories(String command) {
        linearprogress.setVisibility(View.VISIBLE);
        new Post().getProductList(command, 0, 0, 0, new AnswerPosts() {
            @Override
            public void AnswerBase(ArrayList<JobItemsList> answer) {
                linearprogress.setVisibility(View.GONE);
                displayApiResult(answer);
            }
            @Override
            public void SendError(Throwable t) {
                linearprogress.setVisibility(View.GONE);
                onFailRequest();
                dismiss();
            }
        });
    }
    private void displayApiResult(final ArrayList<JobItemsList> items) {
        itemsAdapter.insertData(items);
    }
    private void onFailRequest() {
        if (OnlineCheck.isConnect(context)) {
            showSnackbar(context.getString(R.string.no_internet_message));
        } else {
            showSnackbar(context.getString(R.string.problem_in_recieving_data));
        }
    }
    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        snackbar.show();
        snackbar.setActionTextColor(Color.WHITE);
    }
}
