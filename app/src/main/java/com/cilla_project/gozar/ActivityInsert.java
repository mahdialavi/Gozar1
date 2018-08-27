package com.cilla_project.gozar;

import android.Manifest;
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
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cilla_project.gozar.CustomControl.CustomButton;
import com.cilla_project.gozar.Retrofit.ItemsListUpload;
import com.cilla_project.gozar.Retrofit.Post;
import com.cilla_project.gozar.Retrofit.UploadPosts;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityInsert extends ActivityEnhanced {
    String catname = "";
    int catid = 0;
    int userId = 0;
    int id=0;

    ImageView imgselect1, img_delete_logo;
    CustomButton btncategory;
    TextInputEditText edtmobile, edtaddress, edttozihat, edttitle,edtcat;
    Bitmap bitmap = null;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 123;
    LinearLayout linearcam, catwraper,lineargal;

    String title, mobile, address, imglogo, tozih;
    File photoFile = null;
    Uri uri = null, picUri = null;
    private String imageFilePath = "";

//    Dialog show_P_Dialog;

    public static int currentAndroidDeviceVersion = Build.VERSION.SDK_INT;

    File file;
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

    Bundle bundle = null;
    Button btnSubmit;
    public static int selectedCatid=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
//        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

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


        btncategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Dialog_Category(ActivityInsert.this,btncategory,selectedCatid).setListener(new DialogInterface.OnDismissListener() {
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
                editor.remove(spImage);
                editor.apply();
            }
        });
        findViewById(R.id.linearback).setOnClickListener(new View.OnClickListener() {
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
                        int userId = sharedPreferences.getInt("userId", 0);

                        //                    insert
                        if (userId > 0) {
                            uploadtoserver(id,catid,userId,1 );

                        }
                        else {
                            Intent intent = new Intent(G.Context, Activity_register.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                } else {
                    Toast.makeText(ActivityInsert.this, "دستگاه به اینترنت متصل نیست", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
            byte[] b = Base64.decode(photo, Base64.DEFAULT);
            InputStream is = new ByteArrayInputStream(b);
            bitmap = BitmapFactory.decodeStream(is);
            imgselect1.setImageBitmap(bitmap);
            //if sharedprefs has image delete image is visible
            img_delete_logo.setVisibility(View.VISIBLE);
            //            }
        } else {
            if (!sharedPreferences.contains(spImage)) {
                imgselect1.setImageResource(R.drawable.noimage);
            }

        }

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


    private void clearInputs() {
        edttitle.setText("");

        edtaddress.setText("");
        edtmobile.setText("");
        imgselect1.setImageResource(R.drawable.noimage);
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
        imglogo = sharedPreferences.getString(spImage, "");
        catname= sharedPreferences.getString(spcatname, "");
        catid= sharedPreferences.getInt(spcatid,0);
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

    private void selectImageFrom() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.alertdialogbox);
        linearcam = dialog.findViewById(R.id.linearcam);
        linearcam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (checkPermission()) {
                    if (currentAndroidDeviceVersion >= 24) {
                        captureImage();
                    } else {
                        ClickImageFromCamera();
                    }
                }
            }
        });
        lineargal = dialog.findViewById(R.id.lineargal);
        lineargal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Crop.pickImage(ActivityInsert.this);
            }
        });
        dialog.show();
    }

    private void captureImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
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
        }}

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

            if (ActivityCompat.checkSelfPermission(ActivityInsert.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, Manifest.permission.CAMERA)) {
                    final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                    alertBuilder.setCancelable(false);
                    alertBuilder.setTitle("مجوز دسترسی");
                    alertBuilder.setMessage("برای اضافه کردن تصویر باید اجازه دسترسی به فایل ها را بدهید");
                    alertBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        //                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(ActivityInsert.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions(ActivityInsert.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
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

        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (currentAndroidDeviceVersion >= 24) {
                crop(picUri);
            } else {
                ImageCropFunction();
            }
        } else if (requestCode == 5) {
            if (data != null) {
                Uri inputUri = FileProvider.getUriForFile(this, "com.cilla_project.gozar.provider", photoFile);
                bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(inputUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                imgselect1.setImageBitmap(bitmap);
                img_delete_logo.setVisibility(View.VISIBLE);
                editor.putString(spImage, imgToString());
                editor.commit();

            } } else if (requestCode == 6) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                bitmap = bundle.getParcelable("data");
                imgselect1.setImageBitmap(bitmap);
                img_delete_logo.setVisibility(View.VISIBLE);
                editor.putString(spImage, imgToString());
                editor.commit();
            }
        } else if (requestCode == 7) {
            if (data != null) {
                if (currentAndroidDeviceVersion >= 24) {
                    Uri contentURI = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                        imgselect1.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            } else {
                ImageCropFunction();
            }
        }//crop with library for gallery
        else if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {

            if (data != null) {
                uri=   Crop.getOutput(data);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    imgselect1.setImageBitmap(bitmap);

                    editor.putString(spImage, imgToString());
                    editor.commit();
                    img_delete_logo.setVisibility(View.VISIBLE);

                } catch (IOException e) {
                    e.printStackTrace();
                }
//            handleCrop(resultCode, data);
            }
        }else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(data).getMessage(), Toast.LENGTH_SHORT).show();

        }

    }
    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);

    }

    private String imgToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgbyte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgbyte, Base64.DEFAULT);
    }
    public void crop(Uri picUri) {
        (this).grantUriPermission("com.cilla_project.gozar", picUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(picUri, "image/*");
        //Android N need set permission to uri otherwise system camera don't has permission to access file wait crop
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 4);
        intent.putExtra("aspectY", 4);
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        //image type
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        //true - don't return uri |  false - return uri
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
        startActivityForResult(intent, 5);
    }

    private void uploadtoserver(int id,final int catid , int userId, int code) {
//                G.show_P_Dialog(this, false, false);
        new Post().uploadToServer(id,code,userId,  catid,title, imglogo, mobile,address,tozih, new UploadPosts() {
                    @Override
                    public void AnswerBase(ItemsListUpload answer) {
                        if (answer.response.equals("Image_upload_Successfully")) {
                            int lastId = answer.getLastId();
                            intent = new Intent(G.Context, ActivityCheckAd.class);
                            intent.putExtra("lastid", lastId);
                            intent.putExtra("catid", catid);
                            intent.putExtra("catname", sharedPreferences.getString(spcatname, ""));
                            startActivity(intent);
                            Toast.makeText(G.Context, "اعلان با موفقیت ثبت گردید", Toast.LENGTH_SHORT).show();
                            //                clearInputs();
                            finish();
//                                    Activity_MyAd.activitydestination = "ActivityCheckAd";
                        } else if (answer.response.equals("Image_upload_Failed")) {
                            Toast.makeText(G.Context, "ثبت اعلان با مشکل مواجه گردید", Toast.LENGTH_SHORT).show();
                        }
//                                G.dismiss_P_Dialog();
                    }

                    @Override
                    public void SendError(Throwable t) {
                        Log.i("test", "response_failed");

//                                G.dismiss_P_Dialog();
                        Toast.makeText(G.Context, "ارتباط با سرور با مشکل مواجه گردید", Toast.LENGTH_SHORT).show();
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
    protected void onStart() {
        super.onStart();
        Log.i("name", "onstart");
        if (bundle == null) {

            if (sharedPreferences.contains(spcatname) && sharedPreferences.contains(spcatid)) {
                btncategory.setText(sharedPreferences.getString(spcatname, ""));
                catid = sharedPreferences.getInt(spcatid, 1);
            }
        }
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
        G.startActivity(MainActivity.class, true);
    }
}





