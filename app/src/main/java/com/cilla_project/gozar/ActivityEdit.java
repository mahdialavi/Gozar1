package com.cilla_project.gozar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cilla_project.gozar.CustomControl.CustomButton;
import com.cilla_project.gozar.CustomControl.CustomTextView;
import com.cilla_project.gozar.Retrofit.AnswerPosts;
import com.cilla_project.gozar.Retrofit.AnswerPosts2;
import com.cilla_project.gozar.Retrofit.ItemsListUpload;
import com.cilla_project.gozar.Retrofit.Post;
import com.cilla_project.gozar.Retrofit.UploadPosts;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public class ActivityEdit extends ActivityEnhanced{


    String name,tamas,image,tozihat,address;
    String catname = "";
    int catid = 0;
    int userId = 0;

    ImageView imgselect1, img_delete_logo;
    CustomButton btncategory;
    TextInputEditText edtmobile, edtaddress, edttozihat, edttitle,edtcat;
    Bitmap bitmap = null;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 123;
    LinearLayout linearcam, catwraper,lineargal;
    int citycode=1;


//    Dialog show_P_Dialog;

    File file;
    Uri uri;
    Intent CamIntent, GalIntent, CropIntent, intent;
    public static final int RequestPermissionCode = 1;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Bundle bundle = null;
    Button btnSubmit;
    int id=0;
    public static int selectedCatid=0;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(G.Context);
        editor = sharedPreferences.edit();
        bundle = getIntent().getExtras();

        edttozihat = findViewById(R.id.edttozih);
        edttitle = findViewById(R.id.edttitle);
        btncategory = findViewById(R.id.btncategory);
        edtmobile = findViewById(R.id.edtmobile);
        edtaddress = findViewById(R.id.edtaddress);
        imgselect1 = findViewById(R.id.imgselect1);
        img_delete_logo = findViewById(R.id.del_img_logo);
        btnSubmit = findViewById(R.id.btnsubmit);

        int CityCode = sharedPreferences.getInt("cat_city", 0);
        if (CityCode != 0) {
            citycode=CityCode;
        }

//                findViewById(R.id.lineardelete).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        clearInputs();
//                    }
//                });
        // when we have datas to shared pref we set them to each edt
        bundle = getIntent().getExtras();
        if (bundle != null) {

            catname = bundle.getString("catname");
            catid = bundle.getInt("catid");
            id = bundle.getInt("itemid");
            btncategory.setText(catname);
            requestitem(id);
            btnSubmit.setText("ویرایش اطلاعات");
            btncategory.setEnabled(false);
            btncategory.setTextColor(R.color.grey_90);

        }


        btnSubmit.setText("ثبت اطلاعات");


        btncategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Dialog_Category(ActivityEdit.this,btncategory,selectedCatid).setListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {

                    }
                }).show();
//
            }
        });

        img_delete_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgselect1.setImageResource(R.drawable.noimage);
                img_delete_logo.setVisibility(View.GONE);
