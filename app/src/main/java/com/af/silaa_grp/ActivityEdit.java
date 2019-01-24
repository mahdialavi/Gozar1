package com.af.silaa_grp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.af.silaa_grp.CustomControl.CustomButton;
import com.af.silaa_grp.Retrofit.AnswerPosts;
import com.af.silaa_grp.Retrofit.Api;
import com.af.silaa_grp.Retrofit.ItemsListUpload;
import com.af.silaa_grp.Retrofit.Post;
import com.af.silaa_grp.Retrofit.ServiceGenerator;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityEdit extends ActivityEnhanced{

    private static ActivityInsert.CropConfig config = new ActivityInsert.CropConfig();

    String title, mobile, address, imglogo, imglogo2, imglogo3, tozihat;
    String catname = "";
    int catid = 0;
    int userId = 0;
    public static int currentAndroidDeviceVersion = Build.VERSION.SDK_INT;
    File photoFile = null;
    Uri uri = null, picUri = null;
    private String imageFilePath = "";
    String comp_img_path;

    private static Uri uri_1;

    ImageView imgselect1,imgselect2,imgselect3, img_delete_logo, img_delete_logo2, img_delete_logo3,imgselect;
    CustomButton btncategory,btncam, btngal;
    TextInputEditText edtmobile, edtaddress, edttozihat, edttitle,edtcat;
    Bitmap bitmap = null;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 123;
    LinearLayout linearcam, catwraper,lineargal,linearhide;
    int citycode=1;

    RelativeLayout linearimg1, linearimg2, linearimg3, linearimg4, linearimg5, linearimgselect;
    File file1 = null, file2 = null, file3 = null, file4 = null, file5 = null;
//    Dialog show_P_Dialog;
String mediaPath = "", mediaPath1 = "", path = "";


    File file;
    Intent CamIntent, GalIntent, CropIntent, intent;
    public static final int RequestPermissionCode = 1;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    MultipartBody.Part fileToUpload1 = null, fileToUpload2 = null, fileToUpload3 = null;

    Bundle bundle = null;
    Button btnSubmit;
    int id=0;
    public static int selectedCatid=0;


    RequestBody requestBody1 = null, requestBody2 = null, requestBody3 = null;
    RequestBody R_title = null, R_city_code = null, R_userid = null, R_mobile = null, R_code = null, R_address = null, R_catid = null, R_tozih = null;

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
        imgselect = findViewById(R.id.imgselect);
        imgselect2 = findViewById(R.id.imgselect2);
        imgselect3= findViewById(R.id.imgselect3);
        img_delete_logo = findViewById(R.id.del_img_logo);
        btnSubmit = findViewById(R.id.btnsubmit);

        img_delete_logo = findViewById(R.id.del_img_logo);
        img_delete_logo2 = findViewById(R.id.del_img_logo2);
        img_delete_logo3 = findViewById(R.id.del_img_logo3);
        linearimg1= findViewById(R.id.linearimg1);
        linearimg2= findViewById(R.id.linearimg2);
        linearimg3= findViewById(R.id.linearimg3);
        linearimgselect = findViewById(R.id.linearimgselect);
        linearhide= findViewById(R.id.linearhide);
        linearhide.setVisibility(View.VISIBLE);

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
                new Dialog_Category(ActivityEdit.this,selectedCatid,btncategory).setListener(new DialogInterface.OnDismissListener() {
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
                linearimg1.setVisibility(View.GONE);
                bitmap = null;
                imglogo = "";
                file1 = null;
                requestBody1 = null;
                fileToUpload1 = null;
                visibilityImgSelect();
            }
        });
        img_delete_logo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearimg2.setVisibility(View.GONE);
                imglogo2 = "";
                file2 = null;
                requestBody2 = null;
                fileToUpload2 = null;
                visibilityImgSelect();

            }
        });
        img_delete_logo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearimg3.setVisibility(View.GONE);
                visibilityImgSelect();
                imglogo3 = "";

                file3 = null;
                requestBody3 = null;
                fileToUpload3 = null;


            }
        });

        imgselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageFrom();
            }
        });



        findViewById(R.id.imgback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        imgselect.setOnClickListener(new View.OnClickListener() {
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

                        uploadtoserver(id, catid, userId, 0, citycode);
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

    private void visibilityImgSelect() {
        if (linearimgselect.getVisibility() == View.GONE) {
            linearimgselect.setVisibility(View.VISIBLE);
        }
    }
    private void requestitem(final int id) {
        new Post().getOneItem(id, new AnswerPosts() {
            @Override
            public void AnswerBase(ArrayList<JobItemsList> answer) {
                if (answer!=null) {
                    for (int i = 0; i < answer.size(); i++) {
                        imglogo= answer.get(0).image;
                        imglogo2= answer.get(0).image2;
                        imglogo3= answer.get(0).image3;

                        title=answer.get(0).name;
                        mobile=answer.get(0).tamas;
                        tozihat=answer.get(0).tozihat;
                        address=answer.get(0).address;

                        setDataToTxts();
                        showOrHideImageViews();
                        linearhide.setVisibility(View.GONE);


                    }
                }
            }

            @Override
            public void SendError(Throwable t) {
                onFailRequest();
            }
        });

    }

    public File bitmap_to_file(Bitmap bitmap, String name) {
        File filesDir = G.Context.getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");
        FileOutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, os);
            os.flush();
            os.close();
        } catch (Exception e) {
        }
        return imageFile;
    }

    com.squareup.picasso.Target target1 = new com.squareup.picasso.Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            imgselect1.setImageBitmap(bitmap);
            file1 = bitmap_to_file(bitmap, "img_1");
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            imgselect1.setImageResource(R.drawable.image_empty);

        }
    };
    private com.squareup.picasso.Target target3 = new com.squareup.picasso.Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            imgselect3.setImageBitmap(bitmap);
            file3 = bitmap_to_file(bitmap, "img_3");

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            imgselect3.setImageResource(R.drawable.image_empty);

        }
    };
    private com.squareup.picasso.Target target2 = new com.squareup.picasso.Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            imgselect2.setImageBitmap(bitmap);
            file2 = bitmap_to_file(bitmap, "img_2");

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            imgselect2.setImageResource(R.drawable.image_empty);

        }
    };
    private void showOrHideImageViews() {
        if (!imglogo.equals("")) {
            linearimg1.setVisibility(View.VISIBLE);
            Picasso.with(this).load(G.IMAGE_URL + imglogo).placeholder(R.drawable.image_empty).into(target1);
            img_delete_logo.setVisibility(View.VISIBLE);


        } else {
            linearimg1.setVisibility(View.GONE);
            imgselect1.setImageResource(R.drawable.image_empty);
            img_delete_logo.setVisibility(View.GONE);
        }

            if (imglogo2!=null&& !imglogo2.equals("")) {
                linearimg2.setVisibility(View.VISIBLE);
                Picasso.with(this).load(G.IMAGE_URL + imglogo2).placeholder(R.drawable.image_empty).into(target2);

                img_delete_logo3.setVisibility(View.VISIBLE);
            } else {
                imgselect2.setImageResource(R.drawable.image_empty);
                img_delete_logo2.setVisibility(View.GONE);
            }


            if (!imglogo3.equals("")) {

                linearimg3.setVisibility(View.VISIBLE);
                Picasso.with(this).load(G.IMAGE_URL + imglogo3).placeholder(R.drawable.image_empty).into(target3);

                img_delete_logo3.setVisibility(View.VISIBLE);
            } else {
                imgselect3.setImageResource(R.drawable.image_empty);
                img_delete_logo3.setVisibility(View.GONE);
            }

    }

    private void onFailRequest() {
        linearhide.setVisibility(View.GONE);

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
                linearhide.setVisibility(View.VISIBLE);
                requestitem(id);
            }
        });
    }
    private void setDataToTxts() {
        edttitle.setText(title);
        edtmobile.setText(mobile);
        edttozihat.setText(tozihat);
        edtaddress.setText(address);
        }


    private void clearInputs() {
        edttitle.setText("");

        edtaddress.setText("");
        edtmobile.setText("");
        imgselect1.setImageResource(R.drawable.image_empty);
        img_delete_logo.setVisibility(View.GONE);
        btncategory.setText("دسته بندی");
        catid = 0;



    }

    private void getAllEdts() {
        title = edttitle.getText().toString();
        userId = sharedPreferences.getInt("userId", 0);
        mobile= edtmobile.getText().toString();
        tozihat = edttozihat.getText().toString();

        address = edtaddress.getText().toString();
        userId = sharedPreferences.getInt("userId", 0);



    }

    private boolean validate() {
        Boolean valid = true;

        if (title.length() < 10) {

            edttitle.setError("عنوان نمیتواند کمتر از 10 کاراکتر باشد");
            valid = false;
        } else {

            valid = true;
        }
        if (mobile.length() < 11 || mobile.length() > 11) {

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

        btncam = dialog.findViewById(R.id.btncam);
        btncam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (checkPermission()) {
                    if (currentAndroidDeviceVersion >= 24) {
                        selectImageFromCamera(ActivityEdit.this, null);
                    } else {
                        ClickImageFromCamera(ActivityEdit.this, null);
                    }
                }
            }
        });
        btngal = dialog.findViewById(R.id.btngal);
        btngal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Crop.pickImage(ActivityEdit.this);
            }
        });
        dialog.show();
    }

    private void selectImageFromCamera(Activity context, ActivityInsert.CropConfig config) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            if (config != null) {
                config = config;
            } else {
                config = new ActivityInsert.CropConfig();
            }
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                try {
                    photoFile = createImageFile();
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        picUri = FileProvider.getUriForFile(this, "com.cilla_project.gozar.provider", photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                        startActivityForResult(takePictureIntent, 1);
                    }
                } catch (Exception ex) {
                    // Error occurred while creating the File
                    displayMessage(getBaseContext(), ex.getMessage().toString());
                }
            } else {
                displayMessage(getBaseContext(), "مشکل در اتصال برنامه به دوربین!");
            }
        }
    }
    private void displayMessage(Context baseContext, String s) {
        Toast.makeText(baseContext, s, Toast.LENGTH_LONG).show();
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        imageFilePath = image.getAbsolutePath();
        return image;
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
                    ClickImageFromCamera(this, null);
                } else {
                    Toast.makeText(G.Context, "اجازه دسترسی داده نشد!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //camera code for android less 24
    public void ClickImageFromCamera(Context context, ActivityInsert.CropConfig config) {
        if (config != null) {
            config = config;
        } else {
            config = new ActivityInsert.CropConfig();
        }
        CamIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(Environment.getExternalStorageDirectory(), "file" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        picUri = Uri.fromFile(file);
        CamIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, picUri);
        CamIntent.putExtra("return-data", true);
        startActivityForResult(CamIntent, 1);
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

        if (requestCode == 1 && resultCode == RESULT_OK) {

            crop(picUri);
        } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {

            Uri uri = UCrop.getOutput(data);
            if (uri != null) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    mediaPath = Tools.getPath(this, uri);
                    Random r = new Random();
                    int randnumb = r.nextInt(100 - 1) + 3;
                    comp_img_path = Tools.resizeAndCompressImageBeforeSend(this, mediaPath, randnumb + ".jpg");
                    setImagebitmap(comp_img_path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        } //crop with library for gallery
        else if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            crop(data.getData());

        }

    }
    private void setImagebitmap(String comp_img_path) {

        if (linearimg1.getVisibility() == View.GONE) {
            Toast.makeText(this, "img1", Toast.LENGTH_SHORT).show();
            linearimg1.setVisibility(View.VISIBLE);
            Glide.with(this).load(bitmap)
                    .into(imgselect1);
            img_delete_logo.setVisibility(View.VISIBLE);
            imglogo = comp_img_path;

            checksixthimg();
        } else if (linearimg2.getVisibility() == View.GONE) {

            Toast.makeText(this, "img2", Toast.LENGTH_SHORT).show();
            linearimg2.setVisibility(View.VISIBLE);
            Glide.with(this).load(bitmap)
                    .into(imgselect2);
            img_delete_logo2.setVisibility(View.VISIBLE);
            imglogo2 = comp_img_path;

            checksixthimg();


        } else if (linearimg3.getVisibility() == View.GONE) {
            Toast.makeText(this, "img3", Toast.LENGTH_SHORT).show();
            linearimg3.setVisibility(View.VISIBLE);
            Glide.with(this).load(bitmap)
                    .into(imgselect3);
            img_delete_logo3.setVisibility(View.VISIBLE);
            imglogo3 = comp_img_path;

            checksixthimg();


        }

    }

    private void checksixthimg() {
        if (linearimg1.getVisibility() == View.VISIBLE && linearimg2.getVisibility() == View.VISIBLE && linearimg3.getVisibility() == View.VISIBLE) {

            Toast.makeText(this, "third gon", Toast.LENGTH_SHORT).show();
            linearimgselect.setVisibility(View.GONE);
        }
    }

    public void crop(Uri picUri) {

        Uri mDestinationUri = buildUri();
        UCrop uCrop = UCrop.of(picUri, mDestinationUri);

        uCrop.withAspectRatio(config.aspectRatioX, config.aspectRatioY);
        uCrop.withMaxResultSize(config.maxWidth, config.maxHeight);

        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.NONE, UCropActivity.NONE);
        options.setCompressionQuality(config.quality);
        options.setToolbarTitle("");
        options.setToolbarCancelDrawable(R.drawable.cancel);
        options.setToolbarCropDrawable(R.drawable.yes);

//        options.setOvalDimmedLayer(config.isOval);
        options.setShowCropGrid(config.showGridLine);
        options.setHideBottomControls(config.hideBottomControls);
        options.setShowCropFrame(config.showOutLine);
        options.setActiveWidgetColor(config.toolbarColor);

        options.setToolbarColor(config.toolbarColor);
        options.setStatusBarColor(config.statusBarColor);
        uCrop.withOptions(options);
        uCrop.start(this);
    }
    private static Uri buildUri() {
        File cacheFolder = new File(Environment.getExternalStorageDirectory() + File.separator + "crop");
        if (!cacheFolder.exists()) {
            try {
                boolean result = cacheFolder.mkdir();
                Log.d("uri_1", "generateUri " + cacheFolder + " result: " + (result ? "succeeded" : "failed"));
            } catch (Exception e) {
                Log.e("uri_1", "generateUri failed: " + cacheFolder, e);
            }
        }
        String name = String.format("imagecrop-%d.jpg", System.currentTimeMillis());
        uri_1 = Uri
                .fromFile(cacheFolder)
                .buildUpon()
                .appendPath(name)
                .build();
        Log.e("crop", uri_1.toString());
        return uri_1;
    }
    private void uploadtoserver(int id, final int catid, int userid, int code, int citycode) {
        G.show_progress_dialog(ActivityEdit.this, false, false);

//        fileToUpload1 = MultipartBody.Part.createFormData("file1", file1.getName(), requestBody1);
        if (file1 != null) {
            requestBody1 = RequestBody.create(MediaType.parse("*/*"), file1);
            fileToUpload1 = MultipartBody.Part.createFormData("file1", file1.getName(), requestBody1);

            Log.i("log", "file1 nut null");
        } else {

            if (!imglogo.equals("")) {
                file1 = new File(imglogo);
                requestBody1 = RequestBody.create(MediaType.parse("*/*"), file1);
                fileToUpload1 = MultipartBody.Part.createFormData("file1", file1.getName(), requestBody1);
                Log.i("log", "img 1 nut null");

            }
        }
        if (file2 != null) {
            requestBody2 = RequestBody.create(MediaType.parse("*/*"), file2);
            fileToUpload2 = MultipartBody.Part.createFormData("file2", file2.getName(), requestBody2);
            Log.i("log", "file2 nut null");

        } else {

            if (!imglogo2.equals("")) {
                file2 = new File(imglogo2);
                requestBody2 = RequestBody.create(MediaType.parse("*/*"), file2);
                fileToUpload2 = MultipartBody.Part.createFormData("file2", file2.getName(), requestBody2);
                Log.i("log", "img 2 nut null");

            }
        }
        if (file3 != null) {
            requestBody3 = RequestBody.create(MediaType.parse("*/*"), file3);
            fileToUpload3 = MultipartBody.Part.createFormData("file3", file3.getName(), requestBody3);
            Log.i("log", "file3 nut null");

        } else {
            if (!imglogo3.equals("")) {
                file3 = new File(imglogo3);
                requestBody3 = RequestBody.create(MediaType.parse("*/*"), file3);
                fileToUpload3 = MultipartBody.Part.createFormData("file3", file3.getName(), requestBody3);
                Log.i("log", "img 3 nut null");

            }
        }
        R_catid = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(id));
        R_city_code = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(citycode));
        R_code = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(code));
        R_title = RequestBody.create(MediaType.parse("text/plain"), title);
        R_mobile = RequestBody.create(MediaType.parse("text/plain"), mobile);
        R_tozih = RequestBody.create(MediaType.parse("text/plain"), tozihat);
        R_address = RequestBody.create(MediaType.parse("text/plain"), address);

        Api getResponse = ServiceGenerator.getClient().create(Api.class);
        Call call = getResponse.uploadfile(R_userid, R_catid, R_city_code, R_code, R_title, R_mobile, R_tozih, R_address ,fileToUpload1, fileToUpload2, fileToUpload3);
        call.enqueue(new Callback<ItemsListUpload>() {
            @Override
            public void onResponse(Call<ItemsListUpload> call, Response<ItemsListUpload> response) {
                G.dismiss_P_Dialog();
                try {
                    ItemsListUpload answer = response.body();
                    if (answer != null) {
                        if (answer.response.equals("updated_ok")) {
                                Toast.makeText(G.Context, "تغییرات انجام گردید", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                        }else {
                            Toast.makeText(ActivityEdit.this, "مشکل در ثبت آگهی ", Toast.LENGTH_LONG).show();


                        }
                    }

                } catch (NullPointerException e) {
                    Log.i("log", "null");
                }


            }

            @Override
            public void onFailure(@NonNull Call<ItemsListUpload> call, @NonNull Throwable t) {
                G.dismiss_P_Dialog();
                Toast.makeText(G.Context, "اتصال دستگاه به سرور با مشکل مواجه گردید", Toast.LENGTH_SHORT).show();
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

    private RequestListener save_To_bitmap_1 = new RequestListener() {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {

            Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();

            Log.i("logg", "url image1 " + String.valueOf(getSizeInBytes(bitmap)));

            file1 = bitmap_to_file(bitmap, "img1");
            return false;
        }
    };
    private RequestListener save_To_bitmap_2 = new RequestListener() {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {

            Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
            Log.i("logg", "url image" + String.valueOf(getSizeInBytes(bitmap)));

            file2 = bitmap_to_file(bitmap, "img2");
            return false;
        }
    };
    private RequestListener save_To_bitmap_3 = new RequestListener() {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {

            Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
            Log.i("logg", "url image" + String.valueOf(getSizeInBytes(bitmap)));

            file3 = bitmap_to_file(bitmap, "img3");
            return false;
        }
    };

    @SuppressLint("NewApi")
    public static int getSizeInBytes(@Nullable Bitmap bitmap) {
        if (bitmap == null) {
            return 0;
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            try {
                return bitmap.getAllocationByteCount();
            } catch (NullPointerException npe) {
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }

        // Estimate for earlier platforms. Same code as getByteCount() for Honeycomb.
        return bitmap.getRowBytes() * bitmap.getHeight();
    }


}





