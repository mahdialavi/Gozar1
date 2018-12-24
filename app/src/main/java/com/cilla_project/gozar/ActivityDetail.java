package com.cilla_project.gozar;

import android.content.DialogInterface;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cilla_project.gozar.CustomControl.CustomButton;
import com.cilla_project.gozar.CustomControl.CustomTextView;
import com.cilla_project.gozar.DataBase.Persons;
import com.cilla_project.gozar.Retrofit.AnswerPosts;
import com.cilla_project.gozar.Retrofit.Post;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class ActivityDetail extends ActivityEnhanced {

    String name, tamas, image,image2,image3, tarikh,address;
    int id = 0;
    String tozihat = "";
    CustomTextView txttozih, txtname, txtzaman,txtaddress;
    ViewPager viewPager;
    CircleIndicator indicator;
    Timer timer;
    public static ArrayList<String> sliderArr = new ArrayList<>();
    private JobItemsList items;
    ImageView imgfav;
    LinearLayout linearwait, linearback;
    CustomButton btncontact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        txttozih = findViewById(R.id.txttozih);
        txtname = findViewById(R.id.txtname);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        indicator = findViewById(R.id.indicator);
        linearwait = findViewById(R.id.linearwait);
        linearwait.setVisibility(View.VISIBLE);
        btncontact = findViewById(R.id.btncontact);
        linearback = findViewById(R.id.linearback);
        txtzaman = findViewById(R.id.txtzaman);
        txtaddress= findViewById(R.id.txtaddress);

        linearback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        findViewById(R.id.btncontact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogContact(ActivityDetail.this, items, imgfav).setListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                    }
                }).show();

            }
        });
        findViewById(R.id.linearfav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBookMark();
            }
        });
        if (items == null) {
            items = new JobItemsList();
        }
        imgfav = findViewById(R.id.imgfav);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getInt("id");
            requestitem(id);
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (sliderArr.size() > 0) {
                            activitydetailImages adapterView = new activitydetailImages(G.Context, sliderArr);
                            viewPager.setAdapter(adapterView);
                            adapterView.notifyDataSetChanged();
                            if (sliderArr.size() > 1) {
                                indicator.setViewPager(viewPager);
                            }
                            timer.cancel();
                        }
                    }
                });
            }
        }, 1, 300);
    }

    private void requestitem(final int id) {

        new Post().getOneItem(id, new AnswerPosts() {
            @Override
            public void AnswerBase(ArrayList<JobItemsList> answer) {
                if (answer != null) {
                    for (int i = 0; i < answer.size(); i++) {
                        image = answer.get(0).image;
                        image2 = answer.get(0).image2;
                        image3 = answer.get(0).image3;

                        name = answer.get(0).name;
                        tamas = answer.get(0).tamas;
                        tozihat = answer.get(0).tozihat;
                        tarikh = answer.get(0).tarikh;
                        address= answer.get(0).address;



//                        if (buisinessPics == null || buisinessPics.equals("")) {
//                            ActivityDetail.sliderArr.add(G.IMAGE_URL + image);
//                        } else {
//                            for (int b = 0; b < buisinessPics.size(); b++) {
//                                sliderArr.add(G.IMAGE_URL + buisinessPics.get(b));
//                            }
//                        }
                        setDataToTxts();
                        linearwait.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void SendError(Throwable t) {
                onFailRequest();
            }
        });

    }

    private void setBookMark() {
        // we save into local database
        if (Persons.exists(id)) {
            Persons.delete(id);
            imgfav.setImageResource(R.drawable.imgfav);
            Toast.makeText(this, "deleted", Toast.LENGTH_SHORT).show();
        } else {
            items.id = id;
            items.name = name;
            items.image = image;
            Persons.insert(items);
            imgfav.setImageResource(R.drawable.imgfav2);
            Toast.makeText(G.Context, "inserted", Toast.LENGTH_LONG).show();
        }
    }

    private void setDataToTxts() {

        sliderArr.add(G.IMAGE_URL + image);
        if (image2 != null&&image2.length()>2) {
            sliderArr.add(G.IMAGE_URL + image2);
            }if (image3!=null&&image3.length()>2) {
            sliderArr.add(G.IMAGE_URL + image3);
        }
        txtname.setText(name);
        txttozih.setText(tozihat);
        txtaddress.setText(address);
        txtzaman.setText(tarikh);

        if (tamas != null) {
            items.tamas = tamas;
            items.id = id;
        }

    }

    private void onFailRequest() {
        linearwait.setVisibility(View.GONE);

        if (OnlineCheck.isConnect(this)) {
            showFailedView(true, getString(R.string.failed_text));
        } else {
            showFailedView(true, getString(R.string.no_internet_text));
        }
    }

    private void showFailedView(boolean show, String message) {
        View lyt_failed = findViewById(R.id.lyt_failed);
        ((TextView) findViewById(R.id.failed_message)).setText(message);
        if (show) {
            lyt_failed.setVisibility(View.VISIBLE);

        } else {

            lyt_failed.setVisibility(View.GONE);

        }
        findViewById(R.id.failed_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFailedView(false, "");
                linearwait.setVisibility(View.VISIBLE);
                requestitem(id);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        sliderArr.clear();

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("onstart", "onstart_happend");
        if (Persons.exists(id)) {
            imgfav.setImageResource(R.drawable.imgfav2);
            Toast.makeText(this, "exists", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "not exists", Toast.LENGTH_SHORT).show();

            imgfav.setImageResource(R.drawable.imgfav);
        }
    }
}
