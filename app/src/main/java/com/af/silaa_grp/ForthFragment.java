package com.af.silaa_grp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.af.silaa_grp.CustomControl.CustomButton;
import com.af.silaa_grp.Retrofit.Api;
import com.af.silaa_grp.Retrofit.ItemsListUpload;
import com.af.silaa_grp.Retrofit.ServiceGenerator;
import com.soundcloud.android.crop.Crop;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class ForthFragment extends Fragment {
    String catname = "";
    int catid = 0;
    int userId = 0;
    int id=0;
    private static ActivityInsert.CropConfig config = new ActivityInsert.CropConfig();
    RequestBody R_title=null,R_city_code=null,R_userid=null,R_mobile=null,R_code=null,R_address=null,R_catid=null,R_tozih=null;

    RequestBody requestBody1 = null, requestBody2 = null, requestBody3 = null;
    MultipartBody.Part fileToUpload1=null,fileToUpload2=null,fileToUpload3=null,fileToUpload4=null,fileToUpload5=null;

    ImageView imgselect1,imgselect2,imgselect3,imgselect, img_delete_logo, img_delete_logo2, img_delete_logo3;
    CustomButton btncategory,btncam,btngal;
    TextInputEditText edtmobile, edtaddress, edttozihat, edttitle,edtcat;
    Bitmap bitmap = null;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 123;
    LinearLayout linearcam, catwraper,lineargal;
    String comp_img_path;
    private static Uri uri_1;

    String mediaPath = "", mediaPath1 = "", path = "";

    int citycode=1;
    String title, mobile, address, imglogo,imglogo2,imglogo3, tozih;
    File photoFile = null;
    Uri uri = null, picUri = null;
    private String imageFilePath = "";
    File file1 = null, file2 = null, file3 = null;


//    Dialog show_P_Dialog;

    public static int currentAndroidDeviceVersion = Build.VERSION.SDK_INT;

    File file=null;
    Intent CamIntent, GalIntent, CropIntent, intent;
    public static final int RequestPermissionCode = 1;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static final String sptitle = "SPTITLE";
    public static final String spName= "SPNAME";
    public static final String spcatid = "SPCATID";
    public static final String spcatname = "SPCATNAME";
    public static final String spAddress = "spaddress";
    public static final String spTozihat = "sptozihat";
    public static final String spmobile = "spmobile";
    public static final String spImage = "spImage";
    public static final String spImage2 = "spImage2";
    public static final String spImage3 = "spImage3";

    Bundle bundle = null;
    Button btnSubmit;
    public static int selectedCatid=0;

    RelativeLayout linearimg1, linearimg2, linearimg3, linearimgselect;


    public static ForthFragment newInstance() {
        ForthFragment fragment = new ForthFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_forth, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(G.Context);
        editor = sharedPreferences.edit();
        bundle = getActivity().getIntent().getExtras();

        edttozihat = view.findViewById(R.id.edttozih);
        edttitle = view.findViewById(R.id.edttitle);
        btncategory = view.findViewById(R.id.btncategory);
        edtmobile = view.findViewById(R.id.edtmobile);
        edtaddress = view.findViewById(R.id.edtaddress);
        imgselect1 = view.findViewById(R.id.imgselect1);
        imgselect= view.findViewById(R.id.imgselect);
        imgselect2 = view.findViewById(R.id.imgselect2);
        imgselect3 = view.findViewById(R.id.imgselect3);
        img_delete_logo = view.findViewById(R.id.del_img_logo);
        img_delete_logo2 = view.findViewById(R.id.del_img_logo2);
        img_delete_logo3 = view.findViewById(R.id.del_img_logo3);
        linearimg1 = view.findViewById(R.id.linearimg1);
        linearimg2 = view.findViewById(R.id.linearimg2);
        linearimg3 = view.findViewById(R.id.linearimg3);
        linearimgselect = view.findViewById(R.id.linearimgselect);


        btnSubmit = view.findViewById(R.id.btnsubmit);

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
        if (bundle != null) {

            if (bundle.getString("catname") != null) {
                catname = bundle.getString("catname");
                catid = bundle.getInt("catid");

                editor.putString(spcatname, catname);
                editor.putInt(spcatid, catid);
                editor.apply();
//                Toast.makeText(this, "not null", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(this, "null 666", Toast.LENGTH_SHORT).show();
            }

        }



        setSharedPdata();
        txtChangeLinstener();
        btnSubmit.setText("ثبت اطلاعات");


//        btncategory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new Dialog_Category(getActivity(),btncategory,selectedCatid).setListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialogInterface) {
//
//                    }
//                }).show();
////
//            }
//        });

        img_delete_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearimg1.setVisibility(View.GONE);
                editor.remove(spImage);
                editor.apply();
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
                editor.remove(spImage2);
                editor.apply();
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
                editor.remove(spImage3);
                editor.apply();
                visibilityImgSelect();
                imglogo3 = "";

                file3 = null;
                requestBody3 = null;
                fileToUpload3 = null;


            }
        });
        view.findViewById(R.id.imgback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onBackPressed();
            }
        });

        imgselect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageFrom();
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
                        int userId = sharedPreferences.getInt("userId", 0);

                        //                    insert
