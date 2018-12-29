package com.cilla_project.gozar;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;

public class MainActivity extends ActivityEnhanced {
   public BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainactivityfor_fragment);

        navigation = (BottomNavigationView) findViewById(R.id.bottomnavigation2);

        BottomNavigationViewHelper.disableShiftMode(navigation);
        final Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedfragment = null;
                switch (item.getItemId()) {
                    case R.id.menukhane:
                        item.setChecked(true);
                        selectedfragment = HomeFragment.newInstance();
                        HomeFragment.code=0;
                        MainActivity_Adapter.itemsArraylist.clear();
                        break;

                        case R.id.menuCategory:
                        item.setChecked(true);
                        selectedfragment = Fragment_Categories.newInstance();
                        //empty mainactivity array before setting new data
                        MainActivity_Adapter.itemsArraylist.clear();
                        JobAdapter.itemsArraylist.clear();

                        break;

                    case R.id.menuinsert:
                        item.setChecked(true);
                        G.startActivity(ActivityInsert.class,true);
                        MainActivity_Adapter.itemsArraylist.clear();
                        return false;
                    case R.id.menutablighat:
                        item.setChecked(true);
                        selectedfragment = Fragment_tablighat.newInstance();
                        ItemAdapter.itemsArraylist.clear();

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


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, HomeFragment.newInstance());
        transaction.commit();
    }

    public void setNavigationVisibility(int visible) {
        if (visible == 0) {
            navigation.setVisibility(View.GONE);
        } else if (visible == 1) {
//            navigation.setVisibility(View.VISIBLE);
            slideDown(navigation);

        }
    }
    public void slideDown(View view) {
        view.setVisibility(View.VISIBLE);

        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
              0 );                // toYDelta
        animate.setDuration(400);
        animate.setFillAfter(false);
        view.startAnimation(animate);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