//                editor.remove(spImage);
                editor.apply();
            }
        });
        findViewById(R.id.imgback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        imgselect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageFrom();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllEdts();
                if (validate()) {
                    if (OnlineCheck.isConnect(G.Context)) {
                        Log.i("test", "btnclicked");
//                    insert
//                        if (userId > 0) {
                        uploadtoserver(id,catid,20,2 );
//                        }
//                        else {
//                            Intent intent = new Intent(G.Context, LoginActivity.class);
//                            intent.putExtra("fr_insert_ac", 1);
//                            startActivity(intent);
//                            finish();
//                        }
                    }
                } else {
                    Toast.makeText(G.Context, "دستگاه به اینترنت متصل نیست", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void requestitem(final int id) {
        new Post().getOneItem(id, new AnswerPosts() {
            @Override
            public void AnswerBase(ArrayList<JobItemsList> answer) {
                if (answer!=null) {
                    Toast.makeText(ActivityEdit.this, "not null", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < answer.size(); i++) {
                        image= answer.get(0).image;

                        name=answer.get(0).name;
                        Toast.makeText(ActivityEdit.this, name, Toast.LENGTH_SHORT).show();
                        tamas=answer.get(0).tamas;
                        tozihat=answer.get(0).tozihat;

                        setDataToTxts();
//                        linearwait.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void SendError(Throwable t) {
                onFailRequest();
            }
        });

    }


    private void onFailRequest() {
//        linearwait.setVisibility(View.GONE);

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
                showFailedView(false,"");
//                linearwait.setVisibility(View.VISIBLE);
                requestitem(id);
            }
        });
    }
    private void setDataToTxts() {
        edttitle.setText(name);
        edtmobile.setText(tamas);
        edttozihat.setText(tozihat);
        }


    private void clearInputs() {
        edttitle.setText("");

        edtaddress.setText("");
        edtmobile.setText("");
        imgselect1.setImageResource(R.drawable.noimage);
        img_delete_logo.setVisibility(View.GONE);
        btncategory.setText("دسته بندی");
        catid = 0;



    }

    private void getAllEdts() {
        name = edttitle.getText().toString();
        userId = sharedPreferences.getInt("userId", 0);
        tamas= edtmobile.getText().toString();
        tozihat = edttozihat.getText().toString();
//        imglogo = sharedPreferences.getString(spImage, "");
//        catname= sharedPreferences.getString(spcatname, "");
//        catid= sharedPreferences.getInt(spcatid,0);


    }

    private boolean validate() {
        Boolean valid = true;

        if (name.length() < 10) {

            edttitle.setError("عنوان نمیتواند کمتر از 10 کاراکتر باشد");
            valid = false;
        } else {

            valid = true;
        }
        if (tamas.length() < 11 || tamas.length() > 11) {

            edtmobile.setError("شماره موبایل را به درستی وارد نمایید");
            valid = false;

        } else {
            valid = true;
        }
        if (tozihat.length() < 5) {

            edttozihat.setError("لطفا حداقل 15 حرف وارد کنید");
            valid = false;
        } else {
            valid = true;
        }


//        if (catid == 0) {
//
//            catwraper.setBackgroundResource(R.color.colorHint);
//            txthintcat.setVisibility(View.VISIBLE);
//            catwraper.setFocusableInTouchMode(true);
//            catwraper.requestFocus();
//            valid = false;
//        } else {
//            txthintcat.setVisibility(View.GONE);
////                    catwraper.setBackgroundResource(R.color.);
//            catwraper.setFocusable(false);
//        }
        return valid;
    }

    private void selectImageFrom() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.alertdialogbox);
        linearcam = dialog.findViewById(R.id.linearcam);
        linearcam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (checkPermission()) {
                    ClickImageFromCamera();
                }
            }
        });
        lineargal = dialog.findViewById(R.id.lineargal);
        lineargal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                GetImageFromGallery();
            }
        });
        dialog.show();
    }

    public boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {

            if (ActivityCompat.checkSelfPermission(ActivityEdit.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, Manifest.permission.CAMERA)) {
                    final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                    alertBuilder.setCancelable(false);
                    alertBuilder.setTitle("مجوز دسترسی");
                    alertBuilder.setMessage("برای اضافه کردن تصویر باید اجازه دسترسی به فایل ها را بدهید");
                    alertBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        //                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(ActivityEdit.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions(ActivityEdit.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ClickImageFromCamera();
                } else {
                    Toast.makeText(G.Context, "اجازه دسترسی داده نشد!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void ClickImageFromCamera() {

        CamIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(Environment.getExternalStorageDirectory(),
                "file" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        uri = Uri.fromFile(file);
        CamIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
        CamIntent.putExtra("return-data", true);
        startActivityForResult(CamIntent, 0);

    }

    public void GetImageFromGallery() {
        GalIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(GalIntent, "Select Image From Gallery"), 2);
    }

    public void ImageCropFunction() {
        try {
            CropIntent = new Intent("com.android.camera.action.CROP");
            CropIntent.setDataAndType(uri, "image/*");
            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 250);
            CropIntent.putExtra("outputY", 250);
            CropIntent.putExtra("aspectX", 4);
            CropIntent.putExtra("aspectY", 4);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);
            startActivityForResult(CropIntent, 1);
        } catch (ActivityNotFoundException e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            ImageCropFunction();
        } else if (requestCode == 2) {
            if (data != null) {
                uri = data.getData();
                ImageCropFunction();
            }
        } else if (requestCode == 1) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                bitmap = bundle.getParcelable("data");
                imgselect1.setImageBitmap(bitmap);

                img_delete_logo.setVisibility(View.VISIBLE);
//                editor.putString(spImage, imgToString());
                editor.commit();
            }
        }
    }

    private String imgToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgbyte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgbyte, Base64.DEFAULT);
    }


    private void uploadtoserver(int id,final int catid , int userId, int code) {
//                G.show_P_Dialog(this, false, false);
        new Post().uploadToServer(id,code,userId,citycode,  catid,name, image, tamas,"address",tozihat, new UploadPosts() {
            @Override
            public void AnswerBase(ItemsListUpload answer) {
                if (answer.response.equals("Updated_Suscessfully")) {
                    Toast.makeText(G.Context, "تغییرات با موفقیت ثبت گردید", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    Toast.makeText(G.Context, "ثبت تغییرات با مشکل مواجه گردید", Toast.LENGTH_SHORT).show();
                }
//                                G.dismiss_P_Dialog();
            }
            @Override
            public void SendError(Throwable t) {
//                                G.dismiss_P_Dialog();
                Toast.makeText(G.Context, "ارتباط با سرور با مشکل مواجه گردید", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //    in textwatcher we only save to sharepreferences
    private void deleteEdittexts() {
        edttitle.setText("");
        edtmobile.setText("");
        edtaddress.setText("");
        edttozihat.setText("");
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("name", "onPause");

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        deleteEdittexts();    }
}





