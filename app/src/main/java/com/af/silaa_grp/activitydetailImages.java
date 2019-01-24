package com.af.silaa_grp;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class activitydetailImages extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<String> img;


    public activitydetailImages(Context context, ArrayList<String> img) {
        this.context = context;
        this.img= img;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return img.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

//        Log.i("onpause", img.get(position));
        View v = layoutInflater.inflate(R.layout.item, container, false);
        ImageView image = (ImageView) v.findViewById(R.id.imageView);
        Tools.displayImageOriginal(G.Context, image,img.get(position));
        ((ViewPager) container).addView(v);
                image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(Context, "you clicked image " + (position + 1), Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);
//      destroyItem(container, position, object);

    }


}
