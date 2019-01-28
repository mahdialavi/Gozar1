package com.af.silaa_grp;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import co.ronash.pushe.Pushe;

public class MainActivity extends ActivityEnhanced {
    public BottomNavigationView navigation;
    int fragmentid = 0;
    private boolean exit = false;
    RelativeLayout linearview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainactivityfor_fragment);
        Pushe.initialize(this, true);

        navigation = (BottomNavigationView) findViewById(R.id.bottomnavigation2);
        linearview= findViewById(R.id.linear_mainactivity_view);



        //check if we get 1 from categoryadapter and go to home fragment
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
                fragmentid = bundle.getInt("fragmentid");
            Log.i("log", fragmentid + "");
        }


        BottomNavigationViewHelper.disableShiftMode(navigation);
        final Menu menu = navigation.getMenu();

        //if we come from fragment category id==1 and item 0 is checked
        if (fragmentid == 0) {
            MenuItem menuItem = menu.getItem(2);

            menuItem.setChecked(true);
        }else {
            MenuItem menuItem = menu.getItem(0);
            menuItem.setChecked(true);
        }
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment selectedfragment = null;
                switch (item.getItemId()) {

                    case R.id.menukhane:
                        item.setChecked(true);
                        selectedfragment = HomeFragment.newInstance();
                        HomeFragment.code = 0;
                        Home_Adapter.itemsArraylist.clear();
                        break;

                    case R.id.menuCategory:
                        item.setChecked(true);
                        selectedfragment = Fragment_Categories.newInstance();

                        //empty mainactivity array before setting new data
                        Home_Adapter.itemsArraylist.clear();
                        Categories_Adapter.itemsArraylist.clear();

                        break;

                    case R.id.menuinsert:
                        item.setChecked(true);
                        G.startActivity(ActivityInsert.class, true);
                        Home_Adapter.itemsArraylist.clear();
                        return false;
                    case R.id.menutablighat:
                        item.setChecked(true);
                        selectedfragment = Fragment_tablighat.newInstance();
                        TablighAdapter.itemsArraylist.clear();

                        break;
                    case R.id.menumypage:
                        item.setChecked(true);
                        selectedfragment = Fragment_My_Page.newInstance();
                        break;
                }

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedfragment);
                transaction.commit();
                return false;
            }
        });


        if (fragmentid == 1) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, HomeFragment.newInstance());
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, Fragment_Categories.newInstance());
            transaction.addToBackStack(null);

            transaction.commit();
        }
    }


//    public void setNavigationVisibility(int visible) {
//        if (visible == 0) {
//            navigation.setVisibility(View.GONE);
//        } else if (visible == 1) {
//            slideDown(navigation);
//        }
//    }
//
//    public void slideDown(View view) {
//        view.setVisibility(View.VISIBLE);
//
//        TranslateAnimation animate = new TranslateAnimation(
//                0,                 // fromXDelta
//                0,                 // toXDelta
//                view.getHeight(),  // fromYDelta
//                0);                // toYDelta
//        animate.setDuration(400);
//        animate.setFillAfter(false);
//        view.startAnimation(animate);
//    }

    public void closefragment(Fragment selectedfragment) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(String.valueOf(selectedfragment));
        if(fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }
    @Override
    public void onBackPressed() {
        if (exit) {
            System.exit(0);
            finish();
        } else {
            exit = true;
            G.showSnackbar(linearview,"برای خروج دوباره دکمه بازگشت را بزنید");
           G.HANDLER.postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 2000);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();


    }





}
