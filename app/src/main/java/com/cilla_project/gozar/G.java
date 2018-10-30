package com.cilla_project.gozar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Window;


import com.cilla_project.gozar.DataBase.DB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class G extends Application {

    public static final Handler HANDLER = new Handler();
    public static AppCompatActivity ACTIVITY;
    public static Context Context;
    public static LayoutInflater INFLATER;
    public static SQLiteDatabase DB;
    public static Dialog custom_P_Dialog;

    //    public static ArrayList<ItemsList> itemsArraylist;
    public static Typeface FONT_BOLD;
    public static Typeface FONT_NORMAL;
    public static String direction = Environment.getExternalStorageDirectory().getAbsolutePath() + "/herbal_meds";
    public static Handler hanler;
    public static int PRODUCT_PER_REQUEST = 16;

//    public static final String IMAGE_URL="http://192.168.1.102/gozar";
    public static final String IMAGE_URL="http://hendiabootik.com/test";

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onCreate() {
        super.onCreate();

        Context = getApplicationContext();
        hanler = new Handler();


        File file = new File(direction);
        file.mkdirs();
        try {
            file.createNewFile();
            copyFromAssets(getBaseContext().getAssets().open("material_book.sqlite"),
                    new FileOutputStream(direction + "/material_book.sqlite"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        DB=new DB().getWritableDatabase();

        INFLATER = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        AssetManager assetManager = getAssets();
        FONT_NORMAL = Typeface.createFromAsset(assetManager, "font/IRANSans_Normal.ttf");

        FONT_BOLD = Typeface.createFromAsset(assetManager, "font/IRANSans_Bold.ttf");
    }
    public void copyFromAssets(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    }

    public static void startActivity(Class targetActivity) {
        startActivity(targetActivity, false);
    }

    public static void startActivity(Class targetActivity, boolean finish) {
        Intent intent = new Intent(ACTIVITY, targetActivity);
        ACTIVITY.startActivity(intent);
        if (finish) {
            ACTIVITY.finish();
        }}

    public static int getGridSpanCount(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        float screenWidth = displayMetrics.widthPixels;
        float cellWidth = activity.getResources().getDimension(R.dimen.item_product_width);
        return Math.round(screenWidth / cellWidth);
    }
    public static String timeStamp() {
        return String.valueOf(System.currentTimeMillis()).substring(0, 10);
    }
    public static int getColorCompact(int colorId) {
        return ContextCompat.getColor(Context, colorId);
    }
    public static void show_P_Dialog(Context context, Boolean cancelable, Boolean outcancel) {
        custom_P_Dialog = new Dialog(context);
        custom_P_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        custom_P_Dialog.setContentView(R.layout.prograss_bar_dialog);
        custom_P_Dialog.setCancelable(cancelable);
        custom_P_Dialog.setCanceledOnTouchOutside(outcancel);
        custom_P_Dialog.show();

    }
    public static void dismiss_P_Dialog() {
        custom_P_Dialog.dismiss();
    }
}
