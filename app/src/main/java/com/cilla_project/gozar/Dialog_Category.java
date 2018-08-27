
package com.cilla_project.gozar;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cilla_project.gozar.CustomControl.CustomButton;

import static com.cilla_project.gozar.ActivityInsert.spcatid;
import static com.cilla_project.gozar.ActivityInsert.spcatname;

public class Dialog_Category extends Dialog {
    private OnDismissListener listener;
    private Context context;
    private CustomButton btncategoty;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int selectedCatid=0;

    int catsakhtemani=1,catkeshavarzi=2,catsanati=3,catkhayati=4,cathesabdari=5,catkarkhane=6;

     RelativeLayout linearkashavarzi,linearsanati,linearsakhtemanni,linearkhayati,linearkarkhane,linearhesabdari;

    String sakhteman= "مشاغل ساختمانی";
    String keshavarzi= "کشاورزی و دامپروری";
    String sanati= "صنعتی و تعمیراتی";
    String khayati= "خیاطی و دوزندگی";
    String hesabdari= "حسابداری و فروشندگی";
    String karkhane= "کارخانه های تولید کننده";


    public Dialog_Category(Context context, CustomButton button,int selectedCatid) {
        super(context);

        this.selectedCatid=selectedCatid;
        this.btncategoty=button;
        this.context=context;
        init();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_category);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor=sharedPreferences.edit();
        linearkashavarzi = findViewById(R.id.linearkeshavarzi);
        linearsakhtemanni= findViewById(R.id.linearsakhtemani);
        linearsanati= findViewById(R.id.linearsanati);
        linearhesabdari= findViewById(R.id.linearhesabdari);
        linearkhayati= findViewById(R.id.linearkhayati);
        linearkarkhane= findViewById(R.id.linearkarkhane);

        findViewById(R.id.imgclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        linearkashavarzi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btncategoty.setText(R.string.keshavarzi);
                selectedCatid=catkeshavarzi;

                editor.putString(spcatname, keshavarzi);
                editor.putInt(spcatid, catkeshavarzi);
                editor.apply();
                Toast.makeText(context, +sharedPreferences.getInt(spcatid,0)+"", Toast.LENGTH_SHORT).show();

                dismiss();
            }
        });
        linearsakhtemanni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btncategoty.setText(sakhteman);
                selectedCatid=catsakhtemani;
                editor.putString(spcatname, sakhteman);
                editor.putInt(spcatid, catsakhtemani);
                editor.apply();
                dismiss();
                Toast.makeText(context, sharedPreferences.getString(spcatname,"")+sharedPreferences.getInt(spcatid,0)+"", Toast.LENGTH_SHORT).show();

            }
        });
      linearsanati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btncategoty.setText(R.string.sanatitamirati);
                selectedCatid=catsanati;
                editor.putString(spcatname, sanati);
                editor.putInt(spcatid, catsanati);
                editor.apply();
                dismiss();
                Toast.makeText(context, sharedPreferences.getString(spcatname,"")+sharedPreferences.getInt(spcatid,0)+"", Toast.LENGTH_SHORT).show();

            }
        });
      linearkarkhane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btncategoty.setText(R.string.karkhanetolidi);
                selectedCatid=catkarkhane;
                editor.putString(spcatname, karkhane);
                editor.putInt(spcatid, catkarkhane);
                editor.apply();
                dismiss();
                Toast.makeText(context, sharedPreferences.getString(spcatname,"")+sharedPreferences.getInt(spcatid,0)+"", Toast.LENGTH_SHORT).show();

            }
        });
      linearhesabdari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btncategoty.setText(R.string.hesabdari);
                selectedCatid=cathesabdari;
                editor.putString(spcatname, hesabdari);
                editor.putInt(spcatid, cathesabdari);
                editor.apply();
                Toast.makeText(context, sharedPreferences.getString(spcatname,"")+sharedPreferences.getInt(spcatid,0)+"", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
      linearkhayati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btncategoty.setText(R.string.khayati);
                selectedCatid=catkhayati;
                editor.putString(spcatname, khayati);
                editor.putInt(spcatid, catkhayati);
                editor.apply();
                Toast.makeText(context, sharedPreferences.getString(spcatname,"")+sharedPreferences.getInt(spcatid,0)+"", Toast.LENGTH_SHORT).show();
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

    }
}
