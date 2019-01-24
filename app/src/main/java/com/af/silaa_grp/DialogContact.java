package com.af.silaa_grp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.af.silaa_grp.CustomControl.CustomTextView;
import com.af.silaa_grp.DataBase.Persons;

class DialogContact  extends Dialog {

    private JobItemsList itemsList;
    public Context context;
    private ImageView imgfav;

    private OnDismissListener listener;
    private CustomTextView customTextView;

    public DialogContact(@NonNull Context context, JobItemsList itemsList, ImageView imgfav) {
        super(context);
        this.context=context;
        this.imgfav=imgfav;
        this.itemsList = itemsList;
        init();
    }
    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_contact);

        if (itemsList == null) {
            itemsList = new JobItemsList();
        }
        CustomTextView txtcall = findViewById(R.id.txtCall);
        LinearLayout linearmessage = findViewById(R.id.linearmessage);
        LinearLayout linearbookmark = findViewById(R.id.linearbookmark);
        LinearLayout linearcall = findViewById(R.id.linearcal);
        CustomTextView txtmessage = findViewById(R.id.txtmessage);
        findViewById(R.id.imgcancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        txtcall.setText("تماس با"+itemsList.tamas);
        txtmessage.setText("ارسال پیامک به"+itemsList.tamas);

        if (Persons.exists(itemsList.id)) {
            linearbookmark.setVisibility(View.GONE);
        } else {
            linearbookmark.setVisibility(View.VISIBLE);
        }
        linearcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent call = new Intent(Intent.ACTION_DIAL);
                call.setData(Uri.parse("tel:"+itemsList.tamas));
                context.startActivity(call);
            }
        });
        linearmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(G.Context,"پیام",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"+itemsList.tamas ));
                context.startActivity(intent);
            }
        });
        linearbookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!Persons.exists(itemsList.id)) {

                    Persons.insert(itemsList);
                    imgfav.setImageResource(R.drawable.imgfav2);
//                    imgfav.setImageResource(R.drawable.ic_action_action_bookmark);
                    Toast.makeText(G.Context,"اعلان با موفقیت نشان گردید", Toast.LENGTH_LONG).show();
                    dismiss();
                }
            }
        });
    }
    public DialogContact setListener(OnDismissListener listener) {
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