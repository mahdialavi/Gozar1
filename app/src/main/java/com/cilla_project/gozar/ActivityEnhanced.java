package com.cilla_project.gozar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by alavi on 11/14/2017.
 */

public class ActivityEnhanced extends AppCompatActivity {
    @Override
    protected void onResume() {
        super.onResume();
        G.ACTIVITY=this;
    }

}