//                        if (userId > 0) {
                        uploadtoserver(id,catid,userId,1,citycode );

//                        }
//                        else {
//                            Intent intent = new Intent(G.Context, Activity_register.class);
//                            startActivity(intent);
//                            finish();
//                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "دستگاه به اینترنت متصل نیست", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return  view;
    }

    private void txtChangeLinstener() {

        edttitle.addTextChangedListener(textWatcher);
        edtaddress.addTextChangedListener(textWatcher);
        edtmobile.addTextChangedListener(textWatcher);
        edttozihat.addTextChangedListener(textWatcher);
//        edtcat.addTextChangedListener(textWatcher);

    }

    private void setSharedPdata() {
        if (sharedPreferences.contains(sptitle)) {
            edttitle.setText(sharedPreferences.getString(sptitle, ""));
        }
        if (sharedPreferences.contains(spImage)) {
            String photo = sharedPreferences.getString(spImage, "");
            //image path to bitmap
            Bitmap bitmap = BitmapFactory.decodeFile(photo);
            Glide.with(this).load(bitmap)
                    .into(imgselect1);
            linearimg1.setVisibility(View.VISIBLE);

        }
        if (sharedPreferences.contains(spImage2)) {
            String photo = sharedPreferences.getString(spImage2, "");
            Bitmap bitmap = BitmapFactory.decodeFile(photo);
            Glide.with(this).load(bitmap)
                    .into(imgselect2);
            linearimg2.setVisibility(View.VISIBLE);
        }
        if (sharedPreferences.contains(spImage3)) {
            String photo = sharedPreferences.getString(spImage3, "");
            Bitmap bitmap = BitmapFactory.decodeFile(photo);
            Glide.with(this).load(bitmap)
                    .into(imgselect3);
            linearimg3.setVisibility(View.VISIBLE);
        }
        checksixthimg();
        if (sharedPreferences.contains(spmobile)) {
            edtmobile.setText(sharedPreferences.getString(spmobile, ""));
        }
        if (sharedPreferences.contains(spTozihat)) {
            edttozihat.setText(sharedPreferences.getString(spTozihat, ""));
        }
        if (sharedPreferences.contains(spAddress)) {
            edtaddress.setText(sharedPreferences.getString(spAddress, ""));
        }

        if (sharedPreferences.contains(spcatname)) {
            btncategory.setText(sharedPreferences.getString(spcatname, ""));
            catid = sharedPreferences.getInt(spcatid, 1);

        }

    }

    private void visibilityImgSelect() {
        if (linearimgselect.getVisibility() == View.GONE) {
            linearimgselect.setVisibility(View.VISIBLE);
        }
    }
    private void clearInputs() {
        edttitle.setText("");

        edtaddress.setText("");
        edtmobile.setText("");
        imgselect1.setImageResource(R.drawable.image_empty);
        img_delete_logo.setVisibility(View.GONE);
        btncategory.setText("دسته بندی");
        catid = 0;


        editor.remove(sptitle);
        editor.remove(spcatid);
        editor.remove(spcatname);
        editor.remove(spAddress);
        editor.remove(spImage);
        editor.remove(spmobile);
        editor.apply();
    }

    private void getAllEdts() {
        title = edttitle.getText().toString();
        address = edtaddress.getText().toString();
        userId = sharedPreferences.getInt("userId", 0);
        mobile = edtmobile.getText().toString();
        tozih = edttozihat.getText().toString();
        catname= sharedPreferences.getString(spcatname, "");
        catid= sharedPreferences.getInt(spcatid,0);

        imglogo = sharedPreferences.getString(spImage, "");
        imglogo2 = sharedPreferences.getString(spImage2, "");
        imglogo3 = sharedPreferences.getString(spImage3, "");

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
        if (address.length() < 15) {

            edtaddress.setError("لطفا حداقل 15 حرف وارد کنید");
            valid = false;
        } else {
            valid = true;
        }

        return valid;
    }




    private void displayMessage(Context baseContext, String s) {
        Toast.makeText(baseContext, s, Toast.LENGTH_LONG).show();
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getActivity(), Manifest.permission.CAMERA)) {
                    final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                    alertBuilder.setCancelable(false);
                    alertBuilder.setTitle("مجوز دسترسی");
                    alertBuilder.setMessage("برای اضافه کردن تصویر باید اجازه دسترسی به فایل ها را بدهید");
                    alertBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        //                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
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
                    ClickImageFromCamera(getActivity(),null);
                } else {
                    Toast.makeText(G.Context, "اجازه دسترسی داده نشد!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    public void GetImageFromGallery() {
        GalIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(GalIntent, "Select Image From Gallery"), 1);
    }

    private void selectImageFrom() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.alertdialogbox);

        btncam = dialog.findViewById(R.id.btncam);
        btncam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (checkPermission()) {
                    if (currentAndroidDeviceVersion >= 24) {
                        selectImageFromCamera(getActivity(), null);
                    } else {
                        ClickImageFromCamera(getActivity(), null);
                    }
                }
            }
        });
        btngal = dialog.findViewById(R.id.btngal);
        btngal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Crop.pickImage(getActivity());
            }
        });
        dialog.show();
    }

    //camera code for android less 24
    public void ClickImageFromCamera(Context context, CropConfig config) {
        if (config != null) {
            config = config;
        } else {
            config = new CropConfig();
        }
        CamIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(Environment.getExternalStorageDirectory(),"file" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        picUri = Uri.fromFile(file);
        CamIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, picUri);
        CamIntent.putExtra("return-data", true);
        startActivityForResult(CamIntent, 1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {

            crop(picUri);
        } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {

            Uri uri = UCrop.getOutput(data);
            if (uri != null) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    mediaPath = Tools.getPath(getActivity(), uri);
                    Random r = new Random();
                    int randnumb = r.nextInt(100 - 1) + 3;
                    comp_img_path = Tools.resizeAndCompressImageBeforeSend(getActivity(), mediaPath, randnumb + ".jpg");
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
            Toast.makeText(getActivity(), "img1", Toast.LENGTH_SHORT).show();
            linearimg1.setVisibility(View.VISIBLE);
            Glide.with(this).load(bitmap)
                    .into(imgselect1);
            img_delete_logo.setVisibility(View.VISIBLE);
            editor.putString(spImage, comp_img_path);
            editor.commit();
            checksixthimg();
        }

        else if (linearimg2.getVisibility() == View.GONE) {

            Toast.makeText(getActivity(), "img2", Toast.LENGTH_SHORT).show();
            linearimg2.setVisibility(View.VISIBLE);
            Glide.with(this).load(bitmap)
                    .into(imgselect2);
            img_delete_logo2.setVisibility(View.VISIBLE);
            editor.putString(spImage2, comp_img_path);
            editor.commit();
            checksixthimg();


        } else if (linearimg3.getVisibility() == View.GONE) {
            Toast.makeText(getActivity(), "img3", Toast.LENGTH_SHORT).show();
            linearimg3.setVisibility(View.VISIBLE);
            Glide.with(this).load(bitmap)
                    .into(imgselect3);
            img_delete_logo3.setVisibility(View.VISIBLE);
            editor.putString(spImage3, comp_img_path);
            editor.commit();
            checksixthimg();


        }

    }

    private void checksixthimg() {
        if (linearimg1.getVisibility() == View.VISIBLE && linearimg2.getVisibility() == View.VISIBLE && linearimg3.getVisibility() == View.VISIBLE ) {

            Toast.makeText(getActivity(), "third gon", Toast.LENGTH_SHORT).show();
            linearimgselect.setVisibility(View.GONE);
        }
    }
    @SuppressLint("NewApi")
    private String imgToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgbyte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgbyte, Base64.DEFAULT);
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
        uCrop.start(getActivity());
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

    private void uploadtoserver(int id, final int catid , final int userid, final int code, int citycode) {
//        G.show_P_Dialog(ActivityInsert.this, false, false);

//        fileToUpload1 = MultipartBody.Part.createFormData("file1", file1.getName(), requestBody1);

        if (!imglogo.equals("")) {
            file1 = new File(imglogo);
            requestBody1 = RequestBody.create(MediaType.parse("*/*"), file1);

            fileToUpload1 = MultipartBody.Part.createFormData("file1", file1.getName(), requestBody1);
            Log.i("tabligharray 11", String.valueOf(requestBody1));
        }
        if (!imglogo2.equals("")) {
            Log.i("tabligharray 12", imglogo2);

            file2 = new File(imglogo2);
            requestBody2 = RequestBody.create(MediaType.parse("*/*"), file2);
            fileToUpload2 = MultipartBody.Part.createFormData("file2", file2.getName(), requestBody2);
        } else {
            Log.i("tabligharray 2", imglogo2);
        }
        if (!imglogo3.equals("")) {
            Log.i("tabligharray 13", imglogo3);

            file3 = new File(imglogo3);
            requestBody3 = RequestBody.create(MediaType.parse("*/*"), file3);
            fileToUpload3 = MultipartBody.Part.createFormData("file3", file3.getName(), requestBody3);
        } else {
            Log.i("tabligharray 3", imglogo3);
        }
        R_userid = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(20));
        R_catid= RequestBody.create(MediaType.parse("text/plain"), String.valueOf(catid));
        R_city_code= RequestBody.create(MediaType.parse("text/plain"), String.valueOf(citycode));
        R_code = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(code));
        R_title= RequestBody.create(MediaType.parse("text/plain"), title);
        R_mobile = RequestBody.create(MediaType.parse("text/plain"), mobile);
        R_tozih= RequestBody.create(MediaType.parse("text/plain"), tozih);
        R_address= RequestBody.create(MediaType.parse("text/plain"), address);

        Api getResponse = ServiceGenerator.getClient().create(Api.class);
        Call call = getResponse.uploadfile(R_userid,R_catid, R_city_code, R_code,  R_title,R_mobile, R_address, R_tozih, fileToUpload1, fileToUpload2, fileToUpload3);
        call.enqueue(new Callback<ItemsListUpload>() {
            @Override
            public void onResponse(Call<ItemsListUpload> call, Response<ItemsListUpload> response) {
                ItemsListUpload answer=response.body();
                Toast.makeText(getActivity(),answer.getResponse() , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<ItemsListUpload> call, @NonNull Throwable t) {
//                G.dismiss_P_Dialog();
                Toast.makeText(G.Context, "اتصال دستگاه به سرور با مشکل مواجه گردید", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //    in textwatcher we only save to sharepreferences

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable != null) {
                if (edttitle.getText().hashCode() == editable.hashCode()) {
                    String value = editable.toString();
                    editor.putString(sptitle, value);
                    editor.commit();
                } else if (edtaddress.getText().hashCode() == editable.hashCode()) {
                    String value = editable.toString();
                    editor.putString(spAddress, value);
                    editor.commit();
                } else if (edtmobile.getText().hashCode() == editable.hashCode()) {
                    String value = editable.toString();
                    editor.putString(spmobile, value);
                    editor.commit();
                } else if (edttozihat.getText().hashCode() == editable.hashCode()) {
                    String value = editable.toString();
                    editor.putString(spTozihat, value);
                    editor.commit();
                }


            }
        }


    };


    @Override
    public void onStart() {
        super.onStart();
        Log.i("name", "onstart");
        if (bundle == null) {

            if (sharedPreferences.contains(spcatname) && sharedPreferences.contains(spcatid)) {
                btncategory.setText(sharedPreferences.getString(spcatname, ""));
                catid = sharedPreferences.getInt(spcatid, 1);
            }
        }
    }
    private void selectImageFromCamera(Activity context, CropConfig config) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            if (config != null) {
                config = config;
            } else {
                config = new CropConfig();
            }
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go
                try {
                    photoFile = createImageFile();
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        picUri = FileProvider.getUriForFile(getActivity(), "com.cilla_project.gozar.provider", photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                        startActivityForResult(takePictureIntent, 1);
                    }
                } catch (Exception ex) {
                    // Error occurred while creating the File
                    displayMessage(getActivity().getBaseContext(), ex.getMessage().toString());
                }
            } else {
                displayMessage(getActivity().getBaseContext(), "مشکل در اتصال برنامه به دوربین!");
            }
        }
    }

    public static class CropConfig {
        public int aspectRatioX = 1;
        public int aspectRatioY = 1;
        public int maxWidth = 800;
        public int maxHeight = 600;

        //options
        public int tag;
        public boolean isOval = false;
        public int quality = 100;

        public boolean hideBottomControls = true;
        public boolean showGridLine = false;
        public boolean showOutLine = true;

        public @ColorInt
        int toolbarColor = ContextCompat.getColor(G.Context, R.color.colorToolbar);
        public @ColorInt int statusBarColor = ContextCompat.getColor(G.Context, R.color.colorPrimaryDark);

    }





    }




